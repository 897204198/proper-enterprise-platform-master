package com.proper.enterprise.platform.notice.server.app

import com.proper.enterprise.platform.notice.server.app.vo.AppVO
import com.proper.enterprise.platform.test.AbstractJPATest
import org.springframework.http.HttpStatus

abstract class AbstractServerAppTest extends AbstractJPATest {

    public String initApp(String appKey) {
        String token = get('/notice/server/app/token/init', HttpStatus.OK).getResponse().getContentAsString()
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

    public void deleteApp(String appId) {
        delete('/notice/server/app/?appIds=' + appId, HttpStatus.NO_CONTENT)
    }

    public void enableApp(String appId, boolean enable) {
        put('/notice/server/app?appIds=' + appId + "&enable=" + enable, "", HttpStatus.OK)
    }
}
