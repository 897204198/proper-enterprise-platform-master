package com.proper.enterprise.platform.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proper.enterprise.platform.auth.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    
    UserEntity findByLoginName(String loginName);

}
