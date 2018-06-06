package com.proper.enterprise.platform.dfs.api.common;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.dfs.api.service.DFSService;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Implementations of {@link DFSService} should extends this abstract class and override all abstract methods.
 *
 */
public abstract class DFSServiceSupport implements DFSService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DFSServiceSupport.class);

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
            throw new ErrMsgException(I18NUtil.getMessage("dfs.upload.valid.path.duplicated"));
        }
        LOGGER.debug("Prepare to save file:{} ...", filePath);
        doSaveFile(is, filePath);
        LOGGER.info("Save file:{} done.", filePath);
        is.close();
    }

    protected abstract void doSaveFile(InputStream is, String filePath) throws IOException;

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

    protected abstract void doDeleteFile(String filePath);

}
