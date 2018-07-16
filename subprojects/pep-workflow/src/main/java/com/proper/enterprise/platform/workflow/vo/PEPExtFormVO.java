package com.proper.enterprise.platform.workflow.vo;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.workflow.api.PEPForm;
import org.apache.commons.collections.MapUtils;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;

import java.util.Map;


/**
 * 外置表单 实现PEP表单接口
 */
public class PEPExtFormVO implements PEPForm {

    public PEPExtFormVO(Task task) {
        this.formKey = task.getFormKey();
        this.formData = buildFormData(task.getProcessVariables());
    }

    public PEPExtFormVO(HistoricTaskInstance historicTaskInstance) {
        this.formKey = historicTaskInstance.getFormKey();
        this.formData = historicTaskInstance.getTaskLocalVariables();
    }

    public PEPExtFormVO(String formKey, Map<String, Object> formData) {
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

    /**
     * 从全局变量中获得formData
     *
     * @param processVariables 全局变量
     * @return formData
     */
    private Map<String, Object> buildFormData(Map<String, Object> processVariables) {
        if (MapUtils.isEmpty(processVariables)) {
            return null;
        }
        if (StringUtil.isEmpty(this.getFormKey())) {
            return null;
        }
        return (Map<String, Object>) processVariables.get(this.getFormKey());
    }

    @Override

    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
