package com.proper.enterprise.platform.streamline.api.service;

/**
 * StreamlineService通用接口
 * 根据用户名和密码采用md5方式生成客户端token
 */
public interface StreamlineService {

    /**
     * 根据用户名和密码注册标记
     *
     * @param userName   用户名
     * @param password   密码
     * @param serviceKey 服务端唯一标识
     */
    void addSign(String userName, String password, String serviceKey);

    /**
     * 根据用户名和密码
     *
     * @param userName 用户名
     * @param password 密码
     */
    void deleteSign(String userName, String password);


    /**
     * 根据用户名和密码更新标记
     *
     * @param userName    用户名
     * @param password    密码
     * @param oldUserName 旧用户名
     * @param oldPassword 就密码
     */
    void updateSign(String userName, String password, String oldUserName, String oldPassword);

    /**
     * 根据用户名和密码获得服务端标识
     *
     * @param userName 用户名
     * @param password 密码
     * @return 服务端标识
     */
    String getSign(String userName, String password);

}
