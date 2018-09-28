package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.model.AccessToken;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.service.AccessTokenService;
import com.proper.enterprise.platform.api.auth.service.AuthzService;
import com.proper.enterprise.platform.api.auth.service.MenuService;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AuthzServiceImpl implements AuthzService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthzServiceImpl.class);

    private static PathMatcher matcher = new AntPathMatcher();

    private ResourceService resourceService;

    private MenuService menuService;

    private AccessTokenService accessTokenService;

    @javax.annotation.Resource(name = "ignorePatternsList")
    private List<String> ignoreList;

    @Value("${auth.hasContext}")
    private boolean hasContext;

    public void setIgnoreList(List<String> ignoreList) {
        this.ignoreList = ignoreList;
    }

    public void setHasContext(boolean hasContext) {
        this.hasContext = hasContext;
    }

    @Autowired
    public AuthzServiceImpl(MenuService menuService, ResourceService resourceService, AccessTokenService accessTokenService) {
        this.menuService = menuService;
        this.resourceService = resourceService;
        this.accessTokenService = accessTokenService;
    }

    @Override
    public boolean shouldIgnore(String url, String method) {
        String path = method + ":" + getMappingUrl(url, hasContext);
        LOGGER.debug("Request is {}", path);
        if (CollectionUtil.isEmpty(ignoreList)) {
            LOGGER.debug("Not define ignorePatternsList bean, {} should be authorized.", path);
            return false;
        }
        for (String pattern : ignoreList) {
            if (matcher.match(pattern, path)) {
                LOGGER.debug("{} is match {} in ignore patterns list, no need to be authorized.", path, pattern);
                return true;
            }
        }
        LOGGER.debug("{} needs to be authorized.", path);
        return false;
    }

    @Override
    public boolean accessible(String url, String method, String userId) {
        return accessible(url, method, userId, hasContext);
    }

    @Override
    public boolean accessible(String url, String method, String userId, boolean hasContext) {
        Optional<AccessToken> accessToken = accessTokenService.getByUserId(userId);
        if (accessToken.isPresent()) {
            String resourcesDesc = accessToken.get().getResourcesDescription();
            if (StringUtil.isBlank(resourcesDesc)) {
                return false;
            }
            String path = method + ":" + getMappingUrl(url, hasContext);
            return Arrays.stream(resourcesDesc.split(",")).anyMatch(pattern -> matcher.match(pattern, path));
        } else {
            Resource resource = resourceService.get(getMappingUrl(url, hasContext), RequestMethod.valueOf(method));
            return menuService.accessible(resource, userId);
        }
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

}
