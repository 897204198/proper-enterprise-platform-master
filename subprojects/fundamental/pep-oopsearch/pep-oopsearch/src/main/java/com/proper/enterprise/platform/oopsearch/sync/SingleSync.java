package com.proper.enterprise.platform.oopsearch.sync;

import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.oopsearch.api.enums.SyncMethod;
import com.proper.enterprise.platform.oopsearch.api.model.SyncDocumentModel;
import com.proper.enterprise.platform.oopsearch.document.SearchDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SingleSync {

    /**
     * 操作mongo的mongoTemplate对象
     */
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 增量同步mongo
     * 针对具体的数据变动，来进行mongo的同步操作
     *
     * @param doc 同步文档对象
     */
    public void singleSynchronization(SyncDocumentModel doc) {
        SearchDocument searchDocument = convertToSearchDocument(doc);
        Query query = new Query();
        if (SyncMethod.INSERT == doc.getMethod()) {
            query.addCriteria(Criteria.where("con").is(searchDocument.getCon()));
            query.addCriteria(Criteria.where("tab").is(searchDocument.getTab()));
            query.addCriteria(Criteria.where("col").is(searchDocument.getCol()));
            query.addCriteria(Criteria.where("ali").is(searchDocument.getAli()));
            query.addCriteria(Criteria.where("url").is(searchDocument.getUrl()));
            List<SearchDocument> result = mongoTemplate.find(query, SearchDocument.class);
            if (result.size() == 0) {
                mongoTemplate.insert(searchDocument);
            }
        } else if (SyncMethod.UPDATE == doc.getMethod()) {
            if (StringUtil.isNotEmpty(doc.getPri())) {
                query.addCriteria(Criteria.where("pri").is(searchDocument.getPri()));
                query.addCriteria(Criteria.where("col").is(searchDocument.getCol()));
                query.addCriteria(Criteria.where("tab").is(searchDocument.getTab()));
                Update update = new Update();
                update.set("con", searchDocument.getCon());
                mongoTemplate.updateMulti(query, update, SearchDocument.class);
                return;
            }
            String conBefore = doc.getConb();
            query.addCriteria(Criteria.where("con").is(conBefore));
            query.addCriteria(Criteria.where("tab").is(searchDocument.getTab()));
            query.addCriteria(Criteria.where("col").is(searchDocument.getCol()));
            query.addCriteria(Criteria.where("ali").is(searchDocument.getAli()));
            query.addCriteria(Criteria.where("url").is(searchDocument.getUrl()));
            Update update = new Update();
            update.set("con", searchDocument.getCon());
            mongoTemplate.updateMulti(query, update, SearchDocument.class);
        } else if (SyncMethod.DELETE == doc.getMethod()) {
            query.addCriteria(Criteria.where("pri").is(searchDocument.getPri()));
            query.addCriteria(Criteria.where("tab").is(searchDocument.getTab()));
            mongoTemplate.remove(query, SearchDocument.class);
        }
    }

    /**
     * 文档转换
     * 将SyncDocumentModel转换为SearchDocument
     *
     * @param doc 同步文档对象
     * @return SearchDocument文档对象
     */
    protected SearchDocument convertToSearchDocument(SyncDocumentModel doc) {
        SearchDocument searchDocument = new SearchDocument();
        searchDocument.setCon(doc.getCona());
        searchDocument.setTab(doc.getTab());
        searchDocument.setCol(doc.getCol().toLowerCase());
        searchDocument.setPri(doc.getPri());
        searchDocument.setDes(doc.getDes());
        searchDocument.setAli(doc.getAlias());
        searchDocument.setUrl(doc.getUrl());
        return searchDocument;
    }
}
