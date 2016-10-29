package com.proper.enterprise.platform.auth.jwt.authz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuthzService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthzService.class);

    private static PathMatcher matcher = new AntPathMatcher();

    private List<String> ignorePatterns = new ArrayList<>();

    public void setIgnorePatterns(String ignorePatterns) {
        Collections.addAll(this.ignorePatterns, ignorePatterns.split(","));
    }

    public boolean shouldIgnore(String url, String method, boolean hasContext) throws URISyntaxException {
        URI uri = new URI(hasContext ? url.substring(url.indexOf("/", 1)) : url);
        String path = method + ":" + uri.getPath();
        LOGGER.debug("Request is {}", path);
        for (String pattern : ignorePatterns) {
            if (matcher.match(pattern, path)) {
                LOGGER.debug("{} {} is match {}", method, url, pattern);
                return true;
            }
        }
        return false;
    }

}
