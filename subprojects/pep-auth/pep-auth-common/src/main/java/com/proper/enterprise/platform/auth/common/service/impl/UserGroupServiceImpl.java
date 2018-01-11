package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.api.auth.service.UserGroupService;
import com.proper.enterprise.platform.auth.common.entity.UserGroupEntity;
import com.proper.enterprise.platform.auth.common.repository.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserGroupServiceImpl implements UserGroupService {

    @Autowired
    private UserGroupRepository repository;

    @Override
    public Collection<? extends UserGroup> list() {
        return repository.findAllByValidTrue();
    }

    @Override
    public UserGroup get(String id) {
        return repository.findOne(id);
    }

    @Override
    public UserGroup save(UserGroup group) {
        return repository.save((UserGroupEntity) group);
    }

    @Override
    public void delete(UserGroup group) {
        repository.delete((UserGroupEntity) group);
    }

}
