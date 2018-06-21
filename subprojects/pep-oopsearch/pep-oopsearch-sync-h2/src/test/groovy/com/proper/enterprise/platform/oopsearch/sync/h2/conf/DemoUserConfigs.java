package com.proper.enterprise.platform.oopsearch.sync.h2.conf;

import com.proper.enterprise.platform.oopsearch.config.conf.AbstractSearchConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DemoUserConfigs extends AbstractSearchConfigs {

    public DemoUserConfigs(@Value("${search.user.tables}") String searchTables,
                                 @Value("${search.user.columns}") String searchColumns,
                                 @Value("${search.user.limit}") int limit,
                                 @Value("${search.user.extendDateYear}") String extendByYear,
                                 @Value("${search.user.extendDateMonth}") String extendByMonth,
                                 @Value("${search.user.extendDateDay}") String extendByDay) {
        super(searchTables, searchColumns, limit, extendByYear, extendByMonth, extendByDay);
    }
}
