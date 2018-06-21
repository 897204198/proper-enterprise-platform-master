package com.proper.enterprise.platform.oopsearch.sync.h2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("h2SyncJobService")
public class H2SyncJobService {

    @Autowired
    private H2MongoDataSync h2MongoDataSync;

    public void fullSyncMongo() {
        h2MongoDataSync.fullSynchronization();
    }

}
