package com.proper.enterprise.platform.oopsearch.api.serivce;

import com.proper.enterprise.platform.oopsearch.api.conf.AbstractSearchConfigs;

import java.util.Map;

public interface SearchConfigService {

    AbstractSearchConfigs getSearchConfig(String moduleName);

    Map<String, Object> getSearchConfigs();

    String getURL(String moduleName);

}
