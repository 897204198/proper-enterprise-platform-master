package com.proper.enterprise.platform.auth.jwt.model.impl;

import com.proper.enterprise.platform.auth.jwt.model.JWTPayload;

public class JWTPayloadImpl implements JWTPayload {

    public JWTPayloadImpl() { }

    public JWTPayloadImpl(String name) {
        this.name = name;
    }

    /**
     * 显示名
     */
    private String name;

    /**
     * 是否有角色
     * APP 用户可能只访问不需要权限访问的资源，或只访问没挂在菜单下的资源
     */
    private boolean hasRole;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHasRole() {
        return hasRole;
    }

    public void setHasRole(boolean hasRole) {
        this.hasRole = hasRole;
    }

}
