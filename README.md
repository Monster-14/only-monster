# Monster 个人主页

Vue 3 + Spring Boot 3.3（JDK 17）的个人主页项目。

## 目录

```text
only-monster/
├── web/      Vue3 前台（Vite 5，端口 5173）
├── server/   Spring Boot 后台（端口 8080）
└── sql/      建库脚本
```

本机需要 **Node 18**（`web/.nvmrc`）、**JDK 17**、**MySQL 8**。若默认还是 Node 16：

```bash
nvm use 18.20.4
```

## 数据库

账号：`root` / `1234`。首次执行：

```bash
mysql -uroot -p1234 < sql/init.sql
```

会创建库 `monster_portfolio` 和表 `admin_user`。后端第一次启动会写入默认管理员：

- 用户名：`admin`
- 密码：`admin123`

## 启动

```bash
cd server
mvn spring-boot:run
```

```bash
cd web
npm install
npm run dev
```

- 前台：[http://localhost:5173](http://localhost:5173)
- 后台登录：[http://localhost:5173/admin/login](http://localhost:5173/admin/login)
- 健康检查：[http://localhost:8080/api/public/health](http://localhost:8080/api/public/health)

## 当前进度

1. 脚手架、统一响应、本地代理
2. 管理员 JWT 登录、后台空白页

下一步：个人资料后台编辑。
