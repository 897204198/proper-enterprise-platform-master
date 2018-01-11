package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.model.UserGroup;

import java.util.Collection;

public interface UserGroupService {

    /**
     * 列出所有（有效状态的）用户组
     *
     * @return 用户组集合
     */
    Collection<? extends UserGroup> list();

    /**
     * 根据 ID 获得用户组
     *
     * @param  id 用户组 ID
     * @return 用户组
     */
    UserGroup get(String id);

    /**
     * 保存或更新用户组信息
     *
     * @param group 用户组
     * @return 更新后的用户组
     */
    UserGroup save(UserGroup group);

    /**
     * 删除用户组
     * 删除前需判断用户组引用状态
     * 已经被引用（使用）的用户组不能删除
     *
     * @param group 用户组
     */
    void delete(UserGroup group);

}
