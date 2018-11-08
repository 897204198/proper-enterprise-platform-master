OOPSEARCH
==========================

```
// 接口
├── pep-oopsearch-api
// 配置
├── pep-oopsearch-config
// 基本功能
├── pep-oopsearch
// 关系型数据源数据同步到 oopsearch 中，h2/mysql 实现二选一
├── pep-oopsearch-sync-h2
├── pep-oopsearch-sync-mysql
// mongo 数据源数据同步到 oopsearch 中
└── pep-oopsearch-sync-mongo
```

主要设计目标
----------

* 实现智能查询，采用逆向思维设计
* 采用mongodb缓存业务模块需要查询的数据，从而达到在查询框输入内容即可快速查询mongodb，返回关联数据
* 联想内容根据输入框输入的内容自动进行关联，返回结果为包含输入内容，且是业务模块相关查询表当中的内容


调用oopsearch模块方式
------------------
下面以`authusers`模块为例进行具体内容讲解。
### 初始化`PEP_OOPSEARCH_CONFIG`表数据
```
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('001', 'authusers', 'pep_auth_users', 'id', 'string', 'id', 'authusersUserId', '/authusers');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('002', 'authusers', 'pep_auth_users', 'username', 'string', 'username', 'authusersUsername', '/authusers');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('003', 'authusers', 'pep_auth_users', 'name', 'string', 'name', 'authusersName', '/authusers');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('004', 'authusers', 'pep_auth_users', 'email', 'string', 'email', 'authusersEmail', '/authusers');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('005', 'authusers', 'pep_auth_users', 'phone', 'string', 'phone', 'authusersPhone', '/authusers');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('006', 'authusers', 'pep_auth_users', 'enable', 'string', 'enable', 'authusersEnable', '/authusers');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('007', 'authusers', 'pep_auth_usergroups', 'id', 'string', 'id', 'authusergroupsId', '/authusers');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('008', 'authusers', 'pep_auth_usergroups', 'name', 'string', 'name', 'authusergroupsName', '/authusers');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('009', 'authusers', 'pep_auth_usergroups', 'description', 'string', 'description', 'authusergroupsDescription', '/authusers');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('010', 'authusers', 'pep_auth_usergroups', 'enable', 'string', 'enable', 'authusergroupsEnable', '/authusers');
COMMIT;

```

>注意：目前配置模块的页面开发尚未完成。后续会使用页面来进行配置信息的管理。  
`module_name`与`url`为一对一关系；  
`module_name`与`table_name`为一对多关系；  
`table_name`与`search_column`为一对多关系；  
`search_column`与`column_type`、`column_desc`、`column_alias`为一对一关系  

>字段含义:  
`module_name`:模块名称  
`table_name`:查询使用到的表名  
`search_column`:查询的字段名  
`column_type`:查询字段的类型(string、num、date)  
`column_desc`:查询字段的描述(在输入框输入内容后下方显示数据中的描述内容)  
`column_alias`:查询字段别名(为了区分多表同名字段设置)。该字段内容即为RESTFul接口调用的参数名称。  
例如：  
column_alias 设置为 userId，那么在调用的方法中，应有一个对应变量接收该设置。比如：public void someMethod(**String userId**){...}。  
多条记录中的column_alias，则对应多个变量名称。且应该保证相同RESTFul接口调用的column_alias内容唯一。  
`url`:模块请求跳转的业务url  

### Controller中的调用
当点击查询按钮时，请求会经过oopsearch模块，根据`PEP_OOPSEARCH_CONFIG`表中`module_name`字段找到对应的`url`进行跳转。后续查询具体操作由业务调用者自行处理。

使用pep-oopsearch-sync-mysql自动同步
----------------------------
当mysql中数据发生`insert`、`update`、`delete`操作时，pep-oopsearch-sync-mongo模块，可以自动监听到该操作。
并自动对操作内容进行解析，然后同步到mongodb当中。保持查询信息一致。
>注意：`pep-oopsearch-sync-mysql`模块采用解析`mysql`数据库的`binlog`来实现以上功能。所以要使用该模块，数据库必须使用`mysql`。
`pep-oopsearch-sync-mysql`模块通过把自己伪装成slave，来使用mysql的主从同步功能。进而读取并解析mysql主机的binlog日志。

### 设置mysql

mysql 相关设置已在 `pep-dev-configs` 模块内的 docker-compose.yml 中配置好，使用 docker 方式启动 mysql 即可。

若需以其他方式启动，需检查 mysql 的 binlog 格式需配置为 `row`，并且在 datasource 中使用的用户需包含 
[REPLICATION SLAVE](http://dev.mysql.com/doc/refman/5.5/en/privileges-provided.html#priv_replication-slave)、
[REPLICATION CLIENT](http://dev.mysql.com/doc/refman/5.5/en/privileges-provided.html#priv_replication-client)权限。

>注意:模块通过读取`datasource`的url、user、password、schema 来进行连接mysql读取binlog，所以请务必保持datasource中各项配置正确。且不要轻易修改。
针对`schema`，binlog解析本身并不区分schema，同步模块通过datasource中配置的schema，对binlog监听到是event进行过滤(只对本datasource中设置的schema进行监听)。


使用pep-oopsearch-sync-h2自动同步
------------------------------
该模块实现将h2数据库中的数据，同步到mongo的功能。
该模块采用了`pep-schedule-cluster`的定时任务功能。
默认情况下，每5秒，全量同步一次h2的数据到mongo当中。
>注意:可以查看`cluster-pep-oopsearch-sync-h2`中定时任务具体配置

使用pep-oopsearch-config配置模块
------------------------------
该模块实现对oopsearch的配置相关操作。包括读取配置信息等功能
同时后续开发会实现页面对配置信息进行管理的相关功能

pep-application 中的设置
-----------------------
修改`pep-application.gradle`文件。
```
runtime project(':pep-oopsearch-config'),
        project(':pep-oopsearch'),
        project(':pep-oopsearch-sync-mysql'),
        project(':pep-cache-redis')
```
>注意：根据你使用的缓存实现，需要配置不同的cache实现模块。上面的例子使用了`redis`作为Spring Cache的实现。
如果想使用`ehcache`作为缓存实现，则需修改为如下配置
```
runtime project(':pep-oopsearch-config'),
        project(':pep-oopsearch'),
        project(':pep-oopsearch-sync-mysql'),
        project(':pep-cache-ehcache')
```
>注意：可以根据数据库实际环境，将project(':pep-oopsearch-sync-mysql')替换为project(':pep-oopsearch-sync-h2')
两者不可同时使用。

其他注意事项
----------
oopsearch模块使用到mongodb、redis数据库、ehcache等。同时还使用到`pep-schedule-cluster`定时任务模块进行数据同步。
在开发调试过程中，请确保使用到的各项功能配置文件正确。
