package com.proper.enterprise.platform.workflow.enums;

import com.proper.enterprise.platform.core.utils.StringUtil;

public enum ComponentKeyEnum {

    /**
     * 输入框
     */
    INPUT("Input"),

    /**
     * 文本域
     */
    TEXTAREA("TextArea"),

    /**
     * 数字输入框
     */
    INPUTNUMBER("InputNumber"),

    /**
     * 上传
     */
    OOPUPLOAD("OopUpload"),

    /**
     * 文本
     */
    OOPTEXT("OopText"),

    /**
     * 选择器
     */
    SELECT("Select"),

    /**
     * 单选框
     */
    RADIOGROUP("RadioGroup"),

    /**
     * 多选框
     */
    CHECKBOXGROUP("CheckboxGroup"),

    /**
     * 系统当前
     */
    OOPSYSTEMCURRENT("OopSystemCurrent"),

    /**
     * 日期选择
     */
    DATEPICKER("DatePicker"),

    /**
     * 用户选择
     */
    OOPGROUPUSERPICKER("OopGroupUserPicker"),

    /**
     * 员工选择
     */
    OOPORGEMPPICKER("OopOrgEmpPicker");

    private String componentKey;

    ComponentKeyEnum(String componentKey) {
        this.componentKey = componentKey;
    }

    public static ComponentKeyEnum keyOf(String componentKey) {
        return StringUtil.isBlank(componentKey) ? INPUT : valueOf(componentKey.toUpperCase());
    }
}
