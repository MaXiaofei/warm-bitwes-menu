# mapper 模块

## 职责
- 数据访问层，负责实体映射与 SQL 执行。

## 典型内容
- `entity`：数据库实体对象。
- `mapper`：MyBatis 接口。
- `src/main/resources/mapper`：MyBatis XML。

## 依赖
- 依赖：`common`、MyBatis、MySQL 驱动。
- 禁止：依赖 `controller`、`service`。
