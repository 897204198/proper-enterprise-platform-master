package com.proper.enterprise.platform.api.auth.service;

import java.util.Map;

public interface AuthcService {

    /**
     * 验证用户名和密码;
     * @param username 用户名
     * @param pwd 密码
     * @return ;
     */
    boolean authenticate(String username, String pwd);

    /**
     * 获取用户名账号
     * @param map ;
     * @return 账号;
     */
    String getUsername(Map<String, String> map);

    /**
     * 获取用户密码
     * @param map ;
     * @return 密码;
     */
    String getPassword(Map<String, String> map);

}
