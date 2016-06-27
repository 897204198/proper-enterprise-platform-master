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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

/**
 * 通用的/抽象的用户服务接口实现
 *
 * 其中，获得当前用户的方法由于与安全框架具体实现关联，只能提供抽象实现
 */
public abstract class AbstractUserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    ResourceRepository resRepo;

    @Autowired
    NativeRepository repo;

    @Override
    public abstract User getCurrentUser() throws Exception;

    @Override
    public void save(User user) {
        if (StringUtil.isNotNull(user.getId())) {
            userRepo.save((UserEntity) user);
        } else {
            userRepo.save(new UserEntity(user.getUsername(), user.getPassword()));
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
    public User getByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public Collection<? extends Resource> getResources() throws Exception {
        return getResources(getCurrentUser().getUsername());
    }

    @Override
    public Collection<? extends Resource> getResources(String username) {
        UserEntity userEntity = userRepo.findByUsername(username);
        return getResources(userEntity);
    }

    private  Collection<? extends Resource> getResources(UserEntity userEntity) {
        if (userEntity != null) {
            return resRepo.findAll(userEntity.getRoleEntities());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public Collection<? extends Resource> getResources(ResourceType resourceType) throws Exception {
        String sql = "SELECT res.id AS id, "
                   + "       res.url AS url, "
                   + "       res.method AS method, "
                   + "       res.name AS name "
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
        Collection<ResourceEntity> reses = new ArrayList<>();
        Object[] objs;
        ResourceEntity res;
        for (Object obj : result) {
            objs = (Object[]) obj;
            res = new ResourceEntity();
            res.setId((String) objs[0]);
            res.setURL((String) objs[1]);
            res.setMethod(RequestMethod.valueOf((String) objs[2]));
            res.setName((String) objs[3]);
            reses.add(res);
        }
        return reses;
    }

    @Override
    public Collection<? extends Resource> getResourcesById(String userId) {
        UserEntity userEntity = userRepo.findOne(userId);
        return getResources(userEntity);
    }

}
