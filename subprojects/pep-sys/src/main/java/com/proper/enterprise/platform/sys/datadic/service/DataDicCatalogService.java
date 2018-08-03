package com.proper.enterprise.platform.sys.datadic.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.enums.EnableEnum;
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum;
import com.proper.enterprise.platform.sys.datadic.vo.DataDicCatalogVO;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Collection;

@Validated
public interface DataDicCatalogService {

    /**
     * 根据类型获得全部数据字典
     *
     * @param catalogCode 分类Code
     * @param catalogName 分类名称
     * @param catalogType 数据字典类型
     * @param enable      启用停用
     * @return 数据字典分类集合
     */
    Collection<DataDicCatalogVO> findAll(String catalogCode, String catalogName, DataDicTypeEnum catalogType, EnableEnum enable);

    /**
     * 根据类型获得全部数据字典
     *
     * @param catalogCode 分类Code
     * @param catalogName 分类名称
     * @param catalogType 数据字典类型
     * @param enable      启用停用
     * @param pageable    分页信息
     * @return 数据字典分类集合
     */
    DataTrunk<DataDicCatalogVO> findPage(String catalogCode, String catalogName, DataDicTypeEnum catalogType,
                                         EnableEnum enable, Pageable pageable);


    /**
     * 根据 id 获得数据字典节点
     *
     * @param id ID
     * @return 数据字典节点
     */
    DataDicCatalogVO get(String id);


    /**
     * 保存数据字典分类
     *
     * @param dataDicCatalogVO 数据字典分类VO
     * @return 保存后的数据字典
     */
    DataDicCatalogVO save(@Valid DataDicCatalogVO dataDicCatalogVO);

    /**
     * 更新数据字典分类
     *
     * @param dataDicCatalogVO 数据字典分类VO
     * @return 保存后的数据字典
     */
    DataDicCatalogVO update(@Valid DataDicCatalogVO dataDicCatalogVO);

    /**
     * 批量删除
     *
     * @param ids id集合
     * @return 是否删除
     */
    boolean deleteByIds(String ids);

}
