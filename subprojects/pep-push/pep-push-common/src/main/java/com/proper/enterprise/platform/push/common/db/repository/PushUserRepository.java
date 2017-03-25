package com.proper.enterprise.platform.push.common.db.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.push.common.db.entity.PushUserEntity;

public interface PushUserRepository extends BaseRepository<PushUserEntity, String> {
    PushUserEntity findByAppkeyAndUserid(String appkey, String userid);

    Page<PushUserEntity> findByAppkey(String appkey, Pageable pageable);
}
