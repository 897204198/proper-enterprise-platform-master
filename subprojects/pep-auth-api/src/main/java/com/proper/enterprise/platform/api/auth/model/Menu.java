package com.proper.enterprise.platform.api.auth.model;

import com.proper.enterprise.platform.core.api.IBase;

import java.util.List;

/**
 * 菜单
 *
 * 应用将资源组织成树形结构的`菜单`，菜单名称无需与资源名称一致。
 * 菜单中仅叶子节点与资源对应，叶子节点选择`ROUTE`类型的资源。
 */
public interface Menu extends IBase {

    /**
     * 获得菜单名称
     *
     * @return 菜单名称
     */
    String getName();

    /**
     * 设置菜单名称
     *
     * @param name 菜单名称
     */
    void setName(String name);

    /**
     * 获得上级菜单
     *
     * @return 上级菜单
     */
    Menu getParent();

    /**
     * 设置上级菜单
     *
     * @param menu 上级菜单
     */
    void setParent(Menu menu);

    /**
     * 是否有子菜单
     *
     * @return 是/否
     */
    boolean hasChild();

    /**
     * 获得子菜单集合
     *
     * @return 子菜单集合
     */
    List<Menu> getChildren();

    /**
     * 获得菜单所属应用，即最上级的菜单
     *
     * @return 所属应用
     */
    Application getApplication();

    /**
     * 获得菜单对应资源
     *
     * @return 资源
     */
    Resource getResource();

    /**
     * 设置菜单对应资源
     *
     * @param resource 资源
     */
    void setResource(Resource resource);

}
