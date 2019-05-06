package com.proper.enterprise.platform.workflow.util;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.workflow.entity.WFIdmQueryConfEntity;
import com.proper.enterprise.platform.workflow.plugin.service.WFIdmQueryConfService;

import java.util.Collection;

public class WFIdmQueryConfUtil {

    private WFIdmQueryConfUtil() {

    }

    /**
     * 获取流程权限查询配置集合
     *
     * @return 流程权限查询配置集合
     */
    public static Collection<WFIdmQueryConfEntity> findAll() {
        return PEPApplicationContext.getBean(WFIdmQueryConfService.class).findAll();
    }

    /**
     * 根据类型  获取流程权限查询配置
     *
     * @param type 类型
     * @return 流程权限查询配置
     */
    public static WFIdmQueryConfEntity findByType(String type) {
        return PEPApplicationContext.getBean(WFIdmQueryConfService.class).findByType(type);
    }
}
