package com.proper.enterprise.platform.workflow.vo;

import com.proper.enterprise.platform.core.utils.JSONUtil;

public class PEPPropertyVO {

    public PEPPropertyVO() {}

    /**
     * id
     */
    private String id;

    /**
     * 必填
     */
    private Boolean require;
    /**
     * 可写
     */
    private Boolean writable;
    /**
     * 展示名称
     */
    private String label;

    /**
     * 值
     */
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getRequire() {
        return require;
    }

    public void setRequire(Boolean require) {
        this.require = require;
    }

    public Boolean getWritable() {
        return writable;
    }

    public void setWritable(Boolean writable) {
        this.writable = writable;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
