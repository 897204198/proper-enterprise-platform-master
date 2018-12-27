package com.proper.enterprise.platform.sys.bizrule.repository;

import com.proper.enterprise.platform.core.jpa.annotation.CacheQuery;
import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.sys.bizrule.entity.RuleEntity;

import java.util.Optional;

public interface RuleRepository extends BaseRepository<RuleEntity, String> {

    /**
     * 获取角色
     * @param id ID
     * @return 角色
     */
    @Override
    @CacheQuery
    Optional<RuleEntity> findById(String id);

    /**
     * 获取角色
     * @param catalogue 目录
     * @return 角色集合
     */
    @CacheQuery
    Iterable<RuleEntity> findAllByCatalogue(String catalogue);

}
