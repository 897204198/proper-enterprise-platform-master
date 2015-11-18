package com.proper.enterprise.platform.auth.jwt.model.impl;

import com.proper.enterprise.platform.auth.jwt.model.JWTPayload;

public class JWTPayloadImpl implements JWTPayload {

    private String roles;

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
