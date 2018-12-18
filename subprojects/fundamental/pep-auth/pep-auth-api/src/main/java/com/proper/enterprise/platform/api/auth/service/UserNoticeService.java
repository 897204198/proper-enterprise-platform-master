package com.proper.enterprise.platform.api.auth.service;

/**
 * 用户消息发送接口
 */
public interface UserNoticeService {

    /**
     * 发送验证码
     *
     * @param userName 用户名
     * @return 发送提示
     */
    String sendValidCode(String userName);

}
