package com.proper.enterprise.platform.oopsearch.sync;

import com.proper.enterprise.platform.oopsearch.api.sync.AbstractMongoDataSync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Service
public class H2MongoDataSyncSimulation extends AbstractMongoDataSync {
    private static final Logger LOGGER = LoggerFactory.getLogger(H2MongoDataSyncSimulation.class);

    public List<String> getPrimaryKeys(String tableName) {
        Properties properties = hikariConfig.getDataSourceProperties();
        String url = properties.get("url").toString();
        List<String> primaryKeys = new ArrayList<>();
        // 确保使用的是mysql数据源，避免数据源错误导致执行sql异常
        if (url.toLowerCase().contains("jdbc:h2:")) {
            String sql = "SELECT COLUMN_LIST FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE TABLE_NAME = '"
                + tableName.toUpperCase() + "' AND CONSTRAINT_TYPE = 'PRIMARY KEY'";
            List<Object> resultList = null;
            resultList = nativeRepository.executeQuery(sql);
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
