package com.proper.enterprise.platform.workflow.flowable.idm.service.impl;

import org.flowable.common.engine.api.FlowableException;
import org.flowable.idm.api.*;
import org.flowable.idm.engine.impl.IdmIdentityServiceImpl;
import org.flowable.idm.engine.impl.persistence.entity.GroupEntityImpl;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;

import java.util.ArrayList;
import java.util.List;


public class PEPIdmIdentityServiceImpl extends IdmIdentityServiceImpl {

    @Override
    public UserQuery createUserQuery() {
        return new PEPUserQueryImpl();
    }

    @Override
    public GroupQuery createGroupQuery() {
        return new PEPGroupQueryImpl();
    }

    @Override
    public boolean checkPassword(String userId, String password) {
        return true;
    }

    @Override
    public List<Group> getGroupsWithPrivilege(String name) {
        List<Group> groups = new ArrayList<>();
        List<PrivilegeMapping> privilegeMappings = getPrivilegeMappingsByPrivilegeId(name);
        for (PrivilegeMapping privilegeMapping : privilegeMappings) {
            if (privilegeMapping.getGroupId() != null) {
                Group group = new GroupEntityImpl();
                group.setId(privilegeMapping.getGroupId());
                group.setName(privilegeMapping.getGroupId());
                groups.add(group);
            }
        }

        return groups;
    }

    @Override
    public List<User> getUsersWithPrivilege(String name) {
        List<User> users = new ArrayList<>();
        List<PrivilegeMapping> privilegeMappings = getPrivilegeMappingsByPrivilegeId(name);
        for (PrivilegeMapping privilegeMapping : privilegeMappings) {
            if (privilegeMapping.getUserId() != null) {
                User user = new UserEntityImpl();
                user.setId(privilegeMapping.getUserId());
                user.setLastName(privilegeMapping.getUserId());
                users.add(user);
            }
        }

        return users;
    }

    @Override
    public User newUser(String userId) {
        throw new FlowableException("PEP identity service doesn't support creating a new user");
    }

    @Override
    public void saveUser(User user) {
        throw new FlowableException("PEP identity service doesn't support saving an user");
    }

    @Override
    public NativeUserQuery createNativeUserQuery() {
        throw new FlowableException("PEP identity service doesn't support native querying");
    }

    @Override
    public void deleteUser(String userId) {
        throw new FlowableException("PEP identity service doesn't support deleting an user");
    }

    @Override
    public Group newGroup(String groupId) {
        throw new FlowableException("PEP identity service doesn't support creating a new group");
    }

    @Override
    public NativeGroupQuery createNativeGroupQuery() {
        throw new FlowableException("PEP identity service doesn't support native querying");
    }

    @Override
    public void saveGroup(Group group) {
        throw new FlowableException("PEP identity service doesn't support saving a group");
    }

    @Override
    public void deleteGroup(String groupId) {
        throw new FlowableException("PEP identity service doesn't support deleting a group");
    }

}
