package com.proper.enterprise.platform.integration.webapp.dao.repository;

import org.springframework.data.repository.CrudRepository;

import com.proper.enterprise.platform.api.authc.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, String> {

}
