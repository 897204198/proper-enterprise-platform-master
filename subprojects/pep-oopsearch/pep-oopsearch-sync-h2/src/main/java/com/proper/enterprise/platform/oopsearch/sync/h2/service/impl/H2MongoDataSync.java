package com.proper.enterprise.platform.oopsearch.sync.h2.service.impl;

import com.proper.enterprise.platform.oopsearch.sync.AbstractMongoDataSyncORM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service(value = "h2MongoDataSync")
public class H2MongoDataSync extends AbstractMongoDataSyncORM {
    private static final Logger LOGGER = LoggerFactory.getLogger(H2MongoDataSync.class);

    @Value("database.url")
    private String databaseUrl;

    @Override
    public List<String> getPrimaryKeys(String tableName) {
        String url = databaseUrl;
        List<String> primaryKeys = new ArrayList<>();
        String jdbcType = "jdbc:h2:";
        // 确保使用的是h2数据源，避免数据源错误导致执行sql异常
        if (url.toLowerCase().contains(jdbcType)) {
            String sql = "SELECT COLUMN_LIST FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = '"
                + tableName.toUpperCase() + "' AND CONSTRAINT_TYPE = 'PRIMARY KEY'";
            List resultList = nativeRepository.executeQuery(sql);
            for (Object row : resultList) {
                if (row != null) {
                    String[] tempPrimaryKeys = row.toString().split(",");
                    primaryKeys = Arrays.asList(tempPrimaryKeys);
                }
            }
        }
        return primaryKeys;
    }
}
