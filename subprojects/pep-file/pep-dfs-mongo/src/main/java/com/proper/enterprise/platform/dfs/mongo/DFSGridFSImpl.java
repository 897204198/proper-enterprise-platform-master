/*
 * @author Hinex
 * @date 2015-4-1 16:55:51
 */

package com.proper.enterprise.platform.dfs.mongo;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.dfs.api.common.AbstractDFSServiceSupport;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.InputStream;


/**
 * GridFS implementation of AbstractDFSServiceSupport
 */
@Service
public class DFSGridFSImpl extends AbstractDFSServiceSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(DFSGridFSImpl.class);

    @Value("${dfs.upload.rootpath}")
    private String dbname;

    @Value("${dfs.mongo.bucket}")
    private String bucket;

    @Autowired
    private MongoClient mongoClient;

    private GridFS gridFS;

    @PostConstruct
    public void postConstruct() {

        LOGGER.debug("Prepare to get database:{} ...", dbname);
        DB db = mongoClient.getDB(dbname);

        LOGGER.debug("Prepare to create GridFS instance with bucket:{} ...", bucket);
        gridFS = new GridFS(db, bucket);
        LOGGER.info("Create GridFS instance.");
    }


    /**
     * mongo需要手动关闭
     */
    @PreDestroy
    public void destroy() {
        LOGGER.debug("Prepare to close MongoDB client ...");
        mongoClient.close();
        LOGGER.debug("MongoDB client closed.");
    }

    @Override
    protected void doSaveFile(InputStream is, String filePath) {
        if (exists(filePath)) {
            try {
                deleteFile(filePath);
            } catch (IOException e) {
                throw new ErrMsgException(I18NUtil.getMessage("dfs.upload.valid.path.duplicated"));
            }
        }
        GridFSInputFile file = gridFS.createFile(is, filePath, true);
        LOGGER.debug("Create GridFS input file:{}", file);
        file.save();
        LOGGER.debug("After GridFS input file saving:{}", file);
    }

    @Override
    protected InputStream doGetFile(String filePath) {
        GridFSDBFile file = gridFS.findOne(filePath);
        LOGGER.debug("Get GridFS db file:{}", file);

        return file == null ? null : file.getInputStream();
    }

    @Override
    protected void doDeleteFile(String filePath) {
        gridFS.remove(filePath);
    }

    @Override
    public void createDir(String dirPath) {
    }

    @Override
    public void updateDir(String oldDirPath, String newDirPath) {
    }

    @Override
    public void deleteDir(String dirPath) {
    }

    @Override
    public boolean exists(String filePath) {
        return gridFS.findOne(filePath) != null;
    }

}
