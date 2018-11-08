package com.proper.enterprise.platform.auth.common.jpa.entity;

import com.proper.enterprise.platform.api.auth.model.AccessToken;
import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PEP_AUTH_TOKENS")
@CacheEntity
public class AccessTokenEntity extends BaseEntity implements AccessToken {

    @Column(unique = true)
    private String userId;
    private String name;
    @Column(unique = true)
    private String token;
    @Column(length = 4000)
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
