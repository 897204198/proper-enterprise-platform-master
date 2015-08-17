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
|Tomcat|[7.0.63](http://tomcat.apache.org/download-70.cgi)|
|Spring|[Spring Framework 4.1.7.RELEASE](https://github.com/spring-projects/spring-framework/tree/v4.1.7.RELEASE)|
|Security|[Spring Security 4.0.1.RELEASE](https://github.com/spring-projects/spring-security/tree/4.0.1.RELEASE)|
|Logging|[SLF4J 1.7.12](https://github.com/qos-ch/slf4j/tree/v_1.7.12)<br/>[Logback 1.1.3](https://github.com/qos-ch/logback)|
|Cache|ehcache 2.10.0|
|Validator|Hibernate Validator 5.1.3.Final|
|数据库|[H2 Database 1.3.176](https://github.com/h2database/h2database/tree/version-1.3/version-1.3.176/h2)|
|Connection Pool|[HikariCP 2.4.0](https://github.com/brettwooldridge/HikariCP/tree/HikariCP-2.4.0)|
|JPA Provider|[Hibernate EntityManager 4.3.10.Final](https://github.com/hibernate/hibernate-orm/tree/4.3.10.Final)|
|Data Access Layer|[Spring Data JPA 1.8.2.RELEASE](https://github.com/spring-projects/spring-data-jpa/tree/1.8.2.RELEASE)|
|任务调度|quartz 2.2.1|
|Unit Test|junit 4.12, spock 0.7|
|通用工具|guava 18.0|
|编码及加密|common-codec，bounce-casle|
|xml|dom4j|
|JSON|fastjson 1.2.4|
|消息队列||
|前端框架||
|Build System|[Gradle 2.5](https://github.com/gradle/gradle)|
|持续集成||
|license||


开发规范
-------

**TODO**

* 接口：`com.proper.enterprise.platform.api.[module]..service.*Service`
* 实现：`com.proper.enterprise.platform.[module]..service.impl.*ServiceImpl`
* 实体：`com.proper.enterprise.platform.[module]..entity.*Entity`
    > 实体类需继承基类 `BaseEntity`，表名规则为 `pep_[module]_[name]`，缓存区域为实体类全路径。如：
       
    ```
    @Entity
	@Table(name = "pep_auth_user")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "com.proper.enterprise.platform.auth.entity.UserEntity")
	public class UserEntity extends BaseEntity
    ```
        
* DTO：`com.proper.enterprise.platform.[module]..dto.*DTO`
    > 为避免使用 `openSessionInView` 模式，使用 `DTO` 储存实体中数据，以供数据传输及界面显示使用（合并 `VO` 职能）。`DTO` 中需提供根据相应实体构造 `DTO` 的构造函数
* Repository：`com.proper.enterprise.platform.[module]..repository.*Repository`，需继承 `JpaRepository`
* 单元测试：与被测试的类相同路径，被测试类名称为测试类名前缀，基于 `Junit` 的测试以 `Test` 为后缀，基于 `Spock` 的测试以 `Spec` 为后缀
* 集成测试：`com.proper.enterprise.platform.integration.**.*IntegTest`
    > 集成测试继承 `AbstractIntegTest`，如：

    ```
    class Crud extends AbstractIntegTest
    ```


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

### Eclipse

导入前需先生成 `eclipse` 使用的配置文件：

    $ ./gradlew cleanEclipse eclipse

之后可在 `eclipse` 中选择项目根路径，会自动导入所有子项目。

若某子项目依赖的库有变化，需重新生成其 `eclipse` 配置文件时，可单独更新该子项目的配置，以 `pep-core` 子项目为例：

    $ ./gradlew pep-core:cleanEclipse pep-core:eclipse
    
导入后的项目会自动配置好 `java` 项目或 `web` 项目的类别，`web` 项目可以直接发布到 `tomcat` 下进行开发。

### IntelliJ IDEA

    $ ./gradlew cleanIdea idea


测试
----

* 单元测试 `$ ./gradlew test`
* 集成测试 `$ ./gradlew integTest`
* 测试及代码检查 `$ ./gradlew check`


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

    $ ./gradlew clean war

构建好的 `war` 包会输出到 `pep-webapp` 项目根路径下的 `build/libs` 路径内。
