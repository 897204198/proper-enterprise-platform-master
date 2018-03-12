package com.proper.enterprise.platform.api.auth.model;

import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;

import java.util.Collection;

/**
 * 菜单是组成应用的基本元素，以树形结构组织。
 * 每个菜单可以有一个上级菜单，最顶级的菜单称为`应用`，可以定义多个应用。
 * 菜单中仅叶子节点与资源对应，菜单名称无需与资源名称一致，叶子节点的菜单下可以定义一组相关联的资源。
 *
 * 应用是一组菜单的集合，系统以应用为单位为用户提供服务。
 * 应用将资源组织成树形结构的`菜单`（应用 即为最顶层的 菜单）。
 * 系统提供内置的应用、菜单和资源对应关系，并允许用户自定义。
 * 以应用为单位为`角色`进行权限分配（可选择应用内的菜单）。
 */
public interface Menu extends IBase, Comparable<Menu> {

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
     * 获得菜单对应的前端路径
     *
     * @return 路径
     */
    String getRoute();

    /**
     * 设置菜单对应的前端路径
     *
     * @param route 路径
     */
    void setRoute(String route);

    /**
     * 获得上级菜单
     *
     * @return 上级菜单
     */
    Menu getParent();

    /**
     * 获得上级菜单 id
     *
     * @return 上级菜单 id
     */
    String getParentId();

    /**
     * 设置上级菜单
     *
     * @param menu 上级菜单
     */
    void setParent(Menu menu);

    /**
     * 获得菜单所属应用，即最上级的菜单
     *
     * @return 所属应用
     */
    Menu getApplication();

    /**
     * 是否是根节点
     *
     * @return 是/否
     */
    boolean isRoot();

    /**
     * 是否是叶子节点
     *
     * @return 是/否
     */
    boolean isLeaf();

    /**
     * 获得子菜单集合
     *
     * @return 子菜单集合
     */
    Collection<? extends Menu> getChildren();

    /**
     * 添加子菜单
     *
     * @param menu 菜单
     */
    void addChild(Menu menu);

    /**
     * 移除子菜单
     *
     * @param menu 菜单
     */
    void removeChild(Menu menu);

    /**
     * 获得菜单对应的资源集合
     *
     * @return 资源集合
     */
    Collection<? extends Resource> getResources();

    /**
     * 为菜单下添加资源
     *
     * @param resource 资源
     */
    void add(Resource resource);

    /**
     * 移除菜单下的资源
     *
     * @param resource 资源
     */
    void remove(Resource resource);

    /**
     * 获得菜单排序号（同一级别内）
     *
     * @return 排序号
     */
    int getSequenceNumber();

    /**
     * 设置菜单排序号
     *
     * @param sequenceNumber 排序号
     */
    void setSequenceNumber(int sequenceNumber);

    /**
     * 设置菜单图标样式名称
     *
     * @param icon 图标样式名称
     */
    void setIcon(String icon);

    /**
     * 获得菜单图标样式名称
     *
     * @return 图标样式名称
     */
    String getIcon();

    /**
     * 获得拥有此菜单权限的角色
     *
     * @return 角色集合
     */
    Collection<? extends Role> getRoles();

    /**
     * 获得菜单内容描述
     *
     * @return 菜单内容描述
     */
    String getDescription();

    /**
     * 设置菜单内容描述
     *
     * @param description 菜单内容描述
     */
    void setDescription(String description);

    /**
     * 获得菜单标识
     *
     * @return 菜单标识
     */
    String getIdentifier();

    /**
     * 设置菜单标识
     *
     * @param identifier 菜单标识
     */
    void setIdentifier(String identifier);

    /**
     * 获得菜单类型
     *
     * @return 菜单类型
     */
    DataDicLite getMenuType();

    /**
     * 设置菜单类型
     *
     * @param menuType 菜单类型
     */
    void setMenuType(DataDicLite menuType);

    /**
     * 获取菜单状态是否可用
     *
     * @return 用户菜单是否可用
     */
    boolean isEnable();

    /**
     * 设置菜单状态是否可用
     *
     * @param enable 用户菜单是否可用
     */
    void setEnable(boolean enable);

    /**
     * 获取菜单CODE
     *
     * @return 菜单CODE
     */
    String getMenuCode();

    /**
     * 设置菜单CODE
     *
     * @param menuCode 菜单CODE
     */
    void setMenuCode(String menuCode);

}
