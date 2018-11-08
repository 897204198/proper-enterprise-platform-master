package com.proper.enterprise.platform.oopsearch.config.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.enums.EnableEnum;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.jpa.service.impl.AbstractJpaServiceSupport;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.oopsearch.api.enums.DataBaseType;
import com.proper.enterprise.platform.oopsearch.api.vo.SearchConfigVO;
import com.proper.enterprise.platform.oopsearch.config.OopSearchProperties;
import com.proper.enterprise.platform.oopsearch.config.conf.AbstractSearchConfigs;

import com.proper.enterprise.platform.oopsearch.config.entity.SearchConfigEntity;
import com.proper.enterprise.platform.oopsearch.config.repository.SearchConfigRepository;
import com.proper.enterprise.platform.oopsearch.config.service.SearchConfigService;
import com.proper.enterprise.platform.oopsearch.config.conf.ModuleSearchConfig;
import com.proper.enterprise.platform.core.i18n.I18NService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchConfigServiceImpl extends AbstractJpaServiceSupport<SearchConfigVO, SearchConfigRepository, String>
    implements SearchConfigService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchConfigServiceImpl.class);

    private SearchConfigRepository searchConfigRepository;

    private I18NService i18NService;

    private OopSearchProperties oopSearchProperties;

    @Autowired
    public SearchConfigServiceImpl(SearchConfigRepository searchConfigRepository,
                                   I18NService i18NService,
                                   OopSearchProperties oopSearchProperties) {
        this.searchConfigRepository = searchConfigRepository;
        this.i18NService = i18NService;
        this.oopSearchProperties = oopSearchProperties;
    }

    @Override
    public SearchConfigRepository getRepository() {
        return searchConfigRepository;
    }

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
            columnsSet.add(entity.getTableName() + ":" + entity.getSearchColumn().toLowerCase() + ":" + entity.getColumnType()
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

        int limit = oopSearchProperties.getSearchLimit();
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
        int limit = oopSearchProperties.getSearchLimit();
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
                tempList.get(1).add(entity.getTableName() + ":" + entity.getSearchColumn().toLowerCase() + ":" + entity.getColumnType()
                    + ":" + entity.getColumnDesc() + ":" + entity.getColumnAlias() + ":" + entity.getUrl());
            } else {
                // 创建新的searchconfig
                Set<String> tablesSet = new HashSet<>();
                Set<String> columnsSet = new HashSet<>();
                tablesSet.add(entity.getTableName());
                columnsSet.add(entity.getTableName() + ":" + entity.getSearchColumn().toLowerCase() + ":" + entity.getColumnType()
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

    @Override
    public DataTrunk<SearchConfigVO> findSearchConfigPagination(String name, String url, String tableName,
                                                                String searchColumn, String columnAlias, String columnDesc,
                                                                EnableEnum configEnable) {
        Page<SearchConfigEntity> result = searchConfigRepository.findSearchConfigPagination(wrap(name), wrap(url), wrap(tableName),
            wrap(searchColumn), wrap(columnAlias), wrap(columnDesc),
            configEnable == null ? null : EnableEnum.ENABLE.equals(configEnable), this.getPageRequest());
        List<SearchConfigVO> resVOs = new ArrayList<>();
        for (SearchConfigEntity res : result.getContent()) {
            SearchConfigVO searchConfigVO = new SearchConfigVO();
            BeanUtils.copyProperties(res, searchConfigVO);
            resVOs.add(searchConfigVO);
        }
        Page<SearchConfigVO> pageVO = new PageImpl<>(resVOs, this.getPageRequest(), result.getTotalElements());
        return new DataTrunk<>(pageVO);
    }

    private String wrap(String str) {
        return "%" + (StringUtil.isBlank(str) ? "" : str) + "%";
    }

    @Override
    public SearchConfigVO updateSearchConfig(String id, SearchConfigVO searchConfigVO) {
        try {
            searchConfigVO.setId(id);
            checkParam(searchConfigVO);
            SearchConfigEntity searchConfigEntity = new SearchConfigEntity();
            BeanUtils.copyProperties(searchConfigVO, searchConfigEntity);
            BeanUtils.copyProperties(searchConfigRepository.updateForSelective(searchConfigEntity), searchConfigVO);
            return searchConfigVO;
        } catch (Exception e) {
            throw new ErrMsgException("Failed to save config");
        }
    }

    /**
     * 新增OopSearch配置信息
     *
     * @param searchConfigVO 对应的vo
     */
    @Override
    public SearchConfigVO add(SearchConfigVO searchConfigVO) {
        if (null != searchConfigVO) {
            checkParam(searchConfigVO);
            SearchConfigEntity searchConfigEntity = new SearchConfigEntity();
            BeanUtils.copyProperties(searchConfigVO, searchConfigEntity);
            BeanUtils.copyProperties(searchConfigRepository.save(searchConfigEntity), searchConfigVO);
            return searchConfigVO;
        }
        throw new ErrMsgException("searchConfigEntity is null");

    }

    private void checkParam(SearchConfigVO searchConfigVO) {
        if (StringUtil.isBlank(searchConfigVO.getColumnDesc())
            || StringUtil.isBlank(searchConfigVO.getColumnAlias())
            || StringUtil.isBlank(searchConfigVO.getSearchColumn())
            || StringUtil.isBlank(searchConfigVO.getTableName())
            || StringUtil.isBlank(searchConfigVO.getModuleName())
            || StringUtil.isBlank(searchConfigVO.getUrl())) {
            throw new ErrMsgException("oopsearch config Param is null or ' ' ");
        }
    }

    /**
     * 删除OopSearch配置信息
     *
     * @param ids ID
     * @return true or false
     */
    @Override
    public boolean deleteByIds(String ids) {
        if (StringUtil.isNotBlank(ids)) {
            String[] idArr = ids.split(",");
            List<String> idList = new ArrayList<>();
            Collections.addAll(idList, idArr);
            Collection<SearchConfigEntity> all = searchConfigRepository.findAllById(idList);
            searchConfigRepository.deleteAll(all);
            return all.size() > 0;
        }
        return false;
    }
}
