package com.proper.enterprise.platform.notice.server.push.configurator.xiaomi

import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushConfigMongoRepository
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier

class XiaomiNoticeConfiguratorTest extends AbstractJPATest {

    @Autowired
    @Qualifier("xiaomiNoticeConfigurator")
    private NoticeConfigurator xiaomiNoticeConfigurator

    @Autowired
    private PushConfigMongoRepository pushRepository


    @Before
    void deleteData() {
        pushRepository.deleteAll()
    }

    @Test
    void "post"() {
        def (String appKey, HashMap configMap, HashMap request) = initData()
        def map = xiaomiNoticeConfigurator.post(appKey, configMap, request)
        assert "RGW+NA+T2ucpEX0a6bxyhA==" == map.get("appSecret")
        map = xiaomiNoticeConfigurator.get(appKey, request)
        assert "RGW+NA+T2ucpEX0a6bxyhA==" == map.get("appSecret")
        xiaomiNoticeConfigurator.delete(appKey, request)
    }

    @Test
    void "put"() {
        def (String appKey, HashMap configMap, HashMap request) = initData()
        xiaomiNoticeConfigurator.post(appKey, configMap, request)
        configMap.put("appSecret", "aaa")
        def map = xiaomiNoticeConfigurator.put(appKey, configMap, request)
        assert "aaa" == map.get("appSecret")
        pushRepository.deleteAll()
    }

    private List initData() {
        def appKey = "testDev"
        def configMap = new HashMap()
        configMap.put("appSecret", "RGW+NA+T2ucpEX0a6bxyhA==")
        configMap.put("pushPackage", "pushPackage")
        Map request = new HashMap()
        request.put("pushChannel", PushChannelEnum.XIAOMI.toString())
        [appKey, configMap, request]
    }
}
