package com.proper.enterprise.platform.app.repository;

import com.proper.enterprise.platform.app.entity.AppCatalogEntity;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;

public interface AppCatalogRepository extends BaseJpaRepository<AppCatalogEntity, String> {

    /***
     * 根据code获取应用类别
     * @param code code
     * @return 类别信息
     */
    AppCatalogEntity findByCode(String code);

    /**
     * 根据code删除
     * @param code code
     */
    void deleteByCode(String code);

}
