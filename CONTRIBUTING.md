Proper Enterprise Platform Developer Guidelines
===============================================


开发环境
-------

### IntelliJ IDEA

    $ ./gradlew cleanIdea idea

> 在 IDEA 中需要配置为 gradle 项目才可部署至 tomcat 中开发


内嵌 servlet 容器开发及调试
------------------------

平台使用 [gretty](https://github.com/akhikhl/gretty) gradle 插件以支持在内嵌的容器中运行及调试。

默认使用 tomcat7.x，端口 `8080`（在 [server.xml](configs/docker/tomcat/conf/server.xml) 中配置），上下文根 `pep`（在 [pep-webapp.gradle](subprojects/pep-webapp/pep-webapp.gradle) 中配置）。

### 直接运行

    $ ./gradlew assemble run

> `assemble` 任务的作用是将各个模块均进行编译。当前 web 应用模块 `pep-webapp` 只是将其他模块作为运行时依赖，gradle tomcat 插件在运行时没自动编译这些运行时依赖，故需要手动执行编译，否则会报依赖的包不存在

### 远程调试

    $ ./gradlew assemble debug


IDEA 开启远程调试方式可参见 [IntelliJ Remote Run/Debug Configuration](http://www.jetbrains.com/idea/webhelp/run-debug-configuration-remote.html)

### 热部署修改

    $ ./gradlew assemble

> assemble 任务会将源码重新编译，class 更新后会被 `spring-loaded` 自动加载，实现热部署效果


开发规范
-------

* 统一使用 `PEPConstants.VERSION` 作为 `serialVersionUID`
* 配置文件：
    1. spring 配置文件放置在 `src/main/resources/spring/<module>`。spring 配置文件采用各个模块统一入口的方式来组织，文件名为 `applicationContext-<module>.xml`。在入口中将模块内的配置文件关联起来。需注意避免重复 import。
    2. 其他配置文件放置在 `src/main/resources/conf/<module>`

        > 可参考 `./gradlew init-pep-<module>` 自动生成的配置文件

* Controller：`com.proper.enterprise.platform.<module>..controller.*Controller`，Controller 放置在各模块中。RESTFul Controller 可以继承 `BaseController`，以方便响应 RESTFul 请求。可参考 `UsersController`
* 服务接口：`com.proper.enterprise.platform.api.<module>..service.*Service`
* 服务实现：`com.proper.enterprise.platform.<module>..service.impl.*ServiceImpl`

  > 服务里 `get` 用来命名最多得到**一个**结果的查询，`find` 用来命名得到**集合**的查询；尽量使用动词开头命名 service 中的方法，如 `save*`、`delete*` 等，平台也将根据这些动词对 service 层中的方法进行事务控制。仅需要保存或改动数据的方法会得到可写事务，其余方法均只能使用只读事务。具体事务配置请参照 [applicationContext-transaction.xml](subprojects/pep-webapp-configs/src/main/resources/spring/dal/applicationContext-transaction.xml)
* 平台数据来源包括 RDB 和 MongoDB 两类
* 需操作关系型数据库时，可使用平台提供的 `BaseRepository` 按 `spring-data-jpa` 方式操作数据，也可使用 `NativeRepository` 使用 SQL 操作数据
* 使用 MongoDB 时，可继承 `spring-data-mongodb` 提供的 `MongoRepository`，也可直接注入平台定义好的 `mongoClient` 或 `mongoDatabase`
* 数据接口：数据实体以数据接口对外提供服务，以便可以从其他数据来源获得数据。数据接口只提供 getter 和 setter 方法，且当代表的数据实体继承自 `IBase` 时，数据接口需扩展 `IBase` 接口，如：

    ```
    public interface User extends IBase
    ```

* MongoDB 数据实体：`com.proper.enterprise.platform.<module>..document.*Document`，实体类需继承基类 `BaseDocument`
* JPA 数据实体：`com.proper.enterprise.platform.<module>..entity.*Entity`
    > 实体类需继承基类 `BaseEntity`，且必须有默认的构造函数（否则 JPA 查询的时候会报错）；表名规则为 `PEP_<module>_<NAME>`，表名字段名大写；需缓存的表要添加 `CacheEntity` 注解（`CacheEntity` 注解为实体开启 `JPA` 缓存及 `Hibernate` 二级缓存，可以用作大部分实体的通用配置。如实体有特殊需求，也可自行设置）。如：

    ```
    @Entity
	@Table(name = "PEP_AUTH_USER")
	@CacheEntity
	public class UserEntity extends BaseEntity implements User
    ```

* 同一张表被多个实体（继承关系）映射：以用户表为例，平台定义的基础用户实体（`UserEntity`）只包含必须的用户属性，当项目需要扩展用户表中的属性时，可以定义一个从 `UserEntity` 继承的新实体，如：`CustomUserEntity`。此时需要在基类中设定 `DiscriminatorColumn` 和 `DiscriminatorValue`（如果原本没有定义过的话），并在新继承出的类中定义 `DiscriminatorValue`。
    ```
    @Entity
  @Table(name = "PEP_AUTH_USERS")
  @DiscriminatorColumn(name = "pepDtype")
  @DiscriminatorValue("UserEntity")
  @CacheEntity
  public class UserEntity extends BaseEntity implements User

  @Entity
  @Table(name = "PEP_AUTH_USERS")
  @DiscriminatorValue("CustomUserEntity")
  @CacheEntity
  public class CustomUserEntity extends UserEntity implements User
    ```

    > `DiscriminatorColumn` 统一使用 `pepDtype`，以免默认字段 `DTYPE` 与数据库关键字冲突

* JPA Repository：`com.proper.enterprise.platform.<module>..repository.*Repository`，需继承 `BaseRepository`；需缓存的方法需添加 `CacheQuery` 注解，且对应 `Entity` 也需要有 `CacheEntity` 注解标识：

    ```
    public interface UserRepository extends BaseRepository<UserEntity, String> {

        @CacheQuery
        UserEntity findByUsername(String username);

    }
    ```

* 单元测试：与被测试的类相同路径，被测试类名称为测试类名前缀，基于 `Junit` 的测试以 `Test` 为后缀，基于 `Spock` 的测试以 `Spec` 为后缀。需要 Spring Context 的测试需继承 `AbstractTest` 基类，如：

    ```
    class CrudBaseTest extends AbstractTest
    ```

* 建表：Hibernate 按照 JPA 的注解自动创建
* 初始化数据：sql 语句放在对应模块 `src/main/resources/sql/*.sql`，系统运行时自动执行
* 异常不允许直接 `printStackTrace`，应记录到日志中
* 记录日志时，应采用 `Slf4j` 推荐的方式，避免日志信息通过 `+` 拼接

    ```
    LOGGER.debug("{} concat {} is {}", "a", "b", "ab");
    ```

* 当在日志消息中有非常消耗资源的操作时，可考虑先判断日志级别，如：

    ```
    if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("hello {} world {}", cheap, veryExpensive());
    }
    ```

**TO BE CONTINUED**


开放问题
-------

* HTTP 500 状态码问题：通常系统异常时应该返回 `500` 状态码。但在与 `nginx` 共同部署时，`nginx` 连接上游服务器超时时也会返回 500 状态。当需要故障转移时，就会出现矛盾：因为连接某一台上游服务器超时，其他服务器仍然可能可以处理这个请求；但若请求会导致系统抛异常，其他服务器再处理这个请求应该也会得到同样的结果。当前对这个问题的处理方式是：***`BaseController` 会将所有内部错误的 500 状态码转换为 `400`，`nginx` 仅对 `5xx` 的状态进行故障转移***。

**TO BE CONTINUED**


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


测试
----

* 单元测试 `$ ./gradlew test`
* 测试及代码质量检查 `$ ./gradlew check`

**推送代码至远程仓库或创建 `Pull Request` 之前需确保所有测试及检查能够在本地通过**


持续集成环境
----------

PEP 使用 [TeamCity](https://www.jetbrains.com/teamcity/) 作为持续集成环境。
为保证代码质量，任何提交到 `master` 分支的代码和任何 `Pull Request` 都会触发持续集成环境对代码质量的检查。

Pull Request 的构建结果会直接在列表页和详细信息页面展现

![](https://hhariri.files.wordpress.com/2013/02/image.png)

master 分支的构建结果会在项目首页展现

<a href="https://server.propersoft.cn/teamcity/viewType.html?buildTypeId=PEP_Build">
  <img src="https://server.propersoft.cn/teamcity/app/rest/builds/buildType:(id:PEP_Build)/statusIcon.svg"/>
</a>

测试覆盖率结果也会在项目首页展现

[![codecov](https://codecov.io/gh/propersoft-cn/proper-enterprise-platform/branch/master/graph/badge.svg?token=uthbnLL68t)](https://codecov.io/gh/propersoft-cn/proper-enterprise-platform)

**每位工程师都要为项目构建失败或覆盖率下降负责！**
