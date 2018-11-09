package com.proper.enterprise.platform.notice.server.push.configurator.huawei;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator;
import com.proper.enterprise.platform.notice.server.push.client.huawei.HuaweiNoticeClientManagerApi;
import com.proper.enterprise.platform.notice.server.push.configurator.AbstractPushConfigSupport;
import com.proper.enterprise.platform.notice.server.push.dao.document.PushConfDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("huaweiNoticeConfigurator")
public class HuaweiNoticeConfigurator extends AbstractPushConfigSupport implements NoticeConfigurator {

    public static final String APP_ID = "appId";

    private HuaweiNoticeClientManagerApi huaweiNoticeClientManagerApi;

    @Autowired
    public HuaweiNoticeConfigurator(HuaweiNoticeClientManagerApi huaweiNoticeClientManagerApi) {
        this.huaweiNoticeClientManagerApi = huaweiNoticeClientManagerApi;
    }

    @Override
    public Map post(String appKey, Map<String, Object> config, Map<String, Object> params) {
        if (StringUtil.isBlank(config.get(APP_ID).toString())) {
            throw new ErrMsgException("appId can't be null");
        }
        Map result = super.post(appKey, config, params);
        PushConfDocument pushDocument = BeanUtil.convert(config, PushConfDocument.class);
        huaweiNoticeClientManagerApi.post(appKey, pushDocument);
        return result;
    }

    @Override
    public void delete(String appKey, Map<String, Object> params) {
        super.delete(appKey, params);
        huaweiNoticeClientManagerApi.delete(appKey);
    }

    @Override
    public Map put(String appKey, Map<String, Object> config, Map<String, Object> params) {
        Map result = super.put(appKey, config, params);
        PushConfDocument pushDocument = BeanUtil.convert(config, PushConfDocument.class);
        huaweiNoticeClientManagerApi.put(appKey, pushDocument);
        return result;
    }

    @Override
    public Map get(String appKey, Map<String, Object> params) {
        return super.get(appKey, params);
    }


}
