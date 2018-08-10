package com.proper.enterprise.platform.workflow.handler;

import org.flowable.engine.repository.ProcessDefinition;

import java.util.Map;

public interface GlobalVariableInitHandler {

    /**
     * 初始化全局变量
     * 流程全局变量初始化处理器执行的时候流程还没有开始
     *
     * @param globalVars        全局变量集合  可以获取到表单信息和平台中定义的基础信息
     * @param processDefinition 流程定义
     * @return 初始化后的全局变量
     */
    Map<String, Object> init(Map<String, Object> globalVars, ProcessDefinition processDefinition);
}
