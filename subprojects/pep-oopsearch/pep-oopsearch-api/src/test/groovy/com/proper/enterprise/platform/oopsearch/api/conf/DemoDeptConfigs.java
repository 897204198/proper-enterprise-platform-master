package com.proper.enterprise.platform.oopsearch.api.conf;

import com.proper.enterprise.platform.oopsearch.api.annotation.SearchConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@SearchConfig
public class DemoDeptConfigs extends AbstractSearchConfigs {

    public DemoDeptConfigs(@Value("${search.api.dept.tables}") String searchTables,
                           @Value("${search.api.dept.columns}") String searchColumns,
                           @Value("${search.api.dept.limit}") int limit,
                           @Value("${search.api.dept.extendDateYear}") String extendByYear,
                           @Value("${search.api.dept.extendDateMonth}") String extendByMonth,
                           @Value("${search.api.dept.extendDateDay}") String extendByDay) {
        super(searchTables, searchColumns, limit, extendByYear, extendByMonth, extendByDay);
    }
}
