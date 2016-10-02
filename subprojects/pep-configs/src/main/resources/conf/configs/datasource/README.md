Profiles
--------

平台定义了三个 `profile` 版本，分别为：

* `dev`：开发版，使用属性文件 `datasource-dev.properties`
* `test`：测试版，使用属性文件 `datasource-test.properties`
* `production`：产品版，使用属性文件 `datasource-production.properties`

可以在 `web.xml` 中修改应用所使用的版本，默认为`开发版`

    <context-param>
        <param-name>spring.profiles.default</param-name>
        <param-value>dev</param-value>
    </context-param>

三个版本配置文件中主要区别如下：

1. 数据源相关属性：`database.*`、`mongodb.*`
1. `hibernate.hbm2ddl.auto`：产品版使用 `update`，其余版本使用 `create-drop`
1. 暂未用到的属性：

        hibernate.current_session_context_class=org.springframework.orm.hibernate4.SpringSessionContext
        jdbc.use_scrollable_resultset=false