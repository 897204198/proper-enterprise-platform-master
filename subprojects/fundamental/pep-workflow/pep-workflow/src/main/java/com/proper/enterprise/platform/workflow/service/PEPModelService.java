package com.proper.enterprise.platform.workflow.service;

import com.proper.enterprise.platform.workflow.vo.PEPModelVO;
import org.flowable.ui.common.model.ResultListDataRepresentation;

public interface PEPModelService {
    /**
     * 根据条件查询model
     *
     * @param filter      查询条件 （描述字段 description like)
     * @param sort        排序
     * @param modelType   model类型
     * @param modelStatus 部署状态
     * @return ResultListDataRepresentation
     */
    ResultListDataRepresentation getModels(String filter, String sort, Integer modelType, PEPModelVO.ModelStatus modelStatus);

    /**
     * 根据条件查询model
     *
     * @param filter               查询条件 （描述字段 description like)
     * @param sort                 排序
     * @param modelType            model类型
     * @param workflowCategoryCode 流程类别编码
     * @param modelStatus          部署状态
     * @return ResultListDataRepresentation
     */
    ResultListDataRepresentation getModels(String filter,
                                           String sort,
                                           Integer modelType,
                                           String workflowCategoryCode,
                                           PEPModelVO.ModelStatus modelStatus);

    /**
     * 更新model
     *
     * @param pepModelVO modelVO
     * @return 更新后的modelVO
     */
    PEPModelVO update(PEPModelVO pepModelVO);

    /**
     * 更新model
     *
     * @param id                   modelId
     * @param workflowCategoryCode 流程类别编码
     * @return 更新后的modelVO
     */
    PEPModelVO updateModelCategory(String id, String workflowCategoryCode);
}
