package com.proper.enterprise.platform.notice.server.push.configurator.xiaomi;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.push.configurator.AbstractPushConfigSupport;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import com.xiaomi.xmpush.server.Sender;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 小米推送配置
 */
@Service("xiaomiNoticeConfigurator")
public class XiaomiNoticeConfigurator extends AbstractPushConfigSupport implements XiaomiNoticeClient {


    /**
     * 小米消息发送类管理池
     */
    private Map<String, Sender> xiaomiSenderMap = new HashMap<>();
    /**
     * 小米推送的密钥
     */
    private static final String APP_SECRET = "appSecret";

    @Override
    public Map post(String appKey, Map<String, Object> config, HttpServletRequest request) {
        if (null == config.get(APP_SECRET)) {
            throw new ErrMsgException("appSecret can't be null");
        }
        Map configMap = super.post(appKey, config, request);
        Sender sender = new Sender((String) config.get(APP_SECRET));
        xiaomiSenderMap.put(appKey, sender);
        return configMap;
    }

    @Override
    public void delete(String appKey, HttpServletRequest request) {
        super.delete(appKey, request);
        Sender sender = xiaomiSenderMap.get(appKey);
        if (null == sender) {
            return;
        }
        xiaomiSenderMap.remove(appKey);
    }

    @Override
    public Map put(String appKey, Map<String, Object> config, HttpServletRequest request) {
        Map configMap = super.put(appKey, config, request);
        Sender sender = xiaomiSenderMap.get(appKey);
        if (null != sender) {
            xiaomiSenderMap.remove(appKey);
        }
        xiaomiSenderMap.put(appKey, new Sender((String) config.get(APP_SECRET)));
        return configMap;
    }

    @Override
    public Map get(String appKey, HttpServletRequest request) {
        return super.get(appKey, request);
    }

    @Override
    public String getPushPackage(String appKey) {
        return getPushPackage(appKey, PushChannelEnum.XIAOMI);
    }

    @Override
    public Sender getClient(String appKey) {
        if (StringUtil.isEmpty(appKey)) {
            throw new ErrMsgException("appKey can't be empty");
        }
        Sender sender = xiaomiSenderMap.get(appKey);
        if (null == sender) {
            throw new ErrMsgException("init xiaomi sender error");
        }
        return sender;
    }
}
