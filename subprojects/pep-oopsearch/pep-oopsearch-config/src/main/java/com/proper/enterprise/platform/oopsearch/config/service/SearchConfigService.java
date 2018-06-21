package com.proper.enterprise.platform.oopsearch.config.service;

import com.proper.enterprise.platform.oopsearch.api.enums.DataBaseType;
import com.proper.enterprise.platform.oopsearch.config.conf.AbstractSearchConfigs;
import java.util.Map;

public interface SearchConfigService {

    AbstractSearchConfigs getSearchConfig(String moduleName);

    AbstractSearchConfigs getSearchConfig(String moduleName, DataBaseType dataBaseType);

    Map<String, Object> getSearchConfigs(DataBaseType dataBaseType);

    String getURL(String moduleName);

}
