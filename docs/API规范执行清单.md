# API 规范执行清单（V1 初稿）

## 1. 目标与适用范围
- 目标：统一接口风格，降低前后端联调与维护成本。
- 适用范围：`controller` 模块全部 HTTP 接口（含新增与改造接口）。
- 当前基线：
  - 成功结构：`{code:0, message:"success", data:*}`
  - 失败结构：`{code:非0, message:"可读信息", data:null}`
  - 已有全局异常处理：`GlobalExceptionHandler`

## 2. 统一响应规范

### 2.1 响应结构
- 统一三段式：`code`、`message`、`data`
- 所有 Controller 返回值统一使用 `ApiResponse<T>` 包装
- 禁止直接返回裸字符串、裸对象、裸数组

### 2.2 成功响应
- `code = 0`
- `message = "success"`
- `data`：
  - 查询类接口：返回对象或列表/分页结构
  - 新增/更新/删除类接口：可返回 `null` 或业务对象

### 2.3 失败响应
- `code != 0`
- `message` 提供可读错误信息
- `data = null`

## 3. 错误码规范（V1）

### 3.1 区间规划
- `0`：成功
- `1000-1999`：参数与校验错误
- `2000-2999`：鉴权与权限错误
- `3000-3999`：业务规则错误
- `5000-5999`：系统内部错误

### 3.2 首批错误码清单
- `0`：success
- `1001`：请求体参数校验失败（`MethodArgumentNotValidException`）
- `1002`：请求参数约束失败（`ConstraintViolationException`）
- `2001`：未登录或 token 无效
- `2002`：无权限访问
- `3001`：业务对象不存在
- `3002`：业务状态不允许当前操作
- `5000`：系统异常（统一兜底）

### 3.3 使用约束
- 同一个错误码必须对应稳定语义，禁止“一码多义”
- 业务异常优先抛 `BizException(code, message)`，由全局异常统一转换
- 新增错误码时，同步更新本文档与接口文档说明

## 4. 分页协议规范

### 4.1 分页请求参数
- 推荐统一参数：
  - `pageNum`：页码，从 `1` 开始
  - `pageSize`：每页条数，建议上限 `100`
- 查询参数超过两个时，必须封装为查询 DTO（符合现有项目约束）

### 4.2 分页响应结构
- `data` 统一建议为：
  - `list`：当前页数据
  - `pageNum`：当前页码
  - `pageSize`：每页条数
  - `total`：总记录数
  - `totalPage`：总页数

### 4.3 默认值建议
- `pageNum` 默认 `1`
- `pageSize` 默认 `10`
- `pageSize` 超过上限时返回 `1001`（参数错误）

## 5. 鉴权与权限规范（V1）

### 5.1 鉴权策略
- 默认策略：除健康检查和文档接口外，其余接口默认需要鉴权
- 建议白名单：
  - `GET /menu/api/health`
  - `GET /swagger-ui/**`
  - `GET /v3/api-docs/**`
  - 登录、刷新 token 接口（待实现后加入）

### 5.2 权限模型（V1 最小版）
- 角色建议：
  - `ADMIN`：后台管理能力
  - `USER`：普通业务能力
- 权限控制建议先按路径粒度实现，后续再细化到资源级权限

### 5.3 失败返回约定
- 未登录或 token 失效：返回 `2001`
- 已登录但权限不足：返回 `2002`

## 6. Controller 编码约束（执行项）
- 接口类使用 `@RestController`
- 入参优先使用 DTO + `@Valid`
- 查询参数超过两个必须封装对象
- 新增/修改接口使用对象接收参数
- Controller 不写复杂业务逻辑，仅做参数接收与响应封装

## 7. 响应示例（联调用）

