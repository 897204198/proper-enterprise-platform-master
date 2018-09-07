package com.proper.enterprise.platform.notice.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.enums.PushDeviceType;
import com.proper.enterprise.platform.notice.server.api.enums.PushMode;
import com.proper.enterprise.platform.notice.service.PushDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AuthcIgnore
@RequestMapping("/push/device")
public class PushDeviceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushDeviceController.class);
    @Autowired
    PushDeviceService service;

    @RequestMapping(value = "/startpush", method = RequestMethod.POST)
    public void startpush(@RequestParam(value = "userid", defaultValue = "") final String userid,
            @RequestParam(value = "deviceid", defaultValue = "") final String deviceid,
            @RequestParam(value = "push_token", defaultValue = "") final String pushToken,
            @RequestParam(value = "appkey") final String appkey,
            @RequestParam(value = "device_type", defaultValue = "") final String strDeviceType,
            @RequestParam(value = "push_mode", defaultValue = "") final String strPushMode,
            @RequestParam(value = "device_other_info", defaultValue = "") final String deviceOtherInfo) {
        LOGGER.debug("appkey:" + appkey);
        LOGGER.debug("userid:" + userid);
        LOGGER.debug("deviceid:" + deviceid);
        LOGGER.debug("device_type:" + strDeviceType);
        LOGGER.debug("push_token:" + pushToken);
        LOGGER.debug("push_mode:" + strPushMode);
        LOGGER.debug("device_other_info:" + deviceOtherInfo);
        try {
            PushDeviceType deviceType = null;
            if (StringUtil.isNotEmpty(strDeviceType)) {
                deviceType = Enum.valueOf(PushDeviceType.class, strDeviceType.trim());
            }
            PushMode pushMode = null;
            if (StringUtil.isNotEmpty(strPushMode)) {
                pushMode = Enum.valueOf(PushMode.class, strPushMode);
            }

            service.saveStartpush(userid, deviceid, pushToken, appkey, deviceType, pushMode, deviceOtherInfo);
        } catch (Exception ex) {
            throw new ErrMsgException("system error");
        }
    }

}
