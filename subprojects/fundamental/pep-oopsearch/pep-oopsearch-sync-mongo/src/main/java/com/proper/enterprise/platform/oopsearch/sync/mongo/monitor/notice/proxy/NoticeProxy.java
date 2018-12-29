package com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.notice.proxy;

import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.notice.Notice;
import org.bson.Document;

public class NoticeProxy implements Notice {

    public NoticeProxy(Notice notice) {
        this.notice = notice;
    }

    private Notice notice;


    @Override
    public void handleDelete(ChangeStreamDocument<Document> changeStreamDocument) {
        notice.handleDelete(changeStreamDocument);
    }

    @Override
    public void handleInsert(ChangeStreamDocument<Document> changeStreamDocument) {
        notice.handleInsert(changeStreamDocument);
    }

    @Override
    public void handleUpdate(ChangeStreamDocument<Document> changeStreamDocument) {
        notice.handleUpdate(changeStreamDocument);
    }

    @Override
    public void handleOtherOp(ChangeStreamDocument<Document> changeStreamDocument) {
        notice.handleOtherOp(changeStreamDocument);
    }
}
