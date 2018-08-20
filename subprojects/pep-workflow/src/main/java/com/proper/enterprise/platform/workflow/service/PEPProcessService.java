package com.proper.enterprise.platform.workflow.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO;
import com.proper.enterprise.platform.workflow.vo.PEPWorkflowPageVO;
import com.proper.enterprise.platform.workflow.vo.PEPWorkflowPathVO;
import com.proper.enterprise.platform.workflow.vo.enums.PEPProcInstStateEnum;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

public interface PEPProcessService {
    /**
     * 发起流程
     * 根据流程定义Key发起流程
     * 发起当前流程定义key的最新版本
     *
     * @param procDefKey 流程定义key
     * @param variables  表单信息
     * @return pepProcInstVO 流程实例VO
     */
    PEPProcInstVO startProcess(String procDefKey, Map<String, Object> variables);

    /**
     * 查询当前登录人尕其的流程实例集合
     *
     * @param processDefinitionName 流程定义名称
     * @param state                 流程状态
     * @param pageRequest           分页参数
     * @return 流程实例集合
     */
    DataTrunk<PEPProcInstVO> findProcessStartByMePagination(String processDefinitionName, PEPProcInstStateEnum state, PageRequest pageRequest);

    /**
     * 根据流程实例Id查询所有子流程实例Id及其本身
     *
     * @param procInstId 流程实例Id
     * @return 子流程实力Id集合及其本身的实例Id
     */
    List<String> findProcAndSubInstIds(String procInstId);


    /**
     * 根据流程实例Id查询最上层父流程实例Id
     *
     * @param procInstId 流程实例Id
     * @return 最上层父流程实例Id
     */
    String findTopMostProcInstId(String procInstId);

    /**
     * 根据流程实例Id构建流程页面
     * 相同formKey取最新内容
     *
     * @param procInstId 流程实例Id
     * @return pageVO
     */
    PEPWorkflowPageVO buildPage(String procInstId);

    /**
     * 根据流程实例id查询流程轨迹
     *
     * @param procInstId 流程实例id
     * @return 流程轨迹VO
     */
    PEPWorkflowPathVO findWorkflowPath(String procInstId);
}
