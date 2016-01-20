package com.proper.enterprise.platform.auth.security.spring;

import com.proper.enterprise.platform.api.auth.Resource;
import com.proper.enterprise.platform.api.auth.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UserDetailsServiceImpl implements UserDetailsService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    
    @Autowired
    UserService userService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("Username is {}", username);
        
        User user = userService.getUser(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        
        Collection<GrantedAuthority> grantedAuths = obtainGrantedAuthorities(user.getId());
        return new UserDetailsImpl(grantedAuths, user.getPassword(), user.getUsername(), true, true, true, true);
    }
    
    private Collection<GrantedAuthority> obtainGrantedAuthorities(String userId) {
        Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();
        
        Collection<Resource> resources = userService.getResourcesById(userId);
        for (Resource res : resources) {
            authSet.add(new SimpleGrantedAuthority(res.getMethod().name() + ":" + res.getUrl()));
        }
        return authSet;
    }

}
