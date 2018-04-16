package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.dao.UserGroupDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
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
    public UserGroup get(String id, EnableEnum enable) {
        return userGroupDao.get(id, enable);
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
    public Collection<? extends UserGroup> getGroups(String name, String description, EnableEnum enable) {
        return userGroupDao.getGroups(name, description, enable);
    }

    @Override
    public DataTrunk<? extends UserGroup> getGroupsPagniation(String name, String description, EnableEnum enable) {
        return userGroupDao.getGroupsPagniation(name, description, enable);
    }

    @Override
    public Collection<? extends UserGroup> getFilterGroups(Collection<? extends UserGroup> groups) {
        return getFilterGroups(groups, EnableEnum.ENABLE);
    }

    @Override
    public Collection<? extends UserGroup> getFilterGroups(Collection<? extends UserGroup> groups, EnableEnum userGroupEnable) {
        Collection<UserGroup> result = new HashSet<>();
        for (UserGroup userGroupEntity : groups) {
            if (EnableEnum.ALL == userGroupEnable && userGroupEntity.isValid()) {
                result.add(userGroupEntity);
                continue;
            }
            if (EnableEnum.ENABLE == userGroupEnable && userGroupEntity.isEnable() && userGroupEntity.isValid()) {
                result.add(userGroupEntity);
                continue;
            }
            if (EnableEnum.DISABLE == userGroupEnable && !userGroupEntity.isEnable() && userGroupEntity.isValid()) {
                result.add(userGroupEntity);
            }
        }
        return result;
    }


    @Override
    public UserGroup saveUserGroupRole(String id, String roleId) {
        UserGroup userGroup = get(id, EnableEnum.ENABLE);
        if (userGroup == null) {
            throw new ErrMsgException("can't save beacuse userGropu not find");
        }
        Role role = roleService.get(roleId, EnableEnum.ENABLE);
        if (role == null) {
            throw new ErrMsgException("can't save beacuse role not find");
        }
        userGroup.add(role);
        userGroup = save(userGroup);
        return userGroup;
    }

    @Override
    public UserGroup deleteUserGroupRole(String id, String roleId) {
        UserGroup userGroup = get(id, EnableEnum.ENABLE);
        if (userGroup == null) {
            throw new ErrMsgException("can't save beacuse userGropu not find");
        }
        Role role = roleService.get(roleId, EnableEnum.ALL);
        if (role == null) {
            throw new ErrMsgException("can't save beacuse role not find");
        }
        userGroup.remove(role);
        userGroup = save(userGroup);
        return userGroup;
    }

    @Override
    public UserGroup saveOrUpdateUserGroup(UserGroup userGroup) {
        String id = userGroup.getId();
        UserGroup newUserGroup = userGroupDao.getNewUserGroup();
        if (StringUtil.isNotBlank(id)) {
            newUserGroup = this.get(id, EnableEnum.ALL);
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
            throw new ErrMsgException("can't save beacuse userGropu not find");
        }
        User user = userService.get(userId, EnableEnum.ENABLE);
        if (user == null) {
            throw new ErrMsgException("can't save beacuse user not find");
        }
        userGroup.add(user);
        userGroup = this.save(userGroup);
        return userGroup;
    }

    @Override
    public UserGroup deleteGroupUser(String groupId, String userId) {
        UserGroup userGroup = get(groupId, EnableEnum.ENABLE);
        if (userGroup == null) {
            throw new ErrMsgException("can't save beacuse userGropu not find");
        }
        User user = userService.get(userId, EnableEnum.ALL);
        if (user == null) {
            throw new ErrMsgException("can't save beacuse user not find");
        }
        userGroup.remove(user);
        userGroup = this.save(userGroup);
        return userGroup;
    }

    @Override
    public UserGroup deleteGroupUsers(String groupId, String ids) {
        UserGroup userGroup = get(groupId, EnableEnum.ENABLE);
        if (userGroup == null) {
            throw new ErrMsgException("can't save beacuse userGropu not find");
        }
        if (StringUtil.isNotNull(ids)) {
            Collection<User> menuList = new ArrayList<>();
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                menuList.add(userService.get(id));
            }
            userGroup.removeAllUsers(menuList);
            userGroup = this.save(userGroup);
        }
        return userGroup;
    }


    @Override
    public UserGroup addGroupUserByUserIds(String groupId, List<String> userIds) {
        UserGroup userGroup = get(groupId, EnableEnum.ENABLE);
        if (userGroup == null) {
            throw new ErrMsgException("can't save beacuse userGropu not find");
        }

        Collection<? extends User> collection = userService.getUsersByIds(userIds);
        for (User user : collection) {
            userGroup.add(user);
        }
        userGroup = this.save(userGroup);
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
    public UserGroup createUserGroup(UserGroup userGroup) {
        UserGroup group = userGroupDao.findByValidAndName(true, userGroup.getName());
        if (group != null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.usergroup.name.duplicate"));
        } else {
            return this.save(userGroup);
        }
    }

}
