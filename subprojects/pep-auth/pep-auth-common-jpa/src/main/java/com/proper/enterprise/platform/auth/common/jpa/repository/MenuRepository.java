package com.proper.enterprise.platform.auth.common.jpa.repository;

import com.proper.enterprise.platform.auth.common.jpa.entity.MenuEntity;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;

public interface MenuRepository extends BaseJpaRepository<MenuEntity, String> {

    MenuEntity findByIdAndValid(String id, boolean valid);

    MenuEntity findByIdAndValidAndEnable(String id, boolean valid, boolean enable);
}
