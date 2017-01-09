package com.proper.enterprise.platform.auth.jwt.authz;

import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.service.MenuService;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuthzService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthzService.class);

    private static PathMatcher matcher = new AntPathMatcher();

    private List<String> ignorePatterns = new ArrayList<>();

    private ResourceService resourceService;

    private MenuService menuService;

    public void setIgnorePatterns(String ignorePatterns) {
        Collections.addAll(this.ignorePatterns, ignorePatterns.split(","));
    }

    public void setResourceService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    public boolean shouldIgnore(String url, String method, boolean hasContext) {
        String path = method + ":" + getMappingUrl(url, hasContext);
        LOGGER.debug("Request is {}", path);
        for (String pattern : ignorePatterns) {
            if (matcher.match(pattern, path)) {
                LOGGER.debug("{} {} is match {}", method, url, pattern);
                return true;
            }
        }
        return false;
    }

    private String getMappingUrl(String url, boolean hasContext) {
        String result = "";
        try {
            result = new URI(hasContext ? url.substring(url.indexOf("/", 1)) : url).getPath();
        } catch (URISyntaxException e) {
            LOGGER.error("Parse URI error!", e);
        }
        return result;
    }

    public boolean accessible(String url, String method, boolean hasContext) {
        Resource resource = resourceService.get(getMappingUrl(url, hasContext), RequestMethod.valueOf(method));
        return menuService.accessible(resource);
    }

}
