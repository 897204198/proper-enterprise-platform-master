package com.proper.enterprise.platform.notice.server.push.client.xiaomi;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.push.document.PushConfDocument;
import com.xiaomi.xmpush.server.Sender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class XiaomiNoticeClient implements XiaomiNoticeClientApi {

    /**
     * 小米消息发送类管理池
     */
    private Map<String, Sender> xiaomiSenderMap = new HashMap<>();

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

    @Override
    public void post(String appKey, PushConfDocument pushConf) {
        if (StringUtil.isNull(pushConf.getAppSecret())) {
            throw new ErrMsgException("appSecret can't be null");
        }
        Sender sender = new Sender(pushConf.getAppSecret());
        xiaomiSenderMap.put(appKey, sender);
    }

    @Override
    public void delete(String appKey) {
        Sender sender = xiaomiSenderMap.get(appKey);
        if (null == sender) {
            return;
        }
        xiaomiSenderMap.remove(appKey);
    }

    @Override
    public void put(String appKey, PushConfDocument pushConf) {
        Sender sender = xiaomiSenderMap.get(appKey);
        if (null != sender) {
            xiaomiSenderMap.remove(appKey);
        }
        xiaomiSenderMap.put(appKey, new Sender(pushConf.getAppSecret()));
    }
}
