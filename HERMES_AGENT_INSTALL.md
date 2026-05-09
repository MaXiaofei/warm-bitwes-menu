# Hermes Agent 插件安装说明（skills 方式）

本文记录使用 `npx skills add obra/superpowers` 安装插件的方式，适合在本机快速启用相关能力。

## 1. 前置条件

- 已安装 Node.js（建议 18+）
- 网络可访问 npm（中国大陆建议配置 npm 镜像）
- 终端可用 `npx`

可用以下命令检查环境：

```bash
node -v
npm -v
npx -v
```

## 2. 安装命令

在终端执行：

```bash
npx skills add obra/superpowers
```

> 首次执行会自动下载并安装所需内容，耗时取决于网络环境。

## 3. 常见国内网络加速（可选）

若下载较慢，可先切换 npm 源后再安装：

```bash
npm config set registry https://registry.npmmirror.com
npx skills add obra/superpowers
```

如需恢复官方源：

```bash
npm config set registry https://registry.npmjs.org
```

## 4. 安装后验证

- 重新打开一个终端会话
- 在项目内查看是否生成/更新了与 skills 相关的配置文件（如 `skills-lock.json`）
- 再次执行安装命令，若提示已安装或无变更，通常表示安装成功

验证命令示例：

```bash
npx skills add obra/superpowers
```

## 5. 故障排查

- 网络超时：优先切换 npm 镜像后重试
- 权限问题：避免使用 `sudo`，优先修复当前用户 npm 目录权限
- 命令不存在：确认 `node`/`npm`/`npx` 已正确安装并在 `PATH` 中

