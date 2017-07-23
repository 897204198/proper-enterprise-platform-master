package com.proper.enterprise.platform.sys.datadic.repository;

import com.proper.enterprise.platform.core.annotation.CacheQuery;
import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.sys.datadic.entity.DataDicEntity;

import java.util.Collection;

public interface DataDicRepository extends BaseRepository<DataDicEntity, String> {

    @CacheQuery
    Collection<DataDicEntity> findByCatalogOrderByOrder(String catalog);

    @CacheQuery
    DataDicEntity getByCatalogAndCode(String catalog, String code);

    @CacheQuery
    DataDicEntity getByCatalogAndIsDefaultIsTrue(String catalog);

}
