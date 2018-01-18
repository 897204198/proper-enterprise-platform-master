package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.service.RoleService;
import com.proper.enterprise.platform.auth.common.entity.RoleEntity;
import com.proper.enterprise.platform.auth.common.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Role get(String id) {
        return roleRepository.findOne(id);
    }

    @Override
    public Collection<? extends Role> getByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save((RoleEntity) role);
    }

    @Override
    public void delete(Role role) {
        roleRepository.delete((RoleEntity) role);
    }
}
