package com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.notice;

import com.mongodb.DBObject;

public interface Notice {

    /**
     * 删除操作
     *
     * @param op DB对象
     */
    void handleDelete(DBObject op);

    /**
     * 插入操作
     *
     * @param op DB对象
     */
    void handleInsert(DBObject op);

    /**
     * 更新操作
     *
     * @param op DB对象
     */
    void handleUpdate(DBObject op);

    /**
     * 其他操作
     *
     * @param op DB对象
     */
    void handleOtherOp(DBObject op);

}
