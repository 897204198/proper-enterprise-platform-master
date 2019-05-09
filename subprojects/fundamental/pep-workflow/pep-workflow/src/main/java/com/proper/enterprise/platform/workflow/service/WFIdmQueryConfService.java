package com.proper.enterprise.platform.workflow.service;

import com.proper.enterprise.platform.workflow.entity.WFIdmQueryConfEntity;

import java.util.Collection;

public interface WFIdmQueryConfService {

    /**
     * 获取流程权限查询配置集合
     *
     * @return 流程权限查询配置集合
     */
    Collection<WFIdmQueryConfEntity> findAll();


    /**
     * 根据类型  获取流程权限查询配置
     *
     * @param type 类型
     * @return 流程权限查询配置
     */
    WFIdmQueryConfEntity findByType(String type);
}
