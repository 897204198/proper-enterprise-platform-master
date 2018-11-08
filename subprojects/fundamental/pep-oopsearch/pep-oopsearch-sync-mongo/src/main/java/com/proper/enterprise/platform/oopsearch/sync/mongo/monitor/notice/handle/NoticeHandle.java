package com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.notice.handle;

import com.mongodb.DBObject;
import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.notice.Notice;
import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.notice.proxy.NoticeProxy;
import com.proper.enterprise.platform.oopsearch.sync.mongo.util.ConfigUtil;


public class NoticeHandle {

    private NoticeHandle() {
    }

    public static void notice(DBObject op) {
        Notice notice = ConfigUtil.getMongoMonitorConfig().getNotice();
        Notice noticeProxy = new NoticeProxy(notice);
        switch ((String) op.get("op")) {
            case "u":
                noticeProxy.handleUpdate(op);
                break;
            case "i":
                noticeProxy.handleInsert(op);
                break;
            case "d":
                noticeProxy.handleDelete(op);
                break;
            default:
                noticeProxy.handleOtherOp(op);
                break;
        }
    }
}
