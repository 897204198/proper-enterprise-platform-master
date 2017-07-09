package com.proper.enterprise.platform.sys.datadic.service;

import com.proper.enterprise.platform.sys.datadic.DataDic;

import java.util.Collection;

public interface DataDicService {

    /**
     * 获得某分类下的数据字典集合
     * @param catalog 分类名
     * @return 数据字典集合
     */
    Collection<? extends DataDic> findByCatalog(String catalog);

    /**
     * 根据分类及编码获得数据字典节点
     * @param catalog 分类
     * @param code    编码
     * @return 数据字典节点
     */
    DataDic get(String catalog, String code);

}
