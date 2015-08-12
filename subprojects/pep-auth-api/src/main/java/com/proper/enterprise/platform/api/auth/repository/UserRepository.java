package com.proper.enterprise.platform.api.auth.repository;

import org.springframework.data.repository.CrudRepository;

import com.proper.enterprise.platform.api.auth.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, String> {

}
