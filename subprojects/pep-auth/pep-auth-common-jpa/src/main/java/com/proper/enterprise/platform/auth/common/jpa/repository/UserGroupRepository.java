package com.proper.enterprise.platform.auth.common.jpa.repository;

import com.proper.enterprise.platform.auth.common.jpa.entity.UserGroupEntity;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;

public interface UserGroupRepository extends BaseJpaRepository<UserGroupEntity, String> {

    /**
     * 通过名称查询用户组
     * @param name 用户组名称
     * @return 用户组
     */
    UserGroupEntity findByName(String name);

    /**
     * 通过名称查询用户组
     * @param name 用户组名称
     * @param enable enable
     * @return 用户组
     */
    UserGroupEntity findByNameAndEnable(String name, boolean enable);

}
