package com.proper.enterprise.platform.auth.spring;

import com.proper.enterprise.platform.api.auth.Resource;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import java.util.*;

public class SecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityMetadataSourceImpl.class);

    private static Map<String, Collection<ConfigAttribute>> resourceMap;

    @Autowired
    ResourceService resService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        LOGGER.info("Request URL is {}", requestUrl);
        if (resourceMap == null) {
            loadResourceDefine();
        }
        return resourceMap.get(requestUrl);
    }

    //加载所有资源与权限的关系
    private void loadResourceDefine() {
        if (resourceMap == null) {
            resourceMap = new HashMap<>();
        }

        Set<Resource> resources = resService.getAllResources();
        for (Resource resource : resources) {
            Collection<ConfigAttribute> configAttributes = new ArrayList<>();

            //以权限名封装为Spring的security Object
            ConfigAttribute configAttribute = new SecurityConfig(resource.getId());
            configAttributes.add(configAttribute);
            resourceMap.put(resource.getUrl(), configAttributes);
        }
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}
