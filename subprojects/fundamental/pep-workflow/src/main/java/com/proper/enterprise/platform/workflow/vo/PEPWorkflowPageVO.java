package com.proper.enterprise.platform.workflow.vo;

import com.proper.enterprise.platform.core.utils.JSONUtil;

import java.util.List;
import java.util.Map;

public class PEPWorkflowPageVO {


    private Map<String, Object> globalVariables;

    private List<PEPExtFormVO> forms;

    public Map<String, Object> getGlobalVariables() {
        return globalVariables;
    }

    public void setGlobalVariables(Map<String, Object> globalVariables) {
        this.globalVariables = globalVariables;
    }

    public List<PEPExtFormVO> getForms() {
        return forms;
    }

    public void setForms(List<PEPExtFormVO> forms) {
        this.forms = forms;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
