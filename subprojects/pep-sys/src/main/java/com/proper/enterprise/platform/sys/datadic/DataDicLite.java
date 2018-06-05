package com.proper.enterprise.platform.sys.datadic;

import com.proper.enterprise.platform.core.utils.ConfCenter;
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum;

import java.io.Serializable;

/**
 * 数据字典简化类型
 * 用来表示存储到关系型数据库中的数据字典
 */
public interface DataDicLite extends Serializable {

    /**
     * 数据字典类型属性在持久化时，按照如下格式保存：
     * catalog + DD_CATALOG_CODE_SEPARATOR + code
     * 默认分隔符为分号
     */
    String DD_CATALOG_CODE_SEPARATOR = ConfCenter.get("sys.datadic.separator", ";");

    /**
     * 获得数据字典类别
     *
     * @return 数据字典类别
     */
    String getCatalog();

    /**
     * 设置数据字典类别
     *
     * @param catalog 类别
     */
    void setCatalog(String catalog);

    /**
     * 获得编码
     *
     * @return 编码
     */
    String getCode();

    /**
     * 设置编码
     *
     * @param code 编码
     */
    void setCode(String code);

    /**
     * 获得name
     *
     * @return name
     */
    String getName();

    /**
     * 获得字典类型
     *
     * @return 字典类型
     */
    DataDicTypeEnum getDataDicType();

    /**
     * 设置字典类型
     *
     * @param dataDicType 字典类型
     */
    void setDataDicType(DataDicTypeEnum dataDicType);

    /**
     * 获得启用停用
     *
     * @return true启用 false停用
     */
    Boolean getEnable();

    /**
     * 设置启用停用
     *
     * @param enable true启用 false停用 默认启用
     */
    void setEnable(Boolean enable);

}
