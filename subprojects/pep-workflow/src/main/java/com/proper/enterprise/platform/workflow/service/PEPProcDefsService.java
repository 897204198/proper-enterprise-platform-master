package com.proper.enterprise.platform.workflow.service;

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
}
