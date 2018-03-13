package com.proper.enterprise.platform.oopsearch.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.proper.enterprise.platform.core.repository.NativeRepository;
import com.proper.enterprise.platform.oopsearch.api.serivce.QueryResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * NativeSQL Concat
 * author wanghp
 */
@Service
public class QueryResultServiceImpl extends QueryResultBaseService implements QueryResultService {

    @Autowired
    private NativeRepository nativeRepository;

    @Override
    public Object assemble(JsonNode query, String moduleName) {
        String sql = installSql(query, moduleName);
        return nativeRepository.executeEntityMapQuery(sql);
    }

}
