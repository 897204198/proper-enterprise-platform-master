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
|数据库，事务，连接池||
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


初始化新模块
----------

例如要新增一个 `pep-test-init` 模块，可以通过下面的任务初始化该模块的基本路径及文件

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


