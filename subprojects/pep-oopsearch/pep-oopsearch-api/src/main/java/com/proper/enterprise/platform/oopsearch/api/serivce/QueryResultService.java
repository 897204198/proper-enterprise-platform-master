package com.proper.enterprise.platform.oopsearch.api.serivce;

import com.fasterxml.jackson.databind.JsonNode;
import com.proper.enterprise.platform.oopsearch.api.conf.AbstractSearchConfigs;

/**
 * 查询结果集服务
 * */
public interface QueryResultService {

    /**
     * 点击查询按钮，将当前查询框内输入内容作为查询条件，从数据库中查询结果并返回
     * @param  searchConfigs 查询配置类
     * @param  root 查询条件JsonNode
     * @param  businessId 查询数据库表名
     *
     * @return 查询结果对象
     * */
    Object assemble(AbstractSearchConfigs searchConfigs, JsonNode root, String businessId);
}
