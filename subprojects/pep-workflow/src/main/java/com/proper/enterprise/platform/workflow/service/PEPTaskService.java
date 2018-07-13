package com.proper.enterprise.platform.workflow.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.workflow.vo.PEPTaskVO;
import com.proper.enterprise.platform.workflow.vo.PEPWorkflowPathVO;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

public interface PEPTaskService {

    /**
     * 完成当前任务
     *
     * @param taskId 任务id
     */
    void complete(String taskId);

    /**
     * 完成当前任务
     *
     * @param taskId    任务id
     * @param variables 表单信息
     */
    void complete(String taskId, Map<String, Object> variables);

    /**
     * 查找当前人待办
     *
     * @param searchParam 查询条件
     * @param pageRequest 分页参数
     * @return 待办列表
     */
    DataTrunk<PEPTaskVO> findPagination(Map<String, Object> searchParam, PageRequest pageRequest);

    /**
     * 根据流程实例id查询流程轨迹
     *
     * @param procInstId 流程实例id
     * @return 流程轨迹VO
     */
    PEPWorkflowPathVO findWorkflowPath(String procInstId);
}
