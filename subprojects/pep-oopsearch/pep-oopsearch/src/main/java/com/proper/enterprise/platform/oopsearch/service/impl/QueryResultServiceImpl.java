package com.proper.enterprise.platform.oopsearch.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.proper.enterprise.platform.core.repository.NativeRepository;
import com.proper.enterprise.platform.oopsearch.api.conf.AbstractSearchConfigs;
import com.proper.enterprise.platform.oopsearch.api.serivce.QueryResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * NativeSQL Concat
 * author wanghp
 */
@Service
public class QueryResultServiceImpl extends QueryResultBaseService implements QueryResultService {

    //TODO need method from xuyang
    private String getTableNameByBusinessId(String businessId) {
        return businessId;
    }

    @Autowired
    private NativeRepository nativeRepository;

    @Override
    public Object assemble(AbstractSearchConfigs searchConfigs, JsonNode root, String businessId) {
        String tableName = getTableNameByBusinessId(businessId);
        String sql = installSql(searchConfigs, root, tableName);
        return nativeRepository.executeEntityMapQuery(sql);
    }

}
