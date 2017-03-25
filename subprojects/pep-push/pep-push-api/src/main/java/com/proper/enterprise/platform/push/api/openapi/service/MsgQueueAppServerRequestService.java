package com.proper.enterprise.platform.push.api.openapi.service;

import java.util.Map;

/**
 * 消息队列Listener接口 接收到消息队列的消息后，进行格式转换后，调用targetService对应的方法
 * 
 * @author shen
 *
 */
public interface MsgQueueAppServerRequestService {
    /**
     * 推送消息到一组用户
     * 
     * @param msgData
     *            消息数据
     */
    public void savePushMessageToUsers(Map<String, String> msgData);

    /**
     * 推送消息到全部用户
     * 
     * @param msgData
     *            消息数据
     */
    public void savePushMessageToDevices(Map<String, String> msgData);

    /**
     * 推送消息到一组设备上，这一组设备需要要相同的设备类型
     * 
     * @param msgData
     *            消息数据
     */
    public void savePushMessageToAllUsers(Map<String, String> msgData);

    /**
     * 推送消息到指定类型的全部设备
     * 
     * @param msgData
     *            消息数据
     */
    public void savePushMessageToAllDevices(Map<String, String> msgData);
}
