package com.proper.enterprise.platform.workflow.model;

import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.workflow.api.PEPForm;
import com.proper.enterprise.platform.workflow.vo.PEPWorkflowPageVO;

import java.util.List;
import java.util.Map;

public class PEPWorkflowPage {

    private Map<String, Object> globalVariables;

    private List<PEPForm> forms;

    public Map<String, Object> getGlobalVariables() {
        return globalVariables;
    }

    public void setGlobalVariables(Map<String, Object> globalVariables) {
        this.globalVariables = globalVariables;
    }

    public List<PEPForm> getForms() {
        return forms;
    }

    public void setForms(List<PEPForm> forms) {
        this.forms = forms;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }

    public PEPWorkflowPageVO convert() {
        return BeanUtil.convert(this, PEPWorkflowPageVO.class);
    }
}
