#!/usr/bin/env bash
# Batch2 冒烟：需本机已启动 menu 后端（context-path=/menu），默认账号 admin/123456。
set -euo pipefail
BASE="${BASE:-http://localhost:8080/menu}"

json_get() {
  python3 -c "import json,sys; print(json.load(sys.stdin)$1)" 2>/dev/null || true
}

echo "== 1) admin 登录 =="
ADMIN_JSON=$(curl -sS -X POST "$BASE/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456","clientType":"admin"}')
echo "$ADMIN_JSON" | json_get "['code']"
ADMIN_TOKEN=$(echo "$ADMIN_JSON" | python3 -c "import json,sys; d=json.load(sys.stdin); print(d.get('data',{}).get('accessToken',''))")
if [[ -z "$ADMIN_TOKEN" ]]; then echo "admin 登录失败"; exit 1; fi

echo "== 2) 建模板（需已有菜品 ID 1、2，否则请先造菜）=="
CREATE_TMPL=$(curl -sS -X POST "$BASE/api/menu-templates" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"冒烟模板","templateType":1,"dishIds":[1,2]}')
echo "$CREATE_TMPL" | json_get "['code']"
TID=$(echo "$CREATE_TMPL" | python3 -c "import json,sys; d=json.load(sys.stdin); print(d.get('data',{}).get('id',0))")
if [[ -z "$TID" || "$TID" == "0" ]]; then echo "创建模板失败（检查菜品是否存在）: $CREATE_TMPL"; exit 1; fi

echo "== 3) 模板食材汇总 =="
SUM=$(curl -sS -H "Authorization: Bearer $ADMIN_TOKEN" "$BASE/api/menu-templates/$TID/ingredient-summary")
echo "$SUM" | json_get "['code']"

echo "== 4) mini 登录 =="
MINI_JSON=$(curl -sS -X POST "$BASE/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456","clientType":"mini"}')
MINI_TOKEN=$(echo "$MINI_JSON" | python3 -c "import json,sys; d=json.load(sys.stdin); print(d.get('data',{}).get('accessToken',''))")
if [[ -z "$MINI_TOKEN" ]]; then echo "mini 登录失败"; exit 1; fi

echo "== 5) 创建会话（模板 + 开始时间）=="
SESS=$(curl -sS -X POST "$BASE/api/cook-sessions" \
  -H "Authorization: Bearer $MINI_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"templateId\":$TID,\"dishIds\":[],\"startedAt\":\"2026-05-09T12:00:00\"}")
echo "$SESS" | json_get "['code']"
SID=$(echo "$SESS" | python3 -c "import json,sys; d=json.load(sys.stdin); print(d.get('data',{}).get('sessionId',0))")
if [[ -z "$SID" || "$SID" == "0" ]]; then echo "创建会话失败: $SESS"; exit 1; fi

echo "== 6) 事件时间线（初始可空）=="
EV=$(curl -sS -H "Authorization: Bearer $MINI_TOKEN" "$BASE/api/cook-sessions/$SID/events")
echo "$EV" | json_get "['code']"

echo "== 7) 追加事件 =="
curl -sS -X POST "$BASE/api/cook-sessions/$SID/events" \
  -H "Authorization: Bearer $MINI_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"eventType":"START","eventTime":"2026-05-09T12:05:00","content":"开始做饭"}' | json_get "['code']"

echo "== 8) 复盘 GET/PUT =="
curl -sS -H "Authorization: Bearer $MINI_TOKEN" "$BASE/api/cook-sessions/$SID/retrospective" | json_get "['code']"
curl -sS -X PUT "$BASE/api/cook-sessions/$SID/retrospective" \
  -H "Authorization: Bearer $MINI_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"summary":"口味刚好","improvement":"下次少盐"}' | json_get "['code']"

echo "== 9) 我的会话列表 =="
curl -sS -H "Authorization: Bearer $MINI_TOKEN" "$BASE/api/cook-sessions/current/mine?pageNum=1&pageSize=5" | json_get "['code']"

echo "Batch2 smoke 完成（请人工确认上述 code 均为 0）。"
