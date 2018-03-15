package com.proper.enterprise.platform.oopsearch.sync.h2.conf;

import com.proper.enterprise.platform.oopsearch.api.annotation.SearchConfig;
import com.proper.enterprise.platform.oopsearch.api.conf.AbstractSearchConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@SearchConfig
public class DemoDeptConfigs extends AbstractSearchConfigs {

    public DemoDeptConfigs(@Value("${search.dept.tables}") String searchTables,
                           @Value("${search.dept.columns}") String searchColumns,
                           @Value("${search.dept.limit}") int limit,
                           @Value("${search.dept.extendDateYear}") String extendByYear,
                           @Value("${search.dept.extendDateMonth}") String extendByMonth,
                           @Value("${search.dept.extendDateDay}") String extendByDay) {
        super(searchTables, searchColumns, limit, extendByYear, extendByMonth, extendByDay);
    }
}
