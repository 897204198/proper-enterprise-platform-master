package com.proper.enterprise.platform.oopsearch.sync.mysql;

import com.proper.enterprise.platform.core.repository.NativeRepository;
import com.proper.enterprise.platform.oopsearch.api.serivce.MongoDataSyncService;
import com.proper.enterprise.platform.oopsearch.sync.mysql.service.impl.SyncCacheService;
import com.zaxxer.hikari.HikariConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Properties;


@Component
@Lazy(false)
public class MySQLSyncMongoManager implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLSyncMongoManager.class);

    @Autowired
    private MongoDataSyncService mongoDataSyncService;

    @Autowired
    private NativeRepository nativeRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SyncCacheService mongoSyncService;

    @Autowired
    HikariConfig hikariConfig;

    private String hostname;

    private int port;

    @Value("${binlog.schema}")
    private String schema;

    @Value("${binlog.username}")
    private String username;

    @Value("${binlog.password}")
    private String password;

    @Override
    public void afterPropertiesSet() {
        // 初始同步mysql数据到mongodb中
        mongoDataSyncService.fullSynchronization();
        // 获取数据库ip地址和端口
        Properties properties = hikariConfig.getDataSourceProperties();
        String url = properties.get("url").toString().toLowerCase();
        if (url.contains("mysql")) {
            String result = url.substring(url.indexOf("//"), url.lastIndexOf("/")).substring(2);
            String[] temp = result.split(":");
            hostname = temp[0];
            port = Integer.parseInt(temp[1]);
        } else {
            return;
        }
        // 启动binlog线程
        BinlogThread binlogThread = new BinlogThread(mongoDataSyncService, nativeRepository, mongoTemplate,
            mongoSyncService, hostname, port, schema, username, password);
        binlogThread.start();
    }
}
