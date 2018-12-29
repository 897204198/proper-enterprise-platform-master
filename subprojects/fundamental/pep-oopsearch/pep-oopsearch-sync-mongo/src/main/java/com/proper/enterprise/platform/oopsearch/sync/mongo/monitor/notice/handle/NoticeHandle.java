package com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.notice.handle;

import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.notice.Notice;
import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.notice.proxy.NoticeProxy;
import com.proper.enterprise.platform.oopsearch.sync.mongo.util.ConfigUtil;
import org.bson.Document;


public class NoticeHandle {

    private NoticeHandle() {
    }

    public static void notice(ChangeStreamDocument<Document> changeStreamDocument) {
        Notice notice = ConfigUtil.getMongoMonitorConfig().getNotice();
        Notice noticeProxy = new NoticeProxy(notice);
        switch (changeStreamDocument.getOperationType()) {
            case UPDATE:
                noticeProxy.handleUpdate(changeStreamDocument);
                break;
            case INSERT:
                noticeProxy.handleInsert(changeStreamDocument);
                break;
            case DELETE:
                noticeProxy.handleDelete(changeStreamDocument);
                break;
            default:
                noticeProxy.handleOtherOp(changeStreamDocument);
                break;
        }
    }
}
