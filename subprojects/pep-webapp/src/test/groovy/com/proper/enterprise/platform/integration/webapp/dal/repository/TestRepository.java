package com.proper.enterprise.platform.integration.webapp.dal.repository;

import com.proper.enterprise.platform.integration.webapp.dal.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<TestEntity, String> {

    TestEntity findByLoginName(String loginName);

}
