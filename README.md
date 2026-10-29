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

会创建库 `monster_portfolio` 以及管理员、资料、项目、文章和留言表。后端第一次启动会写入默认管理员：

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
3. 个人资料后台编辑，前台首页读取
4. 项目集：后台 CRUD，前台列表/详情
5. 文章：Markdown 草稿/发布，前台列表/详情
6. 留言：前台联系表单，后台查看/已读/删除
7. 本地图片上传（jpg/png/webp，≤2MB），头像和封面可用

下一步：3D 简历。
