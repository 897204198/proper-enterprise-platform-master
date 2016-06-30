package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.model.Role;

import java.util.Collection;

/**
 * 角色服务接口
 */
public interface RoleService {

    /**
     * 根据角色名称获得角色集合
     *
     * 系统使用角色 ID 作为角色唯一标识，允许存在同名的角色
     *
     * @param name 角色名称
     * @return 角色集合
     */
    Collection<? extends Role> getByName(String name);

}
