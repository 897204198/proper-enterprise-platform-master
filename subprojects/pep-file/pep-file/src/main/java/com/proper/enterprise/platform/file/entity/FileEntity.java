package com.proper.enterprise.platform.file.entity;

import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.file.api.File;

import javax.persistence.*;

@Entity
@Table(name = "PEP_FILE")
public class FileEntity extends BaseEntity implements File {

    public FileEntity() {
    }

    /**
     * 文件名称
     */
    @Column(nullable = false, unique = true)
    private String fileName;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件路径
     */
    @Column(nullable = false, unique = true)
    private String filePath;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
