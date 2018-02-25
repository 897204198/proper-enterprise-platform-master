package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.workflow.convert.UserConvert;
import org.flowable.app.security.FlowableAppUser;
import org.flowable.app.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class PEPSecurityServiceImpl implements SecurityService {
    @Autowired
    private UserService userService;

    @Override
    public FlowableAppUser getCurrentFlowableAppUser() {
        return FlowableUserConvert.convert(userService.getCurrentUser());
    }

    public static class FlowableUserConvert {
        private static List<SimpleGrantedAuthority> collection = new ArrayList<>();

        static {
            collection.add(new SimpleGrantedAuthority("access-admin"));
            collection.add(new SimpleGrantedAuthority("access-idm"));
            collection.add(new SimpleGrantedAuthority("access-modeler"));
            collection.add(new SimpleGrantedAuthority("access-task"));
        }

        public static FlowableAppUser convert(User pepUser) {
            return new FlowableAppUser(UserConvert.convert(pepUser), pepUser.getId(), collection);
        }
    }
}
