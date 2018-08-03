package com.proper.enterprise.platform.workflow.api;

import java.util.Map;

public interface PEPForm {
    /**
     * 获取表单Id
     *
     * @return 表单Id
     */
    String getFormKey();

    /**
     * 获取表单数据
     *
     * @return 表单数据
     */
    Map<String, Object> getFormData();

    /**
     * 获取全局数据
     *
     * @return 全局数据
     */
    Map<String, Object> getGlobalData();

}
