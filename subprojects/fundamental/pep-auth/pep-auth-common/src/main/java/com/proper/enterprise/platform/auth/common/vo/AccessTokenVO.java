package com.proper.enterprise.platform.auth.common.vo;

import com.proper.enterprise.platform.api.auth.model.AccessToken;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.JSONUtil;

public class AccessTokenVO extends BaseVO implements AccessToken {

    private String userId;
    private String name;
    private String token;
    private String resourcesDescription;

    public AccessTokenVO() { }

    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }

    public AccessTokenVO(String userId, String name, String token, String resourcesDescription) {
        this.userId = userId;
        this.name = name;
        this.token = token;
        this.resourcesDescription = resourcesDescription;
    }

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
