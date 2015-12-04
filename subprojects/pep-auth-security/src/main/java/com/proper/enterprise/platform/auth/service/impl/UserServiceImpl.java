package com.proper.enterprise.platform.auth.service.impl;

import com.proper.enterprise.platform.api.auth.User;
import com.proper.enterprise.platform.auth.common.service.impl.AbstractUserServiceImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends AbstractUserServiceImpl {

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal instanceof UserDetails ? ((UserDetails) principal).getUsername() : principal.toString();
        return getUserByUsername(username);
    }

}
