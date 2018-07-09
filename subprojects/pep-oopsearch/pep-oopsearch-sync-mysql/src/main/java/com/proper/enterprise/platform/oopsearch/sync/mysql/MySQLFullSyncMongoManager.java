package com.proper.enterprise.platform.oopsearch.sync.mysql;

import com.proper.enterprise.platform.oopsearch.sync.mysql.service.impl.MySQLMongoDataSync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


@Component(value = "mySQLFullSyncMongoManager")
@Lazy(false)
@DependsOn("liquibase")
public class MySQLFullSyncMongoManager implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLFullSyncMongoManager.class);

    @Autowired
    private MySQLMongoDataSync mySQLMongoDataSync;

    @Autowired
    private BinlogMonitor binlogMonitor;

    @Override
    public void afterPropertiesSet() {
        // 初始同步mysql数据到mongodb中
        mySQLMongoDataSync.fullSynchronization();
    }
}
