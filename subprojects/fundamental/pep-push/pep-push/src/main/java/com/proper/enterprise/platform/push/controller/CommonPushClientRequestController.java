package com.proper.enterprise.platform.push.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.push.api.openapi.service.CommonPushClientRequestService;
import com.proper.enterprise.platform.push.common.model.enums.PushDeviceType;
import com.proper.enterprise.platform.push.common.model.enums.PushMode;

@RestController
@AuthcIgnore
@RequestMapping("/push/request")
public class CommonPushClientRequestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonPushClientRequestController.class);
    @Autowired
    CommonPushClientRequestService service;

    @RequestMapping(value = "/startpush", method = RequestMethod.POST)
    public Map<String, Object> startpush(@RequestParam(value = "userid", defaultValue = "") final String userid,
            @RequestParam(value = "deviceid", defaultValue = "") final String deviceid,
            @RequestParam(value = "push_token", defaultValue = "") final String pushToken,
            @RequestParam(value = "user_other_info", defaultValue = "") final String userOtherInfo,
            @RequestParam(value = "appkey") final String appkey,
            @RequestParam(value = "device_type", defaultValue = "") final String strDeviceType,
            @RequestParam(value = "push_mode", defaultValue = "") final String strPushMode,
            @RequestParam(value = "unbind_other_device", defaultValue = "false") final String strUnbindOtherDevice,
            @RequestParam(value = "device_other_info", defaultValue = "") final String deviceOtherInfo) {
        Map<String, Object> rtn = new LinkedHashMap<String, Object>();
        LOGGER.debug("appkey:" + appkey);
        LOGGER.debug("userid:" + userid);
        LOGGER.debug("deviceid:" + deviceid);
        LOGGER.debug("device_type:" + strDeviceType);
        LOGGER.debug("push_token:" + pushToken);
        LOGGER.debug("push_mode:" + strPushMode);
        LOGGER.debug("user_other_info:" + userOtherInfo);
        LOGGER.debug("device_other_info:" + deviceOtherInfo);
        LOGGER.debug("unbind_other_device:" + strUnbindOtherDevice);
        try {
            PushDeviceType deviceType = null;
            if (StringUtil.isNotEmpty(strDeviceType)) {
                deviceType = Enum.valueOf(PushDeviceType.class, strDeviceType.trim());
            }
            PushMode pushMode = null;
            if (StringUtil.isNotEmpty(strPushMode)) {
                pushMode = Enum.valueOf(PushMode.class, strPushMode);
            }
            boolean unbindOtherDevice = false;
            if (StringUtil.isNotEmpty(strUnbindOtherDevice)) {
                unbindOtherDevice = Boolean.parseBoolean(strUnbindOtherDevice);
            }
            service.saveStartpush(userid, deviceid, pushToken, userOtherInfo, appkey, deviceType, pushMode, deviceOtherInfo,
                    unbindOtherDevice);
            rtn.put("r", "0");
        } catch (Exception ex) {
            LOGGER.error("startpush error!", ex);
            rtn.put("r", "1");
            rtn.put("desc", ex.getMessage());
        }
        return rtn;
    }

}
