package com.proper.enterprise.platform.dfs.local;

import com.proper.enterprise.platform.dfs.api.common.AbstractDFSServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * Local file system implementation of AbstractDFSServiceSupport
 *
 */
@Service
public class DFSLocalImpl extends AbstractDFSServiceSupport {

    @Value("${dfs.local.buffersize}")
    private int bufferSize;

    private static final Logger LOGGER = LoggerFactory.getLogger(DFSLocalImpl.class);

    @Override
    protected void doSaveFile(InputStream is, String filePath) throws IOException {
        File file = new File(filePath);
        boolean mkdirs = file.getParentFile().mkdirs();
        LOGGER.debug("The result of make dirs for {} is {}.", filePath, mkdirs);
        FileOutputStream fos = new FileOutputStream(file);
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        try {
            byte[] buffer = new byte[bufferSize];
            int len;
            while ((len = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
                bos.flush();
            }
        } catch (IOException e) {
            throw e;
        } finally {
            fos.close();
            bos.close();
            bis.close();
        }
    }

    @Override
    public boolean exists(String filePath) {
        return new File(filePath).exists();
    }

    @Override
    protected InputStream doGetFile(String filePath) throws IOException {
        return new FileInputStream(filePath);
    }

    @Override
    protected void doDeleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            boolean result = file.delete();
            LOGGER.debug("Delete file {} return result: {}", filePath, result);
        } else {
            LOGGER.debug("File {} not exists.", filePath);
        }
    }

}
