package com.proper.enterprise.platform.sys.datadic.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.enums.EnableEnum;
import com.proper.enterprise.platform.core.jpa.service.BaseJpaService;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum;

import java.util.Collection;

public interface DataDicService extends BaseJpaService<DataDic, String> {

    /**
     * 获得某分类下的数据字典集合
     * 字典类型默认全部，默认启用
     *
     * @param catalog 分类名
     * @return 数据字典集合
     */
    Collection<? extends DataDic> findByCatalog(String catalog);

    /**
     * 获得某分类下的数据字典集合
     * 默认启用
     *
     * @param catalog     分类名
     * @param dataDicType 字典类型
     * @return 数据字典集合
     */
    Collection<? extends DataDic> findByCatalog(String catalog, DataDicTypeEnum dataDicType);

    /**
     * 获得某分类下的数据字典集合
     *
     * @param catalog     分类名
     * @param dataDicType 字典类型
     * @param enable      启用停用
     * @return 数据字典集合
     */
    Collection<? extends DataDic> findByCatalog(String catalog, DataDicTypeEnum dataDicType, EnableEnum enable);

    /**
     * 根据类型获得全部数据字典
     *
     * @param dataDicType 数据字典类型
     * @return 数据字典集合
     */
    Collection<? extends DataDic> find(DataDicTypeEnum dataDicType);

    /**
     * 根据类型获得全部数据字典
     *
     * @param dataDicType 数据字典类型
     * @return 数据字典集合
     */
    DataTrunk<? extends DataDic> findPage(DataDicTypeEnum dataDicType);

    /**
     * 根据分类及编码获得数据字典节点
     * 字典类型默认全部
     *
     * @param catalog 分类
     * @param code    编码
     * @return 数据字典节点
     */
    DataDic get(String catalog, String code);

    /**
     * 根据分类及编码获得数据字典节点
     * 字典类型默认全部
     *
     * @param catalog     分类
     * @param code        编码
     * @param dataDicType 字典类型
     * @return 数据字典节点
     */
    DataDic get(String catalog, String code, DataDicTypeEnum dataDicType);

    /**
     * 根据分类及编码获得数据字典节点
     *
     * @param dataDicEnum 字典枚举
     * @return 数据字典节点
     */
    DataDic get(Enum dataDicEnum);

    /**
     * 根据 id 获得数据字典节点
     *
     * @param id ID
     * @return 数据字典节点
     */
    DataDic get(String id);

    /**
     * 获得某分类下启用的默认数据字典项
     *
     * @param catalog 分类
     * @return 默认项，或 null
     */
    DataDic getDefault(String catalog);

    /**
     * 保存数据字典
     *
     * @param dataDic 数据字典
     * @return 保存后的数据字典
     */
    DataDic save(DataDic dataDic);

    /**
     * 更新数据字典
     *
     * @param dataDic 数据字典
     * @return 保存后的数据字典
     */
    DataDic update(DataDic dataDic);

    /**
     * 批量删除
     *
     * @param ids id集合
     * @return 是否删除
     */
    boolean deleteByIds(String ids);

}
