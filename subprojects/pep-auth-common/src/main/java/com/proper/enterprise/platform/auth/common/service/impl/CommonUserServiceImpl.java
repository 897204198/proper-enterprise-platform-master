package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.enums.ResourceType;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.common.entity.ResourceEntity;
import com.proper.enterprise.platform.auth.common.entity.UserEntity;
import com.proper.enterprise.platform.auth.common.repository.ResourceRepository;
import com.proper.enterprise.platform.auth.common.repository.UserRepository;
import com.proper.enterprise.platform.core.repository.NativeRepository;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

/**
 * 通用的/抽象的用户服务接口实现
 *
 * 其中，获得当前用户的方法由于与安全框架具体实现关联，只能提供抽象实现
 */
public abstract class CommonUserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUserServiceImpl.class);

    @Autowired
    UserRepository userRepo;

    @Autowired
    ResourceRepository resRepo;

    @Autowired
    NativeRepository repo;

    @Override
    public abstract User getCurrentUser() throws Exception;

    @Override
    public User save(User user) {
        if (StringUtil.isNotNull(user.getId())) {
            return userRepo.save((UserEntity) user);
        } else {
            return userRepo.save(new UserEntity(user.getUsername(), user.getPassword()));
        }
    }

    @Override
    public void save(User... users) {
        List<UserEntity> entities = new ArrayList<>(users.length);
        for (User user : users) {
            if (StringUtil.isNotNull(user.getId())) {
                entities.add((UserEntity) user);
            } else {
                entities.add(new UserEntity(user.getUsername(), user.getPassword()));
            }
        }
        userRepo.save(entities);
    }

    @Override
    public User get(String id) {
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
    public Collection<Resource> getResources() throws Exception {
        String username = getCurrentUser().getUsername();
        LOGGER.trace("Get resources of current user: {}", username);
        return getResources(username);
    }

    @Override
    public Collection<Resource> getResources(String username) {
        UserEntity userEntity = userRepo.findByUsername(username);
        return getResources(userEntity);
    }

    private  Collection<Resource> getResources(UserEntity userEntity) {
        if (userEntity != null) {
            return resRepo.findAll(userEntity.getRoleEntities());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    // TODO 能否用数据约束实现？
    public Collection<Resource> getResources(ResourceType resourceType) throws Exception {
        String sql = "SELECT res.id AS id, "
                   + "       res.url AS url, "
                   + "       res.method AS method, "
                   + "       res.name AS name, "
                   + "       res.resource_type AS resType "
                   + "  FROM pep_auth_resources res, pep_auth_users u, "
                   + "       pep_auth_users_roles ur, pep_auth_roles_resources rr "
                   + " WHERE u.id = ur.user_id "
                   + "   AND ur.role_id = rr.role_id "
                   + "   AND rr.resource_id = res.id "
                   + "   AND u.username = :name "
                   + "   AND res.resource_type = :type";

        Map<String, Object> params = new HashMap<>();
        params.put("name", getCurrentUser().getUsername());
        params.put("type", resourceType.name());

        List result = repo.executeQuery(sql, params);
        Collection<Resource> reses = new ArrayList<>();
        Object[] objs;
        ResourceEntity res;
        for (Object obj : result) {
            objs = (Object[]) obj;
            res = new ResourceEntity();
            res.setId((String) objs[0]);
            res.setURL((String) objs[1]);
            res.setMethod(RequestMethod.valueOf((String) objs[2]));
            res.setName((String) objs[3]);
            res.setResourceType(ResourceType.valueOf((String) objs[4]));
            reses.add(res);
        }
        return reses;
    }

    @Override
    public Collection<Resource> getResourcesById(String userId) {
        UserEntity userEntity = userRepo.findOne(userId);
        return getResources(userEntity);
    }

}
