package com.proper.enterprise.platform.oopsearch.api.repository;

import com.proper.enterprise.platform.core.jpa.annotation.CacheQuery;
import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.oopsearch.api.entity.SearchConfigEntity;

import java.util.List;

/**
 * 对SearchConfig进行操作的repository
 * */
public interface SearchConfigRepository extends BaseRepository<SearchConfigEntity, String> {

    /**
     * 重写findAll方法，并设置CacheQuery，确保在配置信息数据不变动的情况下，
     * 使用缓存查找数据。减少对db的连接使用次数，提高性能。
     *
     * @return 配置信息集合
     * */
    @Override
    @CacheQuery
    List<SearchConfigEntity> findAll();

    /**
     * 根据模块名称，查找配置信息方法
     *
     * @param moduleName 模块名称
     * @return 配置信息集合
     * */
    @CacheQuery
    List<SearchConfigEntity> findByModuleName(String moduleName);

}