### 7.1 成功示例
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 1001,
    "name": "番茄炒蛋"
  }
}
```

### 7.2 参数错误示例
```json
{
  "code": 1001,
  "message": "name: 不能为空; pageSize: 必须大于0",
  "data": null
}
```

### 7.3 业务异常示例
```json
{
  "code": 3001,
  "message": "食材不存在",
  "data": null
}
```

### 7.4 系统异常示例
```json
{
  "code": 5000,
  "message": "系统异常",
  "data": null
}
```

## 8. V1 核心接口清单（按模块分组）

> 统一前缀：`/menu/api`。  
> 标记说明：`P0`=首批联调必备，`P1`=主链路必备，`P2`=闭环增强。

### 8.1 AUTH（P0）
- `POST /auth/login`（登录，返回 token）
- `POST /auth/refresh`（刷新 token）
- `POST /auth/logout`（登出）
- `GET /admin/users`（用户分页）
- `POST /admin/users`（新增用户）
- `PUT /admin/users/{id}`（修改用户）
- `GET /admin/roles`（角色列表）
- `POST /admin/roles`（新增角色）
- `PUT /admin/roles/{id}`（修改角色）
- `GET /admin/permissions`（权限点列表）
- `PUT /admin/roles/{id}/permissions`（角色授权）

### 8.2 DISH（P0）
- `GET /dishes`（菜品分页查询）
- `GET /dishes/{id}`（菜品详情）
- `POST /dishes`（新增菜品）
- `PUT /dishes/{id}`（修改菜品）
- `PUT /dishes/{id}/status`（上/下架）
- `GET /dish-cuisines`（菜系列表）
- `POST /dish-cuisines`（新增菜系）
- `PUT /dish-cuisines/{id}`（修改菜系）
- `GET /dish-categories`（分类列表）
- `POST /dish-categories`（新增分类）
- `PUT /dish-categories/{id}`（修改分类）
- `GET /dish-tags`（标签列表）
- `POST /dish-tags`（新增标签）
- `PUT /dish-tags/{id}`（修改标签）
- `POST /dishes/{id}/media`（上传封面/步骤图）
- `PUT /dishes/{id}/steps`（更新步骤）
- `GET /dishes/{id}/media`（查询图文）

### 8.3 INGR（P0）
- `GET /ingredients`（食材分页查询）
- `GET /ingredients/{id}`（食材详情）
- `POST /ingredients`（新增食材）
- `PUT /ingredients/{id}`（修改食材）
- `DELETE /ingredients/{id}`（删除食材）
- `GET /ingredient-categories`（食材分类列表）
- `POST /ingredient-categories`（新增食材分类）
- `PUT /ingredient-categories/{id}`（修改食材分类）
- `PUT /ingredients/{id}/nutrition`（更新营养信息）
- `GET /ingredients/{id}/nutrition`（查询营养信息）
- `PUT /dishes/{id}/ingredients`（维护菜品食材关联）
- `GET /dishes/{id}/ingredients`（查询菜品食材关联）

### 8.4 MENU（P1）
- `GET /menu-templates`（模板分页查询）
- `GET /menu-templates/{id}`（模板详情）
- `POST /menu-templates`（新增模板）
- `PUT /menu-templates/{id}`（修改模板）
- `PUT /menu-templates/{id}/status`（启停模板）
- `GET /menu-types`（模板类型列表）
- `POST /menu-types`（新增模板类型）
- `PUT /menu-types/{id}`（修改模板类型）
- `GET /menu-matches`（场景/人群匹配规则列表）
- `POST /menu-matches`（新增匹配规则）
- `PUT /menu-matches/{id}`（修改匹配规则）
- `GET /menu-templates/{id}/ingredient-summary`（模板食材汇总）

### 8.5 COOK（P1）
- `POST /cook-sessions`（创建做饭会话）
- `GET /cook-sessions`（会话分页查询）
- `GET /cook-sessions/{id}`（会话详情）
- `PUT /cook-sessions/{id}/status`（会话状态流转）
- `POST /cook-sessions/{id}/events`（记录过程事件）
- `GET /cook-sessions/{id}/events`（查询过程事件）
- `GET /cook-sessions/history`（历史查询）
- `GET /cook-sessions/{id}/history-detail`（历史详情）
- `POST /cook-sessions/{id}/retrospectives`（提交复盘）
- `GET /cook-sessions/{id}/retrospectives`（查询复盘）

### 8.6 PREP（P2）
- `POST /cook-sessions/{id}/prep-list/generate`（生成备菜清单）
- `GET /cook-sessions/{id}/prep-list`（查询备菜清单）
- `GET /cook-sessions/{id}/prep-items`（查询备菜明细）
- `PUT /cook-sessions/{id}/prep-items/{itemId}/status`（更新备菜状态）
- `GET /cook-sessions/{id}/prep-progress`（查询备菜进度）
- `GET /cook-sessions/{id}/prep-shortages`（查询缺货项）
- `POST /cook-sessions/{id}/prep-shortages/confirm`（确认缺货项）

### 8.7 REVIEW（P2）
- `POST /cook-sessions/{id}/reviews/taste`（提交口味评分）
- `POST /cook-sessions/{id}/reviews/difficulty`（提交难度评分）
- `POST /cook-sessions/{id}/reviews/retry-intent`（提交复做意愿）
- `POST /cook-sessions/{id}/reviews/note`（新增点评备注）
- `PUT /cook-sessions/{id}/reviews/note`（修改点评备注）
- `GET /cook-sessions/{id}/reviews`（查询点评汇总）

### 8.8 公共与白名单（P0）
- `GET /health`（健康检查，白名单）
- `GET /swagger-ui/**`（文档，白名单）
- `GET /v3/api-docs/**`（OpenAPI 文档，白名单）

## 9. 落地顺序（建议一周内完成）
- 第 1 步：补充统一错误码常量/枚举（`common` 模块）
- 第 2 步：将已有接口返回统一到 `ApiResponse`
- 第 3 步：补分页 DTO 与分页 VO（先覆盖菜品、食材列表）
- 第 4 步：补鉴权白名单与无权限返回码（`2001/2002`）
- 第 5 步：按模块做回归（健康、菜品、食材）

## 10. 验收清单
- 任一接口成功响应均为 `code/message/data` 三段结构
- 参数异常不再抛裸错误，统一返回 `1001/1002`
- 业务异常统一返回 `300x`
- 未授权和无权限返回 `2001/2002`
- 列表接口分页参数和分页响应字段统一

## 11. 模块级联调响应示例（V1）

> 说明：以下示例用于前后端联调对齐，均遵循统一响应结构。

### 11.1 AUTH（`POST /auth/login`）

成功：
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "accessToken": "xxx.yyy.zzz",
    "refreshToken": "rrr.yyy.zzz",
    "expiresIn": 7200
  }
}
```

失败（未登录或 token 无效场景）：
```json
{
  "code": 2001,
  "message": "未登录或token无效",
  "data": null
}
```

### 11.2 DISH（`POST /dishes`）

成功：
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 1001,
    "name": "番茄炒蛋"
  }
}
```

失败（参数校验）：
```json
{
  "code": 1001,
  "message": "name: 不能为空",
  "data": null
}
```

### 11.3 INGR（`GET /ingredients`）

成功：
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "list": [],
    "pageNum": 1,
    "pageSize": 10,
    "total": 0,
    "totalPage": 0
  }
}
```

失败（参数约束）：
```json
{
  "code": 1002,
  "message": "pageSize: 必须大于0",
  "data": null
}
```

### 11.4 MENU（`GET /menu-templates/{id}/ingredient-summary`）

成功：
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "templateId": 2001,
    "ingredients": [
      { "ingredientId": 3001, "name": "鸡蛋", "amount": 4, "unit": "个" }
    ]
  }
}
```

失败（业务对象不存在）：
```json
{
  "code": 3001,
  "message": "菜单模板不存在",
  "data": null
}
```

### 11.5 COOK（`POST /cook-sessions`）

成功：
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "sessionId": 5001,
    "status": 1
  }
}
```

失败（业务状态不允许）：
```json
{
  "code": 3002,
  "message": "会话状态不允许当前操作",
  "data": null
}
```

### 11.6 PREP（`POST /cook-sessions/{id}/prep-list/generate`）

成功：
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "sessionId": 5001,
    "itemCount": 8
  }
}
```

失败（会话不存在）：
```json
{
  "code": 3001,
  "message": "会话不存在",
  "data": null
}
```

### 11.7 REVIEW（`POST /cook-sessions/{id}/reviews/taste`）

成功：
```json
{
  "code": 0,
  "message": "success",
  "data": null
}
```

失败（评分越界）：
```json
{
  "code": 3002,
  "message": "评分范围非法",
  "data": null
}
```
