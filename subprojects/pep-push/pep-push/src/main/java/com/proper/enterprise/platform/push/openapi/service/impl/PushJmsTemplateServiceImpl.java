package com.proper.enterprise.platform.push.openapi.service.impl;

import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.push.api.openapi.exceptions.PushException;
import com.proper.enterprise.platform.push.api.openapi.service.AppServerRequestService;
import com.proper.enterprise.platform.push.api.openapi.service.MsgQueueAppServerRequestService;
import com.proper.enterprise.platform.push.api.openapi.service.PushJmsTemplateService;
import com.proper.enterprise.platform.push.config.PushGlobalInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Service
public class PushJmsTemplateServiceImpl implements PushJmsTemplateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushJmsTemplateServiceImpl.class);

    @Autowired
    MsgQueueAppServerRequestService targetService;

    @Autowired
    AppServerRequestService appServerRequestService;

    @Override
    public void saveConvertAndSend(String destinationName, Object message) {
        LOGGER.info("push log step2 destinationName,{}msg:{}", destinationName, message);
        try {
            //采用反射的方法，调用targetService上对应的方法
            String methodName = "save" + StringUtil.capitalize(StringUtil
                .substring(destinationName, PushGlobalInfo.JSM_DES_APP_SERVER_REQUEST.length() + 1));
            Class clazz = targetService.getClass();
            Method m1 = clazz.getDeclaredMethod(methodName, Map.class);
            m1.invoke(targetService, message);
        } catch (Exception e) {
            LOGGER.error("push log step2 error:msg:{}", message, e);
            throw new PushException(e.getMessage());
        }
    }

    @Override
    public void sendPushMsg(List pushIds) {
        pushIds.forEach(n -> LOGGER.info("PushJmsTemplateServiceImpl sendPushMsg inner pushId:{},", n));
        try {
            appServerRequestService.updatePushEntityAfterSendMsg(pushIds);
        } catch (Exception e) {
            pushIds.forEach(n -> LOGGER.error("sendPushMsg inner pushId:{},errorMsg:{}", n, e.getMessage(), e));
            throw new PushException(e.getMessage());
        }
    }

}
