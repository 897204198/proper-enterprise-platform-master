package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.service.AuthzService;
import com.proper.enterprise.platform.api.auth.service.MenuService;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class AuthzServiceImpl implements AuthzService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthzService.class);

    private static final String SPLIT_SYMBOL = ",";

    private static PathMatcher matcher = new AntPathMatcher();

    private ResourceService resourceService;

    private MenuService menuService;

    @Value("${auth.ignorePatterns}")
    private String patterns;

    @Value("${auth.hasContext}")
    private boolean hasContext;

    public void setPatterns(String patterns) {
        this.patterns = patterns;
    }

    public void setHasContext(boolean hasContext) {
        this.hasContext = hasContext;
    }

    @Autowired
    public AuthzServiceImpl(MenuService menuService, ResourceService resourceService) {
        this.menuService = menuService;
        this.resourceService = resourceService;
    }

    @Override
    public boolean shouldIgnore(String url, String method) {
        String path = method + ":" + getMappingUrl(url);
        LOGGER.debug("Request is {}", path);
        if (StringUtils.isEmpty(patterns)) {
            return false;
        }
        for (String pattern : patterns.split(SPLIT_SYMBOL)) {
            if (matcher.match(pattern, path)) {
                LOGGER.debug("{} {} is match {}", method, url, pattern);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean accessible(String url, String method, String userId) {
        Resource resource = resourceService.get(getMappingUrl(url), RequestMethod.valueOf(method));
        return menuService.accessible(resource, userId);
    }

    private String getMappingUrl(String url) {
        String result = "";
        try {
            result = new URI(hasContext ? url.substring(url.indexOf("/", 1)) : url).getPath();
        } catch (URISyntaxException e) {
            LOGGER.error("Parse URI error!", e);
        }
        return result;
    }

}
