pep-workflow
============

Proper Enterprise Platform

Workflow Module

基于 [Flowable](https://github.com/flowable/flowable-engine) 构建平台的工作流模块

Web 流程设计器使用 `Flowable` 项目中的 [flowable-ui-modeler](https://github.com/flowable/flowable-engine/tree/master/modules/flowable-ui-modeler) 模块提供的流程设计器，以 jar 包形式为平台所依赖。可通过 [proper-* branch](https://github.com/propersoft-cn/flowable-engine) 来打包这些静态资源


配置
----

引用此模块的项目需要进行一些额外的配置，以保证流程设计器的可用

### 代理(ng2中使用到workflow中的静态资源时需要配置)

因为流程设计器的前端资源是集成在 jar 包中的，需要通过后端服务器的响应返回给前端，故为能够在前端使用流程设计器，需要在后端服务前增加代理服务器。以 nginx 举例，配置如下（假设前端资源上下文根为 `/ng2`）：

- [下载nginx](http://nginx.org/en/download.html "下载nginx")
- 修改nginx配置，路径为conf/nginx.conf
```
#server下添加
#本地静态文件存放地址（具体地址自行修改）
location ^~ /ng2/ {
    proxy_pass http://frontend_server:port/;
}
#服务端静态文件存放地址（具体地址自行修改）
location ^~ /ng2/workflow {
    proxy_pass http://backend_server:port/pep/workflow;
}
```
- 启动nginx，访问[http://127.0.0.1](http://127.0.0.1 "http://127.0.0.1")，看到welcom to nginx,启动成功
- 访问代理路径[http://127.0.0.1/ng2](http://127.0.0.1/ng2 "http://127.0.0.1/ng2")，看到协同管理平台页面，代理成功

### Context Root

流程设计器的默认上下文根在 `app-cfg.js` 中配置成了 `pep`。项目中使用时，须更改此默认配置为项目 **前端** 服务上下文根。修改方法为：

将 [app-cfg.js](./src/main/resources/META-INF/resources/editor-app/app-cfg.js) 复制到项目 **后端** 代码库相同路径下，并将 `contextRoot` 值中的 `pep` 修改为项目 **前端** 服务实际上下文根。

> 平台前端项目开发时会自动启动一个代理服务，将 `/pep` 的请求转发至平台后端服务。实际项目中使用时，并没有这个代理服务，故需要将此处配置为 **前端** 服务的上下文根
