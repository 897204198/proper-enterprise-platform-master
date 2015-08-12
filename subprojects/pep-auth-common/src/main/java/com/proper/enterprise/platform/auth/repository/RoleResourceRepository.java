package com.proper.enterprise.platform.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.proper.enterprise.platform.auth.entity.RoleResourceEntity;

public interface RoleResourceRepository extends JpaRepository<RoleResourceEntity, String> {
    
    @Query("SELECT t FROM RoleResourceEntity t WHERE t.roleId in ?")
    List<RoleResourceEntity> findAllByRoles(String... roles);

}
