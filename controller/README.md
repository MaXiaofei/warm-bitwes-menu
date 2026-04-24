# controller 模块

## 职责
- 对外 API 接口层。
- 负责参数接收、校验、返回结构与视图对象组装。

## 典型内容
- `controller`：`@RestController` 接口实现。
- `dto`：请求参数对象。
- `vo`：响应视图对象。

## 依赖
- 依赖：`common`、`service`。
- 禁止：直接依赖 `mapper`。
