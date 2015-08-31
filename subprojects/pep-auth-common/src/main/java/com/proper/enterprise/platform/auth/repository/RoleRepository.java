package com.proper.enterprise.platform.auth.repository;

import com.proper.enterprise.platform.auth.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;

public interface RoleRepository extends JpaRepository<RoleEntity, String> {

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    RoleEntity findByCode(String code);

}
