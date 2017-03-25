package com.proper.enterprise.platform.push.openapi.controller;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.proper.enterprise.platform.push.common.config.PushGlobalInfo;

/**
 * 推送消息
 * 
 * @author shen
 *
 */
@RestController
@RequestMapping("/appserver/request")
public class AppServerRequestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppServerRequestController.class);
    @Qualifier("pushAppServerRequestJmsTemplate")
    @Autowired
    JmsTemplate appServerRequestJmsTemplate;

    @RequestMapping(value = "/{requestMethod}", method = RequestMethod.POST)
    public Map<String, Object> pushMessageToUsers(@PathVariable("requestMethod") String requestMethod,
            HttpServletRequest request) {
        Map<String, Object> rtn = new LinkedHashMap<String, Object>();
        try {
            Enumeration<String> paramNames = request.getParameterNames();
            Map<String, Object> requestParams = new LinkedHashMap<String, Object>();
            while (paramNames.hasMoreElements()) {
                String key = paramNames.nextElement();
                String value = request.getParameter(key);
                requestParams.put(key, value);
            }
            appServerRequestJmsTemplate.convertAndSend(PushGlobalInfo.JSM_DES_APP_SERVER_REQUEST + "/" + requestMethod,
                    requestParams);
            rtn.put("result", "0");
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            rtn.put("result", "-1");
            rtn.put("errmsg", ex.getMessage());
        }
        return rtn;
    }

}
