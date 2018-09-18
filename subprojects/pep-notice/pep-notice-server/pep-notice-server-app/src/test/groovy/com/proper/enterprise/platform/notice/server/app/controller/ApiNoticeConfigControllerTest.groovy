package com.proper.enterprise.platform.notice.server.app.controller

import com.proper.enterprise.platform.api.auth.service.AccessTokenService
import com.proper.enterprise.platform.auth.common.vo.AccessTokenVO
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class ApiNoticeConfigControllerTest extends AbstractTest {

    @Autowired
    private AccessTokenService accessTokenService

    @Test
    public void postTest() {
        String appKey = 'postApiNoticeConfigurator'
        def accessToken = new AccessTokenVO(appKey, 'for test using', appKey, 'GET:/test')
        accessTokenService.saveOrUpdate(accessToken)
        Map config = new HashMap()
        config.put("a", "a")
        post("/notice/server/config/" + NoticeType.MOCK + "?access_token=" + appKey, JSONUtil.toJSON(config), HttpStatus.CREATED)
        Map searchConf = JSONUtil.parse(get("/notice/server/config/" + NoticeType.MOCK + "?access_token=" + appKey, HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert searchConf.get("a") == "a"
    }

    @Test
    public void putTest() {
        String appKey = 'putApiNoticeConfigurator'
        def accessToken = new AccessTokenVO(appKey, 'for test using', appKey, 'GET:/test')
        accessTokenService.saveOrUpdate(accessToken)
        Map config = new HashMap()
        config.put("a", "a")
        post("/notice/server/config/" + NoticeType.MOCK + "?access_token=" + appKey, JSONUtil.toJSON(config), HttpStatus.CREATED)
        Map searchConf = JSONUtil.parse(get("/notice/server/config/" + NoticeType.MOCK + "?access_token=" + appKey, HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert searchConf.get("a") == "a"
        config.put("a", "a1")
        put("/notice/server/config/" + NoticeType.MOCK + "?access_token=" + appKey, JSONUtil.toJSON(config), HttpStatus.OK)
        Map searchPutConf = JSONUtil.parse(get("/notice/server/config/" + NoticeType.MOCK + "?access_token=" + appKey, HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert searchPutConf.get("a") == "a1"
        waitExecutorDone()
    }

    @Test
    public void delTest() {
        String appKey = 'delApiNoticeConfigurator'
        def accessToken = new AccessTokenVO(appKey, 'for test using', appKey, 'GET:/test')
        accessTokenService.saveOrUpdate(accessToken)
        Map config = new HashMap()
        config.put("a", "a")
        post("/notice/server/config/" + NoticeType.MOCK + "?access_token=" + appKey, JSONUtil.toJSON(config), HttpStatus.CREATED)
        Map searchConf = JSONUtil.parse(get("/notice/server/config/" + NoticeType.MOCK + "?access_token=" + appKey, HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert searchConf.get("a") == "a"
        delete("/notice/server/config/" + NoticeType.MOCK + "?access_token=" + appKey, HttpStatus.NO_CONTENT)
        assert get("/notice/server/config/" + NoticeType.MOCK + "?access_token=" + appKey, HttpStatus.OK).getResponse().getContentAsString() == ""
    }
}
