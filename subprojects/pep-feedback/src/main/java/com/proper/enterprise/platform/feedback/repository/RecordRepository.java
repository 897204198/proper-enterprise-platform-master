package com.proper.enterprise.platform.feedback.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.feedback.entity.RecordEntity;

public interface RecordRepository extends BaseRepository<RecordEntity, String> {

    /**
     * 获取设备浏览记录
     * @param problemId 当前浏览问题Id
     * @param code 当前浏览问题使用的设备号
     * @return 记录
     */
    RecordEntity findByProblemIdAndDeviceId(String problemId, String code);
}
