package com.proper.enterprise.platform.oopsearch.sync.mysql;

import com.proper.enterprise.platform.core.jpa.repository.NativeRepository;
import com.proper.enterprise.platform.oopsearch.api.serivce.MongoDataSyncService;
import com.proper.enterprise.platform.oopsearch.api.serivce.SearchConfigService;
import com.proper.enterprise.platform.oopsearch.sync.mysql.service.impl.SyncCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;


@Component
@Lazy(false)
public class MySQLSyncMongoManager implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLSyncMongoManager.class);

    @Autowired
    private MongoDataSyncService mongoDataSyncService;

    @Autowired
    private SearchConfigService searchConfigService;

    @Autowired
    private NativeRepository nativeRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SyncCacheService mongoSyncService;

    // 数据源config
    @Value("database.url")
    private String databaseUrl;
    @Value("database.username")
    private String username;
    @Value("database.password")
    private String password;

    @Override
    public void afterPropertiesSet() {
        // 初始同步mysql数据到mongodb中
        mongoDataSyncService.fullSynchronization();
        // 获取数据库ip地址和端口
        Object urlObj = databaseUrl;
        Object usernameObj = username;
        Object passwordObj = password;
        if (urlObj != null && usernameObj != null && passwordObj != null) {
            String url = urlObj.toString().toLowerCase();
            if (url.contains("mysql")) {
                String[] temp = (url.substring(url.indexOf("//"), url.lastIndexOf("/")).substring(2)).split(":");
                // 启动binlog线程
                BinlogThread binlogThread = new BinlogThread(mongoDataSyncService, searchConfigService, nativeRepository, mongoTemplate,
                    mongoSyncService, temp[0], Integer.parseInt(temp[1]), url.substring(url.lastIndexOf("/") + 1),
                    usernameObj.toString(), passwordObj.toString());
                binlogThread.start();
            }
        }
    }
}
