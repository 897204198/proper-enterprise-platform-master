package com.proper.enterprise.platform.push.openapi.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.push.api.openapi.exceptions.PushException;
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
import com.proper.enterprise.platform.push.config.PushGlobalInfo;
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
                    + " of data: " + requestParams + " ,time is " + DateUtil.toString(new Date(), "yyyyMMddHHmmss"));
        }
        String userids = requestParams.get("userids");
        String appkey = requestParams.get("appkey");
        final List<String> lstUids = Json.fromJsonAsList(String.class, userids);
        final PushMessage thePushmsg = getPushMessage(requestParams);
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
        String strDeviceType = requestParams.get("device_type");
        String appkey = requestParams.get("appkey");
        final List<String> lstDeviceids = Json.fromJsonAsList(String.class, deviceids);
        final PushMessage thePushmsg = getPushMessage(requestParams);
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
        String appkey = requestParams.get("appkey");
        final PushMessage thePushmsg = getPushMessage(requestParams);
        service.savePushMessageToAllUsers(appkey, thePushmsg);
    }

    private PushMessage getPushMessage(Map<String, String> requestParams) {
        String smsg = requestParams.get("msg");
        final PushMessage thePushmsg;
        try {
            thePushmsg = JSONUtil.parse(smsg, PushMessage.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw  new PushException(e.getMessage());
        }
        return thePushmsg;
    }

    @JmsListener(destination = PushGlobalInfo.JSM_DES_APP_SERVER_REQUEST
            + "/pushMessageToAllDevices", containerFactory = CONTAINER_FACTIORY_NAME)
    @Override
    public void savePushMessageToAllDevices(Map<String, String> requestParams) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("receive jms " + PushGlobalInfo.JSM_DES_APP_SERVER_REQUEST + "/pushMessageToAllDevices"
                    + " of data: " + requestParams);
        }
        String appkey = requestParams.get("appkey");
        String strDeviceType = requestParams.get("device_type");
        PushDeviceType deviceType = null;
        if (StringUtil.isNotEmpty(strDeviceType)) {
            deviceType = Enum.valueOf(PushDeviceType.class, strDeviceType.trim());
        }
        final PushMessage thePushmsg = getPushMessage(requestParams);
        service.savePushMessageToAllDevices(appkey, deviceType, thePushmsg);
    }

}
