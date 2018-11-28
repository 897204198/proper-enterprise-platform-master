package com.proper.enterprise.platform.streamline.client;

import com.proper.enterprise.platform.api.auth.model.AccessToken;
import com.proper.enterprise.platform.core.pojo.BaseVO;

public class MockAccessToken extends BaseVO implements AccessToken {
    private String userId;
    private String name;
    private String token;
    private String resourcesDescription;

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getResourcesDescription() {
        return resourcesDescription;
    }

    @Override
    public void setResourcesDescription(String resourcesDescription) {
        this.resourcesDescription = resourcesDescription;
    }
}
