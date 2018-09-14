package com.proper.enterprise.platform.notice.server.push.client.xiaomi

import com.proper.enterprise.platform.core.exception.ErrMsgException
import com.proper.enterprise.platform.notice.server.push.dao.document.PushConfDocument
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushConfigMongoRepository
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import static com.proper.enterprise.platform.notice.server.push.constant.XiaomiConstant.APPKEY
import static com.proper.enterprise.platform.notice.server.push.constant.XiaomiConstant.APPSECRET
import static com.proper.enterprise.platform.notice.server.push.constant.XiaomiConstant.PACKAGENAME

class XiaomiClientManagerTest extends AbstractTest{

    @Autowired
    private XiaomiNoticeClientManagerApi xiaomiNoticeClientManager

    @Autowired
    private PushConfigMongoRepository pushConfigMongoRepository

    @Before
    void mongoDeleteAll() {
        pushConfigMongoRepository.deleteAll()
    }

    @Test
    void clientTest() {
        try {
            xiaomiNoticeClientManager.getClient()
        } catch (ErrMsgException e) {
            assert "appKey can't be empty" == e.getMessage()
        }
        try {
            xiaomiNoticeClientManager.getClient("test")
        } catch (ErrMsgException e) {
            assert "can't find confDocument by appKey:" + "test" == e.getMessage()
        }
        def pushConfDocument = new PushConfDocument()
        pushConfDocument.setAppKey(APPKEY)
        pushConfDocument.setPushPackage(PACKAGENAME)
        pushConfDocument.setAppSecret(APPSECRET)
        pushConfDocument.setPushChannel(PushChannelEnum.XIAOMI)
        pushConfigMongoRepository.save(pushConfDocument)
        assert null != xiaomiNoticeClientManager.getClient(APPKEY)

        xiaomiNoticeClientManager.delete("test")
        xiaomiNoticeClientManager.delete(APPKEY)

    }


    @Test
    void saveClientTest() {
        def pushConfDocument = new PushConfDocument()
        pushConfDocument.setAppKey(APPKEY)
        pushConfDocument.setPushPackage(PACKAGENAME)
        pushConfDocument.setPushChannel(PushChannelEnum.XIAOMI)
        try {
            xiaomiNoticeClientManager.post(APPKEY, pushConfDocument)
        } catch (ErrMsgException e) {
            assert "appSecret can't be null" == e.getMessage()
        }
        pushConfDocument.setAppSecret(APPSECRET)
        xiaomiNoticeClientManager.post(APPKEY, pushConfDocument)

        xiaomiNoticeClientManager.put(APPKEY, pushConfDocument)
    }

}
