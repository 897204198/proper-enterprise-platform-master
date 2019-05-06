package com.proper.enterprise.platform.workflow.plugin.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.workflow.vo.WFCategoryVO;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Collection;

@Validated
public interface WFCategoryService {

    /**
     * 保存
     *
     * @param wfCategoryVO 流程类别对象
     * @return 保存后的流程类别对象
     */
    WFCategoryVO save(@Valid WFCategoryVO wfCategoryVO);

    /**
     * 根据Id集合删除
     *
     * @param ids id集合(,分割)
     * @return true删除成功  false未删除
     */
    boolean deleteByIds(String ids);

    /**
     * 单表修改
     *
     * @param wfCategoryVO 流程类别对象
     * @return 修改后的流程类别对象
     */
    WFCategoryVO update(WFCategoryVO wfCategoryVO);

    /**
     * 查询
     *
     * @param name   流程类别名称
     * @param code   流程类别编码
     * @param parent 父类别对象
     * @return 流程类别对象集合
     */
    Collection<WFCategoryVO> findAll(String name, String code, WFCategoryVO parent);

    /**
     * 分页查询
     *
     * @param name     流程类别名称
     * @param code     流程类别编码
     * @param parent   父类别对象
     * @param pageable 分页参数
     * @return 分页流程类别对象
     */
    DataTrunk<WFCategoryVO> findAll(String name, String code, WFCategoryVO parent, Pageable pageable);

    /**
     * 根据类别Id获取类别信息
     *
     * @param id 类别Id
     * @return 类别信息
     */
    WFCategoryVO get(String id);

    /**
     * 根据类别编码获取类别信息
     *
     * @param code 类别编码
     * @return 类别信息
     */
    WFCategoryVO getByCode(String code);
}
