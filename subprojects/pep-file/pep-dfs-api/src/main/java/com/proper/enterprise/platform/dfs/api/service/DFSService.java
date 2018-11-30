/*
 * @author Hinex
 * @date 2015-4-1 16:25:14
 */

package com.proper.enterprise.platform.dfs.api.service;

import java.io.IOException;
import java.io.InputStream;

/**
 * Proper Distributed File System Interface
 * <p>
 * Get change logs from README.md
 *
 */
public interface DFSService {

    /**
     * 保存文件
     *
     * @param is       文件流程
     * @param filePath 文件路径
     * @throws IOException io异常
     */
    void saveFile(InputStream is, String filePath) throws IOException;

    /**
     * 保存文件
     *
     * @param is        文件流程
     * @param filePath  文件路径
     * @param overwrite 是否覆盖
     * @throws IOException io异常
     */
    void saveFile(InputStream is, String filePath, boolean overwrite) throws IOException;

    /**
     * 根据文件路径获得文件流
     *
     * @param filePath 文件路径
     * @return 文件流
     * @throws IOException io异常
     */
    InputStream getFile(String filePath) throws IOException;

    /**
     * 根据文件路径删除文件
     *
     * @param filePath 文件路径
     * @return  是否删除成功
     * @throws IOException  io异常
     */
    boolean deleteFile(String filePath) throws IOException;

    /**
     * 创建文件夹
     *
     * @param dirPath 文件夹路径
     */
    void createDir(String dirPath);

    /**
     * 文件夹重命名或移动操作
     *
     * @param oldDirPath 文件夹旧路径
     * @param newDirPath 文件夹新路径
     */
    void updateDir(String oldDirPath, String newDirPath);

    /**
     * 删除文件夹
     *
     * @param dirPath 文件夹路径
     */
    void deleteDir(String dirPath);

}
