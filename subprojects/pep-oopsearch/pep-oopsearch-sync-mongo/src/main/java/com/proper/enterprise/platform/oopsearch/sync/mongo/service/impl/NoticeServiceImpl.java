package com.proper.enterprise.platform.oopsearch.sync.mongo.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.proper.enterprise.platform.oopsearch.api.enums.DataBaseType;
import com.proper.enterprise.platform.oopsearch.api.enums.SyncMethod;
import com.proper.enterprise.platform.oopsearch.api.model.SyncDocumentModel;
import com.proper.enterprise.platform.oopsearch.service.impl.SyncCacheService;
import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.notice.Notice;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("mongoSyncMongoNotice")
public class NoticeServiceImpl implements Notice {

    private static final String SET_KEY = "$set";

    @Autowired
    private SyncCacheService mongoSyncService;

    @Override
    public void handleDelete(DBObject op) {
        SyncDocumentModel syncDocumentModel = new SyncDocumentModel();
        syncDocumentModel.setDataBaseType(DataBaseType.MONGODB);
        syncDocumentModel.setTab(getCollectName(op));
        syncDocumentModel.setPri(getObjectId(op));
        syncDocumentModel.setProcess(false);
        syncDocumentModel.setMethod(SyncMethod.DELETE);
        mongoSyncService.push(getPos(op) + ":delete", syncDocumentModel);
    }

    @Override
    public void handleInsert(DBObject op) {
        String pos = getPos(op);
        for (Map.Entry<String, Object> map : getInsertCols(op)) {
            SyncDocumentModel syncDocumentModel = new SyncDocumentModel();
            syncDocumentModel.setDataBaseType(DataBaseType.MONGODB);
            syncDocumentModel.setTab(getCollectName(op));
            syncDocumentModel.setPri(getObjectId(op));
            syncDocumentModel.setCol(map.getKey());
            syncDocumentModel.setProcess(false);
            syncDocumentModel.setMethod(SyncMethod.INSERT);
            BasicDBObject basicDBObject = (BasicDBObject) op.get("o");
            syncDocumentModel.setCona(basicDBObject.get(map.getKey()).toString());
            mongoSyncService.push(pos + ":" + syncDocumentModel.getCol() + ":insert", syncDocumentModel);
        }
    }

    @Override
    public void handleUpdate(DBObject op) {
        String pos = getPos(op);
        for (Map.Entry<String, Object> map : getUpdateCols(op).entrySet()) {
            SyncDocumentModel syncDocumentModel = new SyncDocumentModel();
            syncDocumentModel.setDataBaseType(DataBaseType.MONGODB);
            syncDocumentModel.setTab(getCollectName(op));
            syncDocumentModel.setPri(getObjectId(op));
            syncDocumentModel.setCol(map.getKey());
            syncDocumentModel.setProcess(false);
            syncDocumentModel.setCona(map.getValue().toString());
            syncDocumentModel.setMethod(SyncMethod.UPDATE);
            mongoSyncService.push(pos + ":" + syncDocumentModel.getCol() + ":update", syncDocumentModel);
        }
    }

    @Override
    public void handleOtherOp(DBObject op) {

    }

    private Set<Map.Entry<String, Object>> getInsertCols(DBObject op) {
        Document colDoc = Document.parse(op.get("o").toString());
        if (null != colDoc.get(SET_KEY)) {
            return (Set<Map.Entry<String, Object>>) colDoc.get(SET_KEY);
        }
        return colDoc.entrySet();
    }

    private Document getUpdateCols(DBObject op) {
        Document colDoc = Document.parse(op.get("o").toString());
        return (Document) colDoc.get(SET_KEY);
    }

    private String getCollectName(DBObject op) {
        String nameSpace = op.get("ns").toString();
        return nameSpace.substring(nameSpace.indexOf(".") + 1);
    }

    private String getObjectId(DBObject op) {
        String o2Key = "o2";
        //update handle
        if (null != op.get(o2Key)) {
            return Document.parse(op.get(o2Key).toString()).getObjectId("_id").toString();
        }
        return Document.parse(op.get("o").toString()).getObjectId("_id").toString();
    }

    private String getPos(DBObject op) {
        return op.get("h").toString();
    }
}
