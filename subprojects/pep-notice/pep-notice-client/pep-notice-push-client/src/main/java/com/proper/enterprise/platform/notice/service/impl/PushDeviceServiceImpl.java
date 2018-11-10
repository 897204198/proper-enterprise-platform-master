package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.entity.PushDeviceEntity;
import com.proper.enterprise.platform.notice.enums.PushDeviceType;
import com.proper.enterprise.platform.notice.repository.PushDeviceRepository;
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
    public void save(String appKey,
                     String userId,
                     String pushMode,
                     String pushToken,
                     String deviceId,
                     String deviceOtherInfo,
                     String deviceType,
                     String unbindOtherDevice) {
        PushDeviceType enumDeviceType = null;
        if (StringUtil.isNotEmpty(deviceType)) {
            enumDeviceType = Enum.valueOf(PushDeviceType.class, deviceType.trim());
        }
        if (StringUtil.isNotEmpty(pushMode)) {
            pushMode = pushMode.trim().toUpperCase();
        }
        if (StringUtil.isEmpty(userId)) {
            throw new ErrMsgException("no user_id");
        }
        deleteByUserId(userId);
        boolean isBind = "true".equals(unbindOtherDevice);
        if (isBind && StringUtil.isEmpty(deviceId)) {
            throw new ErrMsgException("no device_id");
        }
        if (isBind) {
            bindDevice(appKey, userId, pushMode, pushToken, deviceId, deviceOtherInfo, enumDeviceType);
        }
    }

    @Override
    public PushDeviceEntity findDeviceByUserId(String userId) {
        return deviceRepo.findByUserId(userId);
    }

    @Override
    public void deleteByUserId(String userId) {
        deviceRepo.deleteByUserId(userId);
        LOGGER.info("unbindDevice of user:" + userId);
    }

    @Override
    public void deleteByToken(String token) {
        deviceRepo.deleteByPushToken(token);
    }

    private void bindDevice(String appKey,
                            String userId,
                            String pushMode,
                            String pushToken,
                            String deviceId,
                            String deviceOtherInfo,
                            PushDeviceType deviceType) {
        PushDeviceEntity device =  new PushDeviceEntity();
        device.setAppKey(appKey);
        device.setDeviceId(deviceId);
        device.setDeviceType(deviceType);
        device.setDeviceOtherInfo(deviceOtherInfo);
        device.setPushMode(pushMode);
        device.setUserId(userId);
        device.setPushToken(pushToken);
        deviceRepo.save(device);
    }

}
