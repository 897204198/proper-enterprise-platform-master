package com.proper.enterprise.platform.workflow.model;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.workflow.vo.PEPProcessDefinitionVO;
import org.apache.commons.collections.MapUtils;
import org.flowable.engine.FormService;
import org.flowable.engine.form.FormProperty;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PEPProcessDefinition {

    private String id;
    private String name;
    private String key;
    private Integer version;
    private String deploymentId;
    private Date deploymentTime;
    private String startFormKey;
    private Map<String, PEPProperty> formProperties;

    public PEPProcessDefinition(ProcessDefinition processDefinition) {
        BeanUtils.copyProperties(processDefinition, this);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDeploymentTime() {
        if (null == deploymentTime) {
            return null;
        }
        return (Date) deploymentTime.clone();
    }

    public void setDeploymentTime(Date deploymentTime) {
        if (null == deploymentTime) {
            this.deploymentTime = null;
            return;
        }
        this.deploymentTime = (Date) deploymentTime.clone();
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getStartFormKey() {
        if (StringUtil.isNotEmpty(this.startFormKey)) {
            return this.startFormKey;
        }
        if (StringUtil.isEmpty(this.id)) {
            return null;
        }
        return PEPApplicationContext.getBean(FormService.class).getStartFormKey(this.id);
    }

    public void setStartFormKey(String startFormKey) {
        this.startFormKey = startFormKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, PEPProperty> getFormProperties() {
        if (MapUtils.isNotEmpty(this.formProperties)) {
            return this.formProperties;
        }
        if (StringUtil.isEmpty(this.id)) {
            return null;
        }
        List<FormProperty> formProperties = PEPApplicationContext.getBean(FormService.class)
            .getStartFormData(this.id)
            .getFormProperties();
        if (CollectionUtil.isNotEmpty(formProperties)) {
            Map<String, PEPProperty> pepPropertyMap = new HashMap<>(16);
            for (FormProperty formProperty : formProperties) {
                pepPropertyMap.put(formProperty.getId(), new PEPProperty(formProperty));
            }
            return pepPropertyMap;
        }
        return null;
    }

    public void setFormProperties(Map<String, PEPProperty> formProperties) {
        this.formProperties = formProperties;
    }

    public PEPProcessDefinitionVO convert() {
        return BeanUtil.convert(this, PEPProcessDefinitionVO.class);
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
