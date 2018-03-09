package com.proper.enterprise.platform.oopsearch.api;

import com.proper.enterprise.platform.oopsearch.api.annotation.SearchConfig;
import com.proper.enterprise.platform.oopsearch.api.conf.AbstractSearchConfigs;
import org.springframework.stereotype.Component;

@Component
@SearchConfig
public class TestConfigs extends AbstractSearchConfigs {

    public TestConfigs(String searchTables, String searchColumns, int limit, String extendByYear,
                       String extendByMonth, String extendByDay) {
        super(searchTables, searchColumns, limit, extendByYear, extendByMonth, extendByDay);
    }
}
