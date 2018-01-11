package com.proper.enterprise.platform.workflow.service;

import com.proper.enterprise.platform.api.cache.CacheDuration;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("bpmService")
public class BPMService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BPMService.class);

    @Autowired
    private BPMService self;

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;

    /**
     * 根据流程定义 key，发起流程，并将流程运行结束后的流程变量返回
     * 不使用缓存
     *
     * @param  procDefKey 流程定义 key
     * @param  inputs     输入流程变量 map
     * @param  output     输出流程变量名
     * @return 流程变量值
     */
    public Object getVariableAfterProcessDoneWithoutCache(String procDefKey, Map<String, Object> inputs, String output) {
        return getVariableAfterProcessDone(procDefKey, -1, inputs, output);
    }

    /**
     * 根据流程定义 key，发起流程，并将流程运行结束后的流程变量返回
     * 使用缓存，流程定义 key、最新流程定义版本及出入参作为缓存的 key
     *
     * @param  procDefKey 流程定义 key
     * @param  inputs     输入流程变量 map
     * @param  output     输出流程变量名
     * @return 流程变量值
     */
    public Object getVariableAfterProcessDone(String procDefKey, Map<String, Object> inputs, String output) {
        int version = repositoryService.createProcessDefinitionQuery()
            .processDefinitionKey(procDefKey).latestVersion().singleResult()
            .getVersion();
        return self.getVariableAfterProcessDone(procDefKey, version, inputs, output);
    }

    @SuppressWarnings("WeakerAccess")
    @Cacheable(cacheNames = "PEP.BPM.auto")
    @CacheDuration(cacheName = "PEP.BPM.auto", maxIdleTime = 30 * 60 * 1000)
    public Object getVariableAfterProcessDone(String procDefKey, int version, Map<String, Object> inputs, String output) {
        long start = System.currentTimeMillis();
        ProcessInstance procInst = startProcess(procDefKey, inputs);
        Object result = getVariableFromHistory(procInst.getId(), output);
        LOGGER.debug("Running {} v{}({}) with {} and get {}({}) in {} ms.",
            procDefKey, version, procInst.getId(), JSONUtil.toJSONIgnoreException(inputs), output,
            StringUtil.left(JSONUtil.toJSONIgnoreException(result), 200), System.currentTimeMillis() - start);
        return result;
    }

    private ProcessInstance startProcess(String procDefKey, Map<String, Object> vars) {
        ProcessInstanceBuilder builder = runtimeService.createProcessInstanceBuilder();
        builder.processDefinitionKey(procDefKey);
        if (!CollectionUtil.isEmpty(vars)) {
            for (Map.Entry<String, Object> entry : vars.entrySet()) {
                builder.variable(entry.getKey(), entry.getValue());
            }
        }
        return builder.start();
    }

    private Object getVariableFromHistory(String procInstId, String varName) {
        return historyService.createHistoricVariableInstanceQuery()
            .processInstanceId(procInstId).variableName(varName).singleResult()
            .getValue();
    }

}
