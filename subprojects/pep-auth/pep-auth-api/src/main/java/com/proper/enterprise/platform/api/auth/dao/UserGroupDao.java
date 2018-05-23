package com.proper.enterprise.platform.api.auth.dao;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.service.BaseService;

import java.util.Collection;

public interface UserGroupDao extends BaseService<UserGroup, String> {

    UserGroup getNewUserGroup();

    UserGroup save(UserGroup group);

    UserGroup updateForSelective(UserGroup group);

    Collection<? extends UserGroup> findAll(Collection<String> idList);

    UserGroup findByName(String name, EnableEnum enable);

    Collection<? extends UserGroup> getGroups(String name, String description, EnableEnum enable);

    DataTrunk<? extends UserGroup> getGroupsPagniation(String name, String description, EnableEnum enable);
}
