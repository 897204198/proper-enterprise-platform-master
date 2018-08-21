package com.proper.enterprise.platform.oopsearch.sync.mysql

import com.github.shyiko.mysql.binlog.BinaryLogClient
import com.github.shyiko.mysql.binlog.event.*
import com.proper.enterprise.platform.api.cache.CacheKeysSentry
import com.proper.enterprise.platform.core.jpa.repository.NativeRepository
import com.proper.enterprise.platform.oopsearch.document.SearchDocument
import com.proper.enterprise.platform.oopsearch.config.repository.SearchConfigRepository
import com.proper.enterprise.platform.oopsearch.repository.SyncMongoRepository
import com.proper.enterprise.platform.oopsearch.config.service.SearchConfigService
import com.proper.enterprise.platform.oopsearch.service.impl.SyncCacheService
import com.proper.enterprise.platform.oopsearch.sync.mysql.executor.BinlogExecutor
import com.proper.enterprise.platform.oopsearch.sync.mysql.service.impl.MySQLMongoDataSync
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.test.context.jdbc.Sql

class BinlogExecutorTest extends AbstractTest {

    @Autowired
    private MySQLMongoDataSync mongoDataSyncService

    @Autowired
    private SearchConfigService searchConfigService

    @Autowired
    private NativeRepository nativeRepository

    @Autowired
    private MongoTemplate mongoTemplate

    @Autowired
    SchedulerFactoryBean pepSchedulerFactory

    @Autowired
    SyncMongoRepository syncMongoRepository

    @Autowired
    SyncCacheService mongoSyncService

    @Autowired
    CacheManager cacheManager

    @Autowired
    CacheKeysSentry sentry

    @Autowired
    private SearchConfigRepository searchConfigRepository

    private String hostname = "127.0.0.1"

    private int port = 3307

    private String schema = "pep_dev,pep_test"

    private String username = "slave"

    private String password = "testbinlog"


    @Test
    void testEventDataFormatFail() {
        BinlogExecutor binlogThread = new BinlogExecutor(mongoDataSyncService, searchConfigService, nativeRepository, mongoTemplate,
            mongoSyncService, hostname, port, schema, username, password)
        binlogThread.executor()
        sleep(3000)
        List<BinaryLogClient.EventListener> eventListenerList = binlogThread.client.getEventListeners()

        EventHeader header = new EventHeaderV4()
        header.setEventType(EventType.EXT_WRITE_ROWS)
        EventData data = new DeleteRowsEventData()
        Event insertEvent = new Event(header, data)

        BinaryLogClient.EventListener eventListener = eventListenerList.get(0)
        binlogThread.process = true
        try {
            eventListener.onEvent(insertEvent)
        } catch (Exception e) {
            e.printStackTrace()
        }
        sleep(2000)

        header.setEventType(EventType.EXT_UPDATE_ROWS)
        EventData updateData = new WriteRowsEventData()
        Event updateEvent = new Event(header, updateData)
        try {
            eventListener.onEvent(updateEvent)
        } catch (Exception e) {
            e.printStackTrace()
        }
        sleep(2000)

        header.setEventType(EventType.EXT_DELETE_ROWS)
        EventData deleteData = new WriteRowsEventData()
        Event deleteEvent = new Event(header, deleteData)
        try {
            eventListener.onEvent(deleteEvent)
        } catch (Exception e) {
            e.printStackTrace()
        }
        sleep(2000)

    }

    @Test
    void testXid() {
        BinlogExecutor binlogThread = new BinlogExecutor(mongoDataSyncService, searchConfigService, nativeRepository, mongoTemplate,
            mongoSyncService, hostname, port, schema, username, password)
        binlogThread.executor()
        sleep(3000)
        List<BinaryLogClient.EventListener> eventListenerList = binlogThread.client.getEventListeners()

        EventHeader header = new EventHeaderV4()
        header.setEventType(EventType.XID)
        EventData data = new XidEventData()
        Event event = new Event(header, data)

        BinaryLogClient.EventListener eventListener = eventListenerList.get(0)
        try {
            eventListener.onEvent(event)
        } catch (Exception e) {
            e.printStackTrace()
        }
        sleep(2000)
    }

    @Test
    @NoTx
    @Sql("/sql/oopsearch/sync/mysql/demoUserConfigData.sql")
    void testInitTableObject() {
        // 清除cachequery的缓存对象，避免通过@sql插入db的数据因为没有触发cache进行更新，而导致查询时出现脏读现象
        Cache queryCache = cacheManager.getCache("org.hibernate.cache.internal.StandardQueryCache")
        queryCache.clear()

        BinlogExecutor binlogThread = new BinlogExecutor(mongoDataSyncService, searchConfigService, nativeRepository, mongoTemplate,
            mongoSyncService, hostname, port, schema, username, password)
        binlogThread.executor()
        sleep(3000)
        List<BinaryLogClient.EventListener> eventListenerList = binlogThread.client.getEventListeners()

        EventHeader header = new EventHeaderV4()
        header.setEventType(EventType.TABLE_MAP)

        EventData data = new TableMapEventData()
        data.setDatabase("pep_test")
        data.setTable("DEMO_USER")

        Event event = new Event(header, data)

        BinaryLogClient.EventListener eventListener = eventListenerList.get(0)
        try {
            eventListener.onEvent(event)
        } catch (Exception e) {
            e.printStackTrace()
        }
        sleep(2000)
        searchConfigRepository.deleteAll()
    }

