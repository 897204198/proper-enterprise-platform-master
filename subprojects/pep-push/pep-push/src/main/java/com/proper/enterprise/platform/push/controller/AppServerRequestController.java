package com.proper.enterprise.platform.push.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.push.api.openapi.service.PushJmsTemplateService;
import com.proper.enterprise.platform.push.service.PushMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 推送消息
 *
 * @author shen
 */
@RestController
@AuthcIgnore
@RequestMapping("/appserver/request")
public class AppServerRequestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppServerRequestController.class);
    @Autowired
    PushJmsTemplateService appServerRequestJmsTemplate;
    @Autowired
    PushMsgService pushMsgService;

    @RequestMapping(value = "/{requestMethod}", method = RequestMethod.POST)
    public Map<String, Object> doRequestMethod(@PathVariable("requestMethod") String requestMethod,
                                               HttpServletRequest request) {
        Map<String, Object> rtn = new LinkedHashMap<String, Object>();
        List list;
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, String> requestParams = new LinkedHashMap<String, String>();
        while (paramNames.hasMoreElements()) {
            String key = paramNames.nextElement();
            String value = request.getParameter(key);
            requestParams.put(key, value);
        }

        //消息入库
        try {
            String methodName = "save" + StringUtil.capitalize(requestMethod);
            Class clazz = pushMsgService.getClass();
            Method m1 = clazz.getDeclaredMethod(methodName, Map.class);
            list = (List) m1.invoke(pushMsgService, requestParams);
            if (list != null && !list.isEmpty()) {
                list.forEach(n -> LOGGER.info("AppServerRequestController savePushMsg pushId:{},pushParams:{}",
                    n, JSONUtil.toJSONIgnoreException(requestParams)));
            }
        } catch (Exception ex) {
            LOGGER.error("AppServerRequestController savePushMsg error;errMsg:{}", ex.getMessage(), ex);
            rtn.put("result", "-1");
            rtn.put("errmsg", ex.getMessage());
            return rtn;
        }

        //消息发送
        try {
            if (list != null && !list.isEmpty()) {
                appServerRequestJmsTemplate.sendPushMsg(list);
                list.forEach(n -> LOGGER.info("AppServerRequestController sendPushMsg pushId:{},pushParams:{}",
                    n, JSONUtil.toJSONIgnoreException(requestParams)));
                rtn.put("result", "0");
            }
        } catch (Exception ex) {
            list.forEach(n -> LOGGER.error("AppServerRequestController sendPushMsg error pushId:{},errMsg:{}", n, ex.getMessage(), ex));
            rtn.put("result", "-1");
            rtn.put("errmsg", ex.getMessage());
        }
        return rtn;
    }
}
