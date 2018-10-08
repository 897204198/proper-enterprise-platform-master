package com.proper.enterprise.platform.auth.common.jpa.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.RoleDao;
import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.PasswordEncryptService;
import com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity;
import com.proper.enterprise.platform.auth.common.jpa.repository.UserRepository;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.jpa.service.impl.AbstractJpaServiceSupport;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserDaoImpl extends AbstractJpaServiceSupport<User, UserRepository, String> implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleDao roleDao;

    @Override
    public UserRepository getRepository() {
        return userRepo;
    }

    @Autowired
    private PasswordEncryptService pwdService;

    @Override
    public User save(User user) {
        if (null == user.getSuperuser()) {
            UserEntity userEntity = (UserEntity) user;
            userEntity.setSuperuser(false);
        }
        return super.save(user);
    }

    @Override
    public void save(User... users) {
        for (User user : users) {
            this.save(user);
        }
    }


    @Override
    public User getNewUser() {
        return new UserEntity();
    }

    @Override
    public Collection<? extends User> findAll(Collection<String> idList) {
        if (CollectionUtil.isNotEmpty(idList)) {
            return userRepo.findAll(idList);
        }
        return new ArrayList<>();
    }

    @Override
    public User getByUsername(String username, EnableEnum enableEnum) {
        LOGGER.debug("GetByUsername with username {} from DB", username);
        switch (enableEnum) {
            case ALL:
                return userRepo.findByUsername(username);
            case DISABLE:
                return userRepo.findByUsernameAndEnable(username, false);
            case ENABLE:
            default:
                return userRepo.findByUsernameAndEnable(username, true);
        }
    }

    @Override
    public User getCurrentUserByUserId(String userId) {
        return userRepo.findByIdAndEnableTrue(userId);
    }

    @Override
    public Collection<? extends User> getUsersByOrCondition(String condition, EnableEnum enable) {
        Specification<User> specification = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                Predicate predicateOr = null;
                if (StringUtil.isNotNull(condition)) {
                    predicateOr = cb.or(cb.like(root.get("username"), "%".concat(condition).concat("%")),
                        cb.like(root.get("name"), "%".concat(condition).concat("%")),
                        cb.like(root.get("phone"), "%".concat(condition).concat("%")));
                }
                Predicate predicateAnd = null;
                if (null != enable && EnableEnum.ALL != enable) {
                    predicateAnd = cb.and(cb.equal(root.get("enable"), enable == EnableEnum.ENABLE));
                }
                if (null != predicateOr) {
                    predicates.add(predicateOr);
                }
                if (null != predicateAnd) {
                    predicates.add(predicateAnd);
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return this.findAll(specification, new Sort(Sort.Direction.DESC, "lastModifyTime"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends User> getUsersByAndCondition(String userName, String name, String email, String phone, EnableEnum enable) {
        return this.findAll(buildUserSpecification(userName, name, email, phone, enable),
            new Sort(Sort.Direction.DESC, "lastModifyTime"));
    }

    @Override
    public DataTrunk<? extends User> findUsersPagination(String userName, String name, String email, String phone, EnableEnum enable) {
        return this.findPage(buildUserSpecification(userName, name, email, phone, enable),
            new Sort(Sort.Direction.DESC, "lastModifyTime"));
    }

    @Override
    public User changePassword(String userId, String oldPassword, String password) {
        User user = this.findOne(userId);
        if (!pwdService.encrypt(oldPassword).equals(user.getPassword())) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.auth.common.user.change.password.oldpassword.error"));
        }
        user.setPassword(pwdService.encrypt(password));
        return this.updateForSelective(user);
    }

    @Override
    public User resetPassword(String userId, String password) {
        User user = this.findOne(userId);
        user.setPassword(pwdService.encrypt(password));
        return this.updateForSelective(user);
    }

    @Override
    public User addUserRole(String userId, String roleId) {
        User user = this.findOne(userId);
        if (null == hasRole(user, roleId)) {
            Role role = roleDao.findOne(roleId);
            if (role != null) {
                user.add(role);
                user = this.save(user);
            }
        }
        return user;
    }

    @Override
    public User deleteUserRole(String userId, String roleId) {
        User user = this.findOne(userId);
        Role role = hasRole(user, roleId);
        if (null != role) {
            user.remove(role);
            user = this.save(user);
            return user;
        }
        return null;
    }

    @Override
    public boolean deleteByIds(Collection<String> ids) {
        Collection<? extends User> collection = userRepo.findAll(ids);
        for (User userEntity : collection) {
            if (userEntity.getSuperuser()) {
                throw new ErrMsgException(I18NUtil.getMessage("pep.auth.common.user.delete.role.super.failed"));
            }
        }
        this.delete(collection);
        return collection.size() > 0;
    }

    @Override
    public User updateForSelective(User user) {
        return super.updateForSelective(user);
    }

    private Specification<User> buildUserSpecification(String userName, String name, String email, String phone, EnableEnum enable) {
        Specification<User> specification = new Specification<User>() {
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
                if (null != enable && EnableEnum.ALL != enable) {
                    predicates.add(cb.equal(root.get("enable"), enable == EnableEnum.ENABLE));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return specification;
    }


    private Role hasRole(User user, String roleId) {
        if (user == null) {
            return null;
        }
        Collection<Role> roles = (Collection<Role>) user.getRoles();
        if (roles != null && roles.size() > 0 && StringUtil.isNotBlank(roleId)) {
            for (Role role : roles) {
                if (roleId.equals(role.getId())) {
                    return role;
                }
            }
        }
        return null;
    }
}
