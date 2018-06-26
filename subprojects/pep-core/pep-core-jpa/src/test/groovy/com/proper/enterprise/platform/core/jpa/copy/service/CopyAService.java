package com.proper.enterprise.platform.core.jpa.copy.service;

import com.proper.enterprise.platform.core.jpa.copy.entity.CopyAEntity;

public interface CopyAService {

    /**
     * 保存方法
     * @param a a
     * @return CopyAEntity
     */
    CopyAEntity save(CopyAEntity a);

    /**
     * 根据id 查询
     * @param id id
     * @return CopyAEntity
     */
    CopyAEntity findOne(String id);
}
