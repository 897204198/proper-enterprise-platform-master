package com.proper.enterprise.platform.workflow.service;

import com.proper.enterprise.platform.workflow.vo.PEPModelVO;
import org.flowable.app.model.common.ResultListDataRepresentation;

public interface PEPModelService {
    /**
     * 根据条件查询model
     *
     * @param filter    查询条件 （描述字段 description like)
     * @param sort      排序
     * @param modelType model类型
     * @return ResultListDataRepresentation
     */
    ResultListDataRepresentation getModels(String filter, String sort, Integer modelType);

    /**
     * 更新model
     *
     * @param pepModelVO modelVO
     * @return 更新后的modelVO
     */
    PEPModelVO update(PEPModelVO pepModelVO);

}