    @Test
    @NoTx
    @Sql("/sql/oopsearch/sync/mysql/demoUserConfigData.sql")
    void testSync() {
        // 清除cachequery的缓存对象，避免通过@sql插入db的数据因为没有触发cache进行更新，而导致查询时出现脏读现象
        Cache queryCache = cacheManager.getCache("org.hibernate.cache.internal.StandardQueryCache")
        queryCache.clear()

        syncMongoRepository.deleteAll()

        BinlogExecutor binlogThread = new BinlogExecutor(mongoDataSyncService, searchConfigService, nativeRepository, mongoTemplate,
            mongoSyncService, hostname, port, schema, username, password)
        binlogThread.executor()
        sleep(3000)
        List<BinaryLogClient.EventListener> eventListenerList = binlogThread.client.getEventListeners()

        // init tableobject
        TableObject tableObject = new TableObject()
        tableObject.setTableName("demo_user")
        List<String> columnNames = new ArrayList<>()
        columnNames.add("id")
        columnNames.add("user_id")
        columnNames.add("user_name")
        columnNames.add("age")
        columnNames.add("dept_id")
        columnNames.add("create_time")
        tableObject.setColumnNames(columnNames)
        tableObject.setPrimaryKeys("id")
        tableObject.setSchema("pep_test")
        binlogThread.tableObjects.clear()
        binlogThread.tableObjects.add(tableObject)
        binlogThread.process = true

        // insert event
        EventHeader header = new EventHeaderV4()
        header.setEventType(EventType.EXT_WRITE_ROWS)
        header.setEventLength(1)
        header.setNextPosition(2001)
        EventData data = new WriteRowsEventData()
        List<Serializable[]> insertRows = new ArrayList<>()
        Serializable[] insertRow = ["1", "001", "张一", 30, "001", "2018-01-01"]
        insertRows.add(insertRow)
        data.setRows(insertRows)
        Event event = new Event(header, data)


        BinaryLogClient.EventListener eventListener = eventListenerList.get(0)
        try {
            eventListener.onEvent(event)
        } catch (Exception e) {
            e.printStackTrace()
        }
        // mongo sync
//        sleep(5000)
        List<SearchDocument> result = new ArrayList<>()
        for (i in 1..20) {
            result = syncMongoRepository.findAll()
            if (result.size() > 0) {
                break
            } else {
                sleep(1000)
            }
        }
        assert result.size() > 0
        println "mongo inserted:" + result.size()

        // update event
        header.setEventType(EventType.EXT_UPDATE_ROWS)
        header.setEventLength(1)
        header.setNextPosition(2002)
        data = new UpdateRowsEventData()
        List<Map.Entry<Serializable[], Serializable[]>> updateRows = new ArrayList<>()
        Serializable[] key = ["1", "001", "张一", 30, "001", "2018-01-01"]
        Serializable[] value = ["1", "001", null, 30, "001", "2018-01-01"]
        updateRows.add(new AbstractMap.SimpleEntry<Serializable[], Serializable[]>(key, value))
        data.setRows(updateRows)
        event = new Event(header, data)

        try {
            eventListener.onEvent(event)
        } catch (Exception e) {
            e.printStackTrace()
        }
        // mongo sync
//        sleep(5000)
        boolean hasUpdated = false
        for (i in 1..20) {
            result = syncMongoRepository.findAll()
            for (SearchDocument searchDocument : result) {
                if (searchDocument.getCon() == null) {
                    hasUpdated = true
                }
                if (hasUpdated) {
                    break
                }
            }
            if (hasUpdated) {
                break
            } else {
                sleep(1000)
            }
        }
        assert hasUpdated
        println "mongo updated:" + hasUpdated.toString()

        // delete event
        header.setEventType(EventType.EXT_DELETE_ROWS)
        header.setEventLength(1)
        header.setNextPosition(2003)
        data = new DeleteRowsEventData()
        List<Serializable[]> deleteRows = new ArrayList<>()
        Serializable[] deleteRow = ["1", "001", "张二", 30, "001", "2018-01-01"]
        deleteRows.add(deleteRow)
        data.setRows(deleteRows)
        event = new Event(header, data)

        try {
            eventListener.onEvent(event)
        } catch (Exception e) {
            e.printStackTrace()
        }
        // mongo sync
//        sleep(6000)
        for (i in 1..20) {
            result = syncMongoRepository.findAll()
            if (result.size() == 0) {
                break
            } else {
                sleep(1000)
            }
        }
        assert result.size() == 0
        println "after delete mongo size:" + result.size()

        searchConfigRepository.deleteAll()
    }
}
