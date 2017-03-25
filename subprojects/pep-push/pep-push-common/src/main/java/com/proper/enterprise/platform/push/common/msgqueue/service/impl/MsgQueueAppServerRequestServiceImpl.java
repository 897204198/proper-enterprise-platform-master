package com.proper.enterprise.platform.push.common.msgqueue.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.nutz.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.push.api.openapi.model.PushMessage;
import com.proper.enterprise.platform.push.api.openapi.service.AppServerRequestService;
import com.proper.enterprise.platform.push.api.openapi.service.MsgQueueAppServerRequestService;
import com.proper.enterprise.platform.push.common.config.PushGlobalInfo;
import com.proper.enterprise.platform.push.common.model.enums.PushDeviceType;

@Component
@Lazy(false)
public class MsgQueueAppServerRequestServiceImpl implements MsgQueueAppServerRequestService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MsgQueueAppServerRequestServiceImpl.class);
    private static final String CONTAINER_FACTIORY_NAME = "pushAppServerRequestFactory";
    @Autowired
    PushGlobalInfo globalInfo;
    @Autowired
    AppServerRequestService service;

    @PostConstruct
    public void init() {
        LOGGER.info("init @JmsListener of " + PushGlobalInfo.JSM_DES_APP_SERVER_REQUEST);
    }

    @JmsListener(destination = PushGlobalInfo.JSM_DES_APP_SERVER_REQUEST
            + "/pushMessageToUsers", containerFactory = CONTAINER_FACTIORY_NAME)
    @Override
    public void savePushMessageToUsers(Map<String, String> requestParams) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("receive jms " + PushGlobalInfo.JSM_DES_APP_SERVER_REQUEST + "/pushMessageToUsers"
                    + " of data: " + requestParams);
        }
        String userids = requestParams.get("userids");
        String smsg = requestParams.get("msg");
        String appkey = requestParams.get("appkey");
        final List<String> lstUids = Json.fromJsonAsList(String.class, userids);
        final PushMessage thePushmsg = Json.fromJson(PushMessage.class, smsg);
        service.savePushMessageToUsers(appkey, lstUids, thePushmsg);
    }

    @JmsListener(destination = PushGlobalInfo.JSM_DES_APP_SERVER_REQUEST
            + "/pushMessageToDevices", containerFactory = CONTAINER_FACTIORY_NAME)
    @Override
    public void savePushMessageToDevices(Map<String, String> requestParams) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("receive jms " + PushGlobalInfo.JSM_DES_APP_SERVER_REQUEST + "/pushMessageToDevices"
                    + " of data: " + requestParams);
        }
        String deviceids = requestParams.get("deviceids");
        String smsg = requestParams.get("msg");
        String strDeviceType = requestParams.get("device_type");
        String appkey = requestParams.get("appkey");
        final List<String> lstDeviceids = Json.fromJsonAsList(String.class, deviceids);
        final PushMessage thePushmsg = Json.fromJson(PushMessage.class, smsg);
        PushDeviceType deviceType = null;
        if (StringUtil.isNotEmpty(strDeviceType)) {
            deviceType = Enum.valueOf(PushDeviceType.class, strDeviceType.trim());
        }
        service.savePushMessageToDevices(appkey, deviceType, lstDeviceids, thePushmsg);
    }

    @JmsListener(destination = PushGlobalInfo.JSM_DES_APP_SERVER_REQUEST
            + "/pushMessageToAllUsers", containerFactory = CONTAINER_FACTIORY_NAME)
    @Override
    public void savePushMessageToAllUsers(Map<String, String> requestParams) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("receive jms " + PushGlobalInfo.JSM_DES_APP_SERVER_REQUEST + "/pushMessageToAllUsers"
                    + " of data: " + requestParams);
        }
        String smsg = requestParams.get("msg");
        String appkey = requestParams.get("appkey");
        final PushMessage thePushmsg = Json.fromJson(PushMessage.class, smsg);
        service.savePushMessageToAllUsers(appkey, thePushmsg);
    }

    @JmsListener(destination = PushGlobalInfo.JSM_DES_APP_SERVER_REQUEST
            + "/pushMessageToAllDevices", containerFactory = CONTAINER_FACTIORY_NAME)
    @Override
    public void savePushMessageToAllDevices(Map<String, String> requestParams) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("receive jms " + PushGlobalInfo.JSM_DES_APP_SERVER_REQUEST + "/pushMessageToAllDevices"
                    + " of data: " + requestParams);
        }
        String smsg = requestParams.get("msg");
        String appkey = requestParams.get("appkey");
        String strDeviceType = requestParams.get("device_type");
        PushDeviceType deviceType = null;
        if (StringUtil.isNotEmpty(strDeviceType)) {
            deviceType = Enum.valueOf(PushDeviceType.class, strDeviceType.trim());
        }
        final PushMessage thePushmsg = Json.fromJson(PushMessage.class, smsg);
        service.savePushMessageToAllDevices(appkey, deviceType, thePushmsg);
    }

}
