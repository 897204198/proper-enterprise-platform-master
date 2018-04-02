package com.proper.enterprise.platform.api.auth.dao;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.service.BaseService;

import java.util.Collection;

public interface MenuDao extends BaseService<Menu, String> {

    Menu get(String id);

    Menu get(String id, EnableEnum enable);

    Collection<? extends Menu> getByIds(Collection<String> ids);

    Menu save(Menu menu);

    Menu getNewMenuEntity();

    Collection<? extends Menu> findAll(Collection<String> idList);

    Collection<? extends Menu> getMenuByCondition(String name, String description, String route, EnableEnum enable, String parentId);

    DataTrunk<? extends Menu> findMenusPagniation(String name, String description, String route, EnableEnum enable, String parentId);

    void deleteAll();
}
