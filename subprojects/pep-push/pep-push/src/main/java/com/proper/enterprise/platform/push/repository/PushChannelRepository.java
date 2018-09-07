package com.proper.enterprise.platform.push.repository;

import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import com.proper.enterprise.platform.push.entity.PushChannelEntity;

import java.util.List;

public interface PushChannelRepository extends BaseJpaRepository<PushChannelEntity, String> {
    /**
     * 根据渠道名查询
     * @param channelName 渠道名
     * @return PushChannelDocument
     */
    PushChannelEntity findByChannelName(String channelName);

    /**
     * 获取可用的渠道列表
     * @param enable 是否可用
     * @return 渠道列表
     */
    List<PushChannelEntity> findByEnable(boolean enable);

}
