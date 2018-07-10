package com.proper.enterprise.platform.workflow.vo;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.utils.JSONUtil;

import java.io.Serializable;
import java.util.Map;

public class PEPFormVO implements Serializable {

    private static final long serialVersionUID = PEPConstants.VERSION;

    public PEPFormVO(String formKey, Map<String, Object> formData) {
        this.formKey = formKey;
        this.formData = formData;
    }

    private String formKey;

    private Map<String, Object> formData;

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public Map<String, Object> getFormData() {
        return formData;
    }

    public void setFormData(Map<String, Object> formData) {
        this.formData = formData;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
