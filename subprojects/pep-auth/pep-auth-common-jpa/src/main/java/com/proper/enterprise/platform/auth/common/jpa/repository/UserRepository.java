package com.proper.enterprise.platform.auth.common.jpa.repository;

import com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity;
import com.proper.enterprise.platform.core.jpa.annotation.CacheQuery;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

public interface UserRepository extends BaseJpaRepository<UserEntity, String> {

    @CacheQuery
    UserEntity findByUsernameAndEnableTrue(String username);

    @CacheQuery
    UserEntity findByIdAndEnableTrue(String userId);

    @Override
    Page<UserEntity> findAll(Specification<UserEntity> spec, Pageable pageable);

    UserEntity findByIdAndEnable(String id, boolean enable);

    Collection<UserEntity> findByUsernameLikeOrNameLikeOrPhoneLikeAndEnableTrueOrderByName(String username, String name, String phone);

}
