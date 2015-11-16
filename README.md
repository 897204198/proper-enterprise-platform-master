proper-enterprise-platform
==========================

Proper Enterprise Platform


主要设计目标
----------

* 以 [Spring Framework](http://projects.spring.io/spring-framework/) 为基础，采用当前主流且活跃的开源技术和框架，以简化开发步骤、提升开发效率和质量为目标，稳定且可持续发展为愿景，对平台进行架构
* 平台功能按模块进行划分及构建，除核心功能模块和公共模块外，各模块尽量保持独立，且可按需组合
* 部署方式可集中可分布。集中式部署时为一个 web 应用，分布式部署时为多个 web 应用。分布式部署时通过反向代理保持部署方式对用户的透明，系统内部无需单点登录
* 模块间需要依赖时，面向接口和服务编程，以便模块按不同数据来源提供不同实现
* 屏蔽对具体数据库的依赖，初始化数据以 `SQL` 形式组织
* 平台功能及特性需有对应的功能及性能测试


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
|Cache|[ehcache](http://ehcache.org/)-core 2.6.9|
|Validator|Hibernate Validator 5.1.3.Final|
|数据库|[H2 Database 1.3.176](https://github.com/h2database/h2database/tree/version-1.3/version-1.3.176/h2)|
|Connection Pool|[HikariCP 2.4.0](https://github.com/brettwooldridge/HikariCP/tree/HikariCP-2.4.0)|
|JPA|[Java Persistence 2.1](https://jcp.org/aboutJava/communityprocess/final/jsr338/index.html)|
|JPA Provider|[Hibernate EntityManager 4.3.10.Final](https://github.com/hibernate/hibernate-orm/tree/4.3.10.Final)|
|Data Access Layer|[Spring Data JPA 1.8.2.RELEASE](https://github.com/spring-projects/spring-data-jpa/tree/1.8.2.RELEASE)|
|任务调度|quartz 2.2.1|
|Unit Test|[junit 4.12](https://github.com/junit-team/junit/tree/r4.12)<br />[spock 0.7](https://github.com/spockframework/spock/tree/spock-0.7-groovy-2.0)|
|通用工具|guava 18.0|
|编码及加密|[common-codec 1.10](https://github.com/apache/commons-codec/tree/1.10)<br />bounce-casle|
|xml|dom4j|
|JSON|[jackson-databind 2.3.6](https://github.com/FasterXML/jackson-databind/tree/jackson-databind-2.6.3)|
|消息队列||
|前端框架||
|Build System|[Gradle 2.8](https://github.com/gradle/gradle)|
|持续集成||
|license||


开发规范
-------

**TODO**

* 配置文件：
    1. spring 配置文件放置在 `src/main/resources/spring/[module]
    2. 其他配置文件放置在 `src/main/resources/conf/[module]

        > 可参考 `./gradlew init-pep-[module]` 自动生成的配置文件
        
* 页面：`pep-webapp/src/main/webapp/WEB-INF/views/[module]/../*.jsp`
* Controller：`com.proper.enterprise.platform.webapp.[module]..controller.*Controller`，Controller 都放置在 `pep-webapp` 模块中
* 服务接口：`com.proper.enterprise.platform.api.[module]..service.*Service`
* 服务实现：`com.proper.enterprise.platform.[module]..service.impl.*ServiceImpl`
* 数据实体：`com.proper.enterprise.platform.[module]..entity.*Entity`
    > 实体类需继承基类 `BaseEntity`，且必须有可用的默认构造函数；表名规则为 `pep_[module]_[name]`；需缓存的表要添加 `CacheEntity` 注解（`CacheEntity` 注解为实体开启 `JPA` 缓存及 `Hibernate` 二级缓存，可以用作大部分实体的通用配置。如实体有特殊需求，也可自行设置）。如：
       
    ```
    @Entity
	@Table(name = "pep_auth_user")
	@CacheEntity
	public class UserEntity extends BaseEntity
    ```

* 数据接口：数据实体以数据接口和数据传输对象对外提供服务，以便可以从其他数据来源获得数据。数据接口只提供 getter 和 setter 方法，且当代表的数据实体继承自 `BaseEntity` 时，数据接口需扩展 `IBase` 接口，如：

    ```
    public interface User extends IBase
    ```

* 数据传输对象DTO：`com.proper.enterprise.platform.[module]..dto.*DTO`
    > 为避免使用 `openSessionInView` 模式，使用 `DTO` 储存实体中数据，以供数据传输及界面显示使用（合并 `VO` 职能）。`DTO` 中需提供根据相应实体构造 `DTO` 的构造函数。DTO 实现的接口扩展了 `IBase` 接口时，可通过继承 `BaseDTO` 的方式实现所需方法，如：
    
    ```
    public class UserDTO extends BaseDTO implements User
    ```

* Repository：`com.proper.enterprise.platform.[module]..repository.*Repository`，需继承 `BaseRepository`；需缓存的方法需添加 `CacheQuery` 注解，且对应 `Entity` 也需要有 `CacheEntity` 注解标识：

    ```
    public interface UserRepository extends BaseRepository<UserEntity, String> {
    
        @CacheQuery
        UserEntity findByLoginName(String loginName);
    
    }
    ```

* 单元测试：与被测试的类相同路径，被测试类名称为测试类名前缀，基于 `Junit` 的测试以 `Test` 为后缀，基于 `Spock` 的测试以 `Spec` 为后缀
* 集成测试：`com.proper.enterprise.platform.integration.**.*IntegTest`
    > 集成测试继承 `AbstractIntegTest`，如：

    ```
    class Crud extends AbstractIntegTest
    ```

* 建表：Hibernate 按照 JPA 的注解自动创建
* 初始化数据：sql 语句放在对应模块 `src/main/resources/sql/*.sql`，系统运行时自动执行


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

平台只包含一个 web 应用，即 `pep-webapp`，其余模块以 `jar` 包形式被其依赖

    $ ./gradlew clean war

构建好的 `war` 包会输出到 `pep-webapp` 项目根路径下的 `build/libs` 路径内。
