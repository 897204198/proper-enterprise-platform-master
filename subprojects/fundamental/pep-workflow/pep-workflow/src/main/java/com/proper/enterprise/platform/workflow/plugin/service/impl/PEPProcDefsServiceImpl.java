package com.proper.enterprise.platform.workflow.plugin.service.impl;

import com.proper.enterprise.platform.workflow.model.PEPProcessDefinition;
import com.proper.enterprise.platform.workflow.plugin.service.PEPProcDefsService;
import com.proper.enterprise.platform.workflow.vo.PEPProcessDefinitionVO;
import org.apache.commons.io.IOUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class PEPProcDefsServiceImpl implements PEPProcDefsService {

    private RepositoryService repositoryService;

    @Autowired
    PEPProcDefsServiceImpl(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    public byte[] getProcessDefinitionDiagram(String processDefinitionId) throws IOException {
        ProcessDefinition processDefinition = repositoryService
            .createProcessDefinitionQuery()
            .processDefinitionId(processDefinitionId)
            .singleResult();
        if (null == processDefinition) {
            return null;
        }
        InputStream inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
            processDefinition.getDiagramResourceName());
        return IOUtils.toByteArray(inputStream);
    }

    @Override
    public PEPProcessDefinitionVO getLatest(String procDefKey) {
        ProcessDefinition processDefinition = repositoryService
            .createProcessDefinitionQuery()
            .processDefinitionKey(procDefKey)
            .latestVersion()
            .singleResult();
        if (null == processDefinition) {
            return null;
        }
        return new PEPProcessDefinition(processDefinition).convert();
    }
}
