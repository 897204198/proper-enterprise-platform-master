package com.proper.enterprise.platform.api.auth.dao;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.service.BaseService;

import java.util.Collection;

public interface MenuDao extends BaseService<Menu, String> {
    /**
     *通过id获取菜单
     * @param id id
     * @return 菜单
     */
    Menu get(String id);

    /**
     *  根据不同的权限获取菜单
     * @param menuEnable 是否可用
     * @return 菜单集合
     */
    Collection<? extends Menu> findParents(EnableEnum menuEnable);

    /**
     * 根据姓名查询菜单
     * @param name 姓名
     * @return  菜单集合
     */
    Collection<? extends Menu> findAllEq(String name);

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
}
