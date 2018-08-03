package com.proper.enterprise.platform.workflow.model;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.workflow.api.PEPForm;
import com.proper.enterprise.platform.workflow.vo.PEPExtFormVO;
import com.proper.enterprise.platform.workflow.vo.enums.ShowType;
import org.apache.commons.collections.MapUtils;
import org.flowable.engine.FormService;
import org.flowable.engine.form.FormProperty;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;

import java.util.List;
import java.util.Map;

public class PEPExtForm implements PEPForm {


    public PEPExtForm(Task task) {
        this.formKey = task.getFormKey();
        this.formData = buildFormData(task.getProcessVariables());
        this.globalData = task.getProcessVariables();
        this.showType = ShowType.EDIT;
        buildFormProperties(task.getId());
    }

    public PEPExtForm(HistoricTaskInstance historicTaskInstance) {
        this.formKey = historicTaskInstance.getFormKey();
        this.formData = historicTaskInstance.getTaskLocalVariables();
        this.globalData = historicTaskInstance.getProcessVariables();
        this.showType = ShowType.SHOW;
    }

    public PEPExtForm(String formKey, Map<String, Object> formData) {
        this.formKey = formKey;
        this.formData = formData;
    }

    private String formKey;

    private Map<String, Object> formData;

    private Map<String, Object> globalData;

    private List<FormProperty> formProperties;

    private ShowType showType;

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


    public List<FormProperty> getFormProperties() {
        return formProperties;
    }

    public void setFormProperties(List<FormProperty> formProperties) {
        this.formProperties = formProperties;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
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


    private void buildFormProperties(String taskId) {
        FormService formService = PEPApplicationContext.getApplicationContext().getBean(FormService.class);
        setFormProperties(formService.getTaskFormData(taskId).getFormProperties());
    }

    public Map<String, Object> getGlobalData() {
        return globalData;
    }

    public void setGlobalData(Map<String, Object> globalData) {
        this.globalData = globalData;
    }

    public PEPExtFormVO convert() {
        return BeanUtil.convert(this, PEPExtFormVO.class);
    }

}
