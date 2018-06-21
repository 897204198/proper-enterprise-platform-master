package com.proper.enterprise.platform.oopsearch.sync.mysql.executor;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.proper.enterprise.platform.core.jpa.repository.NativeRepository;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.oopsearch.api.enums.DataBaseType;
import com.proper.enterprise.platform.oopsearch.api.enums.SyncMethod;
import com.proper.enterprise.platform.oopsearch.api.model.SearchColumnModel;
import com.proper.enterprise.platform.oopsearch.api.model.SyncDocumentModel;
import com.proper.enterprise.platform.oopsearch.config.conf.AbstractSearchConfigs;
import com.proper.enterprise.platform.oopsearch.config.service.SearchConfigService;
import com.proper.enterprise.platform.oopsearch.service.impl.SyncCacheService;
import com.proper.enterprise.platform.oopsearch.sync.mysql.TableObject;
import com.proper.enterprise.platform.oopsearch.sync.mysql.listener.BinlogLifecycleListener;
import com.proper.enterprise.platform.oopsearch.sync.mysql.service.impl.MySQLMongoDataSync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class BinlogExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BinlogExecutor.class);

    private MySQLMongoDataSync mySQLMongoDataSync;

    private SearchConfigService searchConfigService;

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

    public BinlogExecutor(MySQLMongoDataSync mySQLMongoDataSync, SearchConfigService searchConfigService, NativeRepository nativeRepository,
                          MongoTemplate mongoTemplate, SyncCacheService mongoSyncService, String hostname,
                          int port, String schema, String username, String password) {
        this.searchConfigService = searchConfigService;
        this.mySQLMongoDataSync = mySQLMongoDataSync;
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

    public void executor() {
        initMySQLClient();
    }

    private void initMySQLClient() {
        client = new BinaryLogClient(this.hostname, this.port, this.username, this.password);
        client.registerEventListener(new BinaryLogClient.EventListener() {
            @Override
            public void onEvent(Event event) {
                EventType eventType = event.getHeader().getEventType();
                EventHeaderV4 eventHeaderV4 = (EventHeaderV4) event.getHeader();
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
            client.disconnect();
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
            tableMapEventData = (TableMapEventData) eventData;
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

        String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS "
            + "WHERE "
            + "TABLE_NAME = '" + tableName + "' "
            + "AND "
            + "TABLE_SCHEMA = '" + schema + "'";
        List<String> resultList = null;
        try {
            resultList = nativeRepository.executeQuery(sql);
            List<String> columnObject = new ArrayList<>();
            // 获取字段名称
            for (String columnName : resultList) {
                columnObject.add(columnName);
            }
            TableObject tableObject = new TableObject();
            tableObject.setSchema(schema);
            tableObject.setTableName(tableName);
            tableObject.setColumnNames(columnObject);

            String priSql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE "
                + "WHERE "
                + "TABLE_NAME = '" + tableName + "' "
                + "AND "
                + "TABLE_SCHEMA = '" + schema + "' "
                + "AND "
                + "CONSTRAINT_NAME = 'PRIMARY'";
            resultList = nativeRepository.executeQuery(priSql);
            StringBuffer primaryKey = new StringBuffer();
            for (String primaryColumnName : resultList) {
                primaryKey.append(primaryColumnName).append("|");
            }
            if (primaryKey.length() > 0) {
                primaryKey.deleteCharAt(primaryKey.length() - 1);
            }
            tableObject.setPrimaryKeys(primaryKey.toString());
            tableObjects.add(tableObject);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return;
        }
    }

    private void doInsertSync(EventData eventData, long pos) {
        WriteRowsEventData writeRowsEventData = null;
        if (eventData instanceof WriteRowsEventData) {
            writeRowsEventData = (WriteRowsEventData) eventData;
        } else {
            return;
        }
        List<Serializable[]> rows = writeRowsEventData.getRows();
        int tempPos = 0;
        for (int i = 0; i < rows.size(); i++) {
            Object[] row = rows.get(i);
            for (int j = 0; j < row.length; j++) {
                if (row[j] == null) {
                    // 空值内容不需要存入mongo
                    continue;
                }
                for (TableObject tableObject : tableObjects) {
                    List<SyncDocumentModel> modelList = filterSearchDocument(tableObject.getTableName(),
                        tableObject.getColumnNames().get(j));
                    if (modelList.size() == 0) {
                        // 没有匹配到符合条件的SearchColumnModel,表明不在mongo中,无需更新
                        continue;
                    } else {
                        // 当modelList元素为多个时，说明不同的config类中存在同表同名字段，此时字段描述、别名、url，均可能不同
                        // 所以需要创建对应的document存入mongo当中
                        for (SyncDocumentModel syncDocumentModel : modelList) {
                            String primaryKeysValue = getPrimaryKeysValue(tableObject, row);
                            syncDocumentModel.setTab(tableObject.getTableName());
                            syncDocumentModel.setCol(tableObject.getColumnNames().get(j));
                            syncDocumentModel.setCona(row[j].toString());
                            syncDocumentModel.setPri(primaryKeysValue);
                            syncDocumentModel.setDataBaseType(DataBaseType.RDB);
                            syncDocumentModel.setMethod(SyncMethod.INSERT);
                            mongoSyncService.push(pos + ":" + tempPos + ":insert", syncDocumentModel);
                            tempPos++;
                        }
                    }
                }
            }
        }
    }

    private void doUpdateSync(EventData eventData, long pos) {
        UpdateRowsEventData updateRowsEventData = null;
        if (eventData instanceof UpdateRowsEventData) {
            updateRowsEventData = (UpdateRowsEventData) eventData;
        } else {
            return;
        }
        List<Map.Entry<Serializable[], Serializable[]>> rows = updateRowsEventData.getRows();
        int tempPos = 0;
        for (Map.Entry<Serializable[], Serializable[]> map : rows) {
            Object[] before = map.getKey();
            Object[] after = map.getValue();
            // 去mongo中查找before中对应的字段，更新成after，查找按完全匹配进行
            for (int i = 0; i < before.length; i++) {
                if (!before[i].toString().equals(after[i].toString())) {
                    // 前后值发生变动才通知服务进行mongo同步
                    String tableName = "";
                    String columnName = "";
                    // 多表时需处理顺序
                    for (TableObject tableObject : tableObjects) {
                        tableName = tableObject.getTableName();
                        columnName = tableObject.getColumnNames().get(i);
                    }
                    List<SyncDocumentModel> modelList = filterSearchDocument(tableName, columnName);
                    if (modelList.size() == 0) {
                        // 没有匹配到符合条件的SearchColumnModel,表明不在mongo中,无需更新
                        continue;
                    } else {
                        for (SyncDocumentModel syncDocumentModel : modelList) {
                            // 根据tablename columnname确认doc对象,并更新content为update之后的值
                            String columnContentBefore = before[i].toString();
                            String columnContentAfter = after[i].toString();
                            syncDocumentModel.setCol(columnName);
                            syncDocumentModel.setTab(tableName);
                            syncDocumentModel.setConb(columnContentBefore);
                            syncDocumentModel.setCona(columnContentAfter);
                            syncDocumentModel.setDataBaseType(DataBaseType.RDB);
                            syncDocumentModel.setMethod(SyncMethod.UPDATE);
                            mongoSyncService.push(pos + ":" + tempPos + ":update", syncDocumentModel);
                            tempPos++;
                        }
                    }
                }
            }
        }
    }

    private void doDeleteSync(EventData eventData, long pos) {
        DeleteRowsEventData deleteRowsEventData = null;
        if (eventData instanceof DeleteRowsEventData) {
            deleteRowsEventData = (DeleteRowsEventData) eventData;
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
                for (TableObject tableObject : tableObjects) {
                    tableName = tableObject.getTableName();
                    columnName = tableObject.getColumnNames().get(j);
                    primaryKeysValue = getPrimaryKeysValue(tableObject, row);
                }
                List<SyncDocumentModel> modelList = filterSearchDocument(tableName, columnName);
                if (modelList.size() == 0) {
                    // 没有匹配到符合条件的SearchColumnModel,表明不在mongo中,无需更新
                    continue;
                } else {
                    for (SyncDocumentModel syncDocumentModel : modelList) {
                        syncDocumentModel.setTab(tableName);
                        syncDocumentModel.setCol(columnName);
                        syncDocumentModel.setCona(row[j].toString());
                        syncDocumentModel.setPri(primaryKeysValue);
                        syncDocumentModel.setDataBaseType(DataBaseType.RDB);
                        syncDocumentModel.setMethod(SyncMethod.DELETE);
                        mongoSyncService.push(pos + ":" + tempPos + ":delete", syncDocumentModel);
                        tempPos++;
                    }
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
            for (String pri : temp) {
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

    /**
     * searchDocument过滤方法
     * 根据表名和字段名，从配置信息中获取对应的searchColumnModel。如果获取不到，说明配置中没有该表该字段的信息，
     * mongodb中也就不会存储该信息；如果搜索到，则根据搜索到的次数，创建syncDocumentModel，并将公共信息（别名、描述、url）
     * 预先存入对象中。后续取出该对象，再存入其他属性即可
     *
     * @param tableName  表名
     * @param columnName 字段名
     * @return SyncDocumentModel集合
     */
    public List<SyncDocumentModel> filterSearchDocument(String tableName, String columnName) {
        List<SyncDocumentModel> modelList = new ArrayList<>();
        Map<String, Object> searchConfigBeans = searchConfigService.getSearchConfigs(DataBaseType.RDB);
        for (Map.Entry<String, Object> entry : searchConfigBeans.entrySet()) {
            AbstractSearchConfigs tempSearchConfig = (AbstractSearchConfigs) entry.getValue();
            SearchColumnModel searchColumnModel = tempSearchConfig.getSearchColumnByTableNameAndColumnName(tableName, columnName);
            if (searchColumnModel != null) {
                SyncDocumentModel syncDocumentModel = new SyncDocumentModel();
                syncDocumentModel.setDes(searchColumnModel.getDescColumn());
                syncDocumentModel.setAlias(searchColumnModel.getColumnAlias());
                syncDocumentModel.setUrl(searchColumnModel.getUrl());
                modelList.add(syncDocumentModel);
            }
        }
        return modelList;
    }

    public void containTableName(String tableName) {
        Map<String, Object> searchConfigBeans = searchConfigService.getSearchConfigs(DataBaseType.RDB);
        if (searchConfigBeans == null) {
            LOGGER.error("search config is null");
            return;
        }
        // Map<String, Object> searchConfigBeans = PEPApplicationContext.getApplicationContext().getBeansWithAnnotation(SearchConfig.class);
        for (Map.Entry<String, Object> entry : searchConfigBeans.entrySet()) {
            AbstractSearchConfigs tempSearchConfig = (AbstractSearchConfigs) entry.getValue();
            List<String> tableNameList = tempSearchConfig.getTableNameList();
            for (String tempTableName : tableNameList) {
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
