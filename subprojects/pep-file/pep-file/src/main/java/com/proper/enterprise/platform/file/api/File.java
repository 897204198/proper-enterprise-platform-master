package com.proper.enterprise.platform.file.api;

import com.proper.enterprise.platform.core.api.IBase;

public interface File extends IBase {


    String getFileName();

    void setFileName(String fileName);

    String getFileDescription();

    void setFileDescription(String fileDescription);

    String getFileModule();

    void setFileModule(String fileModule);

    Long getFileSize();

    void setFileSize(Long fileSize);

    String getFileType();

    void setFileType(String fileType);

    String getFilePath();

    void setFilePath(String filePath);
}
