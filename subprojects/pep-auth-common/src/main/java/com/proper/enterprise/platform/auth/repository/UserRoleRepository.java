package com.proper.enterprise.platform.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proper.enterprise.platform.auth.entity.UserRoleEntity;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, String> {
    
    List<UserRoleEntity> findAllByUserId(String userId);

}
