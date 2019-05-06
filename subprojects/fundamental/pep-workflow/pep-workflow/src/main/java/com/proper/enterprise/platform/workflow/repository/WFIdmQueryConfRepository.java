package com.proper.enterprise.platform.workflow.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.workflow.entity.WFIdmQueryConfEntity;

import java.util.List;

public interface WFIdmQueryConfRepository extends BaseRepository<WFIdmQueryConfEntity, String> {

    /**
     * 获取流程权限查询配置集合
     *
     * @return 流程权限查询配置集合
     */
    List<WFIdmQueryConfEntity> findAllByOrderBySort();

    /**
     * 根据类型  获取流程权限查询配置
     *
     * @param type 类型
     * @return 流程权限查询配置
     */
    WFIdmQueryConfEntity findByType(String type);
}
