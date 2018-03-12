package com.proper.enterprise.platform.api.auth.dao;

import com.proper.enterprise.platform.api.auth.model.UserGroup;

import java.util.Collection;

public interface UserGroupDao {

    UserGroup get(String id);

    UserGroup getNewUserGroup();

    UserGroup save(UserGroup group);

    Collection<? extends UserGroup> save(Collection<? extends UserGroup> groups);

    Collection<? extends UserGroup> findAll(Collection<String> idList);

    UserGroup findByValidAndName(boolean valid, String name);

    UserGroup findByValidAndId(boolean valid, String id);

    Collection<? extends UserGroup> getGroups(String name, String description, String enable);
}
