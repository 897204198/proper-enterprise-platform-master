package com.proper.enterprise.platform.auth.jpa.repository;

import com.proper.enterprise.platform.auth.jpa.entity.UserEntity;
import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.core.annotation.CacheQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

public interface UserRepository extends BaseRepository<UserEntity, String> {

    @CacheQuery
    UserEntity findByUsernameAndValidTrueAndEnableTrue(String username);

    @Override
    Page<UserEntity> findAll(Specification<UserEntity> spec, Pageable pageable);

    UserEntity findByValidTrueAndId(String id);

    UserEntity findByIdAndValidAndEnable(String id, boolean valid, boolean enable);

    Collection<UserEntity> findByUsernameLikeOrNameLikeOrPhoneLikeAndEnableTrueAndValidTrueOrderByName(String username, String name, String phone);

}
