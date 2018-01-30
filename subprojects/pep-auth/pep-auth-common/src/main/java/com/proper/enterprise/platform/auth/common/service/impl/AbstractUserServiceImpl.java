package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.model.*;
import com.proper.enterprise.platform.api.auth.service.*;
import com.proper.enterprise.platform.auth.common.entity.RoleEntity;
import com.proper.enterprise.platform.auth.common.entity.UserEntity;
import com.proper.enterprise.platform.auth.common.entity.UserGroupEntity;
import com.proper.enterprise.platform.auth.common.repository.UserRepository;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * 通用的/抽象的用户服务接口实现
 * 其中，获得当前用户的方法由于与安全框架具体实现关联，只能提供抽象实现
 */
public abstract class AbstractUserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractUserServiceImpl.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private I18NService i18NService;

    @Override
    public abstract User getCurrentUser();

    @Override
    public User save(User user) {
        return userRepo.save((UserEntity) user);
    }

    @Override
    public void save(User... users) {
        List<UserEntity> entities = new ArrayList<>(users.length);
        for (User user : users) {
            entities.add((UserEntity) user);
        }
        userRepo.save(entities);
    }

    @Override
    public User save(String userId, Map<String, Object> map) {
        // TODO 具体业务逻辑
        UserEntity user = (UserEntity)this.get(userId);
        user.setName(String.valueOf(map.get("name")));
        user.setEmail(String.valueOf(map.get("email")));
        user.setPhone(String.valueOf(map.get("phone")));
        user.setPassword(String.valueOf(map.get("password")));
        user.setEnable((boolean) map.get("enable"));
        return this.save(user);
    }

    @Override
    public User get(String id) {
        LOGGER.debug("Get user with {} from DB", id);
        User user = userRepo.findOne(id);
        if (user == null || !user.isValid() || !user.isEnable()) {
            return null;
        }
        return user;
    }

    @Override
    public User getByUsername(String username) {
        LOGGER.debug("GetByUsername with username {} from DB", username);
        return userRepo.findByUsernameAndValidTrueAndEnableTrue(username);
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
        user1.setPassword(userMap.get("password") == null ? user1.getPassword() : userMap.get("password").toString());
        return this.save(user1);
    }

    @Override
    public boolean delete(String id) {
        return this.deleteByIds(id);
    }

    @Override
    public void delete(User user) {
        UserEntity userEntity = userRepo.findOne(user.getId());
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
        userRepo.save(userEntity);
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
    public Collection<? extends User> getUsersByCondiction(String condiction) {
        condiction = "%".concat(condiction).concat("%");
        return userRepo.findByUsernameLikeOrNameLikeOrPhoneLikeAndEnableTrueAndValidTrueOrderByName(condiction, condiction, condiction);
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataTrunk<? extends User> getUsersByCondiction(String userName, String name, String email, String phone, String enable,
                                                          Integer pageNo, Integer pageSize) {
        DataTrunk<UserEntity> userDataTrunk = new DataTrunk<>();
        PageRequest pageReq = new PageRequest(pageNo - 1, pageSize, new Sort(Sort.Direction.ASC, "name"));
        Specification specification = new Specification<UserEntity>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtil.isNotNull(userName)) {
                    predicates.add(cb.like(root.get("username"), "%".concat(userName).concat("%")));
                }
                if (StringUtil.isNotNull(name)) {
                    predicates.add(cb.like(root.get("name"), "%".concat(name).concat("%")));
                }
                if (StringUtil.isNotNull(email)) {
                    predicates.add(cb.like(root.get("email"), "%".concat(email).concat("%")));
                }
                if (StringUtil.isNotNull(phone)) {
                    predicates.add(cb.like(root.get("phone"), "%".concat(phone).concat("%")));
                }
                if (StringUtil.isNotNull(enable)) {
                    predicates.add(cb.equal(root.get("enable"), enable.equals("Y")));
                }
                predicates.add(cb.equal(root.get("valid"), true));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Page<UserEntity> usersPage = userRepo.findAll(specification, pageReq);
        userDataTrunk.setCount(usersPage.getTotalElements());
        userDataTrunk.setData(usersPage.getContent());
        return userDataTrunk;
    }

    @Override
    public boolean deleteByIds(String ids) {
        boolean ret = false;
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            List<String> idList = new ArrayList<>();
            Collections.addAll(idList, idArr);
            List<UserEntity> list = userRepo.findAll(idList);
            for (UserEntity userEntity : list) {
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
            userRepo.save(list);
            ret = true;
        }
        return ret;
    }

    @Override
    public Collection<? extends User> updateEanble(Collection<String> idList, boolean enable) {
        Collection<UserEntity> resourceList = new HashSet<>();
        for (String id : idList) {
            resourceList.add(userRepo.findByValidTrueAndId(id));
        }
        for (UserEntity resource : resourceList) {
            resource.setEnable(enable);
        }
        return userRepo.save(resourceList);
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
        Collection<UserEntity> result = new HashSet<>();
        Iterator iterator = users.iterator();
        while (iterator.hasNext()) {
            UserEntity userEntity = (UserEntity) iterator.next();
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
        if (user.isSuperuser()) {
            return true;
        }
        Collection usergroups = user.getUserGroups();
        Iterator iterator = usergroups.iterator();
        while (iterator.hasNext()) {
            UserGroupEntity userGroupEntity = (UserGroupEntity) iterator.next();
            if (userGroupService.hasPermissionOfUserGroup(userGroupEntity, reqUrl, requestMethod)) {
                return true;
            }
        }
        Collection roles = user.getRoles();
        Iterator iterator1 = roles.iterator();
        while (iterator1.hasNext()) {
            RoleEntity roleEntity = (RoleEntity) iterator1.next();
            if (roleService.hasPermissionOfRole(roleEntity, reqUrl, requestMethod)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void checkPermission(String reqUrl, RequestMethod requestMethod) {
        if (!this.hasPermissionOfUser(this.getCurrentUser(), reqUrl, requestMethod)) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.user.permission.failed"));
        }
    }

    @Override
    public Collection<? extends UserGroup> getUserGroups(String userId) {
        User user = userRepo.findByValidTrueAndId(userId);
        if (user == null || !user.isEnable()) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.user.get.failed"));
        }
        return userGroupService.getFilterGroups(user.getUserGroups());
    }

    @Override
    public Collection<? extends Role> getUserRoles(String userId) {
        User user = userRepo.findByValidTrueAndId(userId);
        if (user == null || !user.isEnable()) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.user.get.failed"));
        }
        return roleService.getFilterRoles(user.getRoles());
    }
}
