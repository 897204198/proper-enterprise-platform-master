package com.proper.enterprise.platform.sys.datadic;

import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum;

/**
 * 数据字典
 * 表示系统内编码和名称的对应关系
 * 数据字典按照类别进行归类，通常是 keyvalue 的扁平结构
 */
public interface DataDic extends DataDicLite, IBase {

    /**
     * 获得名称
     *
     * @return 名称
     */
    String getName();

    /**
     * 设置名称
     *
     * @param name 名称
     */
    void setName(String name);

    /**
     * 获得顺序
     *
     * @return 数据项的顺序
     */
    Integer getOrder();

    /**
     * 设置顺序
     *
     * @param order 顺序
     */
    void setOrder(Integer order);

    /**
     * 是否为默认项目
     *
     * @return 是或否
     */
    Boolean getDeft();

    /**
     * 设置是否为默认项目
     *
     * @param deft 是或否
     */
    void setDeft(Boolean deft);

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

}
