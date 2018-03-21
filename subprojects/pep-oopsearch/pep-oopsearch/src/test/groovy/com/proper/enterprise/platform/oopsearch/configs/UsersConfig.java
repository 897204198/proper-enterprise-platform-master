package com.proper.enterprise.platform.oopsearch.configs;

import com.proper.enterprise.platform.oopsearch.api.annotation.SearchConfig;
import com.proper.enterprise.platform.oopsearch.api.conf.AbstractSearchConfigs;
import org.springframework.stereotype.Component;

@Component("authusers")
@SearchConfig
public class UsersConfig extends AbstractSearchConfigs {

    public UsersConfig() {
        super("PEP_AUTH_USERS",
            "PEP_AUTH_USERS:id:string:id,"
                + "PEP_AUTH_USERS:username:string:username,"
                + "PEP_AUTH_USERS:name:string:name,"
                + "PEP_AUTH_USERS:email:string:email,"
                + "PEP_AUTH_USERS:phone:string:phone,"
                + "PEP_AUTH_USERS:enable:string:enable,",
            20,
            "last year,this year,next year",
            "last month,this month,next month",
            "yesterday,today,tomorrow");
    }
}
