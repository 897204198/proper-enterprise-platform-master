package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.model.Menu;
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

}
