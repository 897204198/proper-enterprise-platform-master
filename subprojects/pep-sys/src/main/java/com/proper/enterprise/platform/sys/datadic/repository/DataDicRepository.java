package com.proper.enterprise.platform.sys.datadic.repository;

import com.proper.enterprise.platform.core.jpa.annotation.CacheQuery;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import com.proper.enterprise.platform.sys.datadic.entity.DataDicEntity;

import java.util.Collection;

public interface DataDicRepository extends BaseJpaRepository<DataDicEntity, String> {

    @CacheQuery
    Collection<DataDicEntity> findByCatalogOrderByOrder(String catalog);

    @CacheQuery
    DataDicEntity getByCatalogAndCode(String catalog, String code);

    @CacheQuery
    DataDicEntity getByCatalogAndIsDefaultIsTrue(String catalog);

}
