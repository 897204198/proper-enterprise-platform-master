package com.proper.enterprise.platform.page.custom.grid.document;

import java.io.Serializable;
import java.util.List;

/**
 * 表格页面自定义配置信息(表单元素)
 */
public class CustomGridElementDocument implements Serializable {

    /**
     * 元素绑定键
     */
    private String model;

    /**
     * 表单元素名称
     */
    private String name;

    /**
     * 行号
     */
    private int row;

    /**
     * 元素类型
     */
    private String type;

    /**
     * 元素宽度
     */
    private String width;

    /**
     * 选择类元素基础数据
     */
    private String selectData;

    /**
     * 选择类元素基础数据键
     */
    private String selectCode;

    /**
     * 选择类元素基础数据值
     */
    private String selectName;

    /**
     * 元素验证
     */
    private List<CustomGridValidDocument> valid;

    /**
     * 查询条件
     */
    private CustomGridConditionDocument condition;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSelectData() {
        return selectData;
    }

    public void setSelectData(String selectData) {
        this.selectData = selectData;
    }

    public String getSelectCode() {
        return selectCode;
    }

    public void setSelectCode(String selectCode) {
        this.selectCode = selectCode;
    }

    public String getSelectName() {
        return selectName;
    }

    public void setSelectName(String selectName) {
        this.selectName = selectName;
    }

    public List<CustomGridValidDocument> getValid() {
        return valid;
    }

    public void setValid(List<CustomGridValidDocument> valid) {
        this.valid = valid;
    }

    public CustomGridConditionDocument getCondition() {
        return condition;
    }

    public void setCondition(CustomGridConditionDocument condition) {
        this.condition = condition;
    }
}
