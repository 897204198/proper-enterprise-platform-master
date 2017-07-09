package com.proper.enterprise.platform.sys.bizrule.repository;

import com.proper.enterprise.platform.core.annotation.CacheQuery;
import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.sys.bizrule.entity.RuleEntity;

import java.util.Collection;

public interface RuleRepository extends BaseRepository<RuleEntity, String> {

    @CacheQuery
    Collection<RuleEntity> findByCatalogue(String catalogue);

    @CacheQuery
    RuleEntity findById(String id);

}
