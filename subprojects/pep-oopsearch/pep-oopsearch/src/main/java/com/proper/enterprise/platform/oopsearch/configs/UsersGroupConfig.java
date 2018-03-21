package com.proper.enterprise.platform.oopsearch.configs;

import com.proper.enterprise.platform.oopsearch.api.annotation.SearchConfig;
import com.proper.enterprise.platform.oopsearch.api.conf.AbstractSearchConfigs;
import org.springframework.stereotype.Component;

@Component("authuser-groups")
@SearchConfig
public class UsersGroupConfig extends AbstractSearchConfigs {

    public UsersGroupConfig() {
        super("PEP_AUTH_USERGROUPS",
            "PEP_AUTH_USERGROUPS:id:string:id,"
                + "PEP_AUTH_USERGROUPS:name:string:name,"
                + "PEP_AUTH_USERGROUPS:description:string:description,"
                + "PEP_AUTH_USERGROUPS:enable:string:enable,",
            20,
            "last year,this year,next year",
            "last month,this month,next month",
            "yesterday,today,tomorrow");
    }
}
