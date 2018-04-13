package com.proper.enterprise.platform.oopsearch.api.sync;

import com.proper.enterprise.platform.core.jpa.repository.NativeRepository;
import com.proper.enterprise.platform.oopsearch.api.conf.AbstractSearchConfigs;
import com.proper.enterprise.platform.oopsearch.api.document.SearchDocument;
import com.proper.enterprise.platform.oopsearch.api.model.SearchColumnModel;
import com.proper.enterprise.platform.oopsearch.api.model.SyncDocumentModel;
import com.proper.enterprise.platform.oopsearch.api.repository.SearchConfigRepository;
import com.proper.enterprise.platform.oopsearch.api.repository.SyncMongoRepository;
import com.proper.enterprise.platform.oopsearch.api.serivce.MongoDataSyncService;
import com.proper.enterprise.platform.oopsearch.api.serivce.SearchConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.*;

/**
 * 同步mongo抽象类
 * 该类实现了MongoDataSyncService接口
 * 可以继承此类来实现mongo同步操作
 * 该类提供了一些通用的方法及实现
 * 子类可以仅对接口的fullSynchronization方法进行实现即可
 * */
public abstract class AbstractMongoDataSync implements MongoDataSyncService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMongoDataSync.class);

    // 操作mongo的mongoTemplate对象
    @Autowired
    private MongoTemplate mongoTemplate;

    // 同步mongo所使用的repository
    @Autowired
    private SyncMongoRepository syncMongoRepository;

    // 数据源config
    @Value("database.url")
    private String databaseUrl;

    // 本地化repository
    @Autowired
    protected NativeRepository nativeRepository;

    // 查询配置信息repository
    @Autowired
    SearchConfigRepository searchConfigRepository;

    @Autowired
    SearchConfigService searchConfigService;


    /**
     * 全量同步方法
     * 将配置中需要查询的表，及字段的内容，全部同步到mongodb当中
     *
     * */
    @Override
    public void fullSynchronization() {
        // 整理所有需要查询的表名，及表名中对应的字段
        Map<String, Object> searchConfigBeans = searchConfigService.getSearchConfigs();
        if (searchConfigBeans == null) {
            return;
        }
        Map<String, List<SearchColumnModel>> tableColumnMap = getTableColumnMap(searchConfigBeans);
        List<SearchDocument> documentList = new ArrayList<>();
        for (Map.Entry<String, List<SearchColumnModel>> tableColumnEntry: tableColumnMap.entrySet()) {
            StringBuffer querySql = new StringBuffer();
            querySql.append("SELECT ");
            // 取出该表将要查询的字段
            List<SearchColumnModel> searchColumnList = tableColumnEntry.getValue();
            if (searchColumnList.size() == 0) {
                LOGGER.error("can not find search column");
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
            List<Map<String, Object>> resultList = nativeRepository.executeEntityMapQuery(querySql.toString());
            Map<String, List<String>> contextFilterMap = new HashMap<>();
            for (Map<String, Object> resultMap:resultList) {
                String priValue = "";
                Set<Map.Entry<String, Object>> entrySet = resultMap.entrySet();
                for (Map.Entry<String, Object> getPriEntry: entrySet) {
                    String key = getPriEntry.getKey().toLowerCase();
                    if ("pri".equals(key)) {
                        priValue = getPriEntry.getValue().toString();
                        break;
                    }
                }
                for (Map.Entry<String, Object> entry: entrySet) {
                    String column = entry.getKey();
                    if (!"pri".equalsIgnoreCase(column)) {
                        SearchColumnModel searchColumnMongo = getSearchColumnMongo(searchColumnList, column);
                        if (searchColumnMongo == null) {
                            LOGGER.error("can not find search column");
                        }
                        // 插入mongodb
                        String value;
                        Object objValue = entry.getValue();
                        boolean isRepeat = false;
                        if (objValue != null) {
                            value = objValue.toString();
                            if (!"".equals(value.trim())) {
                                if (contextFilterMap.containsKey(value)) {
                                    List<String> columnList = contextFilterMap.get(value);
                                    for (String columnTemp: columnList) {
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
                                    SearchDocument demoUserDocument = new SearchDocument();
                                    demoUserDocument.setCon(value);
                                    demoUserDocument.setCol(searchColumnMongo.getColumn());
                                    demoUserDocument.setTab(searchColumnMongo.getTable());
                                    demoUserDocument.setDes(searchColumnMongo.getDescColumn());
                                    demoUserDocument.setPri(priValue);
                                    demoUserDocument.setAli(searchColumnMongo.getColumnAlias());
                                    demoUserDocument.setUrl(searchColumnMongo.getUrl());
                                    documentList.add(demoUserDocument);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (documentList.size() > 0) {
            syncMongoRepository.deleteAll();
            syncMongoRepository.save(documentList);
        }
    }

    /**
     * 获取主键方法
     * 各子类根据数据库的不同，进行具体实现
     *
     * @param  tableName 表名
     * @return  主键集合
     * */
    protected abstract List<String> getPrimaryKeys(String tableName);

    /**
     * 增量同步mongo
     * 针对具体的数据变动，来进行mongo的同步操作
     *
     * @param  doc 同步文档对象
     * @param  method 具体操作方式(insert、update、delete)
     * */
    @Override
    public void singleSynchronization(SyncDocumentModel doc, String method) {
        SearchDocument searchDocument = convertToSearchDocument(doc);
        Query query = new Query();
        if ("insert".equalsIgnoreCase(method)) {
            query.addCriteria(Criteria.where("con").is(searchDocument.getCon()));
            query.addCriteria(Criteria.where("tab").is(searchDocument.getTab()));
            query.addCriteria(Criteria.where("col").is(searchDocument.getCol()));
            query.addCriteria(Criteria.where("ali").is(searchDocument.getAli()));
            query.addCriteria(Criteria.where("url").is(searchDocument.getUrl()));
            List<SearchDocument> result = mongoTemplate.find(query, SearchDocument.class);
            if (result.size() == 0) {
                mongoTemplate.insert(searchDocument);
            }
        } else if ("update".equalsIgnoreCase(method)) {
            String conBefore = doc.getConb();
            query.addCriteria(Criteria.where("con").is(conBefore));
            query.addCriteria(Criteria.where("tab").is(searchDocument.getTab()));
            query.addCriteria(Criteria.where("col").is(searchDocument.getCol()));
            query.addCriteria(Criteria.where("ali").is(searchDocument.getAli()));
            query.addCriteria(Criteria.where("url").is(searchDocument.getUrl()));

            Update update = new Update();
            update.set("con", searchDocument.getCon());

            mongoTemplate.updateMulti(query, update, SearchDocument.class);
        } else if ("delete".equalsIgnoreCase(method)) {
            query.addCriteria(Criteria.where("con").is(searchDocument.getCon()));
            query.addCriteria(Criteria.where("pri").is(searchDocument.getPri()));
            query.addCriteria(Criteria.where("tab").is(searchDocument.getTab()));
            query.addCriteria(Criteria.where("col").is(searchDocument.getCol()));
            query.addCriteria(Criteria.where("ali").is(searchDocument.getAli()));
            query.addCriteria(Criteria.where("url").is(searchDocument.getUrl()));
            mongoTemplate.remove(query, SearchDocument.class);
        }
    }

    /**
     * 文档转换
     * 将SyncDocumentModel转换为SearchDocument
     * @param  doc 同步文档对象
     * @return SearchDocument文档对象
     * */
    protected SearchDocument convertToSearchDocument(SyncDocumentModel doc) {
        SearchDocument searchDocument = new SearchDocument();
        searchDocument.setCon(doc.getCona());
        searchDocument.setTab(doc.getTab());
        searchDocument.setCol(doc.getCol());
        searchDocument.setPri(doc.getPri());
        searchDocument.setDes(doc.getDes());
        searchDocument.setAli(doc.getAlias());
        searchDocument.setUrl(doc.getUrl());
        return searchDocument;
    }

    /**
     * 文档转换
     * 将SyncDocumentModel转换为SearchDocument
     * @param  searchConfigBeans 搜索配置bean集合
     * @return 表名字段关系集合对象
     * */
    protected Map<String, List<SearchColumnModel>> getTableColumnMap(Map<String, Object> searchConfigBeans) {
        Map<String, List<SearchColumnModel>> tableColumnMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : searchConfigBeans.entrySet()) {
            AbstractSearchConfigs tempSearchConfig = (AbstractSearchConfigs) entry.getValue();
            Map<String, List<SearchColumnModel>> tempTableColumnMap = tempSearchConfig.getSearchTableColumnMap();
            for (Map.Entry<String, List<SearchColumnModel>> tempEntry: tempTableColumnMap.entrySet()) {
                String tempTableName = tempEntry.getKey();
                if (!tableColumnMap.containsKey(tempTableName)) {
                    tableColumnMap.put(tempTableName, tempEntry.getValue());
                } else {
                    List<SearchColumnModel> searchColumnList = tableColumnMap.get(tempTableName);
                    List<SearchColumnModel> tempSearchColumnList = tempEntry.getValue();
                    List<SearchColumnModel> addSearchColumnList = new ArrayList<>();
                    for (SearchColumnModel tempSearchColumn: tempSearchColumnList) {
                        boolean isRepeat = false;
                        String tempColumn = tempSearchColumn.getColumn();
                        for (SearchColumnModel searchColumn: searchColumnList) {
                            String column = searchColumn.getColumn();
                            if (tempColumn.equalsIgnoreCase(column)) {
                                isRepeat = true;
                                break;
                            }
                        }
                        if (!isRepeat) {
                            addSearchColumnList.add(tempSearchColumn);
                        }
                    }
                    if (addSearchColumnList.size() > 0) {
                        searchColumnList.addAll(addSearchColumnList);
                    }
                }
            }

        }
        return tableColumnMap;
    }

    /**
     * 文档转换
     * 将SyncDocumentModel转换为SearchDocument
     * @param  searchColumnList 搜索字段集合
     * @param  column 字段名称
     * @return SearchColumnModel文档模型
     * */
    protected SearchColumnModel getSearchColumnMongo(List<SearchColumnModel> searchColumnList, String column) {
        for (SearchColumnModel searchColumn: searchColumnList) {
            if (column.equalsIgnoreCase(searchColumn.getColumn())) {
                return searchColumn;
            }
        }
        return null;
    }
}
