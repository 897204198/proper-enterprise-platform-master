package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.entity.PushDeviceEntity;

import com.proper.enterprise.platform.notice.repository.PushDeviceRepository;
import com.proper.enterprise.platform.notice.server.api.enums.PushDeviceType;
import com.proper.enterprise.platform.notice.server.api.enums.PushMode;
import com.proper.enterprise.platform.notice.service.PushDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PushDeviceServiceImpl implements PushDeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushDeviceServiceImpl.class);

    @Autowired
    PushDeviceRepository deviceRepo;

    @Override
    public void saveStartpush(String userid, String deviceid, String pushToken, String appkey,
                              PushDeviceType deviceType, PushMode pushMode, String deviceOtherInfo) {
        if (StringUtil.isNotEmpty(deviceid)) {
            bindDevice(userid, deviceid, pushToken, appkey, deviceType, pushMode, deviceOtherInfo);
        }
    }

    @Override
    public PushDeviceEntity findDeviceByUserId(String userId) {
        return deviceRepo.findByUserid(userId);
    }

    /**
     * 解绑设备操作方法。
     * @param userid 用户id
     */
    private void unbindDevice(String userid) {
        deviceRepo.deleteByUserid(userid);
        LOGGER.info("unbindDevice of user:" + userid);

    }

    /**
     * 设备信息操作方法。
     * @param userid 用户id
     * @param deviceid 设备id
     * @param pushToken 推送的token,由手机端第三方推送框架产生，用于第三方推送惟一标识一台设备，用于向第三方式推送服务器推送消息
     * @param appkey 应用标识
     * @param deviceType 设备类型
     * @param pushMode 推送方式
     * @param deviceOtherInfo 设备的其它信息
     */
    private void bindDevice(String userid, String deviceid, String pushToken, String appkey, PushDeviceType deviceType,
        PushMode pushMode, String deviceOtherInfo) {
        try {
            unbindDevice(userid);
        } catch (Exception e) {
            LOGGER.error("unbindDevice error occurred :" + "deviceid :" + deviceid);
        }
        PushDeviceEntity device =  new PushDeviceEntity();
        device.setAppkey(appkey);
        device.setDeviceid(deviceid);
        device.setDevicetype(deviceType);
        device.setOtherInfo(deviceOtherInfo);
        device.setPushMode(pushMode);
        device.setUserid(userid);
        device.setPushToken(pushToken);
        deviceRepo.save(device);
    }
}
