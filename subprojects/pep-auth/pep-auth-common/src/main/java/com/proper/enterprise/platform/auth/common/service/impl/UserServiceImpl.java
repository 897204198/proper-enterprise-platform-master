package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.*;
import com.proper.enterprise.platform.api.auth.service.*;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.security.util.SecurityUtil;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private PasswordEncryptService pwdService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private I18NService i18NService;

    @Autowired
    private ResourceService resourceService;

    @Override
    public User getCurrentUser() {
        return userDao.getCurrentUserByUserId(SecurityUtil.getCurrentUserId());
    }

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
            newUser = this.get(id, EnableEnum.ALL);
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
    public User get(String id, EnableEnum enable) {
        return userDao.get(id, enable);
    }

    @Override
    public User getByUsername(String username) {
        return userDao.getByUsername(username);
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
    public Collection<? extends Resource> getResources(String userId) {
        User user = get(userId);
        if (user.isSuperuser()) {
            return resourceService.find();
        }
        if (!user.isEnable() || !user.isValid()) {
            return Collections.emptyList();
        }
        Set<Resource> resources = new HashSet<>(filterDisableAndInvalid(user.getRoles()));
        for (UserGroup userGroup : user.getUserGroups()) {
            if (userGroup.isEnable() && userGroup.isValid()) {
                resources.addAll(filterDisableAndInvalid(userGroup.getRoles()));
            }
        }
        return resources;
    }

    private Set<Resource> filterDisableAndInvalid(Collection<? extends Role> roles) {
        Set<Resource> resources = new HashSet<>();
        for (Role role : roles) {
            if (role.isEnable() && role.isValid()) {
                for (Resource resource : role.getResources()) {
                    if (resource.isEnable() && resource.isValid()) {
                        resources.add(resource);
                    }
                }
            }
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
    public Collection<? extends User> getUsersByCondition(String condition) {
        return userDao.getUsersByCondition(condition);
    }

    @Override
    public Collection<? extends User> getUsersByCondition(String userName, String name, String email, String phone, EnableEnum enable) {
        return userDao.getUsersByCondition(userName, name, email, phone, enable);
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataTrunk<? extends User> findUsersPagniation(String userName, String name, String email, String phone, EnableEnum enable) {
        return userDao.findUsersPagniation(userName, name, email, phone, enable);
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
                if (userEntity == null || !userEntity.isValid()) {
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
        User user = this.get(userId, EnableEnum.ENABLE);
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
        User user = this.get(userId, EnableEnum.ENABLE);
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
        return getFilterUsers(users, EnableEnum.ENABLE);
    }

    @Override
    public Collection<? extends User> getFilterUsers(Collection<? extends User> users, EnableEnum userEnable) {
        Collection<User> result = new HashSet<>();
        for (User user : users) {
            if (EnableEnum.ALL == userEnable && user.isValid()) {
                result.add(user);
                continue;
            }
            if (EnableEnum.ENABLE == userEnable && user.isEnable() && user.isValid()) {
                result.add(user);
                continue;
            }
            if (EnableEnum.DISABLE == userEnable && !user.isEnable() && user.isValid()) {
                result.add(user);
            }
        }
        return result;
    }

    @Override
    public Collection<? extends UserGroup> getUserGroups(String userId) {
        return getUserGroups(userId, EnableEnum.ALL, EnableEnum.ENABLE);
    }

    @Override
    public Collection<? extends UserGroup> getUserGroups(String userId, EnableEnum userEnable, EnableEnum userGroupEnable) {
        User user = this.get(userId, userEnable);
        if (user == null) {
            return new ArrayList<>();
        }
        return userGroupService.getFilterGroups(user.getUserGroups(), userGroupEnable);
    }

    @Override
    public Collection<? extends Role> getUserRoles(String userId) {
        return getUserRoles(userId, EnableEnum.ALL, EnableEnum.ENABLE);
    }

    public Collection<? extends Role> getUserRoles(String userId, EnableEnum userEnable, EnableEnum roleEnable) {
        User user = this.get(userId, userEnable);
        if (user == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.user.get.failed"));
        }
        return roleService.getFilterRoles(user.getRoles(), roleEnable);
    }

}
