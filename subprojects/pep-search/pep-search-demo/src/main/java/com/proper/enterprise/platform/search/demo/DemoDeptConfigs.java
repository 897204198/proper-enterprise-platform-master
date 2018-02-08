package com.proper.enterprise.platform.search.demo;

import com.proper.enterprise.platform.search.api.annotation.SearchConfig;
import com.proper.enterprise.platform.search.api.conf.AbstractSearchConfigs;
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
