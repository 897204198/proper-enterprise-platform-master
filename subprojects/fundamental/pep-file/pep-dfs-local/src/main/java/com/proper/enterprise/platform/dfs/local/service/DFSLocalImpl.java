package com.proper.enterprise.platform.dfs.local.service;

import com.proper.enterprise.platform.dfs.common.service.AbstractDFSService;
import com.proper.enterprise.platform.dfs.local.DFSLocalProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * Local file system implementation of AbstractDFSService
 */
@Service
public class DFSLocalImpl extends AbstractDFSService {

    private DFSLocalProperties localProperties;

    @Autowired
    public DFSLocalImpl(DFSLocalProperties localProperties) {
        this.localProperties = localProperties;
    }

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
            byte[] buffer = new byte[localProperties.getBufferSize()];
            int len;
            while ((len = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
                bos.flush();
            }
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

    @Override
    public void createDir(String dirPath) {
        File file = new File(dirPath);
        boolean mkdirs = file.mkdirs();
        LOGGER.debug("The result of make dirs for {} is {}.", dirPath, mkdirs);
    }

    @Override
    public void updateDir(String oldDirPath, String newDirPath) {
        File file = new File(oldDirPath);
        if (file.exists()) {
            boolean result = file.renameTo(new File(newDirPath));
            LOGGER.debug("rename file {} return result: {}", newDirPath, result);
        } else {
            LOGGER.debug("File {} not exists.", oldDirPath);
        }
    }

    @Override
    public void deleteDir(String dirPath) {
        File file = new File(dirPath);
        if (file.exists()) {
            boolean result = file.delete();
            LOGGER.debug("delete file {} return result: {}", dirPath, result);
        } else {
            LOGGER.debug("File {} not exists.", dirPath);
        }
    }

}
