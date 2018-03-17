package com.proper.enterprise.platform.push.openapi.service.impl;

import java.util.List;

import org.nutz.mapl.Mapl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.push.api.openapi.exceptions.PushException;
import com.proper.enterprise.platform.push.api.openapi.service.CommonPushClientRequestService;
import com.proper.enterprise.platform.push.config.PushGlobalInfo;
import com.proper.enterprise.platform.push.entity.PushDeviceEntity;
import com.proper.enterprise.platform.push.entity.PushUserEntity;
import com.proper.enterprise.platform.push.repository.PushDeviceRepository;
import com.proper.enterprise.platform.push.repository.PushUserRepository;
import com.proper.enterprise.platform.push.common.model.enums.PushDeviceStatus;
import com.proper.enterprise.platform.push.common.model.enums.PushDeviceType;
import com.proper.enterprise.platform.push.common.model.enums.PushMode;

@Service
public class CommonPushClientRequestServiceImpl implements CommonPushClientRequestService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonPushClientRequestServiceImpl.class);
    @Autowired
    PushGlobalInfo globalInfo;
    @Autowired
    PushUserRepository userRepo;
    @Autowired
    PushDeviceRepository deviceRepo;

    @Override
    public void saveStartpush(String userid, String deviceid, String pushToken, String userOtherInfo, String appkey,
            PushDeviceType deviceType, PushMode pushMode, String deviceOtherInfo, boolean unbindOtherDevice) {
        Object appconf = Mapl.cell(globalInfo.getPushConfigs(), appkey);
        if (appconf == null) {
            throw new PushException("appkey:" + appkey + " is not valid!");
        }
        if (StringUtil.isNotEmpty(userid)) {
            PushUserEntity user = userRepo.findByAppkeyAndUserid(appkey, userid);
            if (user == null) {
                user = new PushUserEntity();
                user.setAppkey(appkey);
                user.setOtherInfo(userOtherInfo);
                user.setUserid(userid);
                userRepo.save(user);
            } else {
                //更新用户信息
                boolean needSave = false;
                if (StringUtil.isNotEmpty(userOtherInfo)) {
                    if (!StringUtil.equals(user.getOtherInfo(), userOtherInfo)) {
                        user.setOtherInfo(userOtherInfo);
                        needSave = true;
                    }
                }
                if (needSave) {
                    userRepo.save(user);
                }
            }
            // 如果解绑其它设备
            if (unbindOtherDevice) {
                List<PushDeviceEntity> list = deviceRepo.findByAppkeyAndUserid(appkey, userid);
                if (CollectionUtil.isNotEmpty(list)) {
                    for (PushDeviceEntity d : list) {
                        d.setUserid("");
                        LOGGER.info("unbindOtherDevice of user:" + userid + " of device " + d.getDeviceid()
                                + " devicetype:" + d.getDevicetype());
                    }
                    deviceRepo.save(list);
                }
            }
        }

        if (StringUtil.isNotEmpty(deviceid)) {
            PushDeviceEntity device = null;
            device = deviceRepo.findByAppkeyAndDeviceid(appkey, deviceid);
            // 如果设备不存在，则创建
            if (device == null) {
                device = new PushDeviceEntity();
                device.setAppkey(appkey);
                device.setDeviceid(deviceid);
                device.setDevicetype(deviceType);
                device.setMstatus(PushDeviceStatus.VALID);
                device.setOtherInfo(deviceOtherInfo);
                device.setPushMode(pushMode);
                device.setUserid(userid);
                device.setPushToken(pushToken);
                deviceRepo.save(device);
            } else {
                // 如果已存在device了
                // 1.需要查看这个设备id上当前对应的userid是不是参数传进来的userid一致，如果不一致，则说明设备进行了帐号切换操作，修改为参数的userid
                // 2.device_info有变动（手机端系统的升级，eg,ios8升到ios9），修改为参数对应的device_info
                // 3.pushToken有变动，修改为参数对应的pushToken
                boolean needSave = false;
                if (!StringUtil.equals(device.getUserid(), userid)) {
                    device.setUserid(userid);
                    needSave = true;
                }
                if (!StringUtil.equals(device.getOtherInfo(), deviceOtherInfo)) {
                    device.setOtherInfo(deviceOtherInfo);
                    needSave = true;
                }
                if (!StringUtil.equals(device.getPushToken(), pushToken) || device.getPushMode() != pushMode) {
                    device.setPushToken(pushToken);
                    device.setPushMode(pushMode);
                    device.setMstatus(PushDeviceStatus.VALID);
                    needSave = true;
                }

                if (needSave) {
                    deviceRepo.save(device);
                }
            }

        }

    }

}
