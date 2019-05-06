package com.proper.enterprise.platform.workflow.api;

import com.proper.enterprise.platform.workflow.enums.ParserEnum;
import com.proper.enterprise.platform.workflow.model.PEPVariablesChildrenModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractPEPBaseComponent {

    /**
     * 解析类型
     */
    private ParserEnum parserEnum;

    /**
     * 变量名
     */
    protected String name;

    /**
     * 列表
     */
    private List<PEPVariablesChildrenModel> pepVariablesChildrenModels;

    public ParserEnum getParserEnum() {
        return parserEnum;
    }

    public void setParserEnum(ParserEnum parserEnum) {
        this.parserEnum = parserEnum;
    }

    public List<PEPVariablesChildrenModel> getPepVariablesChildrenModels() {
        return pepVariablesChildrenModels;
    }

    public void setPepVariablesChildrenModels(List<PEPVariablesChildrenModel> pepVariablesChildrenModels) {
        this.pepVariablesChildrenModels = pepVariablesChildrenModels;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * 下拉列表Key - value 装箱
     *
     * @return 键值对
     */
    public Map<String, Object> packageSelectMap() {
        return new HashMap<>(0);
    }

    /**
     * 是否为下拉列表
     *
     * @return true/false
     */
    public Boolean getSelect() {
        return false;
    }
}
