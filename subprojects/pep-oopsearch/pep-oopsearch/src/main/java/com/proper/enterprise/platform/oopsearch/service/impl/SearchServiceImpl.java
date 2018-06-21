package com.proper.enterprise.platform.oopsearch.service.impl;

import com.proper.enterprise.platform.oopsearch.document.SearchDocument;
import com.proper.enterprise.platform.oopsearch.api.model.SearchColumnModel;
import com.proper.enterprise.platform.oopsearch.service.SearchService;
import com.proper.enterprise.platform.oopsearch.repository.SearchMongoRepository;
import com.proper.enterprise.platform.oopsearch.config.conf.AbstractSearchConfigs;
import com.proper.enterprise.platform.oopsearch.config.service.SearchConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class SearchServiceImpl implements SearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Autowired
    SearchMongoRepository searchMongoRepository;

    @Autowired
    SearchConfigService searchConfigService;

    @Override
    public List<SearchDocument> getSearchInfo(String inputStr, String moduleName) {
        AbstractSearchConfigs searchConfigs = searchConfigService.getSearchConfig(moduleName);
        if (searchConfigs == null) {
            LOGGER.error("get search config class fail");
            return null;
        }
        List<SearchDocument> demoUserDocumentList;
        List<SearchDocument> resultList = new ArrayList<>();
        try {
            filterDate(resultList, inputStr, searchConfigs);
            PageRequest pageRequest = new PageRequest(0, searchConfigs.getLimit() - resultList.size());
            demoUserDocumentList = searchMongoRepository.findByTabInAndConLike(searchConfigs.getTableNameList(), inputStr, pageRequest);
            resultList.addAll(demoUserDocumentList);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return resultList;
    }

    public void filterDate(List<SearchDocument> resultList, String input, AbstractSearchConfigs searchConfigs) {
        List<SearchColumnModel> searchColumnList = searchConfigs.getSearchColumnListByType("date");
        Pattern patternYear = Pattern.compile("^\\d{4}$");
        Pattern patternMonth = Pattern.compile("^\\d{4}[-|\\/|\\.]\\d{1,2}$");
        Pattern patternDay = Pattern.compile("^\\d{4}[-|\\/|\\.]\\d{1,2}[-|\\/|\\.]\\d{1,2}$");
        String[] extendArr = new String[0];
        if (patternYear.matcher(input).matches()) {
            extendArr = searchConfigs.getExtendByYearArr();
        } else if (patternMonth.matcher(input).matches()) {
            extendArr = searchConfigs.getExtendByMonthArr();
        } else if (patternDay.matcher(input).matches()) {
            extendArr = searchConfigs.getExtendByDayArr();
        } else {
            return;
        }
        if (extendArr != null) {
            for (SearchColumnModel searchColumn : searchColumnList) {
                for (String extend : extendArr) {
                    SearchDocument document = new SearchDocument();
                    document.setCol(searchColumn.getColumn());
                    document.setTab(searchColumn.getTable());
                    document.setDes(searchColumn.getDescColumn());
                    document.setCon(extend);
                    resultList.add(document);
                }
            }
        }
    }
}
