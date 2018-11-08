package com.proper.enterprise.platform.workflow.model;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.workflow.api.PEPForm;
import com.proper.enterprise.platform.workflow.constants.WorkFlowConstants;
import com.proper.enterprise.platform.workflow.vo.PEPExtFormVO;
import com.proper.enterprise.platform.workflow.vo.enums.ShowType;
import org.apache.commons.collections.MapUtils;
import org.flowable.engine.FormService;
import org.flowable.engine.form.FormProperty;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PEPExtForm implements PEPForm {


    public PEPExtForm(Task task) {
        this.formKey = StringUtil.isEmpty(task.getFormKey())
            ? (String) task.getProcessVariables().get(WorkFlowConstants.START_FORM_KEY)
            : task.getFormKey();
        this.formData = buildFormData(task.getProcessVariables());
        this.globalData = task.getProcessVariables();
        this.showType = ShowType.EDIT;
        buildFormProperties(task.getId());
    }

    public PEPExtForm(HistoricTaskInstance historicTaskInstance) {
        this.formKey = StringUtil.isEmpty(historicTaskInstance.getFormKey())
            ? (String) historicTaskInstance.getProcessVariables().get(WorkFlowConstants.START_FORM_KEY)
            : historicTaskInstance.getFormKey();
        this.formData = historicTaskInstance.getTaskLocalVariables();
        this.globalData = historicTaskInstance.getProcessVariables();
        this.showType = ShowType.SHOW;
    }

    public PEPExtForm(String formKey, Map<String, Object> formData) {
        String startFormKey = null == formData ? null : (String) formData.get(WorkFlowConstants.START_FORM_KEY);
        this.formKey = StringUtil.isEmpty(formKey)
            ? startFormKey
            : formKey;
        this.formData = formData;
    }

    private String formKey;

    private Map<String, Object> formData;

    private Map<String, Object> globalData;

    private Map<String, PEPProperty> formProperties;

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


    public Map<String, PEPProperty> getFormProperties() {
        return formProperties;
    }

    public void setFormProperties(Map<String, PEPProperty> formProperties) {
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
        if (StringUtil.isNotEmpty(this.getFormKey())
            && null != processVariables.get(this.getFormKey())) {
            return (Map<String, Object>) processVariables.get(this.getFormKey());
        }
        return processVariables;
    }


    private void buildFormProperties(String taskId) {
        FormService formService = PEPApplicationContext.getApplicationContext().getBean(FormService.class);
        List<FormProperty> formProperties = formService.getTaskFormData(taskId).getFormProperties();
        if (CollectionUtil.isEmpty(formProperties)) {
            return;
        }
        Map<String, PEPProperty> pepPropertyMap = new HashMap<>(16);
        for (FormProperty formProperty : formProperties) {
            pepPropertyMap.put(formProperty.getId(), new PEPProperty(formProperty));
        }
        this.setFormProperties(CollectionUtil.isEmpty(pepPropertyMap) ? null : pepPropertyMap);
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
