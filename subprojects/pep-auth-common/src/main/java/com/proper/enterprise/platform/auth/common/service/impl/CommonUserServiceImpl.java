package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.MenuService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.common.entity.UserEntity;
import com.proper.enterprise.platform.auth.common.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 通用的/抽象的用户服务接口实现
 *
 * 其中，获得当前用户的方法由于与安全框架具体实现关联，只能提供抽象实现
 */
public abstract class CommonUserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MenuService menuService;

    @Override
    public abstract User getCurrentUser();

    @Override
    public User save(User user) {
        return userRepo.save((UserEntity) user);
    }

    @Override
    public void save(User... users) {
        List<UserEntity> entities = new ArrayList<>(users.length);
        for (User user : users) {
            entities.add((UserEntity) user);
        }
        userRepo.save(entities);
    }

    @Override
    public User get(String id) {
        return userRepo.findOne(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public void delete(String id) {
        userRepo.delete(id);
    }

    @Override
    public void delete(User user) {
        userRepo.delete((UserEntity) user);
    }

    @Override
    public Collection<? extends Menu> getMenus() {
        return menuService.getMenus();
    }

    @Override
    public Collection<? extends Menu> getMenusByUsername(String username) {
        return menuService.getMenus(getByUsername(username));
    }

    @Override
    public Collection<? extends Menu> getMenus(String userId) {
        return menuService.getMenus(get(userId));
    }

}
