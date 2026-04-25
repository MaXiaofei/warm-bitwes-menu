#!/usr/bin/env bash
set -euo pipefail

# 鉴权联调 curl 脚本
# 用法：
#   chmod +x docs/superpowers/plans/2026-04-25-鉴权联调-curl.sh
#   BASE_URL="http://localhost:8080/menu" bash docs/superpowers/plans/2026-04-25-鉴权联调-curl.sh

BASE_URL="${BASE_URL:-http://localhost:8080/menu}"
LOGIN_URL="${BASE_URL}/api/auth/login"
ADMIN_CREATE_USER_URL="${BASE_URL}/api/admin/users"

echo "==> BASE_URL: ${BASE_URL}"

echo
echo "==> 1) admin 登录，获取 token（预期 code=0）"
ADMIN_LOGIN_RESP="$(curl -sS -X POST "${LOGIN_URL}" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456","clientType":"admin"}')"
echo "${ADMIN_LOGIN_RESP}"
ADMIN_TOKEN="$(echo "${ADMIN_LOGIN_RESP}" | sed -n 's/.*"accessToken":"\([^"]*\)".*/\1/p')"

echo
echo "==> 2) mini 登录，获取 token（预期 code=0）"
MINI_LOGIN_RESP="$(curl -sS -X POST "${LOGIN_URL}" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456","clientType":"mini"}')"
echo "${MINI_LOGIN_RESP}"
MINI_TOKEN="$(echo "${MINI_LOGIN_RESP}" | sed -n 's/.*"accessToken":"\([^"]*\)".*/\1/p')"

echo
echo "==> 3) admin token 调用管理端新增用户（预期 code=0）"
curl -sS -X POST "${ADMIN_CREATE_USER_URL}" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${ADMIN_TOKEN}" \
  -d '{"username":"ops","nickname":"运营","phone":"13800000000","email":"ops@example.com","password":"123456","roleIds":[1]}'
echo

echo
echo "==> 4) mini token 调用管理端新增用户（预期 code=10003）"
curl -sS -X POST "${ADMIN_CREATE_USER_URL}" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${MINI_TOKEN}" \
  -d '{"username":"ops2","nickname":"运营2","phone":"13800000001","email":"ops2@example.com","password":"123456","roleIds":[1]}'
echo

echo
echo "==> 5) 无 token 调用管理端新增用户（预期 code=10002）"
curl -sS -X POST "${ADMIN_CREATE_USER_URL}" \
  -H "Content-Type: application/json" \
  -d '{"username":"ops3","nickname":"运营3","phone":"13800000002","email":"ops3@example.com","password":"123456","roleIds":[1]}'
echo

echo
echo "==> 6) 无效 token 调用管理端新增用户（预期 code=10002）"
curl -sS -X POST "${ADMIN_CREATE_USER_URL}" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer invalid-token" \
  -d '{"username":"ops4","nickname":"运营4","phone":"13800000003","email":"ops4@example.com","password":"123456","roleIds":[1]}'
echo

echo
echo "==> 说明：当前后端在内存 token 模式下，'admin 但缺权限' 场景需通过测试桩构造，不适合直接用 curl 复现。"
