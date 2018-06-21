package com.proper.enterprise.platform.oopsearch.api.serivce;

/**
 * mongodb同步服务
 * 将数据库中数据的变更，同步到mongodb当中
 */
public interface MongoDataSyncService {

    /**
     * 从数据库中将oopsearch配置的表（各模块查询时使用到的表）一次性全部同步到mongodb当中
     * */
    void fullSynchronization();

}
