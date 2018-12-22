package com.proper.enterprise.platform.file.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.SizeUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.file.api.File;

import java.util.Map;

public class FileVO extends BaseVO implements File {

    public FileVO(){}


    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件大小
     */
    @JsonIgnore
    private Long fileSize;

    /**
     * 文件大小
     */
    @JsonProperty(value = "fileSize")
    private String fileSizeUnit;

    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件路径
     */
    @JsonIgnore
    private String filePath;
    /**
     * 扩展字段
     */
    private String fileExtMsg;

    /**
     * 虚拟文件路径
     */
    @JsonProperty(value = "path")
    private String virPath;

    /**
     * 是否为文件夹
     */
    private Boolean isDir;

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Long getFileSize() {
        return fileSize;
    }

    @Override
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileSizeUnit() {
        return SizeUtil.parseSize(this.fileSize);
    }

    public void setFileSizeUnit(String fileSizeUnit) {
        this.fileSizeUnit = fileSizeUnit;
    }

    @Override
    public String getFileType() {
        return fileType;
    }

    @Override
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Map<String, String> getFileExtMsgMap() {
        return null;
    }

    public String getFileExtMsg() {
        return fileExtMsg;
    }

    public void setFileExtMsg(String fileExtMsg) {
        this.fileExtMsg = fileExtMsg;
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

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }

    public FileVO buildFileName(String fileName, int fileCount) {
        if (fileCount == 0) {
            return this;
        }
        if (isDir) {
            this.fileName = fileName + " (" + fileCount + ")";
        } else {
            this.fileName = fileName.substring(0, fileName.lastIndexOf("."))
                            + " ("
                            + fileCount
                            + ")"
                            + "."
                            + this.fileType;
        }
        return this;
    }
}
