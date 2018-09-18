package com.proper.enterprise.platform.notice.controller

import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.core.utils.StringUtil
import com.proper.enterprise.platform.core.utils.http.HttpClient
import com.proper.enterprise.platform.notice.entity.PushDeviceEntity
import com.proper.enterprise.platform.notice.enums.PushDeviceType
import com.proper.enterprise.platform.notice.enums.PushMode
import com.proper.enterprise.platform.notice.repository.PushDeviceRepository
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

class PushDeviceControllerTest extends AbstractTest{

    private static final String URL_STARTPUSH = "/push/device"

    @Autowired
    PushDeviceRepository deviceRepo

    def appkey = "appkey-123"
    def deviceTypeInvalid = "invalid_devicetype"
    def pushToken = "push_token_test"
    def userid = "userid_test"
    def deviceid = "deviceid_test"

    @After
    void afterData(){
        deviceRepo.deleteAll();
    }

    @Test
    void save() {

        Map<String, Object> param = new HashMap<>()

        param.put("appkey", appkey)

        //合法deviceType
        param.put("device_type", PushDeviceType.android.toString())
        pushRequest(URL_STARTPUSH, param)

        //合法的push_mode
        param.put("push_mode", PushMode.huawei)
        pushRequest(URL_STARTPUSH, param)

        //注册设备
        param.put("push_token", pushToken)
        param.put("userid", userid)
        param.put("deviceid", deviceid)
        pushRequest(URL_STARTPUSH, param)
        PushDeviceEntity p = deviceRepo.findByUserId(userid)
        assert p.userId == userid
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

    Map<String, Object> pushRequest(String url, Map<String, Object> param) {
        pushRequest(url, param, HttpStatus.OK)
    }
}
