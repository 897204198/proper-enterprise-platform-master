package com.proper.enterprise.platform.auth.spring;

import com.proper.enterprise.platform.core.utils.StringUtil;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;

public class AccessDecisionManagerImpl implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object object,
                    Collection<ConfigAttribute> configAttributes) throws AccessDeniedException,
                    InsufficientAuthenticationException {
        if (authentication.getDetails() == null) {
            return;
        }

        FilterInvocation filterInvocation = (FilterInvocation) object;
        String url = filterInvocation.getRequestUrl();

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String authUrl;
            for (GrantedAuthority authority : ((UserDetails) principal).getAuthorities()) {
                authUrl = authority.getAuthority();
                if (url.equals(authUrl) || StringUtil.cleanUrl(url).equals(authUrl)) {
                    return;
                }
            }
        }

        // 没有权限
        throw new AccessDeniedException(" 没有权限访问！ ");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}
