package com.proper.enterprise.platform.search.api.serivce;

import com.fasterxml.jackson.databind.JsonNode;
import com.proper.enterprise.platform.search.api.conf.AbstractSearchConfigs;

public interface QueryResultService {

    Object assemble(AbstractSearchConfigs searchConfigs, JsonNode root, String businessId);
}
