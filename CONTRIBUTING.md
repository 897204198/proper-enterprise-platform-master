Proper Enterprise Platform Developer Guidelines
===============================================


开发环境
-------

### IntelliJ IDEA

可使用 IDEA 直接打开项目路径，自动导入 gradle 项目。

或者手动生成 idea 配置文件后，直接打开，如：

    $ ./gradlew cleanIdea idea

> 在 IDEA 中需要配置为 gradle 项目才可部署至 tomcat 中开发


Spring Boot 开发运行及调试
------------------------

Spring Boot 内置 tomcat 使用默认端口 `8080`，上下文根 `pep` 在 [application.properties](./subprojects/pep-application/src/main/resources/application.properties) 中配置。

### 直接运行

    $ ./gradlew bootRun
    
> `bootRun` 任务会将源码编译后启动运行。

### 远程调试

    $ ./gradlew bootRun --debug-jvm

IDEA 开启远程调试方式可参见 [IntelliJ Remote Run/Debug Configuration](http://www.jetbrains.com/idea/webhelp/run-debug-configuration-remote.html)


修改使用的关系型数据源
-------------------

平台开发及测试环境（profile）默认使用 h2 数据库作为关系型数据源，生产环境默认使用 mysql 数据源。

当在开发环境需要调整数据源时，需调整如下两处内容：

1. [application-dev.properties](./subprojects/pep-application/src/main/resources/application-dev.properties) 中设定需要使用的数据源连接信息
1. [pep-application.gradle](./subprojects/pep-application/pep-application.gradle) 中添加所需数据源的驱动作为运行时依赖，并选择相应数据源的 oopsearch 同步组件

    例如将默认的 h2 数据源修改为 mysql 时，需将
    
    ```
    runtimeOnly libraries.h2,
                project(':pep-oopsearch-sync-h2')
    ```
    
    修改为
    
    ```
    runtimeOnly libraries.mysql,
                project(':pep-oopsearch-sync-mysql')
    ```

    
初始化新模块
----------

例如要新增一个 `pep-test-init` 模块，可以通过下面的任务初始化该模块的基本路径及文件：

    $ ./gradlew buildCode -Pmodel=pep-test-init 
支持参数
 
    -Pmodel 模块名 example  -Pmodel=pep-isme             required 
    -Pbusiness 模块下业务名 example  -Pbusiness=api              
    -PparentPath 模块父包名 example  -PparentPath=pep-is/ 
    
初始化内容包括：

    模块的包路径，properties, spring 配置类及增删改查的基础代码和配套的单元测试

> 注意：初始化任务执行时会先将该模块根路径删除


测试
----

* 单元测试 `$ ./gradlew test`
* 测试及代码质量检查 `$ ./gradlew check`
* 单元测试覆盖 bean 的方法：因为单体测试类可能会因为具体的测试功能需要不同的测试数据，之前的做法是在测试文件中添加一个 Mock 接口的实现类并通过 `@Primary` 将 Mock 的实现类设置为主要实现类，例如：
    ```
    @Primary
    public class MockDepartmentServiceImpl extends DepartmentServiceImpl implements DepartmentService {
        @Override
        List<Department> getAllDepartments() {
            ... ...
        }
    }
    ```
  但是这样处理后当出现有多个不同的测试数据需求时很难进行分别处理，如何解决这个问题呢？
  单体测试可以使用从 Spring 上下文中获取具体实现类，通过新的实现类重写接口方法进行测试验证并在测试验证后将原有的实现类放回上下文中，具体操作如下：
  第一步：通过上下文获取目标接口的实现类，如：
    ```
    DepartmentExtService departmentExtService = PEPApplicationContext.getBean("departmentExtService");
    ```
  第二步：通过 `overrideSingleton` 方法中 使用 `@Override` 方法重写该接口的方法返回所需测试数据，如：
    ```
    overrideSingleton("departmentExtService", new DepartmentExtService() {
        @Override
        List<Department> getAllDepartments() {
            ... ...
        }
    })
    ```
  第三步：在验证测试数据后，需要将原接口的实现类放回至 Spring 上下文中，这样就不会对其他的测试类造成影响，如：
    ```
    overrideSingleton("departmentExtService", departmentExtService);
    ```
  最后，还有一点需要说明，如果要覆盖的 bean 已被其他 bean 所引用，则需要在测试类结束前恢复 **所有** 相关类的实例，否则可能会对其他需要用到未覆盖前 bean 的行为的单元测试造成意外影响！详细可见此 [示例](https://github.com/propersoft-cn/ihos/pull/1531)

**推送代码至远程仓库或创建 `Pull Request` 之前需确保所有测试及检查能够在本地通过**


连接前后台调试
------------

1. 使用静态资源

- 准备一个nginx 将静态资源包解压到html目录，将静态资源粘贴到html根目录
- 配置nginx，直接访问即可用。

		location ^~ /workflow {
		    #服务端流程静态文件存放地址（具体地址自行修改）
			proxy_pass http://127.0.0.1:8082/pep/workflow;
		}
		
		location ^~ /pep {
		    #服务端地址（具体地址自行修改）
			proxy_pass http://127.0.0.1:8082/pep;
		}

1.使用已提供好的的环境
- 后端启动成功后，Ctrl+Alt+鼠标左键，根据需要选择想要连接[前端CD环境](https://propersoft-cn.github.io/Yellow-Page.html#%E5%89%8D%E7%AB%AF)的地址


开发规范
-------

* 统一使用 `PEPConstants.VERSION` 作为 `serialVersionUID`
* 配置类：
    1. @ConfigurationProperties 注解可以把同类的配置信息自动封装成实体类，`com.proper.enterprise.platform.<module>..*Properties`放置在各个模块中
    2. @Configuration 注解标注在类， 命名规则为：`com.proper.enterprise.platform.<module>..*Configuration`放置在各个模块中
    3. 当 Properties 需要多个层级，可以加一级 properties 路径，如 `com.proper.enterprise.platform.configs.properties.*Properties`
* Controller：`com.proper.enterprise.platform.<module>..controller.*Controller`，Controller 放置在各模块中。RESTFul Controller 可以继承 `BaseController`，以方便响应 RESTFul 请求。可参考 `UsersController`
* 服务接口：`com.proper.enterprise.platform.<module>..service.*Service`
* 服务实现：`com.proper.enterprise.platform.<module>..service.impl.*ServiceImpl`

  > 服务里 `get` 用来命名最多得到**一个**结果的查询，`find` 用来命名得到**集合**的查询；尽量使用动词开头命名 service 中的方法，如 `save*`、`delete*` 等，平台也将根据这些动词对 service 层中的方法进行事务控制。仅需要保存或改动数据的方法会得到可写事务，其余方法均只能使用只读事务。具体事务配置请参照 [applicationContext-jpa-transaction.xml](./subprojects/fundamental/pep-core/pep-core-jpa/src/main/resources/applicationContext-jpa-transaction.xml)
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
    > 字段属性采用驼峰式命名，数据库字段名自动转换为蛇形命名。`boolean` 类型的字段需统一设定 `@Type(type = "yes_no")` 注解。
    > 类属性必须使用包装数据类型，不要设定任何属性默认，必须自己显式地进行赋值，任何 NPE 问题，或者入库检查，都由使用者来保证。
    
* 实体间关系按照 JPA 规范通过注解指定。为避免关系的初始数据重复插入，需在定义关系时定义唯一性约束，如：
   ```
   // UserEntity
   @ManyToMany
   @JoinTable(name = "PEP_AUTH_USERS_ROLES",
              joinColumns = @JoinColumn(name = "USER_ID"),
              inverseJoinColumns = @JoinColumn(name = "ROLE_ID"),
              uniqueConstraints = @UniqueConstraint(columnNames = {"USER_ID", "ROLE_ID"}))
   private Collection<RoleEntity> roleEntities = new ArrayList<>();

   // RoleEntity
   @ManyToMany(mappedBy = "roleEntities")
   private Collection<UserEntity> userEntities = new ArrayList<>();
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
* JPA Repository提供updateForSelective，更新单表非空字段

* 单元测试：与被测试的类相同路径，被测试类名称为测试类名前缀，基于 `Junit` 的测试以 `Test` 为后缀。继承 `AbstractSpringTest`是针对controller 进行模拟的请求，如若有事务或者数据库相关，则需继承 `AbstractJPATest`；继承`AbstractIntegrationTest` 为集成测试基类，会真正启动服务，通过 http 请求调用接口，可以测试 filter、事务等在 AbstractSpringTest 和 AbstractJPATest 中无法测试的内容 如：

    ```
    class AllowCrossOriginFilterTest extends AbstractIntegrationTest 
    ```
* 建表及初始化数据：通过 Liquibase 的 Change Log 执行数据库建表语句及初始化数据，具体使用方式可参照 [说明文档](https://oopstorm.github.io/2018/05/15/2018-05-15-liquibase-with-gradle/)。
Change Log 文件放在对应模块下`src/main/resources/liquibase/changelogs`， 为保证 DDL 优先于 DML 执行，命名规范为 `changelog-ddl-*.xml` 和 `changelog-dml-*.xml`。

* 当有较大版本升级时(例如：v0.4.x 升级到v0.5.x)，需要按照重整基线叠加变更的方式处理：changelog 按版本放置，历史版本(v0.4.x) 如：`src/main/resources/0.4.x/history/changelog-ddl-*.xml` 或者 `src/main/resources/0.4.x/history/changelog-dml-*.xml` ，新的版本的基线 如：`src/main/resources/0.5.x/init/changelog-ddl-*.xml`或者 `src/main/resources/0.5.x/init/changelog-dml-*.xml`，累计变更仍需放在原有的changelogs下 如：`src/main/resources/changelogs/changelog-ddl-*.xml`或者 `src/main/resources/changelogs/changelog-dml-*.xml`

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


版本号规范
---------

版本号参照 [语义化版本](https://github.com/semver/semver/blob/master/semver.md) 规范，采用四位数字，如：`1.1522.49.327`

- 第一位表示`主版本`
- 第二位表示`次版本`
- 第三位表示`修订版`
- 第四位表示`bug 修正` 等

第四位为可选位，没有第四位时，第三位也可以表示 `bug 修正` 等含义。

> 前台、后台工程版本号需尽量保持一致（主版本及次版本号必须一致），方便两个工程对应。


Change Log 规范
---------------

Change log 按版本放置（主版本或次版本），每个版本划定一个 Change Log 的基线，从基线开始变化。

#### 路径规范

以 v0.4.x 和 v0.5.x 两个版本为例：

* 平台

```
resources/liquibase/0.5.x/base —— v0.5.x 基线初始化语句
resources/liquibase/0.5.x/base/changelog-ddl-{module}.xml
resources/liquibase/0.5.x/base/changelog-dml-{module}.xml
resources/liquibase/0.5.x/changelogs —— v0.5.x 累积变更
resources/liquibase/0.5.x/changelogs/changelog-ddl-{module}.xml
resources/liquibase/0.5.x/changelogs/changelog-dml-{module}.xml
resources/liquibase/0.4.x/changelogs —— v0.4.x 累积变更
resources/liquibase/0.4.x/changelogs/changelog-ddl-{module}.xml
resources/liquibase/0.4.x/changelogs/changelog-dml-{module}.xml
```

* 产品

```
resources/liquibase/icmp/3.x/base
resources/liquibase/icmp/3.x/base/changelog-ddl-{module}.xml
resources/liquibase/icmp/3.x/base/changelog-dml-{module}.xml
resources/liquibase/icmp/3.x/changelogs
resources/liquibase/icmp/3.x/changelogs/changelog-ddl-{module}.xml
resources/liquibase/icmp/3.x/changelogs/changelog-dml-{module}.xml
resources/liquibase/icmp/2.x/changelogs
resources/liquibase/icmp/2.x/changelogs/changelog-ddl-{module}.xml
resources/liquibase/icmp/2.x/changelogs/changelog-dml-{module}.xml
```

* 项目

```
resources/liquibase/icmp/propersoft/2.x/base
resources/liquibase/icmp/propersoft/2.x/base/changelog-ddl-{module}.xml
resources/liquibase/icmp/propersoft/2.x/base/changelog-dml-{module}.xml
resources/liquibase/icmp/propersoft/2.x/changelogs
resources/liquibase/icmp/propersoft/2.x/changelogs/changelog-ddl-{module}.xml
resources/liquibase/icmp/propersoft/2.x/changelogs/changelog-dml-{module}.xml
resources/liquibase/icmp/propersoft/1.x/changelogs
resources/liquibase/icmp/propersoft/1.x/changelogs/changelog-ddl-{module}.xml
resources/liquibase/icmp/propersoft/1.x/changelogs/changelog-dml-{module}.xml
```

#### 各种阶段和情况应对方案

以 v0.4.x 和 v0.5.x 两个版本为例：

* 处于 v0.4.x，不想升级到 v0.5.x —— 使用平台 v0.4.x 版本即可，无影响
* 处于 v0.4.x，想升级到 v0.5.x —— 代码先升级到 v0.5.x，includeAll liquibase/0.4.x/changelogs && liquibase/0.5.x/changelogs
> 如果已经执行过 v0.4.x 中规范路径前的 changelog，需要修改数据库中的记录，将执行过的 changelog 文件的路径修改为规范后的路径
> 需执行语句：update DATABASECHANGELOG set FILENAME = replace(FILENAME,'liquibase/changelogs/changelog-','liquibase/0.5.x/base/changelog-')
* v0.5.x brand new —— includeAll liquibase/0.5.x/base && liquibase/0.5.x/changelogs 下内容即可
* 处于 v0.5.0-SNAPSHOT —— 修改已执行过的 changelog 记录，将路径调整为规范后的路径，之后 includeAll liquibase/0.5.x/changelogs 即可
> 多出了基线的 base 路径 —— 不需要 include 进去，因为应该在 SNAPSHOT 的 changelog 中都已经执行过了


版本发布流程
-----------

1. 后端修改 build.gradle 中的 `version` 值，并同步修改 PEPVersion.java 中的 版本号 及 dependencies.gradle 中的 `versions.pep` 
1. 提交代码打 tag，并将 tag 推送远程仓库，如：
    ```
    $ git tag v0.4.0
    $ git push upstream v0.4.0
    ```
1. 编写 [Release Note](https://github.com/propersoft-cn/proper-enterprise-platform/releases)

> 注意区分发布分支（master）和开发分支（develop），发布分支的内容自动合并至开发分支，在开发分支发布新版本后，将开发分支中的内容合并至发布分支


开放问题
-------

* HTTP 500 状态码问题：通常系统异常时应该返回 `500` 状态码。
但在与 `nginx` 共同部署时，`nginx` 连接上游服务器超时时也会返回 500 状态。
当需要故障转移时，就会出现矛盾：因为连接某一台上游服务器超时，其他服务器仍然可能可以处理这个请求；但若请求会导致系统抛异常，其他服务器再处理这个请求应该也会得到同样的结果。
当前对这个问题的处理方式是：
***PEP 平台返回的系统异常和业务异常仍然使用 `500` 作为响应的状态码，但会增加一个特殊的响应头 `X-PEP-ERR-TYPE`，并使用这个响应头的内容区分系统异常和业务异常。
系统异常值为 `PEP_SYS_ERR`，业务异常值为 `PEP_BIZ_ERR`。***

**TO BE CONTINUED**


持续集成环境
----------

平台使用 [TeamCity](https://www.jetbrains.com/teamcity/) 作为持续集成环境。
为保证代码质量，任何提交到 `master` 分支的代码和任何 `Pull Request` 都会触发持续集成环境对代码质量的检查。

Pull Request 的构建结果会直接在列表页和详细信息页面展现

![](https://hhariri.files.wordpress.com/2013/02/image.png)

master 分支的构建结果会在项目首页展现

<a href="https://cloud.propersoft.cn/teamcities/viewType.html?buildTypeId=ProperEnterprise_PepParallel">
  <img src="https://cloud.propersoft.cn/teamcities/app/rest/builds/buildType:(id:ProperEnterprise_PepParallel)/statusIcon.svg"/>
</a>

测试覆盖率结果也会在项目首页展现

[![codecov](https://codecov.io/gh/propersoft-cn/proper-enterprise-platform/branch/master/graph/badge.svg?token=uthbnLL68t)](https://codecov.io/gh/propersoft-cn/proper-enterprise-platform)

**每位工程师都要为项目构建失败或覆盖率下降负责！**
