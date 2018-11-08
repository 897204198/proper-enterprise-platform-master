package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.model.AccessToken;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.service.AccessTokenService;
import com.proper.enterprise.platform.api.auth.service.AuthzService;
import com.proper.enterprise.platform.api.auth.service.MenuService;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.RequestUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
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

    public void setIgnoreList(List<String> ignoreList) {
        this.ignoreList = ignoreList;
    }

    @Autowired
    public AuthzServiceImpl(MenuService menuService,
                            ResourceService resourceService,
                            AccessTokenService accessTokenService) {
        this.menuService = menuService;
        this.resourceService = resourceService;
        this.accessTokenService = accessTokenService;
    }

    @Override
    public boolean shouldIgnore(HttpServletRequest request) {
        String path = request.getMethod() + ":" + RequestUtil.getURIPath(request);
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
    public boolean accessible(HttpServletRequest request, String userId) {
        String resDescriptor = request.getMethod() + ":" + RequestUtil.getURIPath(request);
        return accessible(resDescriptor, userId);
    }

    @Override
    public boolean accessible(String resDescriptor, String userId) {
        Optional<AccessToken> accessToken = accessTokenService.getByUserId(userId);
        if (accessToken.isPresent()) {
            String resourcesDesc = accessToken.get().getResourcesDescription();
            if (StringUtil.isBlank(resourcesDesc)) {
                return false;
            }
            return Arrays.stream(resourcesDesc.split(",")).anyMatch(pattern -> matcher.match(pattern, resDescriptor));
        } else {
            String[] splitter = resDescriptor.split(":");
            Resource resource = resourceService.get(splitter[1], RequestMethod.valueOf(splitter[0]));
            return menuService.accessible(resource, userId);
        }
    }

}
