package com.proper.enterprise.platform.workflow.service;

import com.proper.enterprise.platform.core.utils.AntResourceUtil;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DeployService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeployService.class);

    @Autowired
    private RepositoryService repositoryService;

    public Deployment deploy(String name, String resource) {
        return repositoryService.createDeployment()
                    .name(name)
                    .addClasspathResource(resource)
                    .deploy();
    }

    public Deployment deployFromResourcePattern(String name, String locations) throws IOException {
        DeploymentBuilder builder = repositoryService.createDeployment().name(name);
        Resource[] resources = AntResourceUtil.getResources(locations);
        for (Resource resource : resources) {
            LOGGER.debug("Loading {} to deploy", resource.getFilename());
            builder.addInputStream(resource.getFilename(), resource.getInputStream());
        }
        return builder.deploy();
    }

    /**
     * 根据部署名称获得部署列表，按部署时间倒序排列
     *
     * @param name 部署名称
     * @return 部署列表
     */
    public List<Deployment> findByName(String name) {
        return repositoryService.createDeploymentQuery().deploymentName(name).orderByDeploymenTime().desc().list();
    }

    public void delete(String id, boolean cascade) {
        repositoryService.deleteDeployment(id, cascade);
    }

}
