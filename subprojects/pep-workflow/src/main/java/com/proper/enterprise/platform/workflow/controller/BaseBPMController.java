package com.proper.enterprise.platform.workflow.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.workflow.service.BPMService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public abstract class BaseBPMController extends BaseController {

    @Autowired
    private BPMService bpmService;

    /**
     * 根据流程定义 key，发起流程，并将流程运行结束后的流程变量返回
     *
     * @param procDefKey 流程定义 key
     * @param inputs     输入流程变量 map
     * @param output     输出流程变量名
     * @return 流程变量值
     */
    protected Object getVariableAfterProcessDone(String procDefKey, Map<String, Object> inputs, String output) {
        return bpmService.getVariableAfterProcessDone(procDefKey, inputs, output);
    }

    @Override
    protected String handleBody(Exception ex) {
        return findOriginalMessage(ex);
    }

    private String findOriginalMessage(Throwable t) {
        return t.getCause() == null ? t.getMessage() : findOriginalMessage(t.getCause());
    }

}
