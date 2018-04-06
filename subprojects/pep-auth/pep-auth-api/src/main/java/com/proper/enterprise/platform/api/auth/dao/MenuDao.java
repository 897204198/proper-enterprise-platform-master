package com.proper.enterprise.platform.api.auth.dao;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.service.BaseService;

import java.util.Collection;

public interface MenuDao extends BaseService<Menu, String> {

    Menu get(String id);

    Menu get(String id, EnableEnum enable);

    /**
     * 获得用户拥有的所有角色权限范围内的菜单集合，
     * 需去重，并按 parent 和 sequence number 排序
     * 用户可直接被赋予某角色，也可通过隶属某用户组且用户组拥有某角色而获得角色权限
     *
     * @param user 用户
     * @return 菜单集合
     */
    Collection<? extends Menu> getMenus(User user);

    Collection<? extends Menu> getByIds(Collection<String> ids);

    Menu save(Menu menu);

    Menu getNewMenuEntity();

    Collection<? extends Menu> findAll(Collection<String> idList);

    Collection<? extends Menu> getMenuByCondition(String name, String description, String route, EnableEnum enable, String parentId);

    DataTrunk<? extends Menu> findMenusPagniation(String name, String description, String route, EnableEnum enable, String parentId);

    void deleteAll();
}
