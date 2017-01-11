package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.model.User;

import java.util.Collection;

public interface MenuService {

    Menu get(String id);

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

}
