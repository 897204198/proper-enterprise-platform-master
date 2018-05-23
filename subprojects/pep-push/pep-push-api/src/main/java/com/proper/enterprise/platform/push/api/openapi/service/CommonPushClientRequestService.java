package com.proper.enterprise.platform.push.api.openapi.service;

import com.proper.enterprise.platform.push.common.model.enums.PushDeviceType;
import com.proper.enterprise.platform.push.common.model.enums.PushMode;

/**
 * 移动客户端调用的公用的相关的接口
 *
 * @author shen
 *
 */
public interface CommonPushClientRequestService {
    /**
     * 移动客户端向服务端注册设备及userid与deviceceid的绑定设备与取消绑定
     *
     * 如果userid在数据库中不存在，则新增userid到用户表 如果deiviceid不为空，则更新或新增设备信息到设备表
     * 如果userid为空，则取消用户与设备的相互绑定 如果userid不为空，则绑定用户与设备
     *
     * @param userid
     *            用户id
     * @param deviceid
     *            设备id，设备的唯一标识
     * @param pushToken
     *            推送的token,由手机端第三方推送框架产生，用于第三方推送惟一标识一台设备，用于向第三方式推送服务器推送消息
     * @param userOtherInfo
     *            用户的其它信息,相当于备注，手机端根据自己的业务需要来放值。
     *            因为如果直接用userid，后台查找用户时，可能不太方便，这个可以放login_name,phone_number什么的，
     *            这样在查找用户定位问题更方便。
     * @param appkey
     *            应用标识
     * @param deviceType
     *            设备类型
     * @param pushMode
     *            推送方式
     * @param deviceOtherInfo
     *            设备的其它信息，相当于备注。
     *            这个由手机端推送框架来设，现在主要存当前的设备信息，比如操作系统版本，手机型号什么的。是一个json字符串。
     * @param unbindOtherDevice
     *            userid与当前用户绑定时，是否解绑userid与其它设备。
     *            如果为true的话，最多一个用户对应一台设备，按userid来推送消息时，最多只有一台设备收到消息，
     *            不会出现多台设备同时接收消息的情况。
     */
    public void saveStartpush(String userid, String deviceid, String pushToken, String userOtherInfo, String appkey,
                              PushDeviceType deviceType, PushMode pushMode, String deviceOtherInfo, boolean unbindOtherDevice);
}
