# NMUA

![GitHub Workflow Status](https://img.shields.io/github/workflow/status/nessoffice/NMUA/CI)

基于Java的MUA(MakeUp Programming Language)解释器，为翁恺老师PPL课程的项目。MUA是一个语法简单的函数式编程语言，不需要有编译原理的知识也能写出其解释器。

MUA的语法说明见本目录下`.pdf`文件。

## 项目管理

### 需求文档

- BNF描述，及图形化

### CICD

### 自动化测试

## 项目展示

### 命令行端

暂无。

### QQ端

暂无。

### 网页端

基于

- [vue3](https://v3.cn.vuejs.org/)
- [vite.js](https://cn.vitejs.dev/)
- [xterm.js](https://www.npmjs.com/package/xterm)
- [local-echo](https://www.npmjs.com/package/local-echo)

什么是local-echo：不是通过websocket跟后端的命令行程序实时连接，而是等用户输完一段内容后再通过http request发给后端。

为什么用local-echo：不想装node-pty，太大。local一来用户体验快（主要是访问国外的网站，连websocket会很卡），二来也与qq端同构，方便复用。

用的时候碰到什么坑点：

- create-react-app起项目过慢。解决方案：用vite
- local-echo不在淘宝源上。解决方案：给yarn设proxy
- 报错Error: EPERM: operation not permitted, unlink xxx。解决方案：网上的解决方案各不相同也没有讲原理，我是把vite停掉删了他说报错的文件再跑
- local-echo没有ts支持，而笔者没有ts基础。解决方案：先不学ts，用回vue
- 对Promise对象不熟悉导致从local-echo的单行到repl有些无从下手。解决方案：学
- 部署：git subtree push --prefix frontend/dist origin， gh-pages No new revisions were found。解决方案：尝试发现只有master中commit的文件才能推过去。
- 当前是客户端部署的方案，如果能做到在服务端部署就好了（少等一些时间）
- 先push了点垃圾之后出现refusing to merge unrelated histories。解决方案：删branch
- 上传后发现白屏。解决方案：看network发现404认识到是路径问题，查vite文档，给引用到的文件加了个路径偏移。

支持的功能：

- 颜色
- 历史

不支持的功能：

- ctrl+arrow

对terminal的需求分析：

- 咕咕咕

### 后端

暂无。

## IDE支持

暂无。

