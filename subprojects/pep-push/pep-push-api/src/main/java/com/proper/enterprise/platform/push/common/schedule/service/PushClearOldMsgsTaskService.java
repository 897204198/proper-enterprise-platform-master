package com.proper.enterprise.platform.push.common.schedule.service;

/**
 * 清空指定天数之前的历史消息
 *
 * @author shen
 *
 */
public interface PushClearOldMsgsTaskService {

    /**
     * 清空历史消息
     */
    public void deleteOldMsgs();
}
