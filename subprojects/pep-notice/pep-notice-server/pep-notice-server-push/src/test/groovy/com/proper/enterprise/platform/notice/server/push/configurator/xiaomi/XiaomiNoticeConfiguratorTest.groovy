package com.proper.enterprise.platform.notice.server.push.configurator.xiaomi

import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum
import com.proper.enterprise.platform.notice.server.push.repository.PushConfigMongoRepository
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.mock.web.MockHttpServletRequest


class XiaomiNoticeConfiguratorTest extends AbstractTest {

    @Autowired
    @Qualifier("xiaomiNoticeConfigurator")
    private NoticeConfigurator xiaomiNoticeConfigurator

    @Autowired
    private PushConfigMongoRepository pushRepository;


    @Before
    void deleteData() {
        pushRepository.deleteAll()
    }

    @Test
    void "post"() {
        def (String appKey, HashMap configMap, MockHttpServletRequest request) = initData()
        def map = xiaomiNoticeConfigurator.post(appKey, configMap, request)
        assert "RGW+NA+T2ucpEX0a6bxyhA==" == map.get("appSecret")
        map = xiaomiNoticeConfigurator.get(appKey, request)
        assert "RGW+NA+T2ucpEX0a6bxyhA==" == map.get("appSecret")
        pushRepository.deleteAll()
    }

    @Test
    void "put"() {
        def (String appKey, HashMap configMap, MockHttpServletRequest request) = initData()
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
        MockHttpServletRequest request = new MockHttpServletRequest()
        request.setParameter("pushChannel", PushChannelEnum.XIAOMI.toString())
        [appKey, configMap, request]
    }
}
