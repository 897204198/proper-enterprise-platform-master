package com.proper.enterprise.platform.dfs.local;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pep.dfs.local")
public class DFSLocalProperties {

    /**
     * Buffer size when writing file to local file system, defaults to 4096
     */
    private int bufferSize = 4096;

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

}
