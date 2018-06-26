package com.proper.enterprise.platform.oopsearch.config.service;

import com.proper.enterprise.platform.oopsearch.api.enums.DataBaseType;
import com.proper.enterprise.platform.oopsearch.config.conf.AbstractSearchConfigs;
import java.util.Map;

public interface SearchConfigService {

    /**
     * 根据模块名获取配置信息
     *
     * @param moduleName 模块名称
     * @return 配置信息
     */
    AbstractSearchConfigs getSearchConfig(String moduleName);

    /**
     * 根据模块名及数据库类型获取配置信息
     *
     * @param moduleName 模块名称
     * @param dataBaseType 数据库类型
     * @return 配置信息
     */
    AbstractSearchConfigs getSearchConfig(String moduleName, DataBaseType dataBaseType);

    /**
     * 根据数据库类型获取配置信息集合
     *
     * @param dataBaseType 数据库类型
     * @return 配置信息集合
     */
    Map<String, Object> getSearchConfigs(DataBaseType dataBaseType);

    /**
     * 根据模块名称获取访问路径
     *
     * @param moduleName 模块名称
     * @return 访问路径
     */
    String getURL(String moduleName);

}
