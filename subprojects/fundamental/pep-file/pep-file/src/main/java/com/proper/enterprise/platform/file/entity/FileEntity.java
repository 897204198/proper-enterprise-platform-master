package com.proper.enterprise.platform.file.entity;

import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.file.api.File;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.IOException;
import java.util.Map;

@Entity
@Table(name = "PEP_FILE")
public class FileEntity extends BaseEntity implements File {

    public FileEntity() {
    }

    /**
     * 文件名称
     */
    @Column(nullable = false)
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
    @Column(nullable = false)
    private String filePath;

    private String fileExtMsg;

    /**
     * 虚拟文件路径
     */
    private String virPath;

    /**
     * 是否为文件夹
     */
    @Type(type = "yes_no")
    @Column(nullable = false)
    private Boolean isDir = false;

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

    public String getFileExtMsg() {
        return fileExtMsg;
    }

    public void setFileExtMsg(String fileExtMsg) {
        this.fileExtMsg = fileExtMsg;
    }

    public Map<String, String> getFileExtMsgMap() {
        if (StringUtil.isEmpty(fileExtMsg)) {
            return null;
        }
        try {
            return JSONUtil.parse(fileExtMsg, Map.class);
        } catch (IOException e) {
            return null;
        }
    }

    public void setFileExtMsgMap(Map<String, String> fileExtMsg) {
        this.fileExtMsg = JSONUtil.toJSONIgnoreException(fileExtMsg);
    }

    public String getVirPath() {
        return virPath;
    }

    public void setVirPath(String virPath) {
        this.virPath = virPath;
    }

    public Boolean getDir() {
        return isDir;
    }

    public void setDir(Boolean dir) {
        isDir = dir;
    }
}
