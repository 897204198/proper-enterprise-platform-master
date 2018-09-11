package com.proper.enterprise.platform.notice.server.push.configurator.huawei;

import com.alibaba.fastjson.JSONObject;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator;
import com.proper.enterprise.platform.notice.server.push.client.huawei.HuaweiNoticeClientApi;
import com.proper.enterprise.platform.notice.server.push.configurator.AbstractPushConfigSupport;
import com.proper.enterprise.platform.notice.server.push.document.PushConfDocument;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Service("huaweiNoticeConfigurator")
public class HuaweiNoticeConfigurator extends AbstractPushConfigSupport implements NoticeConfigurator {

    public static final String APP_ID = "appId";

    @Autowired
    private HuaweiNoticeClientApi huaweiNoticeClientApi;

    @Override
    public Map post(String appKey, Map<String, Object> config, HttpServletRequest request) {
        if (null == config.get(APP_ID)) {
            throw new ErrMsgException("appId can't be null");
        }
        Map result = super.post(appKey, config, request);
        PushConfDocument pushDocument = BeanUtil.convert(config, PushConfDocument.class);
        huaweiNoticeClientApi.post(appKey, pushDocument);
        return result;
    }

    @Override
    public void delete(String appKey, HttpServletRequest request) {
        super.delete(appKey, request);
        huaweiNoticeClientApi.delete(appKey);
    }

    @Override
    public Map put(String appKey, Map<String, Object> config, HttpServletRequest request) {
        Map result = super.put(appKey, config, request);
        PushConfDocument pushDocument = BeanUtil.convert(config, PushConfDocument.class);
        huaweiNoticeClientApi.put(appKey, pushDocument);
        return result;
    }

    @Override
    public Map get(String appKey, HttpServletRequest request) {
        return super.get(appKey, request);
    }


}
