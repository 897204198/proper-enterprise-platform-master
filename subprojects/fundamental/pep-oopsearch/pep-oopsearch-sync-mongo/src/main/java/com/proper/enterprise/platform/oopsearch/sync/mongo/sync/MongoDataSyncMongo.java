package com.proper.enterprise.platform.oopsearch.sync.mongo.sync;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.mongo.service.MongoShellService;
import com.proper.enterprise.platform.oopsearch.api.enums.DataBaseType;
import com.proper.enterprise.platform.oopsearch.document.SearchDocument;
import com.proper.enterprise.platform.oopsearch.api.model.SearchColumnModel;
import com.proper.enterprise.platform.oopsearch.repository.SyncMongoRepository;
import com.proper.enterprise.platform.oopsearch.config.service.SearchConfigService;
import com.proper.enterprise.platform.oopsearch.sync.AbstractMongoDataSync;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value = "mongoToMongoDataSync")
public class MongoDataSyncMongo extends AbstractMongoDataSync {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDataSyncMongo.class);

    /**
     * 同步mongo所使用的repository
     */
    @Autowired
    private SyncMongoRepository syncMongoRepository;

    /**
     * 本地化repository
     */
    @Autowired
    protected MongoShellService mongoShellService;


    @Autowired
    SearchConfigService searchConfigService;

    /**
     * 全量同步方法
     * 将配置中需要查询的表，及字段的内容，全部同步到mongodb当中
     */
    @Override
    public void fullSynchronization() {
        // 整理所有需要查询的表名，及表名中对应的字段
        Map<String, Object> searchConfigBeans = searchConfigService.getSearchConfigs(DataBaseType.MONGODB);
        if (searchConfigBeans == null) {
            return;
        }
        Map<String, List<SearchColumnModel>> tableColumnMap = getTableColumnMap(searchConfigBeans);
        List<SearchDocument> documentList = new ArrayList<>();
        for (Map.Entry<String, List<SearchColumnModel>> tableColumnEntry : tableColumnMap.entrySet()) {
            // 取出该表将要查询的字段
            List<SearchColumnModel> searchColumnList = tableColumnEntry.getValue();
            //表名
            String tableName = tableColumnEntry.getKey();
            List<Document> resultList = null;
            try {
                resultList = mongoShellService.query(tableName, "");
            } catch (Exception e) {
                LOGGER.error("fullSynchronization mongo query error", e);
                throw new ErrMsgException("fullSynchronization mongo query error");
            }
            for (Document document : resultList) {
                for (Map.Entry<String, Object> entry : document.entrySet()) {
                    String column = entry.getKey();
                    SearchColumnModel searchColumnMongo = getSearchColumnMongo(searchColumnList, column);
                    if (searchColumnMongo == null) {
                        continue;
                    }
                    Object objValue = entry.getValue();
                    if (objValue != null) {
                        String value = objValue.toString();
                        if (!"".equals(value.trim())) {
                            SearchDocument demoUserDocument = new SearchDocument();
                            demoUserDocument.setCon(value);
                            demoUserDocument.setCol(searchColumnMongo.getColumn());
                            demoUserDocument.setTab(searchColumnMongo.getTable());
                            demoUserDocument.setDes(searchColumnMongo.getDescColumn());
                            demoUserDocument.setPri(document.getObjectId("_id").toString());
                            demoUserDocument.setAli(searchColumnMongo.getColumnAlias());
                            demoUserDocument.setUrl(searchColumnMongo.getUrl());
                            documentList.add(demoUserDocument);
                        }
                    }
                }
            }
        }
        if (documentList.size() > 0) {
            syncMongoRepository.deleteAllByTabIn(getConfigTables(DataBaseType.MONGODB));
            syncMongoRepository.saveAll(documentList);
        }
    }

    @Override
    protected List<String> getPrimaryKeys(String tableName) {
        return null;
    }
}
