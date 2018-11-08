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

* 以 [Spring Framework](http://projects.spring.io/spring-framework/) 为基础，采用当前主流且活跃的开源技术和框架，以简化开发步骤、提升开发效率和质量为目标，拥抱并响应变化为宗旨，稳定坚固可持续发展为愿景，对平台进行架构
* 平台功能按模块进行划分及构建，除核心功能模块和公共模块外，各模块尽量保持独立，且可按需组合
* 部署方式可集中可分布。集中式部署时为一个 web 应用，分布式部署时为多个 web 应用。分布式部署时通过反向代理保持部署方式对用户的透明，系统内部无需单点登录
* 模块间需要依赖时，面向接口和服务编程，以便模块按不同数据来源提供不同实现
* 屏蔽对具体数据库的依赖，初始化数据以 `SQL` 形式组织
* 平台功能及特性须有对应的功能及性能测试


主要技术及工具选型
--------------

[所用技术、工具及文档](http://propersoft-cn.github.io/pep-refs)


项目构建
--------

项目构建依托 [gradle](http://www.gradle.org) 构建工具，`gradlew` 是 `gradle warpper`，可不依赖本地的 `gradle` 环境进行构建。若本地已有 `gradle` 构建环境，使用本地环境也可。`wrapper` 中使用的 `gradle` 版本参见 [gradle-wrapper.properties](./gradle/wrapper/gradle-wrapper.properties)。

在构建之前需要配置一下私有仓库，即配置文件`~/.gradle/gradle.properties`
````
nexusUrl=https://server.propersoft.cn:8081
nexusUsername=你的用户名
nexusPassword=你的密码
````
### 整体构建

    $ ./gradlew build

### 子项目独立构建

以 `pep-core` 子项目为例

    $ ./gradlew pep-core:build

项目构建会对源码进行编译、进行代码质量检查、执行单元测试、构建 `jar` 包。构建的内容输出到各子项目的 `build` 路径内。

> 在 `Windows` 环境下使用时，需使用 [gradlew.bat](gradlew.bat) 批处理指令，构建命令也需相应变化，如整体构建命令为 `gradlew build`


构建源码包
---------

    $ ./gradlew sourcesJar

或

    $ ./gradlew pep-core:sourcesJar


构建 `war` 包
------------

### 设定 `profile`

平台定义了三个 `profile` 版本，分别为：

* `dev`：开发版
* `test`：测试版
* `production`：产品版

可以在 `web.xml` 中修改应用所使用的版本，默认为`开发版`

    <context-param>
        <param-name>spring.profiles.default</param-name>
        <param-value>dev</param-value>
    </context-param>

### 打包

平台只包含一个 web 应用，即 `pep-application`，其余模块以 `jar` 包形式被其依赖

    $ ./gradlew clean war

构建好的 `war` 包会输出到 `pep-application` 项目根路径下的 `build/libs` 路径内。


产品部署
-------

产品默认运行在 tomcat 内，并需要对其进行一定的调整。
如 tomcat 对接收到的请求默认使用的编码是 `ISO-8859-1`，需调整为平台统一字符集 `UTF-8`。
针对 tomcat 的调整均在 `[server.xml](configs/docker/tomcat/conf/server.xml)` 中进行设定。

**推荐使用** docker 容器 + war 包的形式发布产品，以使产品运行在一个基本相同的环境内，避免因配置原因造成的各种问题。
tomcat 的 docker 镜像需使用 [docker-tomcat](https://github.com/propersoft-cn/docker-tomcat) 构建出的镜像，以包含针产品针对配置的调整。

**推荐** 通过 `Docker Compose` 编排和运行服务（单节点），可以在 [docker-compose.yml](configs/docker/docker-compose.yml) 中调整所需服务，并运行

    # ./configs/docker
    $ docker-compose up -d


版本号规范
---------
- 版本号采用三位数字（1.1522.49）
- 版本号第一位表示重大更新
- 版本号第二位表示每次需求变更，版本号生成后，要把版本号记录到需求文档上，方便下次查找
- 版本号第三位表示bug等非需求修改
- 前台、后台、raml工程版本号要保持一致，方便三个工程对应。例如第一次需求变更版本号为1.1.0，只修改了前台工程，则前台工程版本号为1.1.0，后台和raml未修改依然为原版本号；第二次需求变更版本号为1.2.0，修改了前台和后台工程，则前台和后台工程版本号变为1.2.0，此处后台工程跳过一个版本直接更新成最新版本


版本发布流程
-----------

### 平台

* **同步** 修改前端、后端、API 等版本号

> 后端修改 build.gradle 中的 `version` 值，并同步修改 PEPVersion.java 中的 版本号 及 dependencies.gradle 中的 `versions.pep`

> 接口文档调整 API 仓库的 `api.raml` 和 `package.json` 中的版本号

> 前端修改`框架`的package.json、修改平台各个`模块`的package.json并与之同步并上传至npm私服。

* 提交代码打 tag，推送远程仓库

> 例：
$ git tag v0.4.0
$ git push upstream v0.4.0

* 编写发布说明

> 平台发布说明在 https://github.com/propersoft-cn/proper-enterprise-platform/releases 中编写

* 区分发布分支和开发分支，发布分支的内容自动合并至开发分支，在开发分支发布新版本后，将开发分支中的内容合并至发布分支

### web 前端发布

#### 产品发布

##### npm相关

- 首先同后台确认发布的版本号，修改产品各个`模块`的package.json并与之同步并上传至npm私服。

##### git代码

- (首次发布)从平台的master创建为该产品的dev分支，添加最新的`模块`依赖并完成开发工作后，合并dev分支到该产品的master，最终发布的为该master主干。
- (非首次发布)修改master主干代码的package.json的`模块`依赖，重新安装，提交至master。

#### 项目发布

##### git代码

- (首次发布)从产品的master创建为该项目的dev分支，完成开发工作后合并dev分支到该项目的master，最终发布的为该master主干

### [iCMP](https://github.com/propersoft-cn/icmp)

* 产品**开发**版可以依赖平台**开发**版
* 产品**发布**版必须依赖平台**发布**版

### [iHos](https://github.com/propersoft-cn/ihos)

##### **同步** 修改后端依赖平台的版本号

> 后端修改依赖平台版本号 dependencies-project.gradle 中的 `versions.pep` 的值

> 后端修改依赖平台版本号 build.gradle 中的 `com.proper.enterprise.platform:pep-dev-configs:` 冒号后面所依赖的平台的版本

* 以上两条所依赖的平台版本必须一致

* 产品**开发**版可以依赖平台**开发**版
* 产品**发布**版必须依赖平台**发布**版

##### **同步** 修改前端、后端、API 等版本号

> 后端修改开发版本号 build.gradle 中的 `version` 中的版本号

> 接口文档调整 API 仓库的 `api.raml` 和 `package.json` 中的版本号

* 提交代码打 tag，推送远程仓库
* 编写发布说明

> 就医后端发布说明在 https://github.com/propersoft-cn/ihos/releases 中编写

* 区分发布分支和开发分支，发布分支的内容自动合并至开发分支，在开发分支发布新版本后，将开发分支中的内容合并至发布分支

### 衍生项目

* 项目**开发**版可以依赖产品和平台的**开发**版
* 项目**发布**版必须依赖产品和平台的**发布**版


开发者文档
--------

[Proper Enterprise Platform Developer Guidelines](./CONTRIBUTING.md)
