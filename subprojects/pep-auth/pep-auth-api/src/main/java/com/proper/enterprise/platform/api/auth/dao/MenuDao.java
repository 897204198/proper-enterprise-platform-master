package com.proper.enterprise.platform.api.auth.dao;

import com.proper.enterprise.platform.api.auth.model.Menu;
import org.springframework.data.domain.Sort;

import java.util.Collection;

public interface MenuDao {

    Menu get(String id);

    Collection<? extends Menu> getByIds(Collection<String> ids);

    Menu save(Menu menu);

    Collection<? extends Menu> save(Collection<? extends Menu> menus);

    Menu getNewMenuEntity();

    Collection<? extends Menu> findAll();

    Collection<? extends Menu> findAll(Collection<String> idList);

    Collection<? extends Menu> findAll(Sort sort);

    Collection<? extends Menu> getMenuByCondition(String name, String description, String route, String enable, String parentId);

    void deleteAll();
}
