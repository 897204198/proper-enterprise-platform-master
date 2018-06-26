package com.proper.enterprise.platform.push.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.push.entity.PushUserEntity;

public interface PushUserRepository extends BaseRepository<PushUserEntity, String> {
    /**
     * 通过应用标识，用户id获取用户信息
     *
     * @param appkey 应用标识
     * @param userid 用户id
     * @return 用户信息
     */
    PushUserEntity findByAppkeyAndUserid(String appkey, String userid);

    /**
     * 通过应用标识，分页信息获取用户集合
     *
     * @param appkey 应用标识
     * @param pageable 分页信息
     * @return 用户信息集合
     */
    Page<PushUserEntity> findByAppkey(String appkey, Pageable pageable);
}
