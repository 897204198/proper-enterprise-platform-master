package com.proper.enterprise.platform.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proper.enterprise.platform.auth.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, String> {
    
    RoleEntity findByCode(String code);

}
