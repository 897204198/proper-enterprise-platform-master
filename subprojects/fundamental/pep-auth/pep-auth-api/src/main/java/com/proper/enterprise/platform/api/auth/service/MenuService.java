package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.*;
import com.proper.enterprise.platform.core.entity.DataTrunk;

import java.util.Collection;

public interface MenuService {

    /**
     * 获取菜单
     * @param id 菜单id;
     * @return 菜单;
     */
    Menu get(String id);

    /**
     * 新增菜单;
     * @param menu 菜单;
     * @return 菜单;
     */
    Menu save(Menu menu);

    /**
     * 更新菜单信息
     *
     * @param menuReq 保存菜单对象
     * @return 菜单信息
     */
    Menu update(Menu menuReq);

    /**
     * 根据当前用户获得用户拥有的所有角色权限范围内的菜单集合，
     * 需去重，并按 parent 和 sequence number 排序
     * 用户可直接被赋予某角色，也可通过隶属某用户组且用户组拥有某角色而获得角色权限
     *
     * @return 菜单集合
     */
    Collection<? extends Menu> getMenus();

    /**
     * 获得用户拥有的所有角色权限范围内的菜单集合，
     * 需去重，并按 parent 和 sequence number 排序
     * 用户可直接被赋予某角色，也可通过隶属某用户组且用户组拥有某角色而获得角色权限
     *
     * @param user 用户
     * @return 菜单集合
     */
    Collection<? extends Menu> getMenus(User user);

    /**
     * 根据当前用户以及查询条件获得用户拥有的所有角色权限范围内的菜单集合，
     *       需去重，并按 parent 和 sequence number 排序
     *       用户可直接被赋予某角色，也可通过隶属某用户组且用户组拥有某角色而获得角色权限
     * @param name 菜单名称
     * @param description 菜单描述
     * @param route 菜单路径
     * @param enable 是否可用
     * @param parentId 上级菜单id
     * @return 菜单集合
     */
    Collection<? extends Menu> getMenus(String name, String description, String route, EnableEnum enable, String parentId);

    /**
     * 根据菜单集合获取过滤后的菜单包括父菜单
     * @param menus 菜单集合
     * @return 菜单集合
     */
    Collection<? extends Menu> getFilterMenusAndParent(Collection<? extends Menu> menus);

    /**
     * 某资源是否能够被某用户访问
     *
     * 当资源未定义或未定义在任何菜单下时，任何人都有权限访问
     * 当资源定义到某菜单下时，用户需拥有菜单及资源的访问权限，才可访问菜单下的资源
     * 用户可通过角色获得菜单及资源的访问权限
     * 用户可直接被赋予某角色，也可通过隶属某用户组且用户组拥有某角色而获得角色权限
     *
     * @param resource 资源
     * @param userId   用户 ID
     * @return true：有权限；false：无权限
     */
    boolean accessible(Resource resource, String userId);

    /**
     * 按照查询条件获取菜单信息列表
     *
     * @param name        菜单名称
     * @param description 菜单描述
     * @param route       前端路径
     * @param enable      菜单状态
     * @param parentId    父菜单ID
     * @return 获取菜单信息列表
     */
    Collection<? extends Menu> getMenuByCondition(String name, String description, String route, EnableEnum enable, String parentId);

    /**
     * 按照查询条件获取菜单信息列表
     *
     * @param name        菜单名称
     * @param description 菜单描述
     * @param route       前端路径
     * @param enable      菜单状态
     * @param parentId    父菜单ID
     * @return 获取菜单信息列表
     */
    DataTrunk<? extends Menu> findMenusPagination(String name, String description, String route, EnableEnum enable, String parentId);

    /**
     * 删除多条菜单数据
     * @param  ids 以 , 分隔的待删除菜单ID列表
     * @return true or false
     */
    boolean deleteByIds(String ids);

    /**
     * 获取父节点列表
     * @param menuEnable  标记字段
     * @return 父节点列表
     */
    Collection<? extends Menu> getMenuParents(EnableEnum menuEnable);

    /**
     * 更新菜单状态
     *
     * @param idList 菜单ID列表
     * @param enable 菜单状态
     * @return 结果
     */
    Collection<? extends Menu> updateEnable(Collection<String> idList, boolean enable);

    /**
     * 菜单添加资源
     * @param menuId 菜单id
     * @param resourceReq 资源
     * @return 资源信息
     */
    Resource addResourceOfMenu(String menuId, Resource resourceReq);

    /**
     * 菜单删除资源
     * @param menuId     菜单Id
     * @param resourceId 资源Id
     * @return
     */
    Boolean deleteResourceOfMenu(String menuId, String resourceId);

    /**
     * 获取指定菜单资源集合
     *
     * @param menuId 菜单ID
     * @param menuEnable 菜单状态
     * @param resourceEnable 资源状态
     * @return 资源集合
     */
    Collection<? extends Resource> getMenuResources(String menuId, EnableEnum menuEnable, EnableEnum resourceEnable);

    /**
     * 获取指定菜单角色集合
     * @param menuId 菜单id
     * @param menuEnable 是否可用
     * @param resourceEnable 是否可用
     * @return 角色集合
     */
    Collection<? extends Role> getMenuRoles(String menuId, EnableEnum menuEnable, EnableEnum resourceEnable);

    /**
     * 获取菜单所有资源集合
     * @return 资源集合
     */
    Collection<? extends Menu> getMenuAllResources();

}
