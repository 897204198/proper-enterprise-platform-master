package com.proper.enterprise.platform.oopsearch.config.repository;

import com.proper.enterprise.platform.core.jpa.annotation.CacheQuery;
import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.oopsearch.api.enums.DataBaseType;
import com.proper.enterprise.platform.oopsearch.config.entity.SearchConfigEntity;

import java.util.List;

/**
 * 对SearchConfig进行操作的repository
 */
public interface SearchConfigRepository extends BaseRepository<SearchConfigEntity, String> {

    /**
     * 根据数据库类型获取数据，并设置CacheQuery，确保在配置信息数据不变动的情况下，
     * 使用缓存查找数据。减少对db的连接使用次数，提高性能。
     *
     * @param dataBaseType 数据库类型
     * @return 配置信息集合
     */
    @CacheQuery
    List<SearchConfigEntity> findByDataBaseType(DataBaseType dataBaseType);

    /**
     * 根据模块名称，查找配置信息方法
     *
     * @param moduleName 模块名称
     * @return 配置信息集合
     */
    @CacheQuery
    List<SearchConfigEntity> findByModuleName(String moduleName);

    /**
     * 根据模块名称及数据库类型，查找配置信息方法
     *
     * @param moduleName 模块名称
     * @param dataBaseType 数据库类型
     * @return 配置信息集合
     */
    @CacheQuery
    List<SearchConfigEntity> findByModuleNameAndDataBaseType(String moduleName, DataBaseType dataBaseType);

    /**
     * 根据表名 查找配置
     *
     * @param tableName 表名
     * @return 配置集合
     */
    @CacheQuery
    List<SearchConfigEntity> findByTableName(String tableName);
}
