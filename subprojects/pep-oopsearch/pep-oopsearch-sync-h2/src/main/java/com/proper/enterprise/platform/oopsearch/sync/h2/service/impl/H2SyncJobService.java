package com.proper.enterprise.platform.oopsearch.sync.h2.service.impl;

import com.proper.enterprise.platform.oopsearch.api.serivce.MongoDataSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("h2SyncJobService")
public class H2SyncJobService {

    @Autowired
    private MongoDataSyncService mongoDataSyncService;

    public void fullSyncMongo() {
        mongoDataSyncService.fullSynchronization();
    }

}
