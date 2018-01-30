package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;

import java.util.Collection;
import java.util.Map;

public interface MenuService {

    Menu get(String id);

    Menu save(Menu menu);

    /**
     * 保存菜单信息
     *
     * @param map 保存参数
     * @return 菜单信息
     */
    Menu save(Map<String, Object> map);

    /**
     * 根据当前用户获得用户拥有的所有角色权限范围内的菜单集合，
     * 需去重，并按 parent 和 sequence number 排序
     *
     * @return 菜单集合
     */
    Collection<? extends Menu> getMenus();

    /**
     * 获得用户拥有的所有角色权限范围内的菜单集合，
     * 需去重，并按 parent 和 sequence number 排序
     *
     * @param  user 用户
     * @return 菜单集合
     */
    Collection<? extends Menu> getMenus(User user);

    /**
     * 根据当前用户以及查询条件获得用户拥有的所有角色权限范围内的菜单集合，
     * 需去重，并按 parent 和 sequence number 排序
     *
     * @return 菜单集合
     */
    Collection<? extends Menu> getMenus(String name, String description, String route, String enable, String parentId);

    /**
     * 根据菜单集合获取过滤后的菜单包括父菜单
     */
    Collection<? extends Menu> getFilterMenusAndParent(Collection<? extends Menu> menus);

    /**
     * 根据菜单ID列表获取菜单列表
     *
     * @param ids 菜单ID列表
     * @return 菜单列表
     */
    Collection<? extends Menu> getByIds(Collection<String> ids);

    /**
     * 某资源是否能够被某用户访问
     *
     * 当资源未定义或未定义在任何菜单下时，任何人都有权限访问
     * 当资源定义到某菜单下时，没有角色的用户（即没有任何菜单访问权限）不能访问该资源
     * 用户需拥有菜单的访问权限，才可访问菜单下的资源
     *
     * @param  resource 资源
     * @param  userId   用户 ID
     * @return true：有权限；false：无权限
     */
    boolean accessible(Resource resource, String userId);

    /**
     * 按照查询条件获取菜单信息列表
     *
     * @param name 菜单名称
     * @param description 菜单描述
     * @param route 前端路径
     * @param enable 菜单状态
     * @param parentId 父菜单ID
     * @return 获取菜单信息列表
     */
    Collection<? extends Menu> getMenuByCondiction(String name, String description, String route, String enable, String parentId);

    /**
     * 删除多条菜单数据
     *
     * @param ids 以 , 分隔的待删除菜单ID列表
     */
    boolean deleteByIds(String ids);

    /**
     * 获取父节点列表
     *
     * @return 父节点列表
     */
    Collection<? extends Menu> getMenuParents();

    /**
     * 更新菜单状态
     *
     * @param idList 菜单ID列表
     * @param enable 菜单状态
     * @return 结果
     */
    Collection<? extends Menu> updateEanble(Collection<String> idList, boolean enable);

    /**
     * 菜单添加资源
     *
     * @param menuId 菜单ID
     * @param resourceId 资源ID
     * @return 菜单
     */
    Menu addMenuResource(String menuId, String resourceId);

    /**
     * 菜单删除资源
     *
     * @param menuId 菜单ID
     * @param resourceId 资源ID
     * @return 菜单
     */
    Menu deleteMenuResource(String menuId, String resourceId);

    /**
     * 获取指定菜单资源集合
     *
     * @param menuId 菜单ID
     * @return 资源集合
     */
    Collection<? extends Resource> getMenuResources(String menuId);

    /**
     * 获取指定菜单角色集合
     *
     * @param menuId 菜单ID
     * @return 角色集合
     */
    Collection<? extends Role> getMenuRoles(String menuId);

}
