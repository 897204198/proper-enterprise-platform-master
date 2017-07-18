package com.proper.enterprise.platform.sys.datadic.service;

import com.proper.enterprise.platform.core.utils.ConfCenter;
import com.proper.enterprise.platform.sys.datadic.DataDic;

import java.util.Collection;

public interface DataDicService {

    /**
     * 数据字典类型属性在持久化时，按照如下格式保存：
     * catalog + DD_CATALOG_CODE_SEPARATOR + code
     * 默认分隔符为分号
     */
    String DD_CATALOG_CODE_SEPARATOR = ConfCenter.get("sys.datadic.separator", ";");

    /**
     * 获得某分类下的数据字典集合
     * @param  catalog 分类名
     * @return 数据字典集合
     */
    Collection<? extends DataDic> findByCatalog(String catalog);

    /**
     * 根据分类及编码获得数据字典节点
     * @param  catalog 分类
     * @param  code    编码
     * @return 数据字典节点
     */
    DataDic get(String catalog, String code);

    /**
     * 根据 id 获得数据字典节点
     * @param  id ID
     * @return 数据字典节点
     */
    DataDic get(String id);

    DataDic save(DataDic dataDic);

}
