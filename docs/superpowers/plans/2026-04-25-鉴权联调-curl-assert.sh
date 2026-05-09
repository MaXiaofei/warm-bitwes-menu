#!/usr/bin/env bash
set -euo pipefail

# 鉴权联调（带断言）脚本
# 用法：
#   chmod +x docs/superpowers/plans/2026-04-25-鉴权联调-curl-assert.sh
#   BASE_URL="http://localhost:8080/menu" bash docs/superpowers/plans/2026-04-25-鉴权联调-curl-assert.sh

BASE_URL="${BASE_URL:-http://localhost:8080/menu}"
LOGIN_URL="${BASE_URL}/api/auth/login"
ADMIN_CREATE_USER_URL="${BASE_URL}/api/admin/users"

FAIL_COUNT=0

extract_code() {
  # 从统一响应结构中提取 code（兼容 macOS sed）
  echo "$1" | tr -d '\n' | sed -E -n 's/.*"code"[[:space:]]*:[[:space:]]*(-?[0-9]+).*/\1/p'
}

extract_access_token() {
  echo "$1" | tr -d '\n' | sed -E -n 's/.*"accessToken"[[:space:]]*:[[:space:]]*"([^"]*)".*/\1/p'
}

assert_token_present() {
  local case_name="$1"
  local token="$2"
  local response="$3"
  if [[ -n "${token}" ]]; then
    echo "[PASS] ${case_name} token extracted"
  else
    echo "[FAIL] ${case_name} token missing"
    echo "       response=${response}"
    FAIL_COUNT=$((FAIL_COUNT + 1))
  fi
}

assert_code() {
  local case_name="$1"
  local expected="$2"
  local response="$3"
  local actual
  actual="$(extract_code "$response")"

  if [[ "$actual" == "$expected" ]]; then
    echo "[PASS] ${case_name} -> code=${actual}"
  else
    echo "[FAIL] ${case_name} -> expected=${expected}, actual=${actual:-<empty>}"
    echo "       response=${response}"
    FAIL_COUNT=$((FAIL_COUNT + 1))
  fi
}

echo "==> BASE_URL: ${BASE_URL}"

echo
echo "==> 1) admin 登录"
ADMIN_LOGIN_RESP="$(curl -sS -X POST "${LOGIN_URL}" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456","clientType":"admin"}')"
assert_code "admin login" "0" "${ADMIN_LOGIN_RESP}"
ADMIN_TOKEN="$(extract_access_token "${ADMIN_LOGIN_RESP}")"
assert_token_present "admin login" "${ADMIN_TOKEN}" "${ADMIN_LOGIN_RESP}"

echo
echo "==> 2) mini 登录"
MINI_LOGIN_RESP="$(curl -sS -X POST "${LOGIN_URL}" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456","clientType":"mini"}')"
assert_code "mini login" "0" "${MINI_LOGIN_RESP}"
MINI_TOKEN="$(extract_access_token "${MINI_LOGIN_RESP}")"
assert_token_present "mini login" "${MINI_TOKEN}" "${MINI_LOGIN_RESP}"

echo
echo "==> 3) admin token 调管理端新增用户"
ADMIN_CREATE_RESP="$(curl -sS -X POST "${ADMIN_CREATE_USER_URL}" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${ADMIN_TOKEN}" \
  -d '{"username":"ops","nickname":"运营","phone":"13800000000","email":"ops@example.com","password":"123456","roleIds":[1]}')"
assert_code "admin token create user" "0" "${ADMIN_CREATE_RESP}"

echo
echo "==> 4) mini token 调管理端新增用户"
MINI_CREATE_RESP="$(curl -sS -X POST "${ADMIN_CREATE_USER_URL}" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${MINI_TOKEN}" \
  -d '{"username":"ops2","nickname":"运营2","phone":"13800000001","email":"ops2@example.com","password":"123456","roleIds":[1]}')"
assert_code "mini token create user" "10003" "${MINI_CREATE_RESP}"

echo
echo "==> 5) 无 token 调管理端新增用户"
NO_TOKEN_RESP="$(curl -sS -X POST "${ADMIN_CREATE_USER_URL}" \
  -H "Content-Type: application/json" \
  -d '{"username":"ops3","nickname":"运营3","phone":"13800000002","email":"ops3@example.com","password":"123456","roleIds":[1]}')"
assert_code "no token create user" "10002" "${NO_TOKEN_RESP}"

echo
echo "==> 6) 无效 token 调管理端新增用户"
INVALID_TOKEN_RESP="$(curl -sS -X POST "${ADMIN_CREATE_USER_URL}" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer invalid-token" \
  -d '{"username":"ops4","nickname":"运营4","phone":"13800000003","email":"ops4@example.com","password":"123456","roleIds":[1]}')"
assert_code "invalid token create user" "10002" "${INVALID_TOKEN_RESP}"

echo
if [[ "${FAIL_COUNT}" -eq 0 ]]; then
  echo "ALL PASS: 鉴权联调用例全部通过。"
else
  echo "FAILED: ${FAIL_COUNT} 个用例未通过。"
  exit 1
fi
