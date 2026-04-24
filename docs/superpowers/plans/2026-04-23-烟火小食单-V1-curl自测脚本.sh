#!/usr/bin/env bash
set -euo pipefail

# 烟火小食单 V1 冒烟自测脚本（P0->P2）
# 用法：
#   BASE_URL="http://localhost:8080/menu/api" bash "docs/superpowers/plans/2026-04-23-烟火小食单-V1-curl自测脚本.sh"

BASE_URL="${BASE_URL:-http://localhost:8080/menu/api}"
VERBOSE="${VERBOSE:-0}"

if [[ "${1:-}" == "--verbose" ]]; then
  VERBOSE=1
fi

json_get() {
  local expr="$1"
  python3 -c "import sys,json; d=json.load(sys.stdin); print(eval(sys.argv[1],{}, {'d':d}))" "$expr"
}

print_step() {
  echo ""
  echo "========== $1 =========="
}

print_verbose() {
  if [[ "$VERBOSE" == "1" ]]; then
    echo "$1"
  fi
}

assert_code_zero() {
  local body="$1"
  local code
  code="$(echo "$body" | json_get "d.get('code')")"
  if [[ "$code" != "0" ]]; then
    echo "接口返回失败，code=$code"
    echo "$body"
    exit 1
  fi
}

request_json() {
  local method="$1"
  local url="$2"
  local auth="${3:-}"
  local data="${4:-}"
  local body

  if [[ -n "$data" ]]; then
    if [[ -n "$auth" ]]; then
      body="$(curl -s -X "$method" "$url" -H "Authorization: Bearer $auth" -H "Content-Type: application/json" -d "$data")"
    else
      body="$(curl -s -X "$method" "$url" -H "Content-Type: application/json" -d "$data")"
    fi
  else
    if [[ -n "$auth" ]]; then
      body="$(curl -s -X "$method" "$url" -H "Authorization: Bearer $auth")"
    else
      body="$(curl -s -X "$method" "$url")"
    fi
  fi

  print_verbose "$body"
  assert_code_zero "$body"
  if [[ "$VERBOSE" != "1" ]]; then
    echo "$body"
  fi
}

print_step "1.1 健康检查"
HEALTH_RES="$(request_json GET "$BASE_URL/health")"
echo "$HEALTH_RES" | json_get "d['data']" >/dev/null
echo "健康检查通过"

print_step "1.2 登录获取 Token"
LOGIN_RES="$(request_json POST "$BASE_URL/auth/login" "" '{"username":"admin","password":"admin123"}')"
TOKEN="$(echo "$LOGIN_RES" | json_get "d['data']['accessToken']")"
if [[ -z "$TOKEN" || "$TOKEN" == "None" ]]; then
  echo "未获取到 TOKEN"
  exit 1
fi
echo "TOKEN 获取成功"

print_step "1.3 新增菜品 + 查询详情"
DISH_CREATE_RES="$(request_json POST "$BASE_URL/dishes" "$TOKEN" '{"name":"番茄炒蛋","durationMin":10,"difficulty":2}')"
DISH_ID="$(echo "$DISH_CREATE_RES" | json_get "d['data']['id']")"
if [[ -z "$DISH_ID" || "$DISH_ID" == "None" ]]; then
  echo "未获取到 DISH_ID"
  exit 1
fi
request_json GET "$BASE_URL/dishes/$DISH_ID" "$TOKEN" >/dev/null
echo "DISH_ID=$DISH_ID"

print_step "1.4 菜品分页"
request_json GET "$BASE_URL/dishes?pageNum=1&pageSize=10" "$TOKEN" >/dev/null
echo "菜品分页通过"

print_step "1.5 新增食材 + 查询详情"
ING_CREATE_RES="$(request_json POST "$BASE_URL/ingredients" "$TOKEN" '{"name":"鸡蛋","unit":"个","caloriesKcalPer100g":143.0,"giValue":0.0}')"
ING_ID="$(echo "$ING_CREATE_RES" | json_get "d['data']['id']")"
if [[ -z "$ING_ID" || "$ING_ID" == "None" ]]; then
  echo "未获取到 ING_ID"
  exit 1
fi
request_json GET "$BASE_URL/ingredients/$ING_ID" "$TOKEN" >/dev/null
echo "ING_ID=$ING_ID"

print_step "1.6 食材分页"
request_json GET "$BASE_URL/ingredients?pageNum=1&pageSize=10" "$TOKEN" >/dev/null
echo "食材分页通过"

print_step "2.1 维护菜品-食材关联"
request_json PUT "$BASE_URL/dishes/$DISH_ID/ingredients" "$TOKEN" "{\"items\":[{\"ingredientId\":$ING_ID,\"amountG\":200,\"sortOrder\":0}]}" >/dev/null
echo "菜品-食材关联通过"

print_step "2.2 新增菜单模板 + 详情"
TPL_CREATE_RES="$(request_json POST "$BASE_URL/menu-templates" "$TOKEN" "{\"name\":\"家常两菜\",\"templateType\":4,\"dishIds\":[$DISH_ID]}")"
TPL_ID="$(echo "$TPL_CREATE_RES" | json_get "d['data']['id']")"
if [[ -z "$TPL_ID" || "$TPL_ID" == "None" ]]; then
  echo "未获取到 TPL_ID"
  exit 1
fi
request_json GET "$BASE_URL/menu-templates/$TPL_ID" "$TOKEN" >/dev/null
echo "TPL_ID=$TPL_ID"

print_step "2.3 模板食材汇总"
request_json GET "$BASE_URL/menu-templates/$TPL_ID/ingredient-summary" "$TOKEN" >/dev/null
echo "模板食材汇总通过"

print_step "2.4 创建做饭会话"
SESSION_CREATE_RES="$(request_json POST "$BASE_URL/cook-sessions" "$TOKEN" "{\"templateId\":$TPL_ID,\"startedAt\":\"2026-04-23T18:00:00\"}")"
SESSION_ID="$(echo "$SESSION_CREATE_RES" | json_get "d['data']['sessionId']")"
if [[ -z "$SESSION_ID" || "$SESSION_ID" == "None" ]]; then
  echo "未获取到 SESSION_ID"
  exit 1
fi
echo "SESSION_ID=$SESSION_ID"

print_step "3.1 生成备菜清单"
request_json POST "$BASE_URL/cook-sessions/$SESSION_ID/prep-list/generate" "$TOKEN" >/dev/null
echo "备菜清单生成通过"

print_step "3.2 提交口味评分"
request_json POST "$BASE_URL/cook-sessions/$SESSION_ID/reviews/taste" "$TOKEN" '{"score":5}' >/dev/null
echo "口味评分提交通过"

print_step "3.3 查询点评汇总"
request_json GET "$BASE_URL/cook-sessions/$SESSION_ID/reviews" "$TOKEN" >/dev/null
echo "点评汇总查询通过"

print_step "完成"
echo "全链路冒烟通过：DISH_ID=$DISH_ID, ING_ID=$ING_ID, TPL_ID=$TPL_ID, SESSION_ID=$SESSION_ID"
if [[ "$VERBOSE" == "1" ]]; then
  echo "verbose 模式已启用：以上步骤已输出完整响应体。"
fi

