package com.proper.enterprise.platform.oopsearch.sync.mysql.service.impl;

import com.proper.enterprise.platform.oopsearch.api.sync.AbstractMongoDataSync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service(value = "mySQLMongoDataSync")
public class MySQLMongoDataSync extends AbstractMongoDataSync {
    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLMongoDataSync.class);

    @Override
    public List<String> getPrimaryKeys(String tableName) {
        Properties properties = hikariConfig.getDataSourceProperties();
        String url = properties.get("url").toString();
        List<String> primaryKeys = new ArrayList<>();
        // 确保使用的是mysql数据源，避免数据源错误导致执行sql异常
        if (url.toLowerCase().contains("jdbc:mysql:")) {
            String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_NAME = '"
                + tableName + "' AND CONSTRAINT_NAME = 'PRIMARY' AND CONSTRAINT_SCHEMA = '"
                + url.substring(url.lastIndexOf("/") + 1) + "'";
            List<Object> resultList = nativeRepository.executeQuery(sql);
            for (Object row:resultList) {
                if (row != null) {
                    primaryKeys.add(row.toString());
                }
            }
        }
        return primaryKeys;
    }
}
