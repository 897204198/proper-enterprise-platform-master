package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.workflow.convert.GroupConvert;
import com.proper.enterprise.platform.workflow.convert.RoleConvert;
import org.flowable.idm.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class PEPIdmIdentityServiceImpl implements IdmIdentityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PEPIdmIdentityServiceImpl.class);

    @Override
    public User newUser(String userId) {
        return null;
    }

    @Override
    public void saveUser(User user) {

    }

    @Override
    public void updateUserPassword(User user) {

    }

    @Override
    public UserQuery createUserQuery() {
        return null;
    }

    @Override
    public NativeUserQuery createNativeUserQuery() {
        return null;
    }

    @Override
    public void deleteUser(String userId) {

    }

    @Override
    public Group newGroup(String groupId) {
        return null;
    }

    @Override
    public List<Group> queryGroupByUserId(String userId) {
        try {
            Collection<? extends UserGroup> userGroups = getUserService().getUserGroups(userId);
            return GroupConvert.convertCollection(userGroups);
        } catch (Exception e) {
            LOGGER.error("queryGroupByUserId in identity find error", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Role> queryRoleByUserId(String userId) {
        try {
            Collection<? extends com.proper.enterprise.platform.api.auth.model.Role> roles = getUserService().getUserRoles(userId);
            return RoleConvert.convertCollection(roles);
        } catch (Exception e) {
            LOGGER.error("queryGroupByUserId in identity find error", e);
            return new ArrayList<>();
        }
    }

    @Override
    public GroupQuery createGroupQuery() {
        return null;
    }

    @Override
    public NativeGroupQuery createNativeGroupQuery() {
        return null;
    }

    @Override
    public void saveGroup(Group group) {

    }

    @Override
    public void deleteGroup(String groupId) {

    }

    @Override
    public void createMembership(String userId, String groupId) {

    }

    @Override
    public void deleteMembership(String userId, String groupId) {

    }

    @Override
    public boolean checkPassword(String userId, String password) {
        return false;
    }

    @Override
    public void setUserPicture(String userId, Picture picture) {

    }

    @Override
    public Picture getUserPicture(String userId) {
        return null;
    }

    @Override
    public Token newToken(String id) {
        return null;
    }

    @Override
    public void saveToken(Token token) {

    }

    @Override
    public void deleteToken(String tokenId) {

    }

    @Override
    public TokenQuery createTokenQuery() {
        return null;
    }

    @Override
    public NativeTokenQuery createNativeTokenQuery() {
        return null;
    }

    @Override
    public void setUserInfo(String userId, String key, String value) {

    }

    @Override
    public String getUserInfo(String userId, String key) {
        return null;
    }

    @Override
    public List<String> getUserInfoKeys(String userId) {
        return null;
    }

    @Override
    public void deleteUserInfo(String userId, String key) {

    }

    @Override
    public Privilege createPrivilege(String privilegeName) {
        return null;
    }

    @Override
    public void addUserPrivilegeMapping(String privilegeId, String userId) {

    }

    @Override
    public void deleteUserPrivilegeMapping(String privilegeId, String userId) {

    }

    @Override
    public void addGroupPrivilegeMapping(String privilegeId, String groupId) {

    }

    @Override
    public void deleteGroupPrivilegeMapping(String privilegeId, String groupId) {

    }

    @Override
    public List<PrivilegeMapping> getPrivilegeMappingsByPrivilegeId(String privilegeId) {
        return null;
    }

    @Override
    public void deletePrivilege(String privilegeId) {

    }

    @Override
    public List<User> getUsersWithPrivilege(String privilegeId) {
        return null;
    }

    @Override
    public List<Group> getGroupsWithPrivilege(String privilegeId) {
        return null;
    }

    @Override
    public PrivilegeQuery createPrivilegeQuery() {
        return null;
    }

    private UserService getUserService() {
        return PEPApplicationContext.getBean(UserService.class);
    }
}
