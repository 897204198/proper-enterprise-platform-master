package com.proper.enterprise.platform.oopsearch.sync.mysql;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.repository.NativeRepository;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.oopsearch.api.annotation.SearchConfig;
import com.proper.enterprise.platform.oopsearch.api.conf.AbstractSearchConfigs;
import com.proper.enterprise.platform.oopsearch.api.model.SearchColumnModel;
import com.proper.enterprise.platform.oopsearch.api.model.SyncDocumentModel;
import com.proper.enterprise.platform.oopsearch.api.serivce.MongoDataSyncService;
import com.proper.enterprise.platform.oopsearch.sync.mysql.listener.BinlogLifecycleListener;
import com.proper.enterprise.platform.oopsearch.sync.mysql.service.impl.SyncCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class BinlogThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(BinlogThread.class);

    private MongoDataSyncService mongoDataSyncService;

    private BinaryLogClient client;

    private NativeRepository nativeRepository;

    private List<TableObject> tableObjects = new ArrayList<>();

    private boolean process;

    private MongoTemplate mongoTemplate;

    private SyncCacheService mongoSyncService;

    private String hostname;

    private int port;

    private Set<String> schemaSet;

    private String username;

    private String password;

    public BinlogThread(MongoDataSyncService mongoDataSyncService, NativeRepository nativeRepository,
                        MongoTemplate mongoTemplate, SyncCacheService mongoSyncService, String hostname,
                        int port, String schema, String username, String password) {
        setName("BinlogThread");
        this.mongoDataSyncService = mongoDataSyncService;
        this.nativeRepository = nativeRepository;
        this.mongoTemplate = mongoTemplate;
        this.mongoSyncService = mongoSyncService;
        this.hostname = hostname;
        this.port = port;
        String[] arr = schema.split(",");
        this.schemaSet = new HashSet<>(Arrays.asList(arr));

        this.username = username;
        this.password = password;
    }

    public void run() {
        initMysqlClient();
    }

    private void initMysqlClient() {
        client = new BinaryLogClient(this.hostname, this.port, this.username, this.password);
        client.registerEventListener(new BinaryLogClient.EventListener() {
            @Override
            public void onEvent(Event event) {
                EventType eventType = event.getHeader().getEventType();
                EventHeaderV4 eventHeaderV4 = (EventHeaderV4)event.getHeader();
                long pos = eventHeaderV4.getPosition();
                if (EventType.TABLE_MAP.equals(eventType)) {
                    initTableObject(event.getData());
                }
                if (EventType.isWrite(eventType) && process) {
                    doInsertSync(event.getData(), pos);
                } else if (EventType.isUpdate(eventType) && process) {
                    doUpdateSync(event.getData(), pos);
                } else if (EventType.isDelete(eventType) && process) {
                    doDeleteSync(event.getData(), pos);
                } else if (EventType.XID.equals(eventType)) {
                    xid();
                }
            }
        });
        BinlogLifecycleListener binlogLifecycleListener = new BinlogLifecycleListener();
        client.registerLifecycleListener(binlogLifecycleListener);
        try {
            client.connect();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

    }

    private void xid() {
        tableObjects.clear();
        process = false;
    }

    private void initTableObject(EventData eventData) {
        tableObjects.clear();
        process = false;

        TableMapEventData tableMapEventData = null;
        if (eventData instanceof TableMapEventData) {
            tableMapEventData = (TableMapEventData)eventData;
        } else {
            return;
        }
        String schema = tableMapEventData.getDatabase();
        if (!schemaSet.contains(schema)) {
            // schema不同，无需后续操作，直接返回，避免不同schema表名相同造成误操作
            return;
        }
        String tableName = tableMapEventData.getTable();
        containTableName(tableName);
        if (!process) {
            // search配置中没有配置的表，不做相关操作，直接返回
            return;
        }

        String sql  = "SELECT "
            + "t1.COLUMN_NAME AS COL, "
            + "t2.COLUMN_NAME AS PRI "
            + "FROM "
            + "INFORMATION_SCHEMA.COLUMNS t1 "
            + "LEFT JOIN "
            + "INFORMATION_SCHEMA.KEY_COLUMN_USAGE t2 "
            + "ON t1.TABLE_NAME = t2.TABLE_NAME "
            + "AND "
            + "t1.COLUMN_NAME = t2.COLUMN_NAME "
            + "WHERE "
            + "t1.TABLE_NAME = '" + tableName + "' "
            + "AND "
            + "t1.TABLE_SCHEMA = '" + schema + "'";
        List<Object[]> resultList = null;
        try {
            resultList = nativeRepository.executeQuery(sql);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return;
        }
        List<String> columnObject = new ArrayList<>();
        StringBuffer primaryKey = new StringBuffer();
        for (Object[] result:resultList) {
            String columnName = result[0].toString();
            if (result[1] != null) {
                primaryKey.append(result[1].toString()).append("|");
            }
            columnObject.add(columnName);
        }
        TableObject tableObject = new TableObject();
        tableObject.setSchema(schema);
        tableObject.setTableName(tableName);
        tableObject.setColumnNames(columnObject);
        if (primaryKey.length() > 0) {
            primaryKey.deleteCharAt(primaryKey.length() - 1);
        }
        tableObject.setPrimaryKeys(primaryKey.toString());
        tableObjects.add(tableObject);
    }

    private void doInsertSync(EventData eventData, long pos) {
        WriteRowsEventData writeRowsEventData = null;
        if (eventData instanceof WriteRowsEventData) {
            writeRowsEventData = (WriteRowsEventData)eventData;
        } else {
            return;
        }
        List<Serializable[]> rows = writeRowsEventData.getRows();
        int tempPos = 0;
        for (int i = 0; i < rows.size(); i++) {
            Object[] row = rows.get(i);
            for (int j = 0; j < row.length; j++) {
                for (TableObject tableObject:tableObjects) {
                    String des = getSearchDocumentDesc(tableObject.getTableName(),
                        tableObject.getColumnNames().get(j));
                    if (StringUtil.isNull(des)) {
                        // 没有描述的column表明不在mongo中,无需更新
                        continue;
                    } else {
                        String primaryKeysValue = getPrimaryKeysValue(tableObject, row);
                        SyncDocumentModel syncDocumentModel = new SyncDocumentModel();
                        syncDocumentModel.setTab(tableObject.getTableName());
                        syncDocumentModel.setCol(tableObject.getColumnNames().get(j));
                        syncDocumentModel.setCona(row[j].toString());
                        syncDocumentModel.setPri(primaryKeysValue);
                        syncDocumentModel.setDes(des);

                        mongoSyncService.syncCache(pos + ":" + tempPos + ":insert", syncDocumentModel);
                        tempPos++;
                    }
                }
            }
        }
    }

    private void doUpdateSync(EventData eventData, long pos) {
        UpdateRowsEventData updateRowsEventData = null;
        if (eventData instanceof UpdateRowsEventData) {
            updateRowsEventData = (UpdateRowsEventData)eventData;
        } else {
            return;
        }
        List<Map.Entry<Serializable[], Serializable[]>> rows = updateRowsEventData.getRows();
        int tempPos = 0;
        for (Map.Entry<Serializable[], Serializable[]> map: rows) {
            Object[] before = map.getKey();
            Object[] after = map.getValue();
            // 去mongo中查找before中对应的字段，更新成after，查找按完全匹配进行
            for (int i = 0; i < before.length; i++) {
                if (!before[i].toString().equals(after[i].toString())) {
                    // 前后值发生变动才通知服务进行mongo同步
                    String tableName = "";
                    String columnName = "";
                    // 多表时需处理顺序
                    for (TableObject tableObject:tableObjects) {
                        tableName = tableObject.getTableName();
                        columnName = tableObject.getColumnNames().get(i);
                    }
                    String des = getSearchDocumentDesc(tableName, columnName);
                    if (StringUtil.isNull(des)) {
                        // 没有描述的column表明不在mongo中,无需更新
                        continue;
                    } else {
                        // 根据tablename columnname确认doc对象,并更新content为update之后的值
                        SyncDocumentModel syncDocumentModel = new SyncDocumentModel();
                        String columnContentBefore = before[i].toString();
                        String columnContentAfter = after[i].toString();
                        syncDocumentModel.setCol(columnName);
                        syncDocumentModel.setTab(tableName);
                        syncDocumentModel.setConb(columnContentBefore);
                        syncDocumentModel.setCona(columnContentAfter);

                        mongoSyncService.syncCache(pos + ":" + tempPos + ":update", syncDocumentModel);
                        tempPos++;
                    }
                }
            }
        }
    }

    private void doDeleteSync(EventData eventData, long pos) {
        DeleteRowsEventData deleteRowsEventData = null;
        if (eventData instanceof DeleteRowsEventData) {
            deleteRowsEventData = (DeleteRowsEventData)eventData;
        } else {
            return;
        }
        List<Serializable[]> rows = deleteRowsEventData.getRows();
        int tempPos = 0;
        for (int i = 0; i < rows.size(); i++) {
            Object[] row = rows.get(i);
            for (int j = 0; j < row.length; j++) {
                String tableName = "";
                String columnName = "";
                String primaryKeysValue = "";
                // 多表时需处理顺序
                for (TableObject tableObject:tableObjects) {
                    tableName = tableObject.getTableName();
                    columnName = tableObject.getColumnNames().get(j);
                    primaryKeysValue = getPrimaryKeysValue(tableObject, row);
                }
                String des = getSearchDocumentDesc(tableName, columnName);
                if (StringUtil.isNull(des)) {
                    // 没有描述的column表明不在mongo中,无需更新
                    continue;
                } else {
                    SyncDocumentModel syncDocumentModel = new SyncDocumentModel();
                    syncDocumentModel.setTab(tableName);
                    syncDocumentModel.setCol(columnName);
                    syncDocumentModel.setCona(row[j].toString());
                    syncDocumentModel.setPri(primaryKeysValue);

                    mongoSyncService.syncCache(pos + ":" + tempPos + ":delete", syncDocumentModel);
                    tempPos++;
                }
            }
        }
    }

    private String getPrimaryKeysValue(TableObject tableObject, Object[] row) {
        String primaryKeys = tableObject.getPrimaryKeys();
        StringBuffer primaryKeysValue = new StringBuffer();
        if (StringUtil.isNotNull(primaryKeys)) {
            primaryKeysValue.append(tableObject.getSchema()).append("|").append(tableObject.getTableName()).append("|");
            String[] temp = primaryKeys.split("\\|");
            List<String> columnNames = tableObject.getColumnNames();
            for (String pri:temp) {
                for (int i = 0; i < columnNames.size(); i++) {
                    if (pri.equalsIgnoreCase(columnNames.get(i))) {
                        primaryKeysValue.append(row[i]).append("|");
                        break;
                    }
                }
            }
            primaryKeysValue.deleteCharAt(primaryKeysValue.length() - 1);
        }
        return primaryKeysValue.toString();
    }

    public String getSearchDocumentDesc(String tableName, String columnName) {
        String des = "";
        boolean flag = false;
        Map<String, Object> searchConfigBeans = PEPApplicationContext.getApplicationContext().getBeansWithAnnotation(SearchConfig.class);
        for (Map.Entry<String, Object> entry : searchConfigBeans.entrySet()) {
            AbstractSearchConfigs tempSearchConfig = (AbstractSearchConfigs) entry.getValue();
            List<SearchColumnModel> searchColumnList = tempSearchConfig.getSearchColumnListByTable(tableName);
            if (searchColumnList != null) {
                for (SearchColumnModel searchColumnModel:searchColumnList) {
                    if (searchColumnModel.getColumn().equalsIgnoreCase(columnName)) {
                        des = searchColumnModel.getDescColumn();
                        flag = true;
                        break;
                    }
                }
            }
            if (flag) {
                break;
            }
        }
        return des;
    }

    public void containTableName(String tableName) {
        Map<String, Object> searchConfigBeans = PEPApplicationContext.getApplicationContext().getBeansWithAnnotation(SearchConfig.class);
        for (Map.Entry<String, Object> entry : searchConfigBeans.entrySet()) {
            AbstractSearchConfigs tempSearchConfig = (AbstractSearchConfigs) entry.getValue();
            List<String> tableNameList = tempSearchConfig.getTableNameList();
            for (String tempTableName:tableNameList) {
                if (tableName.equalsIgnoreCase(tempTableName)) {
                    process = true;
                    break;
                }
            }
            if (process) {
                break;
            }
        }
    }
}
