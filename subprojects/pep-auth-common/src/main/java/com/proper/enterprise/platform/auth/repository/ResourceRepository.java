package com.proper.enterprise.platform.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proper.enterprise.platform.auth.entity.ResourceEntity;

public interface ResourceRepository extends JpaRepository<ResourceEntity, String> {

}
