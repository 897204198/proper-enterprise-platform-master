package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.MenuService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.common.repository.MenuRepository;
import com.proper.enterprise.platform.core.utils.sort.BeanComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository repository;

    @Autowired
    private UserService userService;

    @Override
    public Menu get(String id) {
        return repository.findOne(id);
    }

    @Override
    public Collection<? extends Menu> getMenus() {
        return getMenus(userService.getCurrentUser());
    }

    @Override
    public Collection<? extends Menu> getMenus(User user) {
        Assert.notNull(user, "Could NOT get menus WITHOUT a user");

        if (user.isSuperuser()) {
            return repository.findAll(new Sort("parent", "sequenceNumber"));
        }

        List<Menu> menus = new ArrayList<>();
        for (Role role : user.getRoles()) {
            for (Menu menu : role.getMenus()) {
                if (!menus.contains(menu)) {
                    menus.add(menu);
                }
            }
        }

        Collections.sort(menus, new BeanComparator("parent", "sequenceNumber"));
        return menus;
    }

}
