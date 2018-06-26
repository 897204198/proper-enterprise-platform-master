package com.proper.enterprise.platform.oopsearch.config.service.impl;

import com.proper.enterprise.platform.core.utils.ConfCenter;
import com.proper.enterprise.platform.oopsearch.api.enums.DataBaseType;
import com.proper.enterprise.platform.oopsearch.config.conf.AbstractSearchConfigs;

import com.proper.enterprise.platform.oopsearch.config.entity.SearchConfigEntity;
import com.proper.enterprise.platform.oopsearch.config.repository.SearchConfigRepository;
import com.proper.enterprise.platform.oopsearch.config.service.SearchConfigService;
import com.proper.enterprise.platform.oopsearch.config.conf.ModuleSearchConfig;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchConfigServiceImpl implements SearchConfigService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchConfigServiceImpl.class);

    @Autowired
    SearchConfigRepository searchConfigRepository;

    @Autowired
    I18NService i18NService;

    @Override
    public AbstractSearchConfigs getSearchConfig(String moduleName) {
        List<SearchConfigEntity> result = searchConfigRepository.findByModuleName(moduleName);
        return getSearchConfig(result);
    }

    @Override
    public AbstractSearchConfigs getSearchConfig(String moduleName, DataBaseType dataBaseType) {
        List<SearchConfigEntity> result = searchConfigRepository.findByModuleNameAndDataBaseType(moduleName, dataBaseType);
        return getSearchConfig(result);
    }

    private AbstractSearchConfigs getSearchConfig(List<SearchConfigEntity> configs) {
        if (configs == null || configs.size() == 0) {
            LOGGER.error("no search config result , maybe the moduleName is worry");
            return null;
        }

        Set<String> tablesSet = new HashSet<>();
        Set<String> columnsSet = new HashSet<>();
        for (SearchConfigEntity entity : configs) {
            tablesSet.add(entity.getTableName());
            columnsSet.add(entity.getTableName() + ":" + entity.getSearchColumn() + ":" + entity.getColumnType()
                + ":" + entity.getColumnDesc() + ":" + entity.getColumnAlias() + ":" + entity.getUrl());
        }

        StringBuffer searchTables = new StringBuffer();
        for (String tableName : tablesSet) {
            searchTables.append(tableName + ",");
        }
        searchTables.deleteCharAt(searchTables.length() - 1);

        StringBuffer searchColumns = new StringBuffer();
        for (String searchColumn : columnsSet) {
            searchColumns.append(searchColumn + ",");
        }
        searchColumns.deleteCharAt(searchColumns.length() - 1);

        int limit = Integer.parseInt(ConfCenter.get("search.limit"));
        String extendByYear = i18NService.getMessage("search.extendDateYear");
        String extendByMonth = i18NService.getMessage("search.extendDateMonth");
        String extendByDay = i18NService.getMessage("search.extendDateDay");
        AbstractSearchConfigs searchConfigs = new ModuleSearchConfig(searchTables.toString(), searchColumns.toString(),
            limit, extendByYear, extendByMonth, extendByDay);
        return searchConfigs;
    }

    @Override
    /**
     * 获取配置信息类
     * 将数据库中的配置信息，转换为config配置类。
     *
     * @return config集合
     * */
    public Map<String, Object> getSearchConfigs(DataBaseType dataBaseType) {
        List<SearchConfigEntity> result = searchConfigRepository.findByDataBaseType(dataBaseType);
        if (result == null || result.size() == 0) {
            LOGGER.error("no search config result , db is empty");
            return null;
        }
        int limit = Integer.parseInt(ConfCenter.get("search.limit"));
        String extendByYear = i18NService.getMessage("search.extendDateYear");
        String extendByMonth = i18NService.getMessage("search.extendDateMonth");
        String extendByDay = i18NService.getMessage("search.extendDateDay");

        Map<String, List<Set<String>>> searchConfigsMap = new HashMap<>(16);
        for (SearchConfigEntity entity : result) {
            String moduleName = entity.getModuleName();
            if (searchConfigsMap.containsKey(moduleName)) {
                // 添加到已经存在的searchconfig
                List<Set<String>> tempList = searchConfigsMap.get(moduleName);
                // table set
                tempList.get(0).add(entity.getTableName());
                // search column set
                tempList.get(1).add(entity.getTableName() + ":" + entity.getSearchColumn() + ":" + entity.getColumnType()
                    + ":" + entity.getColumnDesc() + ":" + entity.getColumnAlias() + ":" + entity.getUrl());
            } else {
                // 创建新的searchconfig
                Set<String> tablesSet = new HashSet<>();
                Set<String> columnsSet = new HashSet<>();
                tablesSet.add(entity.getTableName());
                columnsSet.add(entity.getTableName() + ":" + entity.getSearchColumn() + ":" + entity.getColumnType()
                    + ":" + entity.getColumnDesc() + ":" + entity.getColumnAlias() + ":" + entity.getUrl());
                List<Set<String>> tempList = new ArrayList<>();
                tempList.add(tablesSet);
                tempList.add(columnsSet);

                searchConfigsMap.put(moduleName, tempList);
            }
        }

        Map<String, Object> searchConfigs = new HashMap<>(16);

        for (Map.Entry<String, List<Set<String>>> entry : searchConfigsMap.entrySet()) {
            List<Set<String>> tempList = entry.getValue();
            Set<String> tablesSet = tempList.get(0);
            StringBuffer searchTables = new StringBuffer();
            for (String tableName : tablesSet) {
                searchTables.append(tableName + ",");
            }
            searchTables.deleteCharAt(searchTables.length() - 1);

            Set<String> columnsSet = tempList.get(1);
            StringBuffer searchColumns = new StringBuffer();
            for (String searchColumn : columnsSet) {
                searchColumns.append(searchColumn + ",");
            }
            searchColumns.deleteCharAt(searchColumns.length() - 1);

            AbstractSearchConfigs searchConfig = new ModuleSearchConfig(searchTables.toString(), searchColumns.toString(),
                limit, extendByYear, extendByMonth, extendByDay);
            String moduleName = entry.getKey();
            searchConfigs.put(moduleName, searchConfig);
        }

        return searchConfigs;
    }

    @Override
    public String getURL(String moduleName) {
        List<SearchConfigEntity> result = searchConfigRepository.findByModuleName(moduleName);
        if (result == null || result.size() == 0) {
            LOGGER.error("CAN NOT FIND SEARCH CONFIG BY MODULENAME");
            return null;
        }
        return result.get(0).getUrl();
    }
}
