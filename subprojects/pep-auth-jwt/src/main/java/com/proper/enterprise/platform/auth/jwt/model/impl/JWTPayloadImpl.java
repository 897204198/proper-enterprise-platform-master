package com.proper.enterprise.platform.auth.jwt.model.impl;

import com.proper.enterprise.platform.auth.jwt.model.JWTPayload;

public class JWTPayloadImpl implements JWTPayload {

    public JWTPayloadImpl() { }

    public JWTPayloadImpl(String empName) {
        this.empName = empName;
    }

    private String empName;

    private String roles;

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
