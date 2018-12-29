package com.proper.enterprise.platform.oopsearch.sync.mongo.service.impl;

import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.proper.enterprise.platform.oopsearch.api.enums.DataBaseType;
import com.proper.enterprise.platform.oopsearch.api.enums.SyncMethod;
import com.proper.enterprise.platform.oopsearch.api.model.SyncDocumentModel;
import com.proper.enterprise.platform.oopsearch.service.impl.SyncCacheService;
import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.notice.Notice;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("mongoSyncMongoNotice")
public class NoticeServiceImpl implements Notice {

    private static final String OBJID_KEY = "_id";

    @Autowired
    private SyncCacheService mongoSyncService;

    @Override
    public void handleDelete(ChangeStreamDocument<Document> changeStreamDocument) {
        SyncDocumentModel syncDocumentModel = new SyncDocumentModel();
        syncDocumentModel.setDataBaseType(DataBaseType.MONGODB);
        syncDocumentModel.setTab(changeStreamDocument.getNamespace().getCollectionName());
        syncDocumentModel.setPri(getObjectId(changeStreamDocument));
        syncDocumentModel.setProcess(false);
        syncDocumentModel.setMethod(SyncMethod.DELETE);
        mongoSyncService.push(changeStreamDocument.getResumeToken() + ":delete", syncDocumentModel);
    }

    @Override
    public void handleInsert(ChangeStreamDocument<Document> changeStreamDocument) {
        for (String key : changeStreamDocument.getFullDocument().keySet()) {
            if (OBJID_KEY.equals(key)) {
                continue;
            }
            SyncDocumentModel syncDocumentModel = new SyncDocumentModel();
            syncDocumentModel.setDataBaseType(DataBaseType.MONGODB);
            syncDocumentModel.setTab(changeStreamDocument.getNamespace().getCollectionName());
            syncDocumentModel.setPri(getObjectId(changeStreamDocument));
            syncDocumentModel.setCol(key);
            syncDocumentModel.setProcess(false);
            syncDocumentModel.setMethod(SyncMethod.INSERT);
            syncDocumentModel.setCona(changeStreamDocument.getFullDocument().get(key).toString());
            mongoSyncService.push(changeStreamDocument.getResumeToken() + ":" + syncDocumentModel.getCol() + ":insert", syncDocumentModel);
        }
    }

    @Override
    public void handleUpdate(ChangeStreamDocument<Document> changeStreamDocument) {
        for (String key : changeStreamDocument.getFullDocument().keySet()) {
            SyncDocumentModel syncDocumentModel = new SyncDocumentModel();
            syncDocumentModel.setDataBaseType(DataBaseType.MONGODB);
            syncDocumentModel.setTab(changeStreamDocument.getNamespace().getCollectionName());
            syncDocumentModel.setPri(getObjectId(changeStreamDocument));
            syncDocumentModel.setCol(key);
            syncDocumentModel.setProcess(false);
            syncDocumentModel.setCona(changeStreamDocument.getFullDocument().get(key).toString());
            syncDocumentModel.setMethod(SyncMethod.UPDATE);
            mongoSyncService.push(changeStreamDocument.getResumeToken() + ":" + syncDocumentModel.getCol() + ":update", syncDocumentModel);
        }
    }

    @Override
    public void handleOtherOp(ChangeStreamDocument<Document> changeStreamDocument) {

    }

    private String getObjectId(ChangeStreamDocument<Document> changeStreamDocument) {
        return changeStreamDocument.getDocumentKey().getObjectId("_id").toString();
    }

}
