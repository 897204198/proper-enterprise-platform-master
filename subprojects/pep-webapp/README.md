pep-webapp
==========

Proper Enterprise Platform

Web Application Module

此模块为所有模块中唯一的一个 web 应用，其他各个模块均以 jar 包的方式被本模块所依赖。其他模块若包含页面，需将页面资源按规定路径放入本模块中，基本规则为：

    src/main/webapp/WEB-INF/views/[module]/../*.jsp
    
以 `pep-auth-common` 模块为例，该模块下页面资源需放置在 `src/main/webapp/WEB-INF/views/auth/common/` 下，在该路径下可自行创建子路径

在开发及发布时，可根据实际情况调整本模块依赖的模块，以达到定制产品功能的目的