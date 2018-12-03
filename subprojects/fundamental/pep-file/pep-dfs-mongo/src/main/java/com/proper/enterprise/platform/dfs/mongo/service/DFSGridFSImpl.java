package com.proper.enterprise.platform.dfs.mongo.service;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.proper.enterprise.platform.dfs.DFSProperties;
import com.proper.enterprise.platform.dfs.common.service.AbstractDFSService;
import com.proper.enterprise.platform.dfs.mongo.DFSMongoProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.InputStream;


/**
 * GridFS implementation of AbstractDFSService
 */
@Service
public class DFSGridFSImpl extends AbstractDFSService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DFSGridFSImpl.class);

    private DFSProperties dfsProperties;

    private DFSMongoProperties mongoProperties;

    private MongoClient mongoClient;

    @Autowired
    public DFSGridFSImpl(DFSProperties dfsProperties, DFSMongoProperties mongoProperties, MongoClient mongoClient) {
        this.dfsProperties = dfsProperties;
        this.mongoProperties = mongoProperties;
        this.mongoClient = mongoClient;
    }

    private GridFS gridFS;

    @PostConstruct
    public void postConstruct() {
        LOGGER.debug("Prepare to get database:{} ...", dfsProperties.getRootPath());
        DB db = mongoClient.getDB(dfsProperties.getRootPath());

        LOGGER.debug("Prepare to create GridFS instance with bucket:{} ...", mongoProperties.getBucket());
        gridFS = new GridFS(db, mongoProperties.getBucket());
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
                LOGGER.error("Error occurs when delete {}", filePath, e);
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
