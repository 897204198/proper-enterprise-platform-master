package com.proper.enterprise.platform.push.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.push.entity.PushChannelEntity;

public interface PushChannelRepository extends BaseRepository<PushChannelEntity, String> {
    /**
     * 根据渠道名查询
     * @param channelName 渠道名
     * @return PushChannelDocument
     */
    PushChannelEntity findByChannelName(String channelName);
}
