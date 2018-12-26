package com.proper.enterprise.platform.oopsearch.sync.mysql;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.proper.enterprise.platform.core.jpa.repository.NativeRepository;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.oopsearch.config.service.SearchConfigService;
import com.proper.enterprise.platform.oopsearch.service.impl.SyncCacheService;
import com.proper.enterprise.platform.oopsearch.sync.mysql.executor.BinlogExecutor;
import com.proper.enterprise.platform.oopsearch.sync.mysql.service.impl.MySQLMongoDataSync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@DependsOn(value = "mySQLFullSyncMongoManager")
@Lazy(false)
public class BinlogMonitor {

    private MySQLMongoDataSync mySQLMongoDataSync;

    private SearchConfigService searchConfigService;

    private NativeRepository nativeRepository;

    private MongoTemplate mongoTemplate;

    private SyncCacheService mongoSyncService;

    private DataSourceProperties dataSourceProperties;

    private BinaryLogClient client;

    private String schema;

    @Autowired
    public BinlogMonitor(MySQLMongoDataSync mySQLMongoDataSync,
                         SearchConfigService searchConfigService,
                         NativeRepository nativeRepository,
                         MongoTemplate mongoTemplate,
                         SyncCacheService mongoSyncService,
                         DataSourceProperties dataSourceProperties) {
        this.mySQLMongoDataSync = mySQLMongoDataSync;
        this.searchConfigService = searchConfigService;
        this.nativeRepository = nativeRepository;
        this.mongoTemplate = mongoTemplate;
        this.mongoSyncService = mongoSyncService;
        this.dataSourceProperties = dataSourceProperties;
        init();
    }

    public void init() {
        String url = dataSourceProperties.getUrl();
        String mysqlUrlKey = "mysql";
        if (StringUtil.isEmpty(url) || !url.toLowerCase().contains(mysqlUrlKey)) {
            client = null;
        } else {
            String lowerCaseUrl = url.toLowerCase();
            String[] temp = (lowerCaseUrl.substring(url.indexOf("//"), lowerCaseUrl.lastIndexOf("/")).substring(2)).split(":");
            schema = lowerCaseUrl.substring(lowerCaseUrl.lastIndexOf("/") + 1);
            client = new BinaryLogClient(temp[0],
                                         Integer.parseInt(temp[1]),
                                         dataSourceProperties.getUsername(),
                                         dataSourceProperties.getPassword());
        }
    }

    @Scheduled(fixedDelay = 30000L)
    public void syncMysqlBinlog() {
        if (client == null) {
            return;
        }
        new BinlogExecutor(mySQLMongoDataSync, searchConfigService, nativeRepository, mongoTemplate,
            mongoSyncService, schema).executor(client);
    }
}
