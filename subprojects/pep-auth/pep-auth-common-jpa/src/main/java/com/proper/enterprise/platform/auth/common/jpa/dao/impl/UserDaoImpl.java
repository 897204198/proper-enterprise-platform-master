package com.proper.enterprise.platform.auth.common.jpa.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity;
import com.proper.enterprise.platform.auth.common.jpa.repository.UserRepository;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.jpa.service.impl.JpaServiceSupport;
import com.proper.enterprise.platform.core.utils.StringUtil;
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
public class UserDaoImpl extends JpaServiceSupport<User, UserRepository, String> implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    @Autowired
    private UserRepository userRepo;

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
    public User get(String id, EnableEnum enable) {
        switch (enable) {
            case ALL:
                return userRepo.findByValidTrueAndId(id);
            case DISABLE:
                return userRepo.findByIdAndValidAndEnable(id, true, false);
            case ENABLE:
            default:
                return userRepo.findByIdAndValidAndEnable(id, true, true);
        }
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
    public Collection<? extends User> getUsersByCondition(String userName, String name, String email, String phone, EnableEnum enable) {
        return this.findAll(buildUserSpecification(userName, name, email, phone, enable), new Sort(Sort.Direction.ASC, "name"));
    }

    @Override
    public DataTrunk<? extends User> findUsersPagniation(String userName, String name, String email, String phone, EnableEnum enable) {
        return this.findPage(buildUserSpecification(userName, name, email, phone, enable), new Sort(Sort.Direction.ASC, "name"));
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
                predicates.add(cb.equal(root.get("valid"), true));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return specification;
    }

    @Override
    public void deleteAll() {
        userRepo.deleteAll();
    }
}
