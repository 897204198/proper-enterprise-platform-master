package com.proper.enterprise.platform.oopsearch.api.serivce;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 查询结果集服务
 * */
public interface QueryResultService {

    /**
     * 点击查询按钮，将当前查询框内输入内容作为查询条件，从数据库中查询结果并返回
     * @param  query 查询条件JsonNode
     * @param  moduleName 查询数据库表名
     *
     * @return 查询结果对象
     * */
    Object assemble(JsonNode query, String moduleName);
}
