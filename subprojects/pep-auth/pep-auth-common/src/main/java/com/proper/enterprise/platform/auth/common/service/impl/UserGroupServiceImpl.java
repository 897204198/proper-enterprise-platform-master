package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.dao.UserGroupDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.api.auth.service.RoleService;
import com.proper.enterprise.platform.api.auth.service.UserGroupService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return userGroupDao.findById(id);
    }

    @Override
    public UserGroup get(String id, EnableEnum enable) {
        return userGroupDao.findById(id);
    }

    @Override
    public UserGroup save(UserGroup group) {
        validateBeforeSaveOrUpdate(group);
        if (null == group.getEnable()) {
            group.setEnable(true);
        }
        return userGroupDao.save(group);
    }

    @Override
    public boolean delete(UserGroup group) {
        return this.deleteByIds(group.getId());
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
                UserGroup tempGroup = userGroupDao.findById(id);
                if (tempGroup == null) {
                    continue;
                }
                if (CollectionUtil.isNotEmpty(tempGroup.getUsers())) {
                    throw new ErrMsgException(i18NService.getMessage("pep.auth.common.usergroup.delete.relation.user"));
                }
                list.add(tempGroup);
                ret = true;
            }
            userGroupDao.delete(list);
        }
        return ret;
    }

    @Override
    public UserGroup update(UserGroup userGroup) {
        validateBeforeSaveOrUpdate(userGroup);
        return userGroupDao.updateForSelective(userGroup);
    }

    @Override
    public Collection<? extends UserGroup> updateEnable(Collection<String> idList, boolean enable) {
        if (CollectionUtil.isEmpty(idList)) {
            return new ArrayList<>();
        }
        Collection<? extends UserGroup> groupList = userGroupDao.findAll(idList);
        for (UserGroup group : groupList) {
            group.setEnable(enable);
        }
        return userGroupDao.save(groupList);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends UserGroup> getGroups(String name, String description, EnableEnum enable) {
        return userGroupDao.getGroups(name, description, enable);
    }

    @Override
    public DataTrunk<? extends UserGroup> getGroupsPagination(String name, String description, EnableEnum enable) {
        return userGroupDao.getGroupsPagination(name, description, enable);
    }

    @Override
    public Collection<? extends UserGroup> getFilterGroups(Collection<? extends UserGroup> groups) {
        return getFilterGroups(groups, EnableEnum.ENABLE);
    }

    @Override
    public Collection<? extends UserGroup> getFilterGroups(Collection<? extends UserGroup> groups, EnableEnum userGroupEnable) {
        Collection<UserGroup> result = new HashSet<>();
        for (UserGroup userGroupEntity : groups) {
            if (EnableEnum.ALL == userGroupEnable) {
                result.add(userGroupEntity);
                continue;
            }
            if (EnableEnum.ENABLE == userGroupEnable && userGroupEntity.getEnable()) {
                result.add(userGroupEntity);
                continue;
            }
            if (EnableEnum.DISABLE == userGroupEnable && !userGroupEntity.getEnable()) {
                result.add(userGroupEntity);
            }
        }
        return result;
    }


    @Override
    public UserGroup saveUserGroupRole(String id, String roleId) {
        UserGroup userGroup = get(id, EnableEnum.ENABLE);
        if (userGroup == null) {
            throw new ErrMsgException("can't save because userGropu not find");
        }
        Role role = roleService.get(roleId);
        if (role == null) {
            throw new ErrMsgException("can't save because role not find");
        }
        userGroup.add(role);
        userGroup = save(userGroup);
        return userGroup;
    }

    @Override
    public UserGroup deleteUserGroupRole(String id, String roleId) {
        UserGroup userGroup = get(id, EnableEnum.ENABLE);
        if (userGroup == null) {
            throw new ErrMsgException("can't save because userGropu not find");
        }
        Role role = roleService.get(roleId);
        if (role == null) {
            throw new ErrMsgException("can't save because role not find");
        }
        userGroup.remove(role);
        userGroup = save(userGroup);
        return userGroup;
    }

    @Override
    public Collection<? extends Role> getGroupRoles(String groupId, EnableEnum userGroupEnable, EnableEnum roleEnable) {
        UserGroup userGroup = this.get(groupId, userGroupEnable);
        if (userGroup == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.usergroup.get.failed"));
        }
        return roleService.getFilterRoles(userGroup.getRoles(), roleEnable);
    }

    @Override
    public UserGroup addGroupUser(String groupId, String userId) {
        UserGroup userGroup = get(groupId, EnableEnum.ENABLE);
        if (userGroup == null) {
            throw new ErrMsgException("can't save because userGropu not find");
        }
        User user = userService.get(userId);
        if (user == null) {
            throw new ErrMsgException("can't save because user not find");
        }
        user.add(userGroup);
        userService.save(user);
        return userGroup;
    }

    @Override
    public UserGroup deleteGroupUser(String groupId, String userId) {
        UserGroup userGroup = get(groupId, EnableEnum.ENABLE);
        if (userGroup == null) {
            throw new ErrMsgException("can't save because userGropu not find");
        }
        User user = userService.get(userId);
        if (user == null) {
            throw new ErrMsgException("can't save because user not find");
        }
        user.remove(userGroup);
        userService.save(user);
        userGroup.remove(user);
        return userGroup;
    }

    @Override
    public UserGroup deleteGroupUsers(String groupId, String ids) {
        UserGroup userGroup = get(groupId, EnableEnum.ALL);
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                User user = userService.get(id);
                if (null == user) {
                    continue;
                }
                user.remove(userGroup);
                userService.save(user);
                userGroup.remove(user);
            }
        }
        return userGroup;
    }


    @Override
    public UserGroup addGroupUserByUserIds(String groupId, List<String> userIds) {
        UserGroup userGroup = get(groupId, EnableEnum.ENABLE);
        if (userGroup == null) {
            throw new ErrMsgException("can't save because userGropu not find");
        }

        Collection<? extends User> collection = userService.getUsersByIds(userIds);
        for (User user : collection) {
            user.add(userGroup);
            userService.save(user);
            userGroup.add(user);
        }
        return userGroup;
    }

    @Override
    public Collection<? extends User> getGroupUsers(String groupId, EnableEnum userGroupEnable, EnableEnum userEnable) {
        UserGroup userGroup = this.get(groupId, userGroupEnable);
        if (userGroup == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.usergroup.get.failed"));
        }
        return userService.getFilterUsers(userGroup.getUsers(), userEnable);
    }

    @Override
    public Collection<? extends Resource> getGroupResources(Collection<UserGroup> userGroups, EnableEnum resourceEnable) {
        Collection<Resource> resources = new HashSet<>();
        if (CollectionUtil.isNotEmpty(userGroups)) {
            for (UserGroup userGroup : userGroups) {
                resources.addAll(getGroupResources(userGroup, resourceEnable));
            }
            return resources;
        }
        return new ArrayList<>();
    }

    @Override
    public Collection<? extends Resource> getGroupResources(UserGroup userGroup, EnableEnum resourceEnable) {
        if (null == userGroup) {
            return new ArrayList<>();
        }
        Collection<Role> roles = (Collection<Role>) roleService.getFilterRoles(userGroup.getRoles());
        return roleService.getRoleResources(roles, resourceEnable);
    }

    @Override
    public UserGroup createUserGroup(UserGroup userGroup) {
        UserGroup group = userGroupDao.findByName(userGroup.getName(), EnableEnum.ALL);
        if (group != null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.usergroup.name.duplicate"));
        } else {
            return this.save(userGroup);
        }
    }

    private void validateBeforeSaveOrUpdate(UserGroup group) {
        UserGroup userGroup = userGroupDao.findByName(group.getName(), EnableEnum.ALL);
        if (null != userGroup && !userGroup.getId().equals(group.getId())) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.auth.common.usergroup.name.duplicate"));
        }
    }

}
