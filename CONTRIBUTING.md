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

默认使用 tomcat7.x，端口 `9090`，上下文根 `pep`。

### 直接运行

    $ ./gradlew assemble run

> `assemble` 任务的作用是将各个模块均进行编译。当前 web 应用模块 `pep-webapp` 只是将其他模块作为运行时依赖，gradle tomcat 插件在运行时没自动编译这些运行时依赖，故需要手动执行编译，否则会报依赖的包不存在

### 远程调试

    $ ./gradlew assemble debug


IDE 开启远程调试方式可参见：

* [IntelliJ Remote Run/Debug Configuration](http://www.jetbrains.com/idea/webhelp/run-debug-configuration-remote.html)
* [Eclipse Remote Debugging](http://help.eclipse.org/indigo/index.jsp?topic=%2Forg.eclipse.jdt.doc.user%2Fconcepts%2Fcremdbug.htm)

### 热部署修改

    $ ./gradlew assemble

> assemble 任务会将源码重新编译，class 更新后会被 `spring-loaded` 自动加载，实现热部署效果


开发规范
-------

**TODO**

* 统一使用 PEPConstants.VERSION 作为 serialVersionUID
* 配置文件：
    1. spring 配置文件放置在 `src/main/resources/spring/[module]`。spring 配置文件采用各个模块统一入口的方式来组织，文件名为 `applicationContext-[module].xml`。在入口中将模块内的配置文件关联起来。需注意避免重复 import。
    2. 其他配置文件放置在 `src/main/resources/conf/[module]

        > 可参考 `./gradlew init-pep-[module]` 自动生成的配置文件

* Controller：`com.proper.enterprise.platform.[module]..controller.*Controller`，Controller 放置在各模块中
* 服务接口：`com.proper.enterprise.platform.api.[module]..service.*Service`
* 服务实现：`com.proper.enterprise.platform.[module]..service.impl.*ServiceImpl`
* 数据接口：数据实体以数据接口和数据传输对象对外提供服务，以便可以从其他数据来源获得数据。数据接口只提供 getter 和 setter 方法，且当代表的数据实体继承自 `BaseEntity` 时，数据接口需扩展 `IBase` 接口，如：

    ```
    public interface User extends IBase
    ```

* 数据实体：`com.proper.enterprise.platform.[module]..entity.*Entity`
    > 实体类需继承基类 `BaseEntity`，且必须有默认的构造函数（否则 JPA 查询的时候会报错）；表名规则为 `PEP_[MODULE]_[NAME]`，表名字段名大写；需缓存的表要添加 `CacheEntity` 注解（`CacheEntity` 注解为实体开启 `JPA` 缓存及 `Hibernate` 二级缓存，可以用作大部分实体的通用配置。如实体有特殊需求，也可自行设置）。如：

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

* Repository：`com.proper.enterprise.platform.[module]..repository.*Repository`，需继承 `BaseRepository`；需缓存的方法需添加 `CacheQuery` 注解，且对应 `Entity` 也需要有 `CacheEntity` 注解标识：

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

<a href="http://202.199.100.174/teamcity/viewType.html?buildTypeId=PEP_Build">
  <img src="http://202.199.100.174/teamcity/app/rest/builds/buildType:(id:PEP_Build)/statusIcon.svg"/>
</a>

测试覆盖率结果也会在项目首页展现

[![codecov](https://codecov.io/gh/propersoft-cn/proper-enterprise-platform/branch/master/graph/badge.svg?token=uthbnLL68t)](https://codecov.io/gh/propersoft-cn/proper-enterprise-platform)

**每位工程师都要为项目构建失败或覆盖率下降负责！**
