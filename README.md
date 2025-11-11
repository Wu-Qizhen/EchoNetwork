# 回声网络

## 项目简介

这是一个旨在复兴深度内容创作与交流的在线博客应用与内容管理系统，包含前后端代码，前端使用 Vue.js 框架实现，后端使用 Java 与 Kotlin 语言，基于 Servlet 和 JDBC 实现



## 项目展示

请查看 `docs/项目展示` 文件



## 项目结构

- `echo-network-backend`：后端代码，包含控制器、服务、数据访问对象（DAO）、实体类等
- `echo-network-vue`：基于Vue.js的前端代码，包含组件、路由、样式等
- `echo-network-react`：基于 React 的前端代码（未实现）



## 功能模块

### 后端功能模块

- **文章管理**：包括文章的创建、更新、删除、点赞、收藏、评论等操作
- **用户管理**：包括用户的注册、登录、个人信息管理、关注 / 取消关注等操作
- **圈子管理**：包括圈子的创建、加入、退出、更新等操作
- **评论管理**：包括评论的创建、删除、点赞等操作
- **标签管理**：包括标签的创建、查询等操作
- **权限管理**：基于角色的访问控制，如普通用户、管理员等

### 前端功能模块

- **主页**：展示推荐文章、热门文章等
- **文章详情页**：展示文章内容、评论、点赞、收藏等
- **用户中心**：展示用户个人信息、关注 / 粉丝列表、发布的文章等
- **圈子页**：展示圈子信息、成员、文章等
- **搜索页**：支持文章、用户、圈子的搜索



## 技术栈

- **后端**：Java、Servlet、JDBC、MySQL、Redis、JWT
- **前端**：Vue.js、React、TypeScript、HTML、CSS



## 安装和运行

### 后端安装和运行

1. 安装 Java、Kotlin 环境
2. 安装 MySQL 数据库，并创建相应的数据库和表（见 `docs` 目录）
3. 安装 Redis，并启动服务
4. 导入 `echo-network-backend` 项目到 IDE（如 IntelliJ IDEA）
5. 配置数据库连接信息、Redis 连接信息等
6. 运行项目，确保服务启动成功

### 前端安装和运行

1. 安装 Node.js 和 npm
2. 进入 `echo-network-vue` 目录
3. 运行 `npm install `安装依赖
4. 运行 `npm start` 启动开发服务器
5. 打开浏览器访问 `http://localhost:8080`（Vue）查看应用



## 文档和资源

- 项目文档：`docs` 目录包含项目的相关设计文档和截图
- 配置文件：`application.yml` 包含项目的配置信息
- 日志文件：`log4j.properties  `配置日志输出



## 许可证

本项目使用 MIT 许可证，详细信息请查看 `LICENSE` 文件



## 贡献

欢迎贡献代码和文档，提交 PR 或 Issue



> ***Elegance is not a dispensable luxury but a quality that decides between success and failure!***
> **优雅不是可有可无的奢侈品，而是决定成败的品质！**