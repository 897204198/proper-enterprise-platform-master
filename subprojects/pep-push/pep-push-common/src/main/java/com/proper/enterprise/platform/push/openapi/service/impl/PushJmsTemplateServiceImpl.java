package com.proper.enterprise.platform.push.openapi.service.impl;

import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.push.api.openapi.exceptions.PushException;
import com.proper.enterprise.platform.push.api.openapi.service.MsgQueueAppServerRequestService;
import com.proper.enterprise.platform.push.api.openapi.service.PushJmsTemplateService;
import com.proper.enterprise.platform.push.common.config.PushGlobalInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Map;

@Service
public class PushJmsTemplateServiceImpl implements PushJmsTemplateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushJmsTemplateServiceImpl.class);
    @Qualifier("pushAppServerRequestJmsTemplate")
    @Autowired(required = false)
    JmsTemplate appServerRequestJmsTemplate;

    @Autowired
    MsgQueueAppServerRequestService targetService;

    @Override
    public void saveConvertAndSend(String destinationName, Object message) {
        if (appServerRequestJmsTemplate == null) {
            LOGGER.info(destinationName, message);
            try {
                //采用反射的方法，调用targetService上对应的方法
                String methodName = "save" + StringUtil.capitalize(StringUtil.substring(destinationName, PushGlobalInfo.JSM_DES_APP_SERVER_REQUEST.length() + 1));
                Class clazz = targetService.getClass();
                Method m1 = clazz.getDeclaredMethod(methodName, Map.class);
                m1.invoke(targetService, message);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw  new PushException(e.getMessage());
            }
        } else {
            appServerRequestJmsTemplate.convertAndSend(destinationName, message);
        }
    }
}
