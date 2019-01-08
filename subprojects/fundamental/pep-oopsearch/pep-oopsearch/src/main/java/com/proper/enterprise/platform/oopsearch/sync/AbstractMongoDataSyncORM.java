package com.proper.enterprise.platform.oopsearch.sync;

import com.proper.enterprise.platform.core.jpa.repository.NativeRepository;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.oopsearch.api.enums.DataBaseType;
import com.proper.enterprise.platform.oopsearch.document.SearchDocument;
import com.proper.enterprise.platform.oopsearch.api.model.SearchColumnModel;
import com.proper.enterprise.platform.oopsearch.repository.SyncMongoRepository;
import com.proper.enterprise.platform.oopsearch.config.service.SearchConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

/**
 * 同步mongo抽象类
 * 该类实现了MongoDataSyncService接口
 * 可以继承此类来实现mongo同步操作
 * 该类提供了一些通用的方法及实现
 * 子类可以仅对接口的fullSynchronization方法进行实现即可
 */
public abstract class AbstractMongoDataSyncORM extends AbstractMongoDataSync {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMongoDataSyncORM.class);

    /**
     * 数据源config
     */
    @Value("database.url")
    private String databaseUrl;

    /**
     * 同步mongo所使用的repository
     */
    @Autowired
    private SyncMongoRepository syncMongoRepository;

    /**
     * 本地化repository
     */
    @Autowired
    public NativeRepository nativeRepository;

    /**
     * 查询配置信息service
     */
    @Autowired
    private SearchConfigService searchConfigService;

    /**
     * 全量同步方法
     * 将配置中需要查询的表，及字段的内容，全部同步到mongodb当中
     */
    @Override
    @SuppressWarnings("unchecked")
    public void fullSynchronization() {
        Map<String, Object> searchConfigBeans = searchConfigService.getSearchConfigs(DataBaseType.RDB);
        if (searchConfigBeans == null) {
            return;
        }
        Map<String, List<SearchColumnModel>> tableColumnMap = getTableColumnMap(searchConfigBeans);
        List<SearchDocument> documentList = new ArrayList<>();
        for (Map.Entry<String, List<SearchColumnModel>> tableColumnEntry : tableColumnMap.entrySet()) {
            StringBuffer querySql = new StringBuffer();
            List<SearchColumnModel> searchColumnList = tableColumnEntry.getValue();
            getQuerySql(tableColumnEntry, searchColumnList, querySql);
            if (StringUtil.isEmpty(querySql)) {
                continue;
            }
            List<Map<String, Object>> resultList;
            try {
                resultList = nativeRepository.executeEntityMapQuery(querySql.toString());
            } catch (Exception e) {
                LOGGER.error("init binlog conf error,sql:{}", querySql, e);
                continue;
            }
            if (null == resultList) {
                continue;
            }
            Map<String, List<String>> contextFilterMap = new HashMap<>(16);
            for (Map<String, Object> resultMap : resultList) {
                String priValue = "";
                Set<Map.Entry<String, Object>> entrySet = resultMap.entrySet();
                for (Map.Entry<String, Object> getPriEntry : entrySet) {
                    String key = getPriEntry.getKey().toLowerCase();
                    if ("pri".equals(key)) {
                        priValue = getPriEntry.getValue().toString();
                        break;
                    }
                }
                for (Map.Entry<String, Object> entry : entrySet) {
                    String column = entry.getKey();
                    if (!"pri".equalsIgnoreCase(column)) {
                        SearchColumnModel searchColumnMongo = getSearchColumnMongo(searchColumnList, column);
                        if (searchColumnMongo == null) {
                            LOGGER.error("can not find search column:{}", column);
                            continue;
                        }
                        String value;
                        Object objValue = entry.getValue();
                        boolean isRepeat = false;
                        if (objValue != null) {
                            value = objValue.toString();
                            if (!"".equals(value.trim())) {
                                if (contextFilterMap.containsKey(value)) {
                                    List<String> columnList = contextFilterMap.get(value);
                                    for (String columnTemp : columnList) {
                                        if (columnTemp.equalsIgnoreCase(searchColumnMongo.getColumn())) {
                                            isRepeat = true;
                                            break;
                                        }
                                    }
                                    if (!isRepeat) {
                                        columnList.add(searchColumnMongo.getColumn());
                                    }
                                } else {
                                    List<String> columnList = new ArrayList<>();
                                    columnList.add(searchColumnMongo.getColumn());
                                    contextFilterMap.put(value, columnList);
                                }
                                if (!isRepeat) {
                                    documentList.add(buildDocument(value, priValue, searchColumnMongo));
                                }
                            }
                        }
                    }
                }
            }
        }
        if (documentList.size() > 0) {
            syncMongoRepository.deleteAllByTabIn(getConfigTables(DataBaseType.RDB));
            syncMongoRepository.saveAll(documentList);
        }
    }

    private SearchDocument buildDocument(String value, String priValue, SearchColumnModel searchColumnMongo) {
        SearchDocument demoUserDocument = new SearchDocument();
        demoUserDocument.setCon(value);
        demoUserDocument.setCol(searchColumnMongo.getColumn());
        demoUserDocument.setTab(searchColumnMongo.getTable());
        demoUserDocument.setDes(searchColumnMongo.getDescColumn());
        demoUserDocument.setPri(priValue);
        demoUserDocument.setAli(searchColumnMongo.getColumnAlias());
        demoUserDocument.setUrl(searchColumnMongo.getUrl());
        return demoUserDocument;
    }

    /**
     * 获取查询sql语句
     * example: SELECT columnA, CONCAT( 'dbname|tablename|', ID ) AS pri FROM tablename
     *
     * @param tableColumnEntry 表名字段关系集合
     * @param searchColumnList 查询字段列表
     * @param querySql         查询sql语句
     */
    private void getQuerySql(Map.Entry<String, List<SearchColumnModel>> tableColumnEntry, List<SearchColumnModel> searchColumnList,
                             StringBuffer querySql) {
        querySql.append("SELECT ");
        // 取出该表将要查询的字段
        if (searchColumnList.size() == 0) {
            LOGGER.error("can not find search column");
            return;
        }
        for (int j = 0; j < searchColumnList.size(); j++) {
            SearchColumnModel searchColumn = searchColumnList.get(j);
            String columnName = searchColumn.getColumn();
            querySql.append(columnName + ",");
        }
        // 获取主键
        String tableName = tableColumnEntry.getKey();
        List<String> primaryKeys = getPrimaryKeys(tableName);
        if (primaryKeys.size() > 0) {
            String url = databaseUrl;
            String[] temp = url.split(";")[0].split("/");
            String schema = temp[temp.length - 1];
            querySql.append(" CONCAT( '").append(schema).append("|").append(tableName).append("|',");
            for (int k = 0; k < primaryKeys.size(); k++) {
                querySql.append(primaryKeys.get(k) + ",'|',");
            }
            querySql.delete(querySql.length() - ",'|',".length(), querySql.length());
            querySql.append(") AS pri ");
        } else {
            querySql.deleteCharAt(querySql.length() - 1);
        }

        querySql.append(" FROM " + tableName);
    }

}
