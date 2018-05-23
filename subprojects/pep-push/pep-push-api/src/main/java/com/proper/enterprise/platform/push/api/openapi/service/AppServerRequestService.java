package com.proper.enterprise.platform.push.api.openapi.service;

import java.util.List;

import com.proper.enterprise.platform.push.api.openapi.model.PushMessage;
import com.proper.enterprise.platform.push.common.model.enums.PushDeviceType;

/**
 * 推送消息接口，处理应用端的消息推送请求，将消息推送到设备
 *
 * @author shen
 *
 */
public interface AppServerRequestService {
    /**
     * 推送消息到一组用户
     *
     * @param appkey
     *            应用标识
     * @param lstUids
     *            推送的userid集合
     * @param thePushmsg
     *            消息
     */
    void savePushMessageToUsers(String appkey, List<String> lstUids, PushMessage thePushmsg);

    /**
     * 推送消息到全部用户
     *
     * @param appkey
     *            应用标识
     * @param thePushmsg
     *            消息
     */
    void savePushMessageToAllUsers(String appkey, PushMessage thePushmsg);

    /**
     * 推送消息到指定类型的全部设备
     *
     * @param appkey
     *            应用标识
     * @param deviceType
     *            设备类型
     * @param thePushmsg
     *            消息
     */
    void savePushMessageToAllDevices(String appkey, PushDeviceType deviceType, PushMessage thePushmsg);

    /**
     * 推送消息到一组设备上，这一组设备需要要相同的设备类型
     *
     * @param appkey
     *            应用标识
     * @param deviceType
     *            设备类型
     * @param lstDeviceids
     *            推送的deviceid集合
     * @param thePushmsg
     *            消息
     */
    void savePushMessageToDevices(String appkey, PushDeviceType deviceType, List<String> lstDeviceids,
                                  PushMessage thePushmsg);
}
