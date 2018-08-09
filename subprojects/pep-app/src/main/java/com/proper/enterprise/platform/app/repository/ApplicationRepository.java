package com.proper.enterprise.platform.app.repository;

import com.proper.enterprise.platform.app.entity.ApplicationEntity;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;

import java.util.List;

public interface ApplicationRepository extends BaseJpaRepository<ApplicationEntity, String> {

    /**
     * 根据类别的code获取应用的集合
     * @param code code
     * @return 应用集合
     */
    List<ApplicationEntity> findAllByCode(String code);

    /**
     * 获取默认应用
     * @return 应用的集合
     */
    List<ApplicationEntity> findByDefaultValueTrue();

}
