package com.proper.enterprise.platform.oopsearch.sync.h2.conf;

import com.proper.enterprise.platform.oopsearch.api.annotation.SearchConfig;
import com.proper.enterprise.platform.oopsearch.api.conf.AbstractSearchConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@SearchConfig
public class DemoTestConfigs extends AbstractSearchConfigs {

    public DemoTestConfigs(@Value("${search.test.tables}") String searchTables,
                           @Value("${search.test.columns}") String searchColumns,
                           @Value("${search.test.limit}") int limit,
                           @Value("${search.test.extendDateYear}") String extendByYear,
                           @Value("${search.test.extendDateMonth}") String extendByMonth,
                           @Value("${search.test.extendDateDay}") String extendByDay) {
        super(searchTables, searchColumns, limit, extendByYear, extendByMonth, extendByDay);
    }
}
