package com.proper.enterprise.platform.oopsearch.sync.mysql;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.proper.enterprise.platform.core.jpa.repository.NativeRepository;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.HexConvertUtil;
import com.proper.enterprise.platform.core.utils.MacAddressUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.oopsearch.api.enums.DataBaseType;
import com.proper.enterprise.platform.oopsearch.api.enums.SyncMethod;
import com.proper.enterprise.platform.oopsearch.api.model.SearchColumnModel;
import com.proper.enterprise.platform.oopsearch.api.model.SyncDocumentModel;
import com.proper.enterprise.platform.oopsearch.config.conf.AbstractSearchConfigs;
import com.proper.enterprise.platform.oopsearch.config.service.SearchConfigService;
import com.proper.enterprise.platform.oopsearch.service.impl.SyncCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@DependsOn(value = "mySQLFullSyncMongoManager")
@Lazy(false)
public class BinaryLogMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BinaryLogMonitor.class);

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    private SearchConfigService searchConfigService;
    private NativeRepository nativeRepository;
    private SyncCacheService mongoSyncService;

    private BinaryLogClient client;
    private Set<String> schemaSet;
    private boolean needToProcess;
    private TableObject tableObject;
    private static final String PK_SEP = "|";

    @Autowired
    public BinaryLogMonitor(SearchConfigService searchConfigService,
                            NativeRepository nativeRepository,
                            SyncCacheService mongoSyncService) {
        this.searchConfigService = searchConfigService;
        this.nativeRepository = nativeRepository;
        this.mongoSyncService = mongoSyncService;
    }

    @PostConstruct
    public void init() throws IOException, TimeoutException {
        prepareClient();
        registerEventListener();
        registerLifecycleListener();
        client.connect(TimeUnit.SECONDS.toMillis(10));
    }

    private void prepareClient() {
        Assert.isTrue(StringUtil.isNotBlank(url), "Datasource url SHOULD NOT blank!");

        String lowerCaseUrl = url.toLowerCase();
        String mysqlUrlPattern = ".+:mysql://.+:\\d+/.+";
        Assert.isTrue(lowerCaseUrl.matches(mysqlUrlPattern), "Datasource is NOT MySQL! " + url);

        String[] hostAndPort = (lowerCaseUrl.substring(url.indexOf("//"), lowerCaseUrl.lastIndexOf("/")).substring(2)).split(":");
        String schema = lowerCaseUrl.substring(lowerCaseUrl.lastIndexOf("/") + 1);
        schemaSet = new HashSet<>(Arrays.asList(schema.split(",")));
        client = new BinaryLogClient(hostAndPort[0], Integer.parseInt(hostAndPort[1]), schema, username, password);
        client.setServerId(HexConvertUtil.convert64To10(MacAddressUtil.getFullMacAddress()));
    }

    private void registerEventListener() {
        client.registerEventListener(event -> {
            EventType eventType = event.getHeader().getEventType();
            EventHeaderV4 eventHeaderV4 = event.getHeader();
            if (EventType.TABLE_MAP.equals(eventType) && event.getData() instanceof TableMapEventData) {
                TableMapEventData tableMapEventData = event.getData();
                String database = tableMapEventData.getDatabase();
                // schema?????????????????????????????????????????????????????????schema???????????????????????????
                if (!schemaSet.contains(database)) {
                    return;
                }
                String tableName = tableMapEventData.getTable();
                needToProcess = needToProcess(tableName);
                if (needToProcess) {
                    tableObject = initTableObject(database, tableName);
                }
            }

            long pos = eventHeaderV4.getPosition();
            if (needToProcess && EventType.isWrite(eventType) && event.getData() instanceof WriteRowsEventData) {
                doInsertSync(event.getData(), pos);
            } else if (needToProcess && EventType.isUpdate(eventType) && event.getData() instanceof UpdateRowsEventData) {
                doUpdateSync(event.getData(), pos);
            } else if (needToProcess && EventType.isDelete(eventType) && event.getData() instanceof DeleteRowsEventData) {
                doDeleteSync(event.getData(), pos);
            } else if (EventType.XID.equals(eventType)) {
                xid();
            }
        });
    }

    private void registerLifecycleListener() {
        client.registerLifecycleListener(new BinaryLogClient.LifecycleListener() {
            @Override
            public void onConnect(BinaryLogClient client) {
                LOGGER.info("Binary log client connected.");
            }

            @Override
            public void onCommunicationFailure(BinaryLogClient client, Exception ex) {
                LOGGER.error("Binary log client communication failed!", ex);
            }

            @Override
            public void onEventDeserializationFailure(BinaryLogClient client, Exception ex) {
                LOGGER.error("Binary log client deserialization failed!", ex);
            }

            @Override
            public void onDisconnect(BinaryLogClient client) {
                LOGGER.info("Binary log client disconnected.");
            }
        });
    }

    private boolean needToProcess(String tableName) {
        Map<String, Object> searchConfigBeans = searchConfigService.getSearchConfigs(DataBaseType.RDB);
        if (searchConfigBeans == null) {
            LOGGER.warn("Could NOT find RDB search configs, not do any sync work!");
            return false;
        }
        for (Map.Entry<String, Object> entry : searchConfigBeans.entrySet()) {
            AbstractSearchConfigs tempSearchConfig = (AbstractSearchConfigs) entry.getValue();
            List<String> tableNameList = tempSearchConfig.getTableNameList();
            for (String tempTableName : tableNameList) {
                if (tableName.equalsIgnoreCase(tempTableName)) {
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private TableObject initTableObject(String schema, String tableName) {
        String sql = "SELECT COLUMN_NAME "
                     + "FROM INFORMATION_SCHEMA.COLUMNS "
                    + "WHERE TABLE_NAME = '" + tableName + "' "
                      + "AND TABLE_SCHEMA = '" + schema + "'";

        TableObject tableObject = new TableObject();
        tableObject.setSchema(schema);
        tableObject.setTableName(tableName);
        tableObject.setColumnNames(nativeRepository.executeQuery(sql));

        sql = "SELECT COLUMN_NAME "
              + "FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE "
             + "WHERE TABLE_NAME = '" + tableName + "' "
               + "AND TABLE_SCHEMA = '" + schema + "' "
               + "AND CONSTRAINT_NAME = 'PRIMARY'";

        StringBuilder primaryKeys = new StringBuilder();
        for (Object primaryColumnName : nativeRepository.executeQuery(sql)) {
            primaryKeys.append(primaryColumnName).append(PK_SEP);
        }
        if (primaryKeys.length() > 0) {
            primaryKeys.deleteCharAt(primaryKeys.length() - 1);
        }
        tableObject.setPrimaryKeys(primaryKeys.toString());
        return tableObject;
    }

    private void doInsertSync(WriteRowsEventData writeRowsEventData, long pos) {
        int counter = 0;
        for (Object[] row : writeRowsEventData.getRows()) {
            for (int i = 0; i < row.length; i++) {
                if (row[i] == null) {
                    // ???????????????????????????mongo
                    continue;
                }
                counter = syncToMongo(tableObject.getTableName(), tableObject.getColumnNames().get(i),
                    null, row[i].toString(),
                    getPrimaryKeysValue(tableObject, row), SyncMethod.INSERT,
                    pos, counter);
            }
        }
    }

    /**
     * ??????????????? mongo?????????????????????
     *
     * @param table       ??????
     * @param column      ??????
     * @param before      ??????
     * @param after       ??????
     * @param primaryKeys ??????
     * @param method      ????????????
     * @param pos         ??????
     * @param inc         ???????????????
     * @return ?????????????????????
     */
    private int syncToMongo(String table, String column, String before, String after, String primaryKeys, SyncMethod method, long pos, int inc) {
        List<SyncDocumentModel> modelList = filterSearchDocument(table, column);
        if (CollectionUtil.isEmpty(modelList)) {
            // ??????????????????????????????SearchColumnModel,????????????mongo???,????????????
            return inc;
        }

        // ???modelList????????????????????????????????????config???????????????????????????????????????????????????????????????url??????????????????
        // ???????????????????????????document??????mongo??????
        for (SyncDocumentModel syncDocumentModel : modelList) {
            syncDocumentModel.setTab(table);
            syncDocumentModel.setCol(column);
            syncDocumentModel.setConb(before);
            syncDocumentModel.setCona(after);
            syncDocumentModel.setPri(primaryKeys);
            syncDocumentModel.setDataBaseType(DataBaseType.RDB);
            syncDocumentModel.setMethod(method);
            LOGGER.debug("start push data change pos:{},pri:{}", pos, syncDocumentModel.getPri());
            mongoSyncService.push(pos + ":" + inc++ + ":" + method, syncDocumentModel);
        }
        return inc;
    }

    private String getPrimaryKeysValue(TableObject tableObject, Object[] row) {
        String primaryKeys = tableObject.getPrimaryKeys();
        if (StringUtil.isBlank(primaryKeys)) {
            return "";
        }
        StringBuilder primaryKeysValue = new StringBuilder();
        primaryKeysValue.append(tableObject.getSchema()).append(PK_SEP).append(tableObject.getTableName()).append(PK_SEP);
        List<String> columnNames = tableObject.getColumnNames();
        String transCode = "\\" + PK_SEP;
        for (String pri : primaryKeys.split(transCode)) {
            for (int i = 0; i < columnNames.size(); i++) {
                if (pri.equalsIgnoreCase(columnNames.get(i))) {
                    primaryKeysValue.append(row[i]).append(PK_SEP);
                    break;
                }
            }
        }
        primaryKeysValue.deleteCharAt(primaryKeysValue.length() - 1);
        return primaryKeysValue.toString();
    }

    /**
     * searchDocument????????????
     * ????????????????????????????????????????????????????????????searchColumnModel????????????????????????????????????????????????????????????????????????
     * mongodb???????????????????????????????????????????????????????????????????????????????????????syncDocumentModel??????????????????????????????????????????url???
     * ???????????????????????????????????????????????????????????????????????????
     *
     * @param tableName  ??????
     * @param columnName ?????????
     * @return SyncDocumentModel??????
     */
    private List<SyncDocumentModel> filterSearchDocument(String tableName, String columnName) {
        List<SyncDocumentModel> modelList = new ArrayList<>();
        Map<String, Object> searchConfigBeans = searchConfigService.getSearchConfigs(DataBaseType.RDB);
        for (Map.Entry<String, Object> entry : searchConfigBeans.entrySet()) {
            AbstractSearchConfigs tempSearchConfig = (AbstractSearchConfigs) entry.getValue();
            SearchColumnModel searchColumnModel = tempSearchConfig.getSearchColumnByTableNameAndColumnName(tableName, columnName);
            if (searchColumnModel == null) {
                continue;
            }
            SyncDocumentModel syncDocumentModel = new SyncDocumentModel();
            syncDocumentModel.setDes(searchColumnModel.getDescColumn());
            syncDocumentModel.setAlias(searchColumnModel.getColumnAlias());
            syncDocumentModel.setUrl(searchColumnModel.getUrl());
            modelList.add(syncDocumentModel);
        }
        return modelList;
    }

    private void doUpdateSync(UpdateRowsEventData updateRowsEventData, long pos) {
        int counter = 0;
        for (Map.Entry<Serializable[], Serializable[]> map : updateRowsEventData.getRows()) {
            Object[] before = map.getKey();
            Object[] after = map.getValue();
            // ???mongo?????????before??????????????????????????????after??????????????????????????????
            for (int i = 0; i < before.length; i++) {
                if (isChange(before[i], after[i])) {
                    // ??????????????????????????????????????????mongo??????
                    String columnContentBefore = null == before[i] ? null : before[i].toString();
                    String columnContentAfter = null == after[i] ? null : after[i].toString();
                    counter = syncToMongo(tableObject.getTableName(), tableObject.getColumnNames().get(i),
                        columnContentBefore, columnContentAfter, null,
                        SyncMethod.UPDATE, pos, counter);
                }
            }
        }
    }

    private boolean isChange(Object before, Object after) {
        if (null == before && null == after) {
            return false;
        }
        if (null == before || null == after) {
            return true;
        }
        return !before.toString().equals(after.toString());
    }

    private void doDeleteSync(DeleteRowsEventData deleteRowsEventData, long pos) {
        int counter = 0;
        for (Object[] row : deleteRowsEventData.getRows()) {
            for (int i = 0; i < row.length; i++) {
                String tableName = tableObject.getTableName();
                String columnName = tableObject.getColumnNames().get(i);
                String primaryKeysValue = getPrimaryKeysValue(tableObject, row);
                counter = syncToMongo(tableName, columnName,
                    null, row[i].toString(), primaryKeysValue,
                    SyncMethod.DELETE, pos, counter);
            }
        }
    }

    private void xid() {
        needToProcess = false;
    }

}
