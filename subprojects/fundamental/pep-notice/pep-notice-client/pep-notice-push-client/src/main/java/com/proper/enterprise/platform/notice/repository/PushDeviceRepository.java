package com.proper.enterprise.platform.notice.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.notice.entity.PushDeviceEntity;

import java.util.List;

public interface PushDeviceRepository extends BaseRepository<PushDeviceEntity, String> {

    /**
     * 通过用户ID查询设备
     *
     * @param userId 用户id
     * @return 设备实体
     */
    PushDeviceEntity findByUserId(String userId);

    /**
     * 删除绑定设备
     *
     * @param userId 用户id
     */
    void deleteByUserId(String userId);

    /**
     * 删除token
     *
     * @param token token
     */
    void deleteByPushToken(String token);

    /**
     * 查询指定应用的所有设备信息
     *
     * @param appKey 应用标识
     * @return 设备信息
     */
    List<PushDeviceEntity> findByAppKeyAndUserIdIsNotNull(String appKey);
}
