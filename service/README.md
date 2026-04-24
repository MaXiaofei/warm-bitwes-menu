# service 模块

## 职责
- 承载业务规则与流程编排。
- 对下调用数据访问层，对上为接口层提供业务能力。

## 典型内容
- 业务服务类（如 `DishService`、`IngredientService`）。

## 依赖
- 依赖：`common`、`mapper`。
- 禁止：依赖 `controller`（避免反向依赖）。
