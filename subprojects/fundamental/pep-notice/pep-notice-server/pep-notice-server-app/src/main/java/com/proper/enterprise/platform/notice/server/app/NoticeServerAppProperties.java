package com.proper.enterprise.platform.notice.server.app;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pep.notice.server.app")
public class NoticeServerAppProperties {

    /**
     * 消息最大重试次数
     */
    private Integer maxRetryCount = 4;

    /**
     * 应用app token权限
     */
    private String resource = "POST:/notice/server/send,"
                            + "*:/notice/server/config/*,"
                            + "*:/notice/server/push/config,"
                            + "GET:/notice/server/app/appKey,"
                            + "GET:/file/*";

    public Integer getMaxRetryCount() {
        return maxRetryCount;
    }

    public void setMaxRetryCount(Integer maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
