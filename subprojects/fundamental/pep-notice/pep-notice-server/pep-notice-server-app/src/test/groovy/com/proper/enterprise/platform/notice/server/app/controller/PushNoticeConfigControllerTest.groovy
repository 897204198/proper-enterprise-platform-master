package com.proper.enterprise.platform.notice.server.app.controller

import com.proper.enterprise.platform.notice.server.app.vo.AppVO
import com.proper.enterprise.platform.notice.server.push.vo.PushConfigVO
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.http.HttpStatus

class PushNoticeConfigControllerTest extends AbstractJPATest {

    @Test
    public void postTest() {
        PushConfigVO pushConfigVO = new PushConfigVO()
        def huaweiConfig = [:]
        huaweiConfig.put('appSecret', 'cb5b99c684477aaa3b6a28b2c7cbe7b2')
        huaweiConfig.put('pushPackage', 'com.proper.icmp.dev')
        huaweiConfig.put('appId', '100213965')
        pushConfigVO.setHuaweiConf(huaweiConfig)
        String appKey = "huaweiConfTest"
        AppVO appVO = save(appKey, pushConfigVO)
        PushConfigVO getVO = JSONUtil.parse(get("/notice/server/push/config/" + appKey, HttpStatus.OK).getResponse().getContentAsString(), PushConfigVO.class)
        assert null != getVO.getHuaweiConf().get("id")

        def xiaomiConfig = new HashMap()
        xiaomiConfig.put("appSecret", "RGW+NA+T2ucpEX0a6bxyhA==")
        xiaomiConfig.put("pushPackage", "pushPackage")
        getVO.setXiaomiConf(xiaomiConfig)

        put("/notice/server/push/config/" + appKey, JSONUtil.toJSON(getVO), HttpStatus.CREATED)
        PushConfigVO getVO2 = JSONUtil.parse(get("/notice/server/push/config/" + appKey, HttpStatus.OK).getResponse().getContentAsString(), PushConfigVO.class)
        assert null != getVO2.getHuaweiConf().get("id")
        assert null != getVO2.getXiaomiConf().get("id")

    }

    public AppVO save(String appKey, PushConfigVO pushConfigVO) {
        AppVO appVO = initAppReturnVO(appKey)
        post("/notice/server/push/config/" + appKey, JSONUtil.toJSON(pushConfigVO), HttpStatus.CREATED)
        return appVO
    }

    public String initApp(String appKey) {
        String token = get('/notice/server/app/token', HttpStatus.OK).getResponse().getContentAsString()
        AppVO appVO = new AppVO()
        appVO.setAppName("appName")
        appVO.setAppKey(appKey)
        appVO.setAppToken(token)
        appVO.setColor("color")
        appVO.setAppDesc("describe")
        resOfPost('/notice/server/app', appVO)
        return token
    }

    public AppVO initAppReturnVO(String appKey) {
        String token = get('/notice/server/app/token/init', HttpStatus.OK).getResponse().getContentAsString()
        AppVO appVO = new AppVO()
        appVO.setAppName("appName")
        appVO.setAppKey(appKey)
        appVO.setAppToken(token)
        appVO.setColor("color")
        appVO.setAppDesc("describe")
        return resOfPost('/notice/server/app', appVO)
    }
}
