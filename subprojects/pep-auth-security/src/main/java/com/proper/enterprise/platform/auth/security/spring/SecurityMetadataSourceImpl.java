package com.proper.enterprise.platform.auth.spring;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import java.util.ArrayList;
import java.util.Collection;

public class SecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        return getAllConfigAttributes();
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Collection<ConfigAttribute> configAttributes = new ArrayList<>();
        ConfigAttribute configAttribute = new SecurityConfig("Config attribute is not used in pep");
        configAttributes.add(configAttribute);
        return configAttributes;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}
