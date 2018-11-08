package com.proper.enterprise.platform.oopsearch.sync.mysql;

import com.proper.enterprise.platform.core.jpa.repository.NativeRepository;
import com.proper.enterprise.platform.oopsearch.config.service.SearchConfigService;
import com.proper.enterprise.platform.oopsearch.service.impl.SyncCacheService;
import com.proper.enterprise.platform.oopsearch.sync.mysql.executor.BinlogExecutor;
import com.proper.enterprise.platform.oopsearch.sync.mysql.service.impl.MySQLMongoDataSync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@DependsOn(value = "mySQLFullSyncMongoManager")
@Lazy(false)
public class BinlogMonitor {

    @Autowired
    private MySQLMongoDataSync mySQLMongoDataSync;

    @Autowired
    private SearchConfigService searchConfigService;

    @Autowired
    private NativeRepository nativeRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SyncCacheService mongoSyncService;

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Scheduled(fixedDelay = 1000L)
    public void syncMysqlBinlog() {
        String url = databaseUrl.toLowerCase();
        String mysqlUrlKey = "mysql";
        if (!url.contains(mysqlUrlKey)) {
            return;
        }
        String[] temp = (url.substring(url.indexOf("//"), url.lastIndexOf("/")).substring(2)).split(":");
        new BinlogExecutor(mySQLMongoDataSync, searchConfigService, nativeRepository, mongoTemplate,
            mongoSyncService, temp[0], Integer.parseInt(temp[1]), url.substring(url.lastIndexOf("/") + 1),
            username, password).executor();
    }
}
