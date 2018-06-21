package com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.notice.proxy;

import com.mongodb.DBObject;
import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.notice.Notice;

public class NoticeProxy implements Notice {

    public NoticeProxy(Notice notice) {
        this.notice = notice;
    }

    private Notice notice;


    @Override
    public void handleDelete(DBObject op) {
        notice.handleDelete(op);
    }

    @Override
    public void handleInsert(DBObject op) {
        notice.handleInsert(op);
    }

    @Override
    public void handleUpdate(DBObject op) {
        notice.handleUpdate(op);
    }

    @Override
    public void handleOtherOp(DBObject op) {
        notice.handleOtherOp(op);
    }
}
