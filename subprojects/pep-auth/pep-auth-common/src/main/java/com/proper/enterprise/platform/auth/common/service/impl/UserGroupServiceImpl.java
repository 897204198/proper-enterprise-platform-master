package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.dao.UserGroupDao;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.api.auth.service.RoleService;
import com.proper.enterprise.platform.api.auth.service.UserGroupService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

@Service
public class UserGroupServiceImpl implements UserGroupService {

    @Autowired
    private UserGroupDao userGroupDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private I18NService i18NService;

    @Override
    public UserGroup get(String id) {
        return userGroupDao.get(id);
    }

    @Override
    public UserGroup save(UserGroup group) {
        return userGroupDao.save(group);
    }

    @Override
    public void delete(UserGroup group) {
        this.deleteByIds(group.getId());
    }

    @Override
    public boolean deleteByIds(String ids) {
        boolean ret = false;
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            List<String> idList = new ArrayList<>();
            Collections.addAll(idList, idArr);
            List<UserGroup> list = new ArrayList<>(idList.size());
            for (String id : idList) {
                UserGroup tempGroup = userGroupDao.findByValidAndId(true, id);
                if (tempGroup == null) {
                    throw new ErrMsgException(i18NService.getMessage("pep.auth.common.usergroup.get.failed"));
                }
                if (!tempGroup.getRoles().isEmpty()) {
                    throw new ErrMsgException(i18NService.getMessage("pep.auth.common.usergroup.delete.relation.role"));
                }
                if (!tempGroup.getUsers().isEmpty()) {
                    throw new ErrMsgException(i18NService.getMessage("pep.auth.common.usergroup.delete.relation.user"));
                }
                tempGroup.setValid(false);
                tempGroup.setEnable(false);
                list.add(tempGroup);
            }
            userGroupDao.save(list);
            ret = true;
        }
        return ret;
    }

    @Override
    public Collection<? extends UserGroup> updateEnable(Collection<String> idList, boolean enable) {
        Collection<? extends UserGroup> groupList = userGroupDao.findAll(idList);
        for (UserGroup group : groupList) {
            group.setEnable(enable);
        }
        return userGroupDao.save(groupList);        // TODO: 2018/3/5 0005  这里没改完
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends UserGroup> getGroups(String name, String description, String enable) {
        return userGroupDao.getGroups(name, description, enable);
    }

    @Override
    public DataTrunk<? extends UserGroup> getGroupsPagniation(String name, String description, String enable) {
        return userGroupDao.getGroupsPagniation(name, description, enable);
    }

    @Override
    public Collection<? extends UserGroup> getFilterGroups(Collection<? extends UserGroup> groups) {
        Collection<UserGroup> result = new HashSet<>();
        for (UserGroup userGroupEntity : groups) {
            if (!userGroupEntity.isEnable() || !userGroupEntity.isValid()) {
                continue;
            }
            result.add(userGroupEntity);
        }
        return result;
    }


    @Override
    public UserGroup saveUserGroupRole(String id, String roleId) {
        UserGroup userGroup = get(id);
        if (userGroup != null) {
            Role role = roleService.get(roleId);
            if (role != null) {
                userGroup.add(role);
                userGroup = save(userGroup);
            }
        }
        return userGroup;
    }

    @Override
    public UserGroup deleteUserGroupRole(String id, String roleId) {
        UserGroup userGroup = get(id);
        if (userGroup != null) {
            Role role = roleService.get(roleId);
            if (role != null) {
                userGroup.remove(role);
                userGroup = save(userGroup);
            }
        }
        return userGroup;
    }

    @Override
    public UserGroup saveOrUpdateUserGroup(UserGroup userGroup) {
        String id = userGroup.getId();
        UserGroup newUserGroup = userGroupDao.getNewUserGroup();
        if (StringUtil.isNotBlank(id)) {
            newUserGroup = this.get(id);
        } else {
            if (userGroupDao.findByValidAndName(true, userGroup.getName()) != null) {
                throw new ErrMsgException(i18NService.getMessage("pep.auth.common.usergroup.name.duplicate"));
            }
        }
        newUserGroup.setName(userGroup.getName());
        newUserGroup.setDescription(userGroup.getDescription());
        newUserGroup.setSeq(userGroup.getSeq());
        newUserGroup.setEnable(userGroup.isEnable());
        return this.save(newUserGroup);
    }

    @Override
    public Collection<? extends Role> getGroupRoles(String groupId) {
        UserGroup userGroup = this.get(groupId);
        if (userGroup == null || !userGroup.isEnable()) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.usergroup.get.failed"));
        }
        return roleService.getFilterRoles(userGroup.getRoles());
    }

    @Override
    public UserGroup addGroupUser(String groupId, String userId) {
        UserGroup userGroup = this.get(groupId);
        if (userGroup != null) {
            User user = userService.get(userId);
            if (user != null) {
                userGroup.add(user);
                userGroup = this.save(userGroup);
            }
        }
        return userGroup;
    }

    @Override
    public UserGroup deleteGroupUser(String groupId, String userId) {
        UserGroup userGroup = this.get(groupId);
        if (userGroup != null) {
            User user = userService.get(userId);
            if (user != null) {
                userGroup.remove(user);
                userGroup = this.save(userGroup);
            }
        }
        return userGroup;
    }

    @Override
    public UserGroup updateGroupUser(String groupId, List<String> userIds) {
        UserGroup userGroup = this.get(groupId);
        if (userGroup != null) {
            Collection<? extends User> collection = userService.getUsersByIds(userIds);
            userGroup.removeAllUsers();
            for (User user : collection) {
                userGroup.add(user);
            }
            userGroup = this.save(userGroup);
        }
        return userGroup;
    }

    @Override
    public Collection<? extends User> getGroupUsers(String groupId) {
        UserGroup userGroup = this.get(groupId);
        if (userGroup == null || !userGroup.isEnable()) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.usergroup.get.failed"));
        }
        return userService.getFilterUsers(userGroup.getUsers());
    }

    @Override
    public boolean hasPermissionOfUserGroup(UserGroup userGroup, String reqUrl, RequestMethod requestMethod) {
        if (userGroup == null || !userGroup.isEnable() || !userGroup.isValid()) {
            return false;
        }
        Collection<? extends Role> roles = userGroup.getRoles();
        for (Role role : roles) {
            if (roleService.hasPermissionOfRole(role, reqUrl, requestMethod)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public UserGroup createUserGroup(UserGroup userGroup) {
        UserGroup group = userGroupDao.findByValidAndName(true, userGroup.getName());
        if (group != null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.usergroup.name.duplicate"));
        } else {
            return this.save(userGroup);
        }
    }

}
