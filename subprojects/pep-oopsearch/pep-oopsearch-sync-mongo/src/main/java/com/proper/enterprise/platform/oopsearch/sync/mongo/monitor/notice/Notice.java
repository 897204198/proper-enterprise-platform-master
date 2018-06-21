package com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.notice;

import com.mongodb.DBObject;

public interface Notice {

    void handleDelete(DBObject op);

    void handleInsert(DBObject op);

    void handleUpdate(DBObject op);

    void handleOtherOp(DBObject op);

}
