# boot 模块

## 职责
- 应用启动入口与运行时装配。
- 放置 `MenuApplication`、全局异常处理、任务处理器、运行配置文件。

## 典型内容
- `src/main/java`：启动类、`advice`、`job`、配置类。
- `src/main/resources`：`application.yml`、日志配置。

## 依赖
- 依赖：`common`、`mapper`、`service`、`controller`。
- 说明：仅 `boot` 模块负责最终打包与运行。
