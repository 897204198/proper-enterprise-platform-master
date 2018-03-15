package com.proper.enterprise.platform.oopsearch.sync.h2.service.impl;

import com.proper.enterprise.platform.oopsearch.api.serivce.MongoDataSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("h2SyncJobService")
public class H2SyncJobService {
    private static final Logger LOGGER = LoggerFactory.getLogger(H2SyncJobService.class);

    @Autowired
    private MongoDataSyncService mongoDataSyncService;


    public void fullSyncMongo() {
        mongoDataSyncService.fullSynchronization();
    }
}
