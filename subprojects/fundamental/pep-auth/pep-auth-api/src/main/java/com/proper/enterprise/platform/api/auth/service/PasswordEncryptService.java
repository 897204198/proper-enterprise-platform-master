package com.proper.enterprise.platform.api.auth.service;

/**
 * 用来对用户的密码进行加密
 * 具体的加密算法可以通过注入不同的实现类来区分
 */
public interface PasswordEncryptService {

    /**
     * 对密码进行加密
     * @param password 密码
     * @return 加密后的密码
     */
    String encrypt(String password);

}
