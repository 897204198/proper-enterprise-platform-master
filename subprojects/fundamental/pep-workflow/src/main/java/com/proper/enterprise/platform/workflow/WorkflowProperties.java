package com.proper.enterprise.platform.workflow;

import com.proper.enterprise.platform.workflow.enums.ProcDefDeployType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pep.workflow")
public class WorkflowProperties {

    /**
     * 流程部署类型 部署应用内置流程
     */
    private ProcDefDeployType deployType = ProcDefDeployType.ONCE;

    /**
     * 流程部署路径 部署应用内置流程
     */
    private String deployLocations = "classpath*:bpmn/**/*.xml";

    public ProcDefDeployType getDeployType() {
        return deployType;
    }

    public void setDeployType(ProcDefDeployType deployType) {
        this.deployType = deployType;
    }

    public String getDeployLocations() {
        return deployLocations;
    }

    public void setDeployLocations(String deployLocations) {
        this.deployLocations = deployLocations;
    }

}
