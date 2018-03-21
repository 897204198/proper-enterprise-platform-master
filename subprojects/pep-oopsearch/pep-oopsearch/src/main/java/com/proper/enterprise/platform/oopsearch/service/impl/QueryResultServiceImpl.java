package com.proper.enterprise.platform.oopsearch.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.proper.enterprise.platform.core.jpa.repository.NativeRepository;
import com.proper.enterprise.platform.oopsearch.api.serivce.QueryResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NativeSQL Concat
 * author wanghp
 */
@Service
public class QueryResultServiceImpl extends QueryResultBaseService implements QueryResultService {

    @Autowired
    private NativeRepository nativeRepository;

    @Override
    public Object assemble(JsonNode query, String moduleName, String pageNo, String pageSize) {
        String sql = installSql(query, moduleName);
        List listWithoutPage = nativeRepository.executeEntityMapQuery(sql);
        String sqlWithinPage = addPage(sql, pageNo, pageSize);
        List listWithinPage = nativeRepository.executeEntityMapQuery(sqlWithinPage);
        Map<String, Object> returnValue = new HashMap<>();
        returnValue.put("data", listWithinPage);
        returnValue.put("count", listWithoutPage.size());
        return returnValue;
    }

}
