package com.proper.enterprise.platform.notice.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.notice.service.PushDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AuthcIgnore
@RequestMapping("/push/device")
public class PushDeviceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushDeviceController.class);

    @Autowired
    PushDeviceService service;

    @PostMapping
    public void save(@RequestParam(value = "userid", defaultValue = "") final String userid,
            @RequestParam(value = "deviceid", defaultValue = "") final String deviceid,
            @RequestParam(value = "push_token", defaultValue = "") final String pushToken,
            @RequestParam(value = "appkey") final String appkey,
            @RequestParam(value = "device_type", defaultValue = "") final String strDeviceType,
            @RequestParam(value = "push_mode", defaultValue = "") final String strPushMode,
            @RequestParam(value = "device_other_info", defaultValue = "") final String deviceOtherInfo) {
        LOGGER.debug("appkey:" + appkey);
        LOGGER.debug("userid:" + userid);
        LOGGER.debug("push_mode:" + strPushMode);
        LOGGER.debug("push_token:" + pushToken);
        LOGGER.debug("deviceid:" + deviceid);
        LOGGER.debug("device_other_info:" + deviceOtherInfo);
        LOGGER.debug("device_type:" + strDeviceType);
        service.save(appkey, userid, strPushMode, pushToken, deviceid, deviceOtherInfo, strDeviceType);
    }

}
