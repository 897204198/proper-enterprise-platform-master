package com.proper.enterprise.platform.core.jpa.copy.service;

import com.proper.enterprise.platform.core.jpa.copy.entity.CopyBEntity;

public interface CopyBService {

    /**
     * 保存方法
     * @param a a
     * @return CopyBEntity
     */
    CopyBEntity save(CopyBEntity a);

    /**
     * 根据id查询
     * @param id id
     * @return CopyBEntity
     */
    CopyBEntity findOne(String id);
}
