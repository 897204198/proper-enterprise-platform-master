package com.proper.enterprise.platform.notice.server.push.client.huawei

import com.proper.enterprise.platform.core.exception.ErrMsgException
import com.proper.enterprise.platform.notice.server.push.constant.HuaweiConstant
import com.proper.enterprise.platform.notice.server.push.dao.document.PushConfDocument
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushConfigMongoRepository
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class HuaweiClientTest extends AbstractTest {

    @Autowired
    private HuaweiNoticeClientManagerApi huaweiNoticeClientApi

    @Autowired
    private PushConfigMongoRepository pushConfigMongoRepository

    @Test
    void test() {
        try {
            huaweiNoticeClientApi.get()
        } catch (ErrMsgException e) {
            e.getMessage().contains("appKey can't be empty")
        }
        try {
            huaweiNoticeClientApi.get("test")
        } catch (ErrMsgException e) {
            e.getMessage().contains("Can't get Huawei push config")
        }
        PushConfDocument pushConfDocument = new PushConfDocument()

        pushConfDocument.setAppKey("testHuawei")
        pushConfDocument.setPushChannel(PushChannelEnum.HUAWEI)
        pushConfDocument.setAppId(HuaweiConstant.CLIENT_ID)
        pushConfDocument.setAppSecret(HuaweiConstant.CLIENT_SECRET)
        pushConfigMongoRepository.save(pushConfDocument)

        assert null != huaweiNoticeClientApi.get("testHuawei")
    }
}
