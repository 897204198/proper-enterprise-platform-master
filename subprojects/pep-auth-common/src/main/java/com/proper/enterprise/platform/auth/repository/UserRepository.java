package com.proper.enterprise.platform.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proper.enterprise.platform.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    UserEntity findByLoginName(String loginName);

}
