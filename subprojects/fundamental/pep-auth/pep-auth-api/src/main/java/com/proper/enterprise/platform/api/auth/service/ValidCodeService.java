package com.proper.enterprise.platform.api.auth.service;

public interface ValidCodeService {

    /**
     * 获取找回密码验证码
     *
     * @param username 用户名
     * @return 验证码
     */
    String getPasswordValidCode(String username);
}
