package com.proper.enterprise.platform.notice.service;

import com.proper.enterprise.platform.notice.entity.PushDeviceEntity;

public interface PushDeviceService {
    /**
     * 移动客户端向服务端注册设备及userid与deviceceid的绑定设备与取消绑定
     *
     * @param appKey              应用标识
     * @param userId              用户id
     * @param pushMode            推送方式
     * @param pushToken           推送的token,由手机端第三方推送框架产生，用于第三方推送惟一标识一台设备，用于向第三方式推送服务器推送消息
     * @param deviceId            设备id，设备的唯一标识
     * @param deviceOtherInfo     设备的其它信息，相当于备注。 这个由手机端推送框架来设，现在主要存当前的设备信息，比如操作系统版本，手机型号什么的。是一个json字符串。
     * @param deviceType          设备类型
     * @param unbindOtherDevice   是否绑定设备
     */
    void save(String appKey,
              String userId,
              String pushMode,
              String pushToken,
              String deviceId,
              String deviceOtherInfo,
              String deviceType,
              String unbindOtherDevice);

    /**
     * 查询用户绑定的设备
     * @param userId 用户id
     * @return 设备实体
     */
    PushDeviceEntity findDeviceByUserId(String userId);

    /**
     * 清除指定用户的设备信息
     *
     * @param userId 用户唯一标识
     */
    void deleteByUserId(String userId);
}

