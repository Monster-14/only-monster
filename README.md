# Monster 个人主页

Vue 3 + Spring Boot 3.3（JDK 17）的个人主页项目。当前完成的是第一步：前后端脚手架、统一响应、跨域和本地代理。

## 目录

```text
only-monster/
├── web/      Vue3 前台（Vite 5，端口 5173）
└── server/   Spring Boot 后台（端口 8080）
```

本机需要 **Node 18**（仓库已写 `web/.nvmrc`）和 **JDK 17**。若默认还是 Node 16，先执行：

```bash
nvm use 18.20.4
```

## 启动

先开后端：

```bash
cd server
mvn spring-boot:run
```

再开前端：

```bash
cd web
npm install
npm run dev
```

浏览器打开 [http://localhost:5173](http://localhost:5173)。首页会请求 `/api/public/health`，Vite 会把 `/api` 代理到 `8080`。也可以直接访问 [http://localhost:8080/api/public/health](http://localhost:8080/api/public/health)。

成功时返回：

```json
{
  "code": 0,
  "message": "ok",
  "data": {
    "app": "monster-portfolio",
    "status": "UP"
  }
}
```

## 这一步还没有

- 数据库 / MyBatis-Plus
- 登录与 JWT
- 后台管理页
- 3D 简历
