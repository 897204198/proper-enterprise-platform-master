package com.proper.enterprise.platform.workflow.plugin.service;

import com.proper.enterprise.platform.workflow.vo.PEPProcessDefinitionVO;

import java.io.IOException;

public interface PEPProcDefsService {

    /**
     * 根据流程定义Id查询model图片
     *
     * @param processDefinitionId 流程定义Id
     * @return model图片
     * @throws IOException io异常
     */
    byte[] getProcessDefinitionDiagram(String processDefinitionId) throws IOException;

    /**
     * 根据流程定义Key 查找最新流程定义
     *
     * @param procDefKey 流程定义Key
     * @return 最新流程定义VO
     */
    PEPProcessDefinitionVO getLatest(String procDefKey);
}
