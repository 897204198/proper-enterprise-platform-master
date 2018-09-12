package com.proper.enterprise.platform.notice.server.push.client.huawei;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.push.dao.document.PushConfDocument;
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushConfigMongoRepository;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("huaweiNoticeClientManager")
@Primary
public class HuaweiNoticeClientManager implements HuaweiNoticeClientManagerApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(HuaweiNoticeClientManager.class);

    @Autowired
    private PushConfigMongoRepository pushConfigMongoRepository;

    /**
     * Huawei client 池
     */
    private Map<String, HuaweiNoticeClient> huaweiNoticeClientPool = new HashMap<>(16);

    @Override
    public HuaweiNoticeClient get(String appKey) {
        if (StringUtil.isEmpty(appKey)) {
            throw new ErrMsgException("appKey can't be empty");
        }
        HuaweiNoticeClient huaweiNoticeClient = huaweiNoticeClientPool.get(appKey);
        if (huaweiNoticeClient == null) {
            PushConfDocument pushConf1 = pushConfigMongoRepository.findByAppKeyAndPushChannel(appKey, PushChannelEnum.HUAWEI);
            if (pushConf1 == null) {
                throw new ErrMsgException("Can't get Huawei push config");
            }
            huaweiNoticeClientPool.put(appKey, initClient(pushConf1));
            huaweiNoticeClient = huaweiNoticeClientPool.get(appKey);
        }
        if (huaweiNoticeClient.getTokenExpiredTime() <= System.currentTimeMillis()) {
            PushConfDocument pushConf2 = pushConfigMongoRepository.findByAppKeyAndPushChannel(appKey, PushChannelEnum.HUAWEI);
            huaweiNoticeClientPool.put(appKey, initClient(pushConf2));
        }
        return huaweiNoticeClientPool.get(appKey);
    }

    private HuaweiNoticeClient initClient(PushConfDocument pushConfDocument) {
        HuaweiNoticeClient huaweiNoticeClient = new HuaweiNoticeClient(pushConfDocument);
        return huaweiNoticeClient;
    }

    @Override
    public void post(String appKey, PushConfDocument pushConf) {
        huaweiNoticeClientPool.put(appKey, initClient(pushConf));
    }

    @Override
    public void delete(String appKey) {
        huaweiNoticeClientPool.remove(appKey);
    }

    @Override
    public void put(String appKey, PushConfDocument pushConf) {
        huaweiNoticeClientPool.put(appKey, initClient(pushConf));
    }
}
