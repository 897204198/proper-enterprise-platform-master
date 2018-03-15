package com.proper.enterprise.platform.oopsearch.api.sync;

import com.proper.enterprise.platform.oopsearch.api.conf.AbstractSearchConfigs;
import com.proper.enterprise.platform.oopsearch.api.document.SearchDocument;
import com.proper.enterprise.platform.oopsearch.api.model.SearchColumnModel;
import com.proper.enterprise.platform.oopsearch.api.model.SyncDocumentModel;
import com.proper.enterprise.platform.oopsearch.api.serivce.MongoDataSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 同步mongo抽象类
 * 该类实现了MongoDataSyncService接口
 * 可以继承此类来实现mongo同步操作
 * 该类提供了一些通用的方法及实现
 * 子类可以仅对接口的fullSynchronization方法进行实现即可
 * */
public abstract class AbstractMongoDataSync implements MongoDataSyncService {

    //操作mongo的mongoTemplate对象
    @Autowired
    MongoTemplate mongoTemplate;

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
