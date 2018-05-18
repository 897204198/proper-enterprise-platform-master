package com.proper.enterprise.platform.workflow.controller;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.workflow.service.BPMService;
import org.flowable.engine.common.api.FlowableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import java.util.Map;

public abstract class BaseBPMController extends BaseController {

    @Autowired
    private BPMService bpmService;

    /**
     * 根据流程定义 key，发起流程，并将流程运行结束后的流程变量返回
     * 使用缓存，流程定义 key、最新流程定义版本及出入参作为缓存的 key
     *
     * @param  procDefKey 流程定义 key
     * @param  inputs     输入流程变量 map
     * @param  output     输出流程变量名
     * @return 流程变量值
     */
    protected Object getVariableAfterProcessDone(String procDefKey, Map<String, Object> inputs, String output) {
        return bpmService.getVariableAfterProcessDone(procDefKey, inputs, output);
    }

    /**
     * 根据流程定义 key，发起流程，并将流程运行结束后的流程变量返回
     * 不使用缓存
     *
     * @param  procDefKey 流程定义 key
     * @param  inputs     输入流程变量 map
     * @param  output     输出流程变量名
     * @return 流程变量值
     */
    protected Object getVariableAfterProcessDoneWithoutCache(String procDefKey, Map<String, Object> inputs, String output) {
        return bpmService.getVariableAfterProcessDoneWithoutCache(procDefKey, inputs, output);
    }

    @Override
    protected HttpHeaders handleHeaders(Exception ex) {
        HttpHeaders headers = super.handleHeaders(ex);
        if (ex instanceof FlowableException) {
            headers.set(PEPConstants.RESPONSE_HEADER_ERROR_TYPE, PEPConstants.RESPONSE_BUSINESS_ERROR);
        }
        return headers;
    }

    @Override
    protected String handleBody(Exception ex) {
        return findOriginalMessage(ex);
    }

    private String findOriginalMessage(Throwable t) {
        return t.getCause() == null ? t.getMessage() : findOriginalMessage(t.getCause());
    }

}
