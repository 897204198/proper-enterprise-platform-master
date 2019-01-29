package com.proper.enterprise.platform.workflow.model;

import java.util.List;

public class PEPVariablesModel {

    /**
     * ‍变量Id
     */
    private String id;

    /**
     * ‍变量名
     */
    private String name;

    /**
     * ‍是否为文本值
     */
    private Boolean textValue;

    /**
     * ‍‍组件名
     */
    private String componentKey;

    /**
     * ‍字典类型数据对应关系
     */
    private List<PEPVariablesChildrenModel> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getTextValue() {
        if (null == textValue) {
            return false;
        }
        return textValue;
    }

    public void setTextValue(Boolean textValue) {
        this.textValue = textValue;
    }

    public String getComponentKey() {
        return componentKey;
    }

    public void setComponentKey(String componentKey) {
        this.componentKey = componentKey;
    }

    public List<PEPVariablesChildrenModel> getChildren() {
        return children;
    }

    public void setChildren(List<PEPVariablesChildrenModel> children) {
        this.children = children;
    }
}
