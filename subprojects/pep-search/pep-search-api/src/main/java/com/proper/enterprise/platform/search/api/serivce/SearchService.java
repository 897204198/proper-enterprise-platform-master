package com.proper.enterprise.platform.search.api.serivce;

import com.proper.enterprise.platform.search.api.conf.AbstractSearchConfigs;
import com.proper.enterprise.platform.search.api.document.SearchColumn;

import java.util.Collection;

public interface SearchService {

    Collection<? extends SearchColumn> getSearchInfo(String inputStr, AbstractSearchConfigs searchConfigs);

}
