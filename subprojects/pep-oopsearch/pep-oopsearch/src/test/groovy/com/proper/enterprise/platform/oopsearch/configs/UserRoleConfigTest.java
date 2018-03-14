package com.proper.enterprise.platform.oopsearch.configs;

import com.proper.enterprise.platform.oopsearch.api.annotation.SearchConfig;
import com.proper.enterprise.platform.oopsearch.api.conf.AbstractSearchConfigs;
import org.springframework.stereotype.Component;

@Component
@SearchConfig
public class UserRoleConfigTest extends AbstractSearchConfigs {

    public UserRoleConfigTest() {
        super("pep_auth_users,pep_auth_roles",
            "pep_auth_users:username:string:username,"
                + "pep_auth_users:email:string:email,"
                + "pep_auth_users:phone:string:phone,"
                + "pep_auth_users:name:string:name,"
                + "pep_auth_roles:name:string:name,"
                + "pep_auth_roles:description:string:description",
            20,
            "last year,this year,next year",
            "last month,this month,next month",
            "yesterday,today,tomorrow");
    }
}
