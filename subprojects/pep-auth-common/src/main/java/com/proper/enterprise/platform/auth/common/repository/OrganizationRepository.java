package com.proper.enterprise.platform.auth.common.repository;

import com.proper.enterprise.platform.auth.common.entity.OrganizationEntity;
import com.proper.enterprise.platform.core.repository.BaseRepository;

public interface OrganizationRepository extends BaseRepository<OrganizationEntity, String> {

    OrganizationEntity findByName(String name);

}
