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

    private HttpProperties http = new HttpProperties();

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

    public HttpProperties getHttp() {
        return http;
    }

    public void setHttp(HttpProperties http) {
        this.http = http;
    }

    public static class HttpProperties {

        private Integer connectTimeout = 5000;

        private Integer socketTimeout = 5000;

        private Integer connectionRequestTimeout = 5000;

        private Integer requestRetryLimit = 3;

        public Integer getConnectTimeout() {
            return connectTimeout;
        }

        public void setConnectTimeout(Integer connectTimeout) {
            this.connectTimeout = connectTimeout;
        }

        public Integer getSocketTimeout() {
            return socketTimeout;
        }

        public void setSocketTimeout(Integer socketTimeout) {
            this.socketTimeout = socketTimeout;
        }

        public Integer getConnectionRequestTimeout() {
            return connectionRequestTimeout;
        }

        public void setConnectionRequestTimeout(Integer connectionRequestTimeout) {
            this.connectionRequestTimeout = connectionRequestTimeout;
        }

        public Integer getRequestRetryLimit() {
            return requestRetryLimit;
        }

        public void setRequestRetryLimit(Integer requestRetryLimit) {
            this.requestRetryLimit = requestRetryLimit;
        }
    }

}
