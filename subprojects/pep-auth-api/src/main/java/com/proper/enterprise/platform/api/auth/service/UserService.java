package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.User;

import java.util.Collection;

/**
 * 用户服务接口
 *
 * 用户名（username）作为用户唯一标识，可为用户所见
 * 用户ID（userId）同样为用户唯一标识，但对用户透明，仅在系统内部使用
 */
public interface UserService {

    User save(User user);

    void save(User... users);

    User getCurrentUser();

    User get(String id);

    User getByUsername(String username);

    void delete(String id);

    void delete(User user);

    /**
     * 获得当前登录用户权限范围内菜单集合
     *
     * @return 菜单集合
     */
    Collection<? extends Menu> getMenus();

    Collection<? extends Menu> getMenus(String userId);

    Collection<? extends Menu> getMenusByUsername(String username);

}
