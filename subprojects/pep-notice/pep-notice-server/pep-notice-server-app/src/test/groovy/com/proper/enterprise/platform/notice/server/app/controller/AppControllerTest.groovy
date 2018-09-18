package com.proper.enterprise.platform.notice.server.app.controller

import com.proper.enterprise.platform.notice.server.api.util.AppUtil
import com.proper.enterprise.platform.notice.server.api.vo.AppVO
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.http.HttpStatus

class AppControllerTest extends AbstractTest {

    @Test
    public void crud() {
        String appKey = get('/notice/server/app/appKey', HttpStatus.OK).getResponse().getContentAsString()
        String token = get('/notice/server/app/token', HttpStatus.OK).getResponse().getContentAsString()
        AppVO appVO = new AppVO()
        appVO.setAppName("appName")
        appVO.setAppKey(appKey)
        appVO.setAppToken(token)
        appVO.setColor("color")
        appVO.setDescribe("describe")
        AppVO saveAPP = post(appVO)
        assert saveAPP.getDescribe() == "describe"

        saveAPP.setDescribe("qqq")
        AppVO putApp = put(saveAPP)
        assert putApp.getDescribe() == "qqq"

        //验证token
        Map config = new HashMap()
        config.put("a", "a")
        post("/rest/notice/server/config/" + NoticeType.MOCK + "?access_token=" + token, JSONUtil.toJSON(config), HttpStatus.CREATED)

        assert null != get(putApp.getId())

        //验证停用
        enable(putApp.getId(), false)
        assert !AppUtil.isEnable(putApp.getAppKey())

        delete(putApp.getId())
        post("/rest/notice/server/config/" + NoticeType.MOCK + "?access_token=" + token, JSONUtil.toJSON(config), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    private AppVO post(AppVO appVO) {
        return postAndReturn('/notice/server/app', appVO)
    }

    private AppVO put(AppVO appVO) {
        return JSONUtil.parse(put('/notice/server/app/' + appVO.getId(), JSONUtil.toJSON(appVO), HttpStatus.OK).getResponse().getContentAsString(), AppVO.class)
    }

    private AppVO get(String appId) {
        return JSONUtil.parse(get('/notice/server/app/appId/' + appId, HttpStatus.OK).getResponse().getContentAsString(), AppVO.class)
    }

    private void enable(String appIds, boolean enable) {
        put('/notice/server/app?appIds=' + appIds + "&enable=" + enable, "", HttpStatus.OK)
    }

    private void delete(String appId) {
        delete('/notice/server/app/' + appId, HttpStatus.NO_CONTENT)
    }
}
