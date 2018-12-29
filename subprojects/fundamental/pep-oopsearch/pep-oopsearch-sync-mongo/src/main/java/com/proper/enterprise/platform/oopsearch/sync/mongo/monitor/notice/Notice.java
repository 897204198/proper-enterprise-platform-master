package com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.notice;

import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.Document;

public interface Notice {

    /**
     * 删除操作
     *
     * @param changeStreamDocument 变更对象
     */
    void handleDelete(ChangeStreamDocument<Document> changeStreamDocument);

    /**
     * 插入操作
     *
     * @param changeStreamDocument 变更对象
     */
    void handleInsert(ChangeStreamDocument<Document> changeStreamDocument);

    /**
     * 更新操作
     *
     * @param changeStreamDocument 变更对象
     */
    void handleUpdate(ChangeStreamDocument<Document> changeStreamDocument);

    /**
     * 其他操作
     *
     * @param changeStreamDocument 变更对象
     */
    void handleOtherOp(ChangeStreamDocument<Document> changeStreamDocument);

}
