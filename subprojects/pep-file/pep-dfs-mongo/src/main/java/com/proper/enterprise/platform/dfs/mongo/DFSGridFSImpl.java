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
import com.proper.enterprise.platform.dfs.api.common.DFSServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.InputStream;


/**
 * GridFS implementation of DFSServiceSupport
 *
 */
@Service
public class DFSGridFSImpl extends DFSServiceSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(DFSGridFSImpl.class);

    @Value("${mongodb.host}")
    private String host;

    @Value("${mongodb.port}")
    private int port;

    @Value("${file.upload.rootpath}")
    private String dbname;

    @Value("${dfs.mongo.bucket}")
    private String bucket;

    private MongoClient mongoClient;

    private GridFS gridFS;

    @PostConstruct
    public void postConstruct() {
        LOGGER.debug("Prepare to create MongoDB client with host:{}, port:{} ...", host, port);
        mongoClient = new MongoClient(host, port);
        LOGGER.info("MongoDB client created.");

        LOGGER.debug("Prepare to get database:{} ...", dbname);
        DB db = mongoClient.getDB(dbname);

        LOGGER.debug("Prepare to create GridFS instance with bucket:{} ...", bucket);
        gridFS = new GridFS(db, bucket);
        LOGGER.info("Create GridFS instance.");
    }

    // TODO: 2018/6/5 mongo需要手动关闭
    @PreDestroy
    public void destroy() {
        LOGGER.debug("Prepare to close MongoDB client ...");
        mongoClient.close();
        LOGGER.debug("MongoDB client closed.");
    }

    @Override
    protected void doSaveFile(InputStream is, String filePath) {
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
    public boolean exists(String filePath) {
        return gridFS.findOne(filePath) != null;
    }

}
