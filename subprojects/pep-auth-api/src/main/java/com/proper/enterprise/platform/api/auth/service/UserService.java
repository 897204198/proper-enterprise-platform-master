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
 */
public interface UserService {

    User save(User user);

    void save(User... users);

    User getCurrentUser() throws Exception;

    User get(String id);

    User getByUsername(String username);

    void delete(String id);

    void delete(User user);

    /**
     * 获得当前登录用户权限范围内资源集合
     *
     * @return 资源集合
     * @throws Exception
     */
    Collection<Resource> getResources() throws Exception;

    /**
     * 根据资源类型获得当前登录用户权限范围内资源集合
     *
     * @param resourceType 资源类型
     * @return 资源集合
     * @throws Exception
     */
    Collection<Resource> getResources(ResourceType resourceType) throws Exception;

    Collection<Resource> getResourcesById(String userId);

    Collection<Resource> getResources(String username);

}
