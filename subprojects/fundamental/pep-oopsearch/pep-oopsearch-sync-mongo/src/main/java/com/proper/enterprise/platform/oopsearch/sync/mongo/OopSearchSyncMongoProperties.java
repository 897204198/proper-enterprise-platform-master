package com.proper.enterprise.platform.oopsearch.sync.mongo;

import com.proper.enterprise.platform.core.utils.StringUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pep.oopsearch.sync.mongo")
public class OopSearchSyncMongoProperties {

    /**
     * collection需要过滤的collection名称集合 ,分隔
     */
    private String ignoreCollections = "search_column";

    public String getIgnoreCollections() {
        return ignoreCollections;
    }

    public String[] getIgnoreCollectionAttr() {
        if (StringUtil.isEmpty(this.ignoreCollections)) {
            return null;
        }
        return ignoreCollections.split(",");
    }

    public void setIgnoreCollections(String ignoreCollections) {
        this.ignoreCollections = ignoreCollections;
    }
}
