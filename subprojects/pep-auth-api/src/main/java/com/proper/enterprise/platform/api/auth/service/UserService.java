package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.Resource;
import com.proper.enterprise.platform.api.auth.User;
import com.proper.enterprise.platform.core.enums.ResourceType;

import java.util.Collection;

/**
 * 用户服务接口
 * 用户名（username）作为系统内可见的用户唯一标识
 * 用户ID（userId）同样为用户唯一标识，但系统内不可见
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
    Collection<Resource> getResources();

    /**
     * 根据资源类型获得当前登录用户权限范围内资源集合
     *
     * @param resourceType 资源类型
     * @return 资源集合
     */
    Collection<Resource> getResources(ResourceType resourceType);

    Collection<Resource> getResourcesById(String userId);

    Collection<Resource> getResources(String username);

}
