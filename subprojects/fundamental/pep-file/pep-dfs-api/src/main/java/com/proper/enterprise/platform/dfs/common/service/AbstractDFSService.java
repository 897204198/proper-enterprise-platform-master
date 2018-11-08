package com.proper.enterprise.platform.dfs.common.service;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.dfs.api.service.DFSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Implementations of {@link DFSService} should extends this abstract class and override all abstract methods.
 *
 */
public abstract class AbstractDFSService implements DFSService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDFSService.class);

    @Override
    public void saveFile(InputStream is, String filePath) throws IOException {
        saveFile(is, filePath, false);
    }

    @Override
    public void saveFile(InputStream is, String filePath, boolean overwrite) throws IOException {
        if (null == is) {
            throw new IOException("inputStream is null");
        }
        if (!overwrite && exists(filePath)) {
            throw new ErrMsgException("A file of the same name already exists in the same path: " + filePath);
        }
        LOGGER.debug("Prepare to save file:{} ...", filePath);
        doSaveFile(is, filePath);
        LOGGER.info("Save file:{} done.", filePath);
        is.close();
    }

    /**
     * 保存文件
     * @param is 文件流程
     * @param filePath 文件路径
     * @throws IOException io异常
     */
    protected abstract void doSaveFile(InputStream is, String filePath) throws IOException;

    /**
     * 文件是否存在
     * @param filePath 文件
     * @return 成功true 失败false
     */
    public abstract boolean exists(String filePath);

    @Override
    public InputStream getFile(String filePath) throws IOException {
        LOGGER.debug("Prepare to get file:{} ...", filePath);
        InputStream is = doGetFile(filePath);
        if (is == null) {
            return null;
        } else {
            LOGGER.info("Get file:{} and return, remember to close InputStream after use.", filePath);
            return is;
        }
    }

    /**
     * 获取文件流
     * @param filePath 文件路径
     * @return 文件流程
     * @throws IOException io异常
     */
    protected abstract InputStream doGetFile(String filePath) throws IOException;

    @Override
    public boolean deleteFile(String filePath) throws IOException {
        LOGGER.debug("Prepare to delete file:{} ...", filePath);
        if (!exists(filePath)) {
            return false;
        }
        doDeleteFile(filePath);
        LOGGER.info("Delete file:{} done.", filePath);
        return true;
    }

    /**
     * 删除文件
     * @param filePath 文件路径
     * @throws IOException IO 异常
     */
    protected abstract void doDeleteFile(String filePath) throws IOException;

}
