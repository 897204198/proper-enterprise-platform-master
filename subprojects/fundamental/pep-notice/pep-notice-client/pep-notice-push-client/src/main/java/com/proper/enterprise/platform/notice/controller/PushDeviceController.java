package com.proper.enterprise.platform.notice.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.notice.service.PushDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AuthcIgnore
@Api(tags = "/push/device")
@RequestMapping("/push/device")
public class PushDeviceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushDeviceController.class);

    @Autowired
    PushDeviceService service;

    @PostMapping
    @ApiOperation("‍‍移动客户端向服务端注册设备及userid与deviceceid的绑定设备与取消绑定")
    public void save(@ApiParam(value = "‍‍用户ID", required = true) @RequestParam(value = "userid", defaultValue = "") final String userid,
                     @ApiParam(value = "‍设备的唯一标识", required = true) @RequestParam(value = "deviceid", defaultValue = "") final String deviceid,
                     @ApiParam(value = "‍‍推送Token", required = true) @RequestParam(value = "push_token", defaultValue = "") final String pushToken,
                     @ApiParam(value = "‍应用标识", required = true) @RequestParam(value = "appkey") final String appkey,
                     @ApiParam(value = "‍设备类型", required = true) @RequestParam(value = "device_type", defaultValue = "") final String strDeviceType,
                     @ApiParam(value = "‍‍推送方式", required = true) @RequestParam(value = "push_mode", defaultValue = "") final String strPushMode,
                     @ApiParam(value = "‍‍是否绑定其他设备", required = true)
                     @RequestParam(value = "unbind_other_device", defaultValue = "false") final String strUnbindOtherDevice,
                     @ApiParam(value = "‍‍设备的其它信息") @RequestParam(value = "device_other_info", defaultValue = "") final String deviceOtherInfo) {
        LOGGER.debug("appkey:" + appkey);
        LOGGER.debug("userid:" + userid);
        LOGGER.debug("push_mode:" + strPushMode);
        LOGGER.debug("push_token:" + pushToken);
        LOGGER.debug("deviceid:" + deviceid);
        LOGGER.debug("device_other_info:" + deviceOtherInfo);
        LOGGER.debug("unbind_other_device:" + strUnbindOtherDevice);
        LOGGER.debug("device_type:" + strDeviceType);
        service.save(appkey, userid, strPushMode, pushToken, deviceid, deviceOtherInfo, strDeviceType, strUnbindOtherDevice);
    }

}
