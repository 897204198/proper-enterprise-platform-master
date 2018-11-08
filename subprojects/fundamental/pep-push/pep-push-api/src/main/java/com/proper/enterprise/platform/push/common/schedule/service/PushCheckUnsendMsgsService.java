package com.proper.enterprise.platform.push.common.schedule.service;

/**
 * 检查未推送成功的消息，进行重新推送操作
 * 
 * @author shen
 *
 */
public interface PushCheckUnsendMsgsService {
    /**
     * 检查未推送成功的消息，进行重新推送操作
     */
    public void saveCheckUnsendMsgs();
}
