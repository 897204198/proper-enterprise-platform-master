package com.proper.enterprise.platform.oopsearch.sync;

import com.proper.enterprise.platform.oopsearch.api.enums.DataBaseType;
import com.proper.enterprise.platform.oopsearch.config.entity.SearchConfigEntity;
import com.proper.enterprise.platform.oopsearch.api.model.SearchColumnModel;
import com.proper.enterprise.platform.oopsearch.config.repository.SearchConfigRepository;
import com.proper.enterprise.platform.oopsearch.api.serivce.MongoDataSyncService;
import com.proper.enterprise.platform.oopsearch.config.conf.AbstractSearchConfigs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 同步mongo抽象类
 * 该类实现了MongoDataSyncService接口
 * 可以继承此类来实现mongo同步操作
 * 该类提供了一些通用的方法及实现
 * 子类可以仅对接口的fullSynchronization方法进行实现即可
 */
public abstract class AbstractMongoDataSync implements MongoDataSyncService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMongoDataSync.class);

    @Autowired
    SearchConfigRepository searchConfigRepository;

    /**
     * 获取主键方法
     * 各子类根据数据库的不同，进行具体实现
     *
     * @param tableName 表名
     * @return 主键集合
     */
    protected abstract List<String> getPrimaryKeys(String tableName);





    /**
     * 文档转换
     * 将SyncDocumentModel转换为SearchDocument
     *
     * @param searchConfigBeans 搜索配置bean集合
     * @return 表名字段关系集合对象
     */
    protected Map<String, List<SearchColumnModel>> getTableColumnMap(Map<String, Object> searchConfigBeans) {
        Map<String, List<SearchColumnModel>> tableColumnMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : searchConfigBeans.entrySet()) {
            AbstractSearchConfigs tempSearchConfig = (AbstractSearchConfigs) entry.getValue();
            Map<String, List<SearchColumnModel>> tempTableColumnMap = tempSearchConfig.getSearchTableColumnMap();
            for (Map.Entry<String, List<SearchColumnModel>> tempEntry : tempTableColumnMap.entrySet()) {
                String tempTableName = tempEntry.getKey();
                if (!tableColumnMap.containsKey(tempTableName)) {
                    tableColumnMap.put(tempTableName, tempEntry.getValue());
                } else {
                    List<SearchColumnModel> searchColumnList = tableColumnMap.get(tempTableName);
                    List<SearchColumnModel> tempSearchColumnList = tempEntry.getValue();
                    List<SearchColumnModel> addSearchColumnList = new ArrayList<>();
                    for (SearchColumnModel tempSearchColumn : tempSearchColumnList) {
                        boolean isRepeat = false;
                        String tempColumn = tempSearchColumn.getColumn();
                        for (SearchColumnModel searchColumn : searchColumnList) {
                            String column = searchColumn.getColumn();
                            if (tempColumn.equalsIgnoreCase(column)) {
                                isRepeat = true;
                                break;
                            }
                        }
                        if (!isRepeat) {
                            addSearchColumnList.add(tempSearchColumn);
                        }
                    }
                    if (addSearchColumnList.size() > 0) {
                        searchColumnList.addAll(addSearchColumnList);
                    }
                }
            }

        }
        return tableColumnMap;
    }

    /**
     * 文档转换
     * 将SyncDocumentModel转换为SearchDocument
     *
     * @param searchColumnList 搜索字段集合
     * @param column           字段名称
     * @return SearchColumnModel文档模型
     */
    protected SearchColumnModel getSearchColumnMongo(List<SearchColumnModel> searchColumnList, String column) {
        for (SearchColumnModel searchColumn : searchColumnList) {
            if (column.equalsIgnoreCase(searchColumn.getColumn())) {
                return searchColumn;
            }
        }
        return null;
    }

    public Collection<String> getConfigTables(DataBaseType dataBaseType) {
        Set<String> tabs = new HashSet<>();
        List<SearchConfigEntity> result = searchConfigRepository.findByDataBaseType(dataBaseType);
        for (SearchConfigEntity searchConfigEntity : result) {
            tabs.add(searchConfigEntity.getTableName());
        }
        return tabs;
    }
}
