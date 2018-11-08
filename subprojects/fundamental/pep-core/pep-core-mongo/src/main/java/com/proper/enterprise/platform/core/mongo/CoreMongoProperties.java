package com.proper.enterprise.platform.core.mongo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pep.core.mongo")
public class CoreMongoProperties {

    /**
     * 副本集  支持逗号分隔
     */
    private String replicaSet;

    public String getReplicaSet() {
        return replicaSet;
    }

    public void setReplicaSet(String replicaSet) {
        this.replicaSet = replicaSet;
    }
}
