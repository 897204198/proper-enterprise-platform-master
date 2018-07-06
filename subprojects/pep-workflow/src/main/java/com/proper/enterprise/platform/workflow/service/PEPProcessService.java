package com.proper.enterprise.platform.workflow.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO;

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
     * @return 流程实例集合
     */
    DataTrunk<PEPProcInstVO> findProcessStartByMe();
}
