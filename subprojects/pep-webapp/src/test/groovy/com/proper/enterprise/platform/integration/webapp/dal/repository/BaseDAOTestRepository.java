package com.proper.enterprise.platform.integration.webapp.dal.repository;

import org.springframework.data.repository.CrudRepository;

import com.proper.enterprise.platform.auth.entity.UserEntity;

public interface BaseDAOTestRepository extends CrudRepository<UserEntity, String> {

}
