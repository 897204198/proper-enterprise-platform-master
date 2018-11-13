package com.proper.enterprise.platform.notice.server.app.controller

import com.proper.enterprise.platform.notice.server.api.util.AppUtil
import com.proper.enterprise.platform.notice.server.app.vo.AppVO
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.http.HttpStatus

class AppControllerTest extends AbstractJPATest {

    @Test
    public void crud() {
        String appKey = get('/notice/server/app/appKey/init', HttpStatus.OK).getResponse().getContentAsString()
        String token = get('/notice/server/app/token/init', HttpStatus.OK).getResponse().getContentAsString()
        AppVO appVO = new AppVO()
        appVO.setAppName("appName")
        appVO.setAppKey(appKey)
        appVO.setAppToken(token)
        appVO.setColor("color")
        appVO.setAppDesc("describe")
        AppVO saveAPP = post(appVO)
        assert saveAPP.getAppDesc() == "describe"

        saveAPP.setAppDesc("qqq")
        AppVO putApp = put(saveAPP)
        assert putApp.getAppDesc() == "qqq"

        //验证token
        Map config = new HashMap()
        config.put("a", "a")
        post("/notice/server/config/" + NoticeType.MOCK + "?access_token=" + token, JSONUtil.toJSON(config), HttpStatus.CREATED)

        AppVO app = get(putApp.getAppKey())
        assert null != app
        assert !app.getHaveEmailConf()

        //验证停用
        enable(putApp.getId(), false)
        assert !AppUtil.isEnable(putApp.getAppKey())

        delete(putApp.getId())
        post("/notice/server/config/" + NoticeType.MOCK + "?access_token=" + token, JSONUtil.toJSON(config), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    private AppVO post(AppVO appVO) {
        return resOfPost('/notice/server/app', appVO)
    }

    private AppVO put(AppVO appVO) {
        return JSONUtil.parse(put('/notice/server/app/' + appVO.getId(), JSONUtil.toJSON(appVO), HttpStatus.OK).getResponse().getContentAsString(), AppVO.class)
    }

    private AppVO get(String appKey) {
        return JSONUtil.parse(get('/notice/server/app/appKey/' + appKey, HttpStatus.OK).getResponse().getContentAsString(), AppVO.class)
    }

    private void enable(String appIds, boolean enable) {
        put('/notice/server/app?appIds=' + appIds + "&enable=" + enable, "", HttpStatus.OK)
    }

    private void delete(String appId) {
        delete('/notice/server/app?appIds=' + appId, HttpStatus.NO_CONTENT)
    }
}
