package com.proper.enterprise.platform.oopsearch.api.conf;

import com.proper.enterprise.platform.oopsearch.api.annotation.SearchConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@SearchConfig
public class DemoUserConfigs extends AbstractSearchConfigs {

    public DemoUserConfigs(@Value("${search.api.user.tables}") String searchTables,
                                 @Value("${search.api.user.columns}") String searchColumns,
                                 @Value("${search.api.user.limit}") int limit,
                                 @Value("${search.api.user.extendDateYear}") String extendByYear,
                                 @Value("${search.api.user.extendDateMonth}") String extendByMonth,
                                 @Value("${search.api.user.extendDateDay}") String extendByDay) {
        super(searchTables, searchColumns, limit, extendByYear, extendByMonth, extendByDay);
    }
}
