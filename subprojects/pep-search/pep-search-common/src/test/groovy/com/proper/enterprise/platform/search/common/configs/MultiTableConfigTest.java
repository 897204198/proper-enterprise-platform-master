package com.proper.enterprise.platform.search.common.configs;

import com.proper.enterprise.platform.search.api.annotation.SearchConfig;
import com.proper.enterprise.platform.search.api.conf.AbstractSearchConfigs;
import org.springframework.stereotype.Component;

@Component
@SearchConfig
public class MultiTableConfigTest extends AbstractSearchConfigs {

    public MultiTableConfigTest() {
        super("test_table,test_table2",
            "test_table:user_id:string:create_time,"
                + "test_table:user_name:string:user_name,"
                + "test_table2:dept_name:string:dept_name,"
                + "test_table:age:int:user_name,"
                + "test_table:create_time:date:create_time",
            20,
            "last year,this year,next year",
            "last month,this month,next month",
            "yesterday,today,tomorrow");
    }
}
