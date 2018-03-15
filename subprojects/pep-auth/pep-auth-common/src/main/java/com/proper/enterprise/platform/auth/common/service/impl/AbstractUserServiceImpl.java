package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.api.auth.service.*;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

public abstract class AbstractUserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private PasswordEncryptService pwdService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private I18NService i18NService;

    @Override
    public abstract User getCurrentUser();

    @Override
    public User save(User user) {
        return userDao.save(user);
    }

    @Override
    public void save(User... users) {
        userDao.save(users);
    }

    @Override
    public User save(String userId, Map<String, Object> map) {
        User user = this.get(userId);
        user.setName(String.valueOf(map.get("name")));
        user.setEmail(String.valueOf(map.get("email")));
        user.setPhone(String.valueOf(map.get("phone")));
        user.setPassword(pwdService.encrypt(String.valueOf(map.get("password"))));
        user.setEnable((boolean) map.get("enable"));
        return this.save(user);
    }

    @Override
    public User saveOrUpdateUser(User user) {
        String id = user.getId();
        User newUser = userDao.getNewUser();
        if (StringUtil.isNotBlank(id)) {
            newUser = this.get(id);
        }
        String username = user.getUsername();
        if (StringUtil.isNotBlank(username)) {
            newUser.setUsername(user.getUsername());
        }
        newUser.setPassword(user.getPassword());
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPhone(user.getPhone());
        newUser.setEnable(user.isEnable());
        return this.save(newUser);
    }

    @Override
    public User get(String id) {
        return userDao.get(id);
    }

    @Override
    public User getByUsername(String username) {
        return userDao.getByUsername(username);
    }

    @Override
    public User updateByUser(Map<String, Object> userMap) {
        User user1 = this.get(userMap.get("id").toString());
        if (user1 == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.user.get.failed"));
        }
        user1.setName(userMap.get("name") == null ? user1.getName() : userMap.get("name").toString());
        user1.setEmail(userMap.get("email") == null ? user1.getEmail() : userMap.get("email").toString());
        user1.setPhone(userMap.get("phone") == null ? user1.getPhone() : userMap.get("phone").toString());
        user1.setPassword(userMap.get("password") == null ? user1.getPassword() : pwdService.encrypt(userMap.get("password").toString()));
        user1.setEnable(userMap.get("enable") == null ? user1.isEnable() : (boolean) userMap.get("enable"));
        return this.save(user1);
    }

    @Override
    public boolean delete(String id) {
        return this.deleteByIds(id);
    }

    @Override
    public void delete(User user) {
        User userEntity = userDao.get(user.getId());
        if (userEntity == null || !userEntity.isValid() || !userEntity.isEnable()) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.user.get.failed"));
        }
        if (userEntity.isSuperuser()) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.user.delete.role.super.failed"));
        }
        if (userEntity.getRoles().size() > 0 || userEntity.getUserGroups().size() > 0) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.user.delete.role.relation.failed"));
        }
        userEntity.setValid(false);
        userEntity.setEnable(false);
        userDao.save(userEntity);
    }

    @Override
    public Collection<? extends Menu> getMenus() {
        return menuService.getMenus();
    }

    @Override
    public Collection<? extends Menu> getMenus(String userId) {
        return menuService.getMenus(get(userId));
    }

    @Override
    public Collection<? extends Menu> getMenusByUsername(String username) {
        return menuService.getMenus(getByUsername(username));
    }


    @Override
    public Collection<? extends User> getUsersByCondition(String condition) {
        return userDao.getUsersByCondition(condition);
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataTrunk<? extends User> getUsersByCondition(String userName, String name, String email, String phone, String enable) {
        return userDao.getUsersByCondition(userName, name, email, phone, enable);
    }

    @Override
    public boolean deleteByIds(String ids) {
        boolean ret = false;
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            List<String> idList = new ArrayList<>();
            Collections.addAll(idList, idArr);
            Collection<? extends User> collection = userDao.findAll(idList);
            for (User userEntity : collection) {
                if (userEntity == null || !userEntity.isValid() || !userEntity.isEnable()) {
                    throw new ErrMsgException(i18NService.getMessage("pep.auth.common.user.get.failed"));
                }
                if (userEntity.isSuperuser()) {
                    throw new ErrMsgException(i18NService.getMessage("pep.auth.common.user.delete.role.super.failed"));
                }
                if (userEntity.getRoles().size() > 0 || userEntity.getUserGroups().size() > 0) {
                    throw new ErrMsgException(i18NService.getMessage("pep.auth.common.user.delete.role.relation.failed"));
                }
                userEntity.setValid(false);
                userEntity.setEnable(false);
            }
            userDao.save(collection);
            ret = true;
        }
        return ret;
    }

    @Override
    public Collection<? extends User> updateEnable(Collection<String> idList, boolean enable) {
        Collection<User> resourceList = new HashSet<>();
        for (String id : idList) {
            resourceList.add(userDao.findByValidTrueAndId(id));
        }
        for (User resource : resourceList) {
            resource.setEnable(enable);
        }
        return userDao.save(resourceList);
    }

    @Override
    public User addUserRole(String userId, String roleId) {
        User user = this.get(userId);
        if (roleService.userHasTheRole(user, roleId) == null) {
            Role role = roleService.get(roleId);
            if (role != null) {
                user.add(role);
                user = this.save(user);
            }
        }
        return user;
    }

    @Override
    public User deleteUserRole(String userId, String roleId) {
        User user = this.get(userId);
        Role role = roleService.userHasTheRole(user, roleId);
        if (role != null) {
            user.remove(role);
            user = this.save(user);
            return user;
        }
        return null;
    }

    @Override
    public User groupHasTheUser(UserGroup userGroup, String userId) {
        Collection users = null;
        if (userGroup != null) {
            users = userGroup.getUsers();
        }
        if (users != null && StringUtil.isNotBlank(userId) && users.size() > 0) {
            Iterator iterator = users.iterator();
            while (iterator.hasNext()) {
                User user = (User) iterator.next();
                if (userId.equals(user.getId())) {
                    return user;
                }
            }
        }
        return null;
    }

    @Override
    public Collection<? extends User> getFilterUsers(Collection<? extends User> users) {
        Collection<User> result = new HashSet<>();
        for (User userEntity : users) {
            if (!userEntity.isValid() || !userEntity.isEnable()) {
                continue;
            }
            result.add(userEntity);
        }
        return result;
    }

    @Override
    public boolean hasPermissionOfUser(User user, String reqUrl, RequestMethod requestMethod) {
        if (StringUtil.isBlank(reqUrl) || requestMethod == null) {
            return false;
        }
        if (user == null || StringUtil.isBlank(user.getId()) || !user.isEnable() || !user.isValid()) {
            return false;
        }
        if (user.isSuperuser()) {
            return true;
        }
        return userDao.hasPermissionOfUser(user, reqUrl, requestMethod);
    }

    @Override
    public void checkPermission(String reqUrl, RequestMethod requestMethod) {
        if (!this.hasPermissionOfUser(this.getCurrentUser(), reqUrl, requestMethod)) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.user.permission.failed"));
        }
    }

    @Override
    public Collection<? extends UserGroup> getUserGroups(String userId) {
        User user = this.get(userId);
        if (user == null) {
            return new ArrayList<>();
        }
        return userGroupService.getFilterGroups(user.getUserGroups());
    }

    @Override
    public Collection<? extends Role> getUserRoles(String userId) {
        User user = this.get(userId);
        if (user == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.user.get.failed"));
        }
        return roleService.getFilterRoles(user.getRoles());
    }

}
