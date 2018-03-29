OOPSEARCH
==========================

主要设计目标
----------

* 实现智能查询，采用逆向思维设计
* 采用mongodb缓存业务模块需要查询的数据，从而达到在查询框输入内容即可快速查询mongodb，返回关联数据
* 联想内容根据输入框输入的内容自动进行关联，返回结果为包含输入内容，且是业务模块相关查询表当中的内容


调用oopsearch模块方式
------------------
下面以`DemoDept`模块为例进行具体内容讲解。
### 设定 `search-*.properties`配置文件
该文件路径需符合`conf/oopsearch/**/search-*.properties` 规则。
以下为`search-dept.properties`配置文件具体内容

    # 需要查询的表,多张表以逗号(",")分割
    search.dept.tables=demo_dept
    # 查询联想的字段.表名:查询字段:字段类型:描述字段 多个条件以逗号(",")分割
    search.dept.columns=demo_dept:dept_id:string:部门id,demo_dept:dept_name:string:部门名称,demo_dept:create_time:date:创建时间,demo_dept:dept_member_count:num:部门人数
    # 每次查询返回结果条数限制
    search.dept.limit=10
    # 时间类型字段扩充处理 多个扩展内容以逗号(",")分割
    search.dept.extendDateYear=去年,今年,明年
    search.dept.extendDateMonth=上月,本月,下月
    search.dept.extendDateDay=昨天,今天,明天


>注意：由于存在多个模块配置了多个配置文件的情况，所以应避免当前key值与其他已经配置的key重复。
key的定义规则为：`search.业务名.tables`等。

### 创建Config Java类
需创建一个配置Java类，该类继承`AbstractSearchConfigs`类，并从`search-dept.properties`读取配置信息。
调用父类构造方法，具体如下：

```java
@Component
@SearchConfig
public class DemoDeptConfigs extends AbstractSearchConfigs {

    public DemoDeptConfigs(@Value("${search.dept.tables}") String searchTables,
                           @Value("${search.dept.columns}") String searchColumns,
                           @Value("${search.dept.limit}") int limit,
                           @Value("${search.dept.extendDateYear}") String extendByYear,
                           @Value("${search.dept.extendDateMonth}") String extendByMonth,
                           @Value("${search.dept.extendDateDay}") String extendByDay) {
        super(searchTables, searchColumns, limit, extendByYear, extendByMonth, extendByDay);
    }
}
```
>注意：该类需设置注解 `@SearchConfig`、`@Component`

### Controller中的调用
在你的业务Controller中调用oopsearch模块，获取查询信息。具体如下：
```java
@RestController
@RequestMapping("/search")
public class DemoDeptController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoDeptController.class);

    @Autowired
    private SearchService searchService;

    @Autowired
    private QueryResultService queryResultService;

    @Autowired
    private DemoDeptConfigs demoDeptConfigs;

    @GetMapping("/dept")
    public ResponseEntity<List<OOPSearchDocument>> searchInfo(@RequestParam String data) {
        List<OOPSearchDocument> docs = (List<OOPSearchDocument>) searchService.getSearchInfo(data, demoDeptConfigs);
        ResponseEntity<List<OOPSearchDocument>> result = responseOfGet(docs);
        return result;
    }

    @GetMapping("/dept/query")
    public ResponseEntity deptResult(String req, String tableName) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            req = URLDecoder.decode(req, PEPConstants.DEFAULT_CHARSET.toString());
            JsonNode jn = objectMapper.readValue(req, JsonNode.class);
            return responseOfGet(queryResultService.assemble(demoDeptConfigs, jn, tableName));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return responseOfGet(new ArrayList<>());
    }
}
```
>注意：`OOPSearchDocument`为oopsearch的标准entity接口。`data`参数为前台界面传递的查询内容。restful的访问请求路径可以质询前台框架设定。
`searchInfo`方法为查询框输入内容后动态查询联想结果并返回前台，作为下拉框提示内容
`deptResult`方法为点击查询按钮后，查询后台数据库，返回结果列表内容

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

pep-webapp中的设置
----------------
修改`pep-webapp.gradle`文件。
```
runtime project(':pep-oopsearch'),
        project(':pep-oopsearch-sync-mysql'),
        project(':pep-cache-redis')
```
>注意：根据你使用的缓存实现，需要配置不同的cache实现模块。上面的例子使用了`redis`作为Spring Cache的实现。
如果想使用`ehcache`作为缓存实现，则需修改为如下配置
```
runtime project(':pep-oopsearch'),
        project(':pep-oopsearch-sync-mysql'),
        project(':pep-cache-ehcache')
```
>注意：可以根据数据库实际环境，将project(':pep-oopsearch-sync-mysql')替换为project(':pep-oopsearch-sync-h2')
两者不可同时使用。

其他注意事项
----------
oopsearch模块使用到mongodb、redis数据库、ehcache等。同时还使用到`pep-schedule-cluster`定时任务模块进行数据同步。
在开发调试过程中，请确保使用到的各项功能配置文件正确。
