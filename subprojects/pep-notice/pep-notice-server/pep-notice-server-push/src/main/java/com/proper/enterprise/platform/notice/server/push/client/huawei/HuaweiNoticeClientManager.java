package com.proper.enterprise.platform.notice.server.push.client.huawei;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.push.dao.document.PushConfDocument;
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushConfigMongoRepository;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("huaweiNoticeClientManager")
public class HuaweiNoticeClientManager implements HuaweiNoticeClientManagerApi {

    private PushConfigMongoRepository pushConfigMongoRepository;

    /**
     * Huawei client 池
     */
    private Map<String, HuaweiNoticeClient> huaweiNoticeClientPool = new HashMap<>(16);

    @Autowired
    public HuaweiNoticeClientManager(PushConfigMongoRepository pushConfigMongoRepository) {
        this.pushConfigMongoRepository = pushConfigMongoRepository;
    }

    @Override
    public HuaweiNoticeClient get(String appKey) {
        if (StringUtil.isEmpty(appKey)) {
            throw new ErrMsgException("appKey can't be empty");
        }
        HuaweiNoticeClient huaweiNoticeClient = huaweiNoticeClientPool.get(appKey);
        if (huaweiNoticeClient == null) {
            PushConfDocument pushConf = pushConfigMongoRepository.findByAppKeyAndPushChannel(appKey, PushChannelEnum.HUAWEI);
            if (pushConf == null) {
                throw new ErrMsgException("Can't get Huawei push config");
            }
            huaweiNoticeClientPool.put(appKey, initClient(pushConf));
        }
        return huaweiNoticeClientPool.get(appKey);
    }

    private HuaweiNoticeClient initClient(PushConfDocument pushConfDocument) {
        return new HuaweiNoticeClient(pushConfDocument);
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
