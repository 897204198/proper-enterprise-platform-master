package com.proper.enterprise.platform.integration.webapp.dal.repository;

import org.springframework.data.repository.CrudRepository;

import com.proper.enterprise.platform.integration.webapp.dal.entity.TestEntity;

public interface TestRepository extends CrudRepository<TestEntity, String> {

}
