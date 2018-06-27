package com.proper.enterprise.platform.api.auth.dao;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.service.BaseService;

import java.util.Collection;

public interface UserGroupDao extends BaseService<UserGroup, String> {

    /**
     * 获取新的用户组
     * @return 用户组
     */
    UserGroup getNewUserGroup();

    /**
     * 添加用户组
     * @param group 用户组;
     * @return 用户组;
     */
    UserGroup save(UserGroup group);

    /**
     * 修改用户组信息
     * @param group 用户组;
     * @return 用户组;
     */
    UserGroup updateForSelective(UserGroup group);

    /**
     * 通过id查询用户组
     * @param idList 多个用户组id;
     * @return 用户组集合;
     */
    Collection<? extends UserGroup> findAll(Collection<String> idList);

    /**
     * 通过名称查询用户组
     * @param name 用户组名称;
     * @param enable 是否可用;
     * @return 用户组;
     */
    UserGroup findByName(String name, EnableEnum enable);

    /**
     * 多条件组合获取用户组集合 模糊查询
     * @param name 用户组名称
     * @param description 描述信息;
     * @param enable 标记字段;
     * @return 用户组集合;
     */
    Collection<? extends UserGroup> getGroups(String name, String description, EnableEnum enable);

    /**
     * 分页+多条件组合获取用户组集合 模糊查询
     * @param name 用户组名称;
     * @param description 描述信息;
     * @param enable 是否可用;
     * @return  用户组集合;
     */
    DataTrunk<? extends UserGroup> getGroupsPagination(String name, String description, EnableEnum enable);
}
