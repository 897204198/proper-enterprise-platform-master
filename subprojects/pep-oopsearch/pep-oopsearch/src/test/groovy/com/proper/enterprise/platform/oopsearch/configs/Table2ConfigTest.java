package com.proper.enterprise.platform.oopsearch.configs;

import com.proper.enterprise.platform.oopsearch.config.conf.AbstractSearchConfigs;
import org.springframework.stereotype.Component;

@Component
public class Table2ConfigTest extends AbstractSearchConfigs {

    public Table2ConfigTest() {
        super("test_table2",
            "test_table2:dept_id:string:dept_id,"
                + "test_table2:dept_name:string:dept_name,"
                + "test_table2:create_time:date:create_time,"
                + "test_table2:dept_member_count:num:dept_member_count",
            10,
            "last year,this year,next year",
            "last month,this month,next month",
            "yesterday,today,tomorrow");
    }
}
