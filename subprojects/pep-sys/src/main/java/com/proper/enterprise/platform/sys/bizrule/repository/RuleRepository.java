package com.proper.enterprise.platform.sys.bizrule.repository;

import com.proper.enterprise.platform.core.jpa.annotation.CacheQuery;
import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.sys.bizrule.entity.RuleEntity;

import java.util.Collection;

public interface RuleRepository extends BaseRepository<RuleEntity, String> {

    /**
     * 获取角色
     * @param  catalogue 目录
     * @return 角色集合
     */
    @CacheQuery
    Collection<RuleEntity> findByCatalogue(String catalogue);

    /**
     * 获取角色
     * @param id ID
     * @return 角色
     */
    @CacheQuery
    RuleEntity findById(String id);

}
