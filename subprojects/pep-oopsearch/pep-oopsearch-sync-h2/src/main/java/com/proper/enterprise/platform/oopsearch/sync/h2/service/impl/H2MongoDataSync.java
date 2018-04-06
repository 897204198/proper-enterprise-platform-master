package com.proper.enterprise.platform.oopsearch.sync.h2.service.impl;

import com.proper.enterprise.platform.oopsearch.api.sync.AbstractMongoDataSync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Service(value = "h2MongoDataSync")
public class H2MongoDataSync extends AbstractMongoDataSync {
    private static final Logger LOGGER = LoggerFactory.getLogger(H2MongoDataSync.class);

    @Override
    public List<String> getPrimaryKeys(String tableName) {
        Properties properties = hikariConfig.getDataSourceProperties();
        String url = properties.get("url").toString();
        List<String> primaryKeys = new ArrayList<>();
        // 确保使用的是h2数据源，避免数据源错误导致执行sql异常
        if (url.toLowerCase().contains("jdbc:h2:")) {
            String sql = "SELECT COLUMN_LIST FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = '"
                + tableName.toUpperCase() + "' AND CONSTRAINT_TYPE = 'PRIMARY KEY'";
            List resultList = nativeRepository.executeQuery(sql);
            for (Object row:resultList) {
                if (row != null) {
                    String[] tempPrimaryKeys = row.toString().split(",");
                    primaryKeys = Arrays.asList(tempPrimaryKeys);
                }
            }
        }
        return primaryKeys;
    }
}
