package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.MenuService;
import com.proper.enterprise.platform.api.auth.service.RoleService;
import com.proper.enterprise.platform.api.auth.service.UserGroupService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.common.entity.UserEntity;
import com.proper.enterprise.platform.auth.common.repository.UserRepository;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

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
    private MenuService menuService;

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
        user.setEnable((boolean)map.get("enable"));
        return this.save(user);
    }

    @Override
    public User get(String id) {
        LOGGER.debug("Get user with {} from DB", id);
        return userRepo.findOne(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public void delete(String id) {
        userRepo.delete(id);
    }

    @Override
    public void delete(User user) {
        userRepo.delete((UserEntity) user);
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
            // TODO 对删除业务进行逻辑判断
            try {
                userRepo.delete(userRepo.findAll(idList));
                ret = true;
            } catch (Exception e) {
                throw new ErrMsgException("Delete error!");
            }
        } else {
            throw new ErrMsgException("Param Error!");
        }
        return ret;
    }

    @Override
    public Collection<? extends User> updateEanble(Collection<String> idList, boolean enable) {
        // TODO 具体实现
        Collection<UserEntity> resourceList = userRepo.findAll(idList);
        for (UserEntity resource : resourceList) {
            resource.setEnable(enable);
        }
        return userRepo.save(resourceList);
    }

    @Override
    public User addUserRole(String userId, String roleId) {
        User user = this.get(userId);
        Role role = roleService.get(roleId);
        if (role != null) {
            user.add(role);
            user = this.save(user);
        }
        return user;
    }

    @Override
    public User deleteUserRole(String userId, String roleId) {
        User user = this.get(userId);
        Role role = roleService.get(roleId);
        if (role != null) {
            user.remove(role);
            user = this.save(user);
        }
        return user;
    }
}
