package com.proper.enterprise.platform.dfs.mongo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("pep.dfs.mongo")
public class DFSMongoProperties {

    /**
     * Bucket to use in the given database, defaults to 'dfs'
     */
    private String bucket = "dfs";

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

}
