package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.enums.ResourceType;

import java.util.Collection;

/**
 * 用户服务接口
 *
 * 用户名（username）作为用户唯一标识，可为用户所见
 * 用户ID（userId）同样为用户唯一标识，但对用户透明，仅在系统内部使用
 *
 * // TODO
 * 用户服务接口提供如下服务：
 *
 *  - 用户的增删改查操作
 *  - 根据提供的用户或当前用户查询角色及资源
 */
public interface UserService {

    void addUser(User... users);

    User getCurrentUser();

    User getUser(String username);

    /**
     * 获得当前登录用户权限范围内资源集合
     *
     * @return 资源集合
     */
    Collection<? extends Resource> getResources();

    /**
     * 根据资源类型获得当前登录用户权限范围内资源集合
     *
     * @param resourceType 资源类型
     * @return 资源集合
     */
    Collection<? extends Resource> getResources(ResourceType resourceType);

    Collection<? extends Resource> getResourcesById(String userId);

    Collection<? extends Resource> getResources(String username);

}
