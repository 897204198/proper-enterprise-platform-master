package com.proper.enterprise.platform.integration.webapp.dal.repository;

import com.proper.enterprise.platform.integration.webapp.dal.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;

public interface TestRepository extends JpaRepository<TestEntity, String> {

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    TestEntity findByLoginName(String loginName);

}
