package com.proper.enterprise.platform.push.common.schedule.service;

/**
 * 清空指定天数之前的历史消息
 *
 * @author shen
 */
public interface PushClearOldMsgsTaskService {

    /**
     * 清空发送成功历史消息
     */
    public void deleteOldMsgs();

    /**
     * 清空发送失败历史消息
     */
    public void deleteOldUnsendMsgs();
}
