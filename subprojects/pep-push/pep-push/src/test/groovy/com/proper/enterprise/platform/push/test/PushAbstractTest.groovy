package com.proper.enterprise.platform.push.test

import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.core.utils.StringUtil
import com.proper.enterprise.platform.core.utils.http.HttpClient
import com.proper.enterprise.platform.push.vo.PushChannelVO
import com.proper.enterprise.platform.test.AbstractTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

/**
 * 推送相关的推送支持类
 * Created by shen on 2017/5/11.
 */
abstract class PushAbstractTest extends AbstractTest {

    protected static final String VALID_APPKEY = "test"
    protected static final String TEST_USERID1 = "testuser1"
    protected static final String TEST_USERID2 = "testuser2"
    protected static final String TEST_DEVICEID1 = "testdevice1"
    protected static final String TEST_DEVICEID2 = "testdevice2"
    protected static final String TEST_DEVICEID3 = "testdevice3"

    protected static final String TEST_PUSHTOKEN1 = "testpushtoken1"
    protected static final String TEST_PUSHTOKEN2 = "testpushtoken2"


    Map<String, Object> pushRequest(String url, Map<String, Object> param) {
        pushRequest(url, param, HttpStatus.OK)
    }

    String apiRequest(String url, Map<String, Object> param) {
        apiRequest(url, param, HttpStatus.OK)
    }
    /**
     * http请求
     * @param url 请求地址
     * @param param 参数
     * @param status 期待返回的状态码
     * @return 将http 请求返回的json字符串转换成map
     */
    Map<String, Object> pushRequest(String url, Map<String, Object> param, HttpStatus status) {
        String str = post(url,
            MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.APPLICATION_JSON_UTF8,
            HttpClient.getFormUrlEncodedData(param),
            status).getResponse()
            .getContentAsString();
        if (StringUtil.isEmpty(str)) {
            return new HashMap<>()
        }

        return JSONUtil.parse(
            str,
            Map.class)
    }

    def url = "/push/channels"

    PushChannelVO initData() {
        PushChannelVO pushChannelVo = new PushChannelVO()
        pushChannelVo.setChannelName("test")
        pushChannelVo.setChannelDesc("推送平台test")
        pushChannelVo.setMsgSaveDays(3)
        pushChannelVo.setMaxSendCount(5)
        pushChannelVo.setSecretKey("b2024e00064bc5d8db70fdee087eae4f")
        pushChannelVo.setAndroid(new PushChannelVO.Android(
            new PushChannelVO.Android.HuaweiBean(
                "10819197", "fbfe31923440e417f8fb9f4ce133e3c1",
                "com.proper.mobile.oa.shengjing.htest"),
            new PushChannelVO.Android.XiaomiBean("2AF1VndMLqwLF/4zOHgWNw==",
                "com.proper.mobile.oa.shengjing.htest")))
        pushChannelVo.setIos(new PushChannelVO.IOS(false, "h123456",
            "com.proper.mobile.oa.shengjing.htest"))
        this.addChannel(pushChannelVo)
    }

    PushChannelVO addChannel(PushChannelVO pushChannelVo) {
        def resultContent = post(url, com.proper.enterprise.platform.test.utils.JSONUtil.toJSON(pushChannelVo), HttpStatus.CREATED).getResponse().getContentAsString()
        return com.proper.enterprise.platform.test.utils.JSONUtil.parse(resultContent, PushChannelVO.class)
    }

    void delete(String ids) {
        delete(url + "?ids=" + ids, HttpStatus.NO_CONTENT)
    }
    /**
     * http请求
     * @param url 请求地址
     * @param param 参数
     * @param status 期待返回的状态码
     * @return 将http 请求返回的json字符串转换成map
     */
    String apiRequest(String url, Map<String, Object> param, HttpStatus status) {
        String str = post(url,
            MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.APPLICATION_JSON_UTF8,
            HttpClient.getFormUrlEncodedData(param),
            status).getResponse()
            .getContentAsString();
        if (StringUtil.isEmpty(str)) {
            return ""
        }

        return str
    }

}
