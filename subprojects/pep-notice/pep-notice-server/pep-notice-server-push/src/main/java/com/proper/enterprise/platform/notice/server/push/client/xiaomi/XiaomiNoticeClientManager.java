package com.proper.enterprise.platform.notice.server.push.client.xiaomi;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.push.dao.document.PushConfDocument;
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushConfigMongoRepository;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import com.xiaomi.xmpush.server.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class XiaomiNoticeClientManager implements XiaomiNoticeClientManagerApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(XiaomiNoticeClientManager.class);

    private PushConfigMongoRepository pushConfigMongoRepository;

    @Autowired
    public XiaomiNoticeClientManager(PushConfigMongoRepository pushConfigMongoRepository) {
        this.pushConfigMongoRepository = pushConfigMongoRepository;
    }

    /**
     * 小米消息发送类管理池
     */
    private Map<String, Sender> xiaomiSenderMap = new HashMap<>(16);

    @Override
    public Sender getClient(String appKey) {
        if (StringUtil.isEmpty(appKey)) {
            throw new ErrMsgException("appKey can't be empty");
        }
        Sender sender = xiaomiSenderMap.get(appKey);
        if (null == sender) {
            PushConfDocument confDocument = pushConfigMongoRepository.findByAppKeyAndPushChannel(appKey, PushChannelEnum.XIAOMI);
            if (null == confDocument) {
                throw new ErrMsgException("can't find confDocument by appKey:" + appKey);
            }
            try {
                xiaomiSenderMap.put(appKey, initClient(confDocument));
            } catch (Exception e) {
                LOGGER.error("init xiaomi sender error,confDocument:{}", confDocument.toString(), e);
                throw new ErrMsgException("init xiaomi sender error");
            }
        }
        return xiaomiSenderMap.get(appKey);
    }

    private Sender initClient(PushConfDocument confDocument) {
        return new Sender(confDocument.getAppSecret());
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
