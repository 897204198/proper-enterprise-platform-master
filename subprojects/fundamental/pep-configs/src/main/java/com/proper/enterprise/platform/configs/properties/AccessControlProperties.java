package com.proper.enterprise.platform.configs.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pep.access-control")
public class AccessControlProperties {

    private String allowCredentials = "true";
    private String allowHeaders = "Content-Type, Authorization, X-Requested-With, X-PEP-TOKEN, X-PEP-ERR-TYPE";
    private String allowMethods = "GET, PUT, POST, DELETE, OPTIONS";
    /**
     * 允许跨域访问的来源
     */
    private String allowOrigin = "*";
    /**
     * 将自定义的 header 暴露给外部
     */
    private String exposeHeaders = "X-PEP-ERR-TYPE,X-SERVICE-KEY";
    private String maxAge = "1000";

    public String getAllowCredentials() {
        return allowCredentials;
    }

    public void setAllowCredentials(String allowCredentials) {
        this.allowCredentials = allowCredentials;
    }

    public String getAllowHeaders() {
        return allowHeaders;
    }

    public void setAllowHeaders(String allowHeaders) {
        this.allowHeaders = allowHeaders;
    }

    public String getAllowMethods() {
        return allowMethods;
    }

    public void setAllowMethods(String allowMethods) {
        this.allowMethods = allowMethods;
    }

    public String getAllowOrigin() {
        return allowOrigin;
    }

    public void setAllowOrigin(String allowOrigin) {
        this.allowOrigin = allowOrigin;
    }

    public String getExposeHeaders() {
        return exposeHeaders;
    }

    public void setExposeHeaders(String exposeHeaders) {
        this.exposeHeaders = exposeHeaders;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }
}
