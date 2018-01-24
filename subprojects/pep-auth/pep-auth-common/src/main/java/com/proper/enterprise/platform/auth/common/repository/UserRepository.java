package com.proper.enterprise.platform.auth.common.repository;

import com.proper.enterprise.platform.auth.common.entity.UserEntity;
import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.core.annotation.CacheQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

public interface UserRepository extends BaseRepository<UserEntity, String> {

    @CacheQuery
    UserEntity findByUsername(String username);

    Page<UserEntity> findAll(Specification<UserEntity> spec, Pageable pageable);

    Collection<UserEntity> findByUsernameLikeOrNameLikeOrPhoneLikeAndEnableTrueAndValidTrueOrderByName(String username, String name, String phone);

}
