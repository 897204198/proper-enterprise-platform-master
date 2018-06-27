package com.proper.enterprise.platform.oopsearch.config.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.enums.EnableEnum;
import com.proper.enterprise.platform.core.jpa.service.BaseJpaService;
import com.proper.enterprise.platform.oopsearch.api.enums.DataBaseType;
import com.proper.enterprise.platform.oopsearch.api.vo.SearchConfigVO;
import com.proper.enterprise.platform.oopsearch.config.conf.AbstractSearchConfigs;
import java.util.Map;

public interface SearchConfigService extends BaseJpaService<SearchConfigVO, String> {

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


    /**
     * 获取配置信息列表
     *
     * @param name 模块名称
     * @param url 访问地址
     * @param tableName 表名
     * @param searchColumn 字段名称
     * @param columnAlias 字段别名
     * @param columnDesc 字段描述
     * @param configEnable 是否可用
     * @return 配置信息列表
     */
    DataTrunk<SearchConfigVO> findSearchConfigPagination(String name, String url, String tableName,
                                                         String searchColumn, String columnAlias, String columnDesc,
                                                         EnableEnum configEnable);

    /**
     * 根据id更新模块配置信息
     *
     * @param id 配置信息id
     * @param searchConfigVO 配置信息
     * @return 更新后的配置信息
     */
    SearchConfigVO updateSearchConfig(String id, SearchConfigVO searchConfigVO);

    /**
     * 增加配置信息
     *
     * @param searchConfigVO 配置信息
     * @return 配置信息
     */
    SearchConfigVO add(SearchConfigVO searchConfigVO);

    /**
     * 删除多条配置信息
     *
     * @param ids 以 , 分割的待删除配置信息ID列表
     * @return boolean
     */
    boolean deleteByIds(String ids);
}
