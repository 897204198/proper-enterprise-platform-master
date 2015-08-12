package com.proper.enterprise.platform.auth.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proper.enterprise.platform.auth.entity.RoleResourceEntity;

public interface RoleResourceRepository extends JpaRepository<RoleResourceEntity, String> {
    
    List<RoleResourceEntity> findByRoleIdIn(Collection<String> roleIds);

}
