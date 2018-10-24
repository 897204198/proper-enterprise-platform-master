package com.proper.enterprise.platform.workflow.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.workflow.vo.PEPTaskVO;
import com.proper.enterprise.platform.workflow.vo.PEPWorkflowPageVO;
import org.springframework.data.domain.PageRequest;

import java.util.List;
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
     * 查询当前人待办数量
     *
     * @return 待办数量
     */
    long getTodoCount();

    /**
     * 根据用户Id获取待办数量
     *
     * @param  userId 用户Id
     * @return 待办数量
     */
    long getTodoCount(String userId);

    /**
     * 查找当前人待办
     *
     * @param processDefinitionName 流程定义名称
     * @param pageRequest           分页参数
     * @return 待办列表
     */
    DataTrunk<PEPTaskVO> findTodoPagination(String processDefinitionName, PageRequest pageRequest);

    /**
     * 查找当前人处理过的待办
     *
     * @param processDefinitionName 流程定义名称
     * @param pageRequest           分页参数
     * @return 待办处理过的待办列表
     */
    DataTrunk<PEPTaskVO> findTaskAssigneeIsMePagination(String processDefinitionName, PageRequest pageRequest);

    /**
     * 根据流程实例id 获取已完成的历史Task
     * 根据完成时间倒序
     *
     * @param procInstId 流程实例id
     * @return 历史Task集合
     */
    List<PEPTaskVO> findHisTasks(String procInstId);

    /**
     * 根据流程实例id 获取当前Task
     * 根据完成时间倒序
     *
     * @param procInstId 流程实例id
     * @return 历史Task集合
     */
    List<PEPTaskVO> findCurrentTasks(String procInstId);

    /**
     * 根据taskId构建流程页面
     * 相同formKey取最新内容
     *
     * @param taskId taskId
     * @return 页面VO
     */
    PEPWorkflowPageVO buildPage(String taskId);

}
