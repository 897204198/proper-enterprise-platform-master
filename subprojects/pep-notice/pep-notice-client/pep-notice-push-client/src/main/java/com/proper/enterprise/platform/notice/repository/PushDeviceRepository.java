package com.proper.enterprise.platform.notice.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.notice.entity.PushDeviceEntity;
import java.util.List;

public interface PushDeviceRepository extends BaseRepository<PushDeviceEntity, String> {

    /**
     * 通过应用标识，用户id获取设备集合
     *
     * @param appkey 应用标识
     * @param userid 用户id
     * @return 设备信息集合
     */
    List<PushDeviceEntity> findByAppkeyAndUserid(String appkey, String userid);

    /**
     * 通过用户ID查询设备
     * @param userid 用户id
     * @return 设备实体
     */
    PushDeviceEntity findByUserid(String userid);

    /**
     * 删除绑定设备
     * @param userid 用户id
     */
    void deleteByUserid(String userid);
}
