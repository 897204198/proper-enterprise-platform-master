package com.proper.enterprise.platform.api.auth.service;

public interface AuthcService {

    /**
     * 验证用户名和密码;
     * @param username 用户名
     * @param pwd 密码
     * @return ;
     */
    boolean authenticate(String username, String pwd);

}
