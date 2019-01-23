package com.proper.enterprise.platform.sys.datadic;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.PEPPropertiesLoader;
import com.proper.enterprise.platform.core.view.BaseView;
import com.proper.enterprise.platform.sys.properties.SysDataDicProperties;

import java.io.Serializable;

/**
 * 数据字典简化类型
 * 用来表示存储到关系型数据库中的数据字典
 * 仅包含必须持久化到数据库中的数据字典信息（类别 和 编码）
 */
public interface DataDicLite extends Serializable {

    public interface DataDicView extends BaseView {

    }

    /**
     * 数据字典类型属性在持久化时，按照如下格式保存：
     * catalog + DD_CATALOG_CODE_SEPARATOR + code
     * 默认分隔符为分号
     */
    String DD_CATALOG_CODE_SEPARATOR = PEPPropertiesLoader.load(SysDataDicProperties.class).getSeparator();

    /**
     * 获得数据字典类别
     *
     * @return 数据字典类别
     */
    @JsonView(value = BaseView.class)
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
    @JsonView(value = BaseView.class)
    String getCode();

    /**
     * 设置编码
     *
     * @param code 编码
     */
    void setCode(String code);

}
