package com.proper.enterprise.platform.workflow.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.workflow.api.PEPForm;
import com.proper.enterprise.platform.workflow.vo.enums.ShowType;
import org.apache.commons.collections.MapUtils;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;

import java.util.Map;


/**
 * 外置表单 实现PEP表单接口
 */
public class PEPExtFormVO implements PEPForm {

    public PEPExtFormVO() {
    }

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

    @JsonView(value = {PEPTaskVO.ToDoView.class})
    private String formKey;

    @JsonView(value = {PEPTaskVO.ToDoView.class})
    private Map<String, Object> formData;

    @JsonIgnore
    private Map<String, Object> globalData;

    private ShowType showType;

    private Map<String, PEPPropertyVO> formProperties;

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

    public ShowType getShowType() {
        return showType;
    }

    public void setShowType(ShowType showType) {
        this.showType = showType;
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
        return processVariables;
    }

    public Map<String, PEPPropertyVO> getFormProperties() {
        return formProperties;
    }

    public void setFormProperties(Map<String, PEPPropertyVO> formProperties) {
        this.formProperties = formProperties;
    }

    public Map<String, Object> getGlobalData() {
        return globalData;
    }

    public void setGlobalData(Map<String, Object> globalData) {
        this.globalData = globalData;
    }

    @Override

    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }

}
