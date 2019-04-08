package com.proper.enterprise.platform.auth.common.jpa.repository;

import com.proper.enterprise.platform.auth.common.jpa.entity.MenuEntity;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;

import java.util.List;

public interface MenuRepository extends BaseJpaRepository<MenuEntity, String> {

    /**
     * 根据父菜单查询子菜单集合
     *
     * @param parent 父菜单
     * @return 菜单集合
     */
    List<MenuEntity> findByParent(MenuEntity parent);

    /**
     * 根据菜单名称集合查询菜单集合
     *
     * @param menuNames 菜单名称集合
     * @return 菜单集合
     */
    List<MenuEntity> findByNameIn(List<String> menuNames);
}
