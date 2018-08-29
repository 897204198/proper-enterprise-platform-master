package com.proper.enterprise.platform.file.api;

import com.proper.enterprise.platform.core.api.IBase;

import java.util.Map;

public interface File extends IBase {


    /**
     * 获取文件名
     *
     * @return 文件名
     */
    String getFileName();

    /**
     * 设置文件名
     *
     * @param fileName 文件名
     */
    void setFileName(String fileName);

    /**
     * 获取文件大小
     *
     * @return 文件大小
     */
    Long getFileSize();

    /**
     * 设置文件大小
     *
     * @param fileSize 文件大小
     */
    void setFileSize(Long fileSize);

    /**
     * 获取文件类型
     *
     * @return 文件类型
     */
    String getFileType();

    /**
     * 设置文件类型
     *
     * @param fileType 文件类型
     */
    void setFileType(String fileType);

    /**
     * 获取文件路径
     *
     * @return 文件路径
     */
    String getFilePath();

    /**
     * 设置文件路径
     *
     * @param filePath 文件路径
     */
    void setFilePath(String filePath);

    /**
     * 获取文件扩展信息
     *
     * @return 文件扩展信息
     */
    Map<String, String> getFileExtMsgMap();
}
