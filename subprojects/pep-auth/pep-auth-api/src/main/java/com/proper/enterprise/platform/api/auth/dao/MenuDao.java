package com.proper.enterprise.platform.api.auth.dao;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.service.BaseService;

import java.util.Collection;

public interface MenuDao extends BaseService<Menu, String> {

    Menu get(String id);

    Menu getNewMenuEntity();

    Collection<? extends Menu> findParents(EnableEnum menuEnable);

    Collection<? extends Menu> findAllEq(String name);

    Collection<? extends Menu> findAll(String name, String description, String route, EnableEnum enable, String parentId);

    DataTrunk<? extends Menu> findPage(String name, String description, String route, EnableEnum enable, String parentId);

    void deleteAll();

    Menu updateForSelective(Menu menu);
}
