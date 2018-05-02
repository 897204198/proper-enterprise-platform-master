pep-test
========

Proper Enterprise Platform

Test Fixtures

平台测试基础模块，其他模块需要进行测试时可依赖本模块

由于一些集成测试需要用到 `pep-webapp` 模块中的配置文件，在 `gradle` 对本模块进行编译的时候会自动同步 `pep-webapp` 中的配置文件（resources/conf 及 resources/spring）

若不使用 `gradle` 编译，还可以使用 `gradle` 任务进行同步，同步方式如下：

    $ ./gradlew syncCtx