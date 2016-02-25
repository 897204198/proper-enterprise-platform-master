package com.proper.enterprise.platform.auth.common.repository;

import com.proper.enterprise.platform.auth.common.entity.PersonEntity;
import com.proper.enterprise.platform.core.repository.BaseRepository;

public interface PersonRepository extends BaseRepository<PersonEntity, String> {

    PersonEntity findByName(String name);

}
