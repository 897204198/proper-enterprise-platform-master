package com.proper.enterprise.platform.sys.datadic.service;

import com.proper.enterprise.platform.sys.datadic.DataDic;

import java.util.Collection;

public interface DataDicService {

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
     * 根据分类及编码获得数据字典节点
     *
     * @param dataDicEnum 字典枚举
     * @return 数据字典节点
     */
    DataDic get(Enum dataDicEnum);

    /**
     * 根据 id 获得数据字典节点
     * @param  id ID
     * @return 数据字典节点
     */
    DataDic get(String id);

    /**
     * 获得某分类下的默认数据字典项
     * @param  catalog 分类
     * @return 默认项，或 null
     */
    DataDic getDefault(String catalog);

    /**
     * 保存数据字典
     * @param  dataDic 数据字典
     * @return 保存后的数据字典
     */
    DataDic save(DataDic dataDic);

}
