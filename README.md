proper-enterprise-platform
==========================

Proper Enterprise Platform


主要技术选型
-----------

|Catalog|Version|
|:--|:--|
|JDK|oraclejdk7|
|Servlet|3.0.1|
|JSP|2.2|
|JSTL|1.2|
|Tomcat|7.0.63|
|Spring|spring-framework 4.1.7.RELEASE|
|Security|spring-security 4.0.1.RELEASE|
|Logging|Slf4j 1.7.12，Logback 1.1.3|
|Cache|ehcache 2.10.0|
|Validator|Hibernate Validator 5.1.3.Final|
|ORM|Hibernate ORM 4.3.10.Final|
|数据库，事务||
|连接池|[HikariCP](https://github.com/brettwooldridge/HikariCP) 2.4.0|
|任务调度|quartz 2.2.1|
|Unit Test|junit 4.12, spock 0.7|
|通用工具|guava 18.0|
|编码及加密|common-codec，bounce-casle|
|xml|dom4j|
|JSON|fastjson 1.2.4|
|消息队列||
|前端框架||
|自动构建|Gradle 2.5|
|持续集成||
|license||


开发规范
-------

**TODO**


项目构建
--------

项目构建依托 [gradle](http://www.gradle.org) 构建工具，`gradlew` 是 `gradle warpper`，可不依赖本地的 `gradle` 环境进行构建。若本地已有 `gradle` 构建环境，使用本地环境也可。`wrapper` 中使用的 `gradle` 版本为 `2.5`。

若需使用 `gradle` 的 `eclipse` 插件，可参考 [wiki](https://github.com/proper4git/proper-uip/wiki) 中 [eclipse gradle 插件使用](https://github.com/proper4git/proper-uip/wiki/eclipse-gradle-%E6%8F%92%E4%BB%B6%E4%BD%BF%E7%94%A8)

### 整体构建

    $ ./gradlew build

### 子项目独立构建

以 `pep-core` 子项目为例

    $ ./gradlew pep-core:build

项目构建会对源码进行编译、进行代码质量检查、执行单元测试、构建 `jar` 包。构建的内容输出到各子项目的 `build` 路径内。

> 在 `Windows` 环境下使用时，需使用 [gradlew.bat](gradlew.bat) 批处理指令，构建命令也需相应变化，如整体构建命令为 `gradlew build`


开发环境
-------

目前仅支持作为 `eclipse` 工程导入。导入前需先生成 `eclipse` 使用的配置文件：

    $ ./gradlew cleanEclipse eclipse

之后可在 `eclipse` 中选择项目根路径，会自动导入所有子项目。

若某子项目依赖的库有变化，需重新生成其 `eclipse` 配置文件时，可单独更新该子项目的配置，以 `pep-core` 子项目为例：

    $ ./gradlew pep-core:cleanEclipse pep-core:eclipse
    
导入后的项目会自动配置好 `java` 项目或 `web` 项目的类别，`web` 项目可以直接发布到 `tomcat` 下进行开发。


初始化新模块
----------

例如要新增一个 `pep-test-init` 模块，可以通过下面的任务初始化该模块的基本路径及文件：

    $ ./gradlew init-pep-test-init

初始化内容包括：

    subprojects/pep-test-init
    subprojects/pep-test-init/src/main/java
    subprojects/pep-test-init/src/main/resources
    subprojects/pep-test-init/src/main/resources/conf/test/init
    subprojects/pep-test-init/src/main/resources/conf/test/init/test-init-context.xml
    subprojects/pep-test-init/src/main/resources/conf/test/init/test-init.properties
    subprojects/pep-test-init/src/test/groovy
    subprojects/pep-test-init/src/test/resources
    subprojects/pep-test-init/pep-test-init.gradle
    subprojects/pep-test-init/README.md

> 注意：初始化任务执行时会先将改模块根路径删除


构建源码包
---------

    $ ./gradlew sourcesJar

或

    $ ./gradlew pep-core:sourcesJar


构建 `war` 包
------------

平台只包含一个 web 应用，即 `pep-webapp`，其余模块以 `jar` 包形式被其依赖

    $ ./gradlew pep-webapp:clean pep-webapp:war

构建好的 `war` 包会输出到 `pep-webapp` 项目根路径下的 `build/libs` 路径内。
