Profiles
--------

平台定义了三个 `profile` 版本，分别为：

* `dev`：开发版，使用属性文件 `datasource-dev.properties`
* `test`：测试版，使用属性文件 `datasource-test.properties`（在 `pep-test` 模块中）
* `production`：产品版，使用属性文件 `datasource-production.properties`

可以在 `web.xml` 中修改应用所使用的版本，默认为`开发版`

    <context-param>
        <param-name>spring.profiles.default</param-name>
        <param-value>dev</param-value>
    </context-param>

三个版本配置文件中主要区别如下：

1. 数据源相关属性：`database.*`
1. `hibernate.hbm2ddl.auto`：产品版及开发版使用 `none`（依赖 `liquibase` 创建表结构及初始化数据），测试版本使用 `create-drop`（不使用 `liquibase`）
1. 固定数目连接池个数（`database.maxPoolSize`），生产环境需根据实际硬件情况进行调整
1. `liquibase` 使用的 profile，与 datasource 的 profile 保持一致即可
