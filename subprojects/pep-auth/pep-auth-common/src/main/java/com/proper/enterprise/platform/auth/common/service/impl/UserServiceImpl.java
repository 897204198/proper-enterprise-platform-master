package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.*;
import com.proper.enterprise.platform.api.auth.service.*;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private I18NService i18NService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private PasswordEncryptService pwdService;

    @Override
    public User getCurrentUser() {
        return userDao.getCurrentUserByUserId(Authentication.getCurrentUserId());
    }

    @Override
    public User save(User user) {
        validateUserName(user);
        if (null == user.getEnable()) {
            user.setEnable(true);
        }
        if (null == user.getSuperuser()) {
            user.setSuperuser(false);
        }
        if (StringUtil.isNotEmpty(user.getId())) {
            User userOld = this.get(user.getId());
            if (null == userOld || !user.getPassword().equals(userOld.getPassword())) {
                user.setPassword(pwdService.encrypt(user.getPassword()));
            }
            return userDao.save(user);
        }
        user.setPassword(pwdService.encrypt(user.getPassword()));
        return userDao.save(user);
    }

    @Override
    public void save(User... users) {
        for (User user : users) {
            validateUserName(user);
        }
        userDao.save(users);
    }

    @Override
    public User update(User user) {
        validateUserName(user);
        User userOld = this.get(user.getId());
        if (null == userOld) {
            throw new PersistenceException("entity not persist");
        }
        if (null != user.getPassword() && !user.getPassword().equals(userOld.getPassword())) {
            user.setPassword(pwdService.encrypt(user.getPassword()));
        }
        return userDao.updateForSelective(user);
    }

    @Override
    public User updateChangePassword(String userId, String oldPassword, String password) {
        return userDao.changePassword(userId, oldPassword, password);
    }

    @Override
    public User get(String id) {
        return userDao.findOne(id);
    }

    @Override
    public User getByUsername(String username, EnableEnum enableEnum) {
        return userDao.getByUsername(username, enableEnum);
    }

    @Override
    public boolean delete(String id) {
        return this.deleteByIds(id);
    }

    @Override
    public boolean deleteByIds(String ids) {
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            List<String> idList = new ArrayList<>();
            Collections.addAll(idList, idArr);
            return userDao.deleteByIds(idList);
        }
        return false;
    }

    @Override
    public Collection<? extends Menu> getUserMenus(String userId, EnableEnum menuEnable) {
        return menuService.getMenus(get(userId));
    }

    @Override
    public Collection<? extends Resource> getUserResources(String userId, EnableEnum resourceEnable) {
        User user = get(userId);
        if (null == user) {
            return new ArrayList<>();
        }
        //超级管理员 拥有全部资源
        if (user.getSuperuser()) {
            return resourceService.findAll(resourceEnable);
        }
        Collection<Resource> resources = new HashSet<>();
        //获得用户启用角色的资源
        Collection<Role> roles = (Collection<Role>) roleService.getFilterRoles(user.getRoles());
        if (CollectionUtil.isNotEmpty(roles)) {
            resources.addAll(roleService.getRoleResources(roles, resourceEnable));
        }
        //获得用户启用用户组的资源
        Collection<UserGroup> groups = (Collection<UserGroup>) userGroupService.getFilterGroups(user.getUserGroups());
        if (CollectionUtil.isNotEmpty(groups)) {
            resources.addAll(userGroupService.getGroupResources(groups, resourceEnable));
        }
        return resources;
    }

    @Override
    public Collection<? extends User> getUsersByIds(List<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return userDao.findAll(ids);
    }

    @Override
    public Collection<? extends User> getUsersByOrCondition(String condition, EnableEnum enable) {
        return userDao.getUsersByOrCondition(condition, enable);
    }

    @Override
    public Collection<? extends User> getUsersByAndCondition(String userName, String name, String email, String phone, EnableEnum enable) {
        return userDao.getUsersByAndCondition(userName, name, email, phone, enable);
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataTrunk<? extends User> findUsersPagination(String userName, String name, String email, String phone, EnableEnum enable) {
        return userDao.findUsersPagination(userName, name, email, phone, enable);
    }

    @Override
    public Collection<? extends User> updateEnable(Collection<String> idList, boolean enable) {
        if (CollectionUtil.isEmpty(idList)) {
            return new ArrayList<>();
        }
        Collection<? extends User> users = userDao.findAll(idList);
        for (User user : users) {
            user.setEnable(enable);
        }
        return userDao.save(users);
    }

    @Override
    public User addUserRole(String userId, String roleId) {
        return userDao.addUserRole(userId, roleId);
    }

    @Override
    public User deleteUserRole(String userId, String roleId) {
        return userDao.deleteUserRole(userId, roleId);
    }

    @Override
    public Collection<? extends User> getFilterUsers(Collection<? extends User> users, EnableEnum userEnable) {
        if (EnableEnum.ALL == userEnable) {
            return users;
        }
        Collection<User> result = new HashSet<>();
        for (User user : users) {
            if (EnableEnum.ENABLE == userEnable && user.getEnable()) {
                result.add(user);
                continue;
            }
            if (EnableEnum.DISABLE == userEnable && !user.getEnable()) {
                result.add(user);
            }
        }
        return result;
    }

    @Override
    public Collection<? extends UserGroup> getUserGroups(String userId) {
        return getUserGroups(userId, EnableEnum.ENABLE);
    }

    @Override
    public Collection<? extends UserGroup> getUserGroups(String userId, EnableEnum userGroupEnable) {
        User user = this.get(userId);
        if (user == null) {
            return new ArrayList<>();
        }
        return userGroupService.getFilterGroups(user.getUserGroups(), userGroupEnable);
    }

    @Override
    public Collection<? extends Role> getUserRoles(String userId) {
        return getUserRoles(userId, EnableEnum.ENABLE);
    }

    public Collection<? extends Role> getUserRoles(String userId, EnableEnum roleEnable) {
        User user = this.get(userId);
        if (user == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.user.get.failed"));
        }
        return roleService.getFilterRoles(user.getRoles(), roleEnable);
    }

    private void validateUserName(User user) {
        User haveUser = this.getByUsername(user.getUsername(), EnableEnum.ALL);
        if (null != haveUser && !haveUser.getId().equals(user.getId())) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.auth.common.user.username.duplicated"));
        }
    }

}
