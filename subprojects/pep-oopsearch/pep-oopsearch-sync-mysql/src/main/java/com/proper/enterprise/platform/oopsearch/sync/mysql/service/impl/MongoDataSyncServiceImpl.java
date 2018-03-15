package com.proper.enterprise.platform.oopsearch.sync.mysql.service.impl;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.jpa.repository.NativeRepository;
import com.proper.enterprise.platform.oopsearch.api.annotation.SearchConfig;
import com.proper.enterprise.platform.oopsearch.api.conf.AbstractSearchConfigs;
import com.proper.enterprise.platform.oopsearch.api.model.SearchColumnModel;
import com.proper.enterprise.platform.oopsearch.api.model.SyncDocumentModel;
import com.proper.enterprise.platform.oopsearch.api.serivce.MongoDataSyncService;
import com.proper.enterprise.platform.oopsearch.api.document.SearchDocument;
import com.proper.enterprise.platform.oopsearch.sync.mysql.repository.SyncMongoRepository;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import com.zaxxer.hikari.HikariConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value = "MongoDataSyncServiceImpl")
public class MongoDataSyncServiceImpl implements MongoDataSyncService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDataSyncServiceImpl.class);

    @Autowired
    // 初始化PEPApplicationContext，避免后续方法使用时尚未初始化导致获取不到application进而出错
    PEPApplicationContext pepApplicationContext;

    @Autowired
    private I18NService i18NService;

    @Autowired
    private NativeRepository nativeRepository;

    @Autowired
    SyncMongoRepository syncMongoRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    HikariConfig hikariConfig;

    @Override
    public void singleSynchronization(SyncDocumentModel doc, String method) {
        SearchDocument searchDocument = convertToSearchDocument(doc);
        Query query = new Query();
        if ("insert".equalsIgnoreCase(method)) {
            query.addCriteria(Criteria.where("tab").is(searchDocument.getTab()));
            query.addCriteria(Criteria.where("col").is(searchDocument.getCol()));
            query.addCriteria(Criteria.where("con").is(searchDocument.getCon()));
            List<SearchDocument> result = mongoTemplate.find(query, SearchDocument.class);
            if (result.size() == 0) {
                mongoTemplate.insert(searchDocument);
            }
        } else if ("update".equalsIgnoreCase(method)) {
            query.addCriteria(Criteria.where("tab").is(searchDocument.getTab()));
            query.addCriteria(Criteria.where("col").is(searchDocument.getCol()));
            String conBefore = doc.getConb();
            query.addCriteria(Criteria.where("con").is(conBefore));

            Update update = new Update();
            update.set("con", searchDocument.getCon());

            mongoTemplate.updateMulti(query, update, SearchDocument.class);
        } else if ("delete".equalsIgnoreCase(method)) {
            query.addCriteria(Criteria.where("tab").is(searchDocument.getTab()));
            query.addCriteria(Criteria.where("col").is(searchDocument.getCol()));
            query.addCriteria(Criteria.where("con").is(searchDocument.getCon()));
            query.addCriteria(Criteria.where("pri").is(searchDocument.getPri()));
            mongoTemplate.remove(query, SearchDocument.class);
        }
    }

    private SearchDocument convertToSearchDocument(SyncDocumentModel doc) {
        SearchDocument searchDocument = new SearchDocument();
        searchDocument.setCon(doc.getCona());
        searchDocument.setTab(doc.getTab());
        searchDocument.setCol(doc.getCol());
        searchDocument.setPri(doc.getPri());
        searchDocument.setDes(doc.getDes());
        return searchDocument;
    }

    @Override
    public void fullSynchronization() {
        syncMongoRepository.deleteAll();

        // 整理所有需要查询的表名，及表名中对应的字段
        Map<String, Object> searchConfigBeans = pepApplicationContext.getApplicationContext().getBeansWithAnnotation(SearchConfig.class);
        Map<String, List<SearchColumnModel>> tableColumnMap = getTableColumnMap(searchConfigBeans);
        List<SearchDocument> documentList = new ArrayList<>();
        for (Map.Entry<String, List<SearchColumnModel>> tableColumnEntry: tableColumnMap.entrySet()) {
            StringBuffer querySql = new StringBuffer();
            querySql.append("SELECT ");
            // 取出该表将要查询的字段
            List<SearchColumnModel> searchColumnList = tableColumnEntry.getValue();
            if (searchColumnList.size() == 0) {
                throw new ErrMsgException(i18NService.getMessage("search.can.not.find.column"));
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
                String url = hikariConfig.getDataSourceProperties().get("url").toString();
                String[] temp = url.split(";")[0].split("/");
                String schema = temp[temp.length - 1];
                querySql.append(" CONCAT( '").append(schema).append("|").append(tableName).append("|',");
                for (int k = 0; k < primaryKeys.size(); k++) {
                    querySql.append(primaryKeys.get(k) + ",'|'");
                }
                querySql.delete(querySql.length() - 4, querySql.length());
                querySql.append(") AS pri ");
            } else {
                querySql.deleteCharAt(querySql.length() - 1);
            }

            querySql.append(" FROM " + tableName);
            List<Map<String, Object>> resultList = nativeRepository.executeEntityMapQuery(querySql.toString());
            Map<String, List<String>> contextFilterMap = new HashMap<>();
            for (Map<String, Object> resultMap:resultList) {
                String priValue = "";
                if (resultMap.containsKey("pri")) {
                    priValue = resultMap.get("pri").toString();
                }
                Set<Map.Entry<String, Object>> entrySet = resultMap.entrySet();
                for (Map.Entry<String, Object> entry: entrySet) {
                    String column = entry.getKey();
                    if (!"pri".equalsIgnoreCase(column)) {
                        SearchColumnModel searchColumnMongo = getSearchColumnMongo(searchColumnList, column);
                        if (searchColumnMongo == null) {
                            throw new ErrMsgException(i18NService.getMessage("search.can.not.find.column"));
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
                                    documentList.add(demoUserDocument);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (documentList.size() > 0) {
            syncMongoRepository.save(documentList);
        }
    }

    public Map<String, List<SearchColumnModel>> getTableColumnMap(Map<String, Object> searchConfigBeans) {
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

    public SearchColumnModel getSearchColumnMongo(List<SearchColumnModel> searchColumnList, String column) {
        for (SearchColumnModel searchColumn: searchColumnList) {
            if (column.equalsIgnoreCase(searchColumn.getColumn())) {
                return searchColumn;
            }
        }
        return null;
    }

    public List<String> getPrimaryKeys(String tableName) {
        Properties properties = hikariConfig.getDataSourceProperties();
        String url = properties.get("url").toString();
        List<String> primaryKeys = new ArrayList<>();
        // 确保使用的是mysql数据源，避免数据源错误导致执行sql异常
        if (url.toLowerCase().contains("jdbc:mysql:")) {
            String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_NAME = '" + tableName + "'";
            List<Object> resultList = null;
            resultList = nativeRepository.executeQuery(sql);
            for (Object row:resultList) {
                primaryKeys.add(row.toString());
            }
        }
        return primaryKeys;
    }
}
