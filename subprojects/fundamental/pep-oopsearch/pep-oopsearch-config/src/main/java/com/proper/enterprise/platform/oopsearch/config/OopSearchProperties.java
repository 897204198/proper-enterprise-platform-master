package com.proper.enterprise.platform.oopsearch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pep.oopsearch")
public class OopSearchProperties {

    /**
     * 每次查询返回结果条数限制
     */
    private int searchLimit = 10;

    public int getSearchLimit() {
        return searchLimit;
    }

    public void setSearchLimit(int searchLimit) {
        this.searchLimit = searchLimit;
    }
}
