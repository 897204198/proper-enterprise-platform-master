Proper Enterprise Platform
==========================

<a href="https://cloud.propersoft.cn/teamcities/viewType.html?buildTypeId=ProperEnterprise_PepParallel">
  <img src="https://cloud.propersoft.cn/teamcities/app/rest/builds/buildType:(id:ProperEnterprise_PepParallel)/statusIcon.svg"/>
</a>
<a href="https://codecov.io/gh/propersoft-cn/proper-enterprise-platform">
  <img src="https://codecov.io/gh/propersoft-cn/proper-enterprise-platform/branch/master/graph/badge.svg?token=uthbnLL68t"/>
</a>


主要设计目标
----------

* 以 [Spring Boot](http://projects.spring.io/spring-boot/) 为基础，采用当前主流且活跃的开源技术和框架，以简化开发步骤、提升开发效率和质量为目标，拥抱并响应变化为宗旨，稳定坚固可持续发展为愿景，对平台进行架构
* 平台功能按模块进行划分及构建，除核心功能模块和公共模块外，各模块尽量保持独立，且可按需组合
* 部署方式可集中可分布。集中式部署时为一个 web 应用，分布式部署时为多个 web 应用。分布式部署时通过反向代理保持部署方式对用户的透明，系统内部无需单点登录
* 模块间需要依赖时，面向接口和服务编程，以便模块按不同需求提供不同实现
* 屏蔽对具体数据库的依赖，表结构及初始化数据以 [Database Change Log File](http://www.liquibase.org/documentation/databasechangelog.html) 形式组织
* 平台功能及特性须有对应的单元测试，及必要的集成测试及性能测试


主要技术及工具选型
--------------

[所用技术、工具及文档](https://github.com/propersoft-cn/pep-refs/blob/develop/0.5.x-README.md)


项目构建
--------

项目构建依托 [gradle](http://www.gradle.org) 构建工具，`gradlew` 是 `gradle warpper`，作用是使不同的开发环境能够使用统一版本的构建工具进行构建，
避免版本不同带来的兼容性等问题。`wrapper` 中使用的 `gradle` 版本参见 [gradle-wrapper.properties](./gradle/wrapper/gradle-wrapper.properties)。

在构建之前需要配置一下私有仓库，即配置文件`~/.gradle/gradle.properties`

````
nexusUrl=http://nexus.propersoft.cn:8081/
nexusUsername=<your username>
nexusPassword=<your password>
````

### 整体构建

    $ ./gradlew build

### 子项目独立构建

`<module_name>:build`，以 `pep-core` 子项目为例

    $ ./gradlew pep-core:build

项目构建会对源码进行编译、进行代码质量检查、执行测试、构建 `jar` 包。构建的内容输出到各子项目的 `build` 路径内。

> 在 `Windows` 环境下使用时，需使用 [gradlew.bat](gradlew.bat) 批处理指令，构建命令也需相应变化，如整体构建命令为 `gradlew build`


构建源码包
---------

    $ ./gradlew sourcesJar

或

    $ ./gradlew pep-core:sourcesJar


构建可执行 Jar 包
---------------

### 设定 `profile`

平台定义了三个 `profile` 版本，分别为：

* `dev`：开发版
* `test`：测试版
* `production`：产品版

可以在 [application.properties](./subprojects/pep-application/src/main/resources/application.properties) 中修改应用所使用的版本，默认为`开发版`

    spring.profiles.active=dev

### 打包

平台只包含一个 web 应用，即 `pep-application`，其余模块以 `jar` 包形式被其依赖

    $ ./gradlew clean bootJar

构建好的 `jar` 包会输出到 `pep-application` 项目根路径下的 `build/libs` 路径内。


部署及运行
---------

Spring boot 默认配置 tomcat 使用的字符集为 `UTF-8`（org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory#DEFAULT_CHARSET），
使用的 connector 为 `Http11NioProtocol`（org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory#DEFAULT_PROTOCOL）。

**推荐使用** docker 容器 + Jar 包的形式部署及运行，避免因运行环境差异导致的各种问题。
通过 `Docker Compose` 编排和运行服务（单节点），可以在 [docker-compose.yml](./docker/docker-compose.yml) 中调整所需服务，并运行

    $ docker-compose up -d


开发者文档
--------

[Proper Enterprise Platform Developer Guidelines](./CONTRIBUTING.md)
