package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.model.Role;

import java.util.Collection;

/**
 * 角色服务接口
 */
public interface RoleService {

    /**
     * 根据角色 ID 获得角色
     *
     * @param id 角色 ID
     * @return 角色
     */
    Role get(String id);

    /**
     * 根据角色名称获得角色集合
     *
     * 系统使用角色 ID 作为角色唯一标识，允许存在同名的角色
     *
     * @param name 角色名称
     * @return 角色集合
     */
    Collection<? extends Role> getByName(String name);

    /**
     * 保存或更新角色信息
     *
     * @param role 角色
     * @return 更新后的角色信息
     */
    Role save(Role role);

    /**
     * 删除角色
     * 删除前需判断角色引用状态
     * 已经使用的角色不能删除
     *
     * @param role 角色
     */
    void delete(Role role);

}
