package com.proper.enterprise.platform.auth.jpa.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.MenuDao;
import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.RoleService;
import com.proper.enterprise.platform.api.auth.service.UserGroupService;
import com.proper.enterprise.platform.auth.jpa.entity.RoleEntity;
import com.proper.enterprise.platform.auth.jpa.entity.UserEntity;
import com.proper.enterprise.platform.auth.jpa.entity.UserGroupEntity;
import com.proper.enterprise.platform.auth.jpa.repository.UserRepository;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.jpa.service.impl.JpaServiceSupport;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.sort.BeanComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class UserDaoImpl extends JpaServiceSupport<User, UserRepository, String> implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private MenuDao menuDao;

    @Override
    public UserRepository getRepository() {
        return userRepo;
    }

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
    public User getNewUser() {
        return new UserEntity();
    }

    @Override
    public Collection<? extends User> findAll(Collection<String> idList) {
        return userRepo.findAll(idList);
    }

    @Override
    public User findByValidTrueAndId(String id) {
        return userRepo.findByValidTrueAndId(id);
    }

    @Override
    public User get(String id) {
        LOGGER.debug("Get user with {} from DB", id);
        return userRepo.findByIdAndValidAndEnable(id, true, true);
    }

    @Override
    public User getByUsername(String username) {
        LOGGER.debug("GetByUsername with username {} from DB", username);
        return userRepo.findByUsernameAndValidTrueAndEnableTrue(username);
    }

    @Override
    public Collection<? extends User> getUsersByCondition(String condition) {
        condition = "%".concat(condition).concat("%");
        return userRepo.findByUsernameLikeOrNameLikeOrPhoneLikeAndEnableTrueAndValidTrueOrderByName(condition, condition, condition);
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataTrunk<? extends User> getUsersByCondition(String userName, String name, String email, String phone, String enable) {
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
        return this.findPage(specification, new Sort(Sort.Direction.ASC, "name"));
    }

    @Override
    public boolean hasPermissionOfUser(User user, String reqUrl, RequestMethod requestMethod) {
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
    public Collection<? extends Menu> getMenus(User user) {
        Assert.notNull(user, "Could NOT get menus WITHOUT a user");
        if (user.isSuperuser()) {
            return menuDao.findAll(new Sort("parent", "sequenceNumber"));
        }
        List<Menu> menus = new ArrayList<>();
        for (Role role : user.getRoles()) {
            for (Menu menu : role.getMenus()) {
                if (!menus.contains(menu)) {
                    menus.add(menu);
                }
            }
        }
        Collections.sort(menus, new BeanComparator("parent", "sequenceNumber"));
        return menus;
    }

    @Override
    public void deleteAll() {
        userRepo.deleteAll();
    }
}
