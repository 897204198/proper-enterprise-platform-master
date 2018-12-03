package com.proper.enterprise.platform.dfs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pep.dfs")
public class DFSProperties {

    /**
     * 上传的最大字节数 单位/B
     */
    private long uploadMaxsize = 1048576L;

    /**
     * 文件根路径
     */
    private String rootPath = "proper";

    /**
     * 文件或文件夹名称最大长度
     */
    private int nameMaxLength = 24;

    public long getUploadMaxsize() {
        return uploadMaxsize;
    }

    public void setUploadMaxsize(long uploadMaxsize) {
        this.uploadMaxsize = uploadMaxsize;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public int getNameMaxLength() {
        return nameMaxLength;
    }

    public void setNameMaxLength(int nameMaxLength) {
        this.nameMaxLength = nameMaxLength;
    }
}
