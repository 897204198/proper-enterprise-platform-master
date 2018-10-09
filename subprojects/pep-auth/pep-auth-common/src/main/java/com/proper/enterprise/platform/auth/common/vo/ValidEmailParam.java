package com.proper.enterprise.platform.auth.common.vo;

import com.proper.enterprise.platform.core.utils.JSONUtil;

public class ValidEmailParam {

    private String username;

    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
