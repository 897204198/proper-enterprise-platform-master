package com.proper.enterprise.platform.api.auth.model;

import com.proper.enterprise.platform.core.utils.JSONUtil;

public class RetrievePasswordParam {
    /**
     * 用户名
     */
    private String username;

    /**
     * 验证码
     */
    private String validCode;

    /**
     * 密码
     */
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getValidCode() {
        return validCode;
    }

    public void setValidCode(String validCode) {
        this.validCode = validCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
