package com.proper.enterprise.platform.oopsearch.api.serivce;

import com.proper.enterprise.platform.oopsearch.api.model.SyncDocumentModel;

/**
 * mongodb同步服务
 * 将数据库中数据的变更，同步到mongodb当中
 */
public interface MongoDataSyncService {

    /**
     * 从数据库中将oopsearch配置的表（各模块查询时使用到的表）一次性全部同步到mongodb当中
     * */
    void fullSynchronization();

    /**
     * 从mysql的binlog日志文件当中，解析数据变更，并同步到mongodb当中
     * @param  doc 同步mongodb文档对象
     * @param  method 具体操作（insert、update、delete）
     * */
    void singleSynchronization(SyncDocumentModel doc, String method);
}
