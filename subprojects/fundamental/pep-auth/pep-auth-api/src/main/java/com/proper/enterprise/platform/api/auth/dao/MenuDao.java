package com.proper.enterprise.platform.api.auth.dao;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.service.BaseService;

import java.util.Collection;
import java.util.List;

public interface MenuDao extends BaseService<Menu, String> {
    /**
     *通过id获取菜单
     * @param id id
     * @return 菜单
     */
    Menu get(String id);

    /**
     *  根据父菜单ID获取子菜单
     * @param parentId 父菜单ID
     * @return 菜单集合
     */
    Collection<? extends Menu> findByParentId(String parentId);

    /**
     *  根据不同的权限获取菜单
     * @param menuEnable 是否可用
     * @return 菜单集合
     */
    Collection<? extends Menu> findParents(EnableEnum menuEnable);

    /**
     * 根据姓名路由查询菜单
     * @param name 姓名
     * @param route 路由
     * @return  菜单集合
     */
    Collection<? extends Menu> findAllEq(String name, String route);

    /**
     * 多条件组合查询菜单
     * @param name 姓名
     * @param description 说明
     * @param route 路径
     * @param enable 标记
     * @param parentId 上级菜单
     * @return 菜单集合
     */
    Collection<? extends Menu> findAll(String name, String description, String route, EnableEnum enable, String parentId);

    /**
     * 多条件组合查询菜单
     * @param name 姓名
     * @param description 说明
     * @param route 路径
     * @param enable 标记
     * @param parentId 上级菜单
     * @return 菜单集合
     */
    DataTrunk<? extends Menu> findPage(String name, String description, String route, EnableEnum enable, String parentId);

    /**
     * 删除所有
     */
    void deleteAll();

    /**
     * 修改 菜单
     * @param menu 将修改的菜单实体
     * @return 修改后的菜单实体
     */
    Menu updateForSelective(Menu menu);

    /**
     *  根据菜单名称集合获取菜单列表
     * @param names 菜单名称集合
     * @return 菜单集合
     */
    Collection<? extends Menu> getMenusByNames(List<String> names);
}
