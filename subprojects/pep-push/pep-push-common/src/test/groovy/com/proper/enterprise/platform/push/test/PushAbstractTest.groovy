package com.proper.enterprise.platform.push.test

import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.core.utils.StringUtil
import com.proper.enterprise.platform.core.utils.http.HttpClient
import com.proper.enterprise.platform.test.AbstractTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

/**
 * 推送相关的推送支持类
 * Created by shen on 2017/5/11.
 */
abstract class PushAbstractTest extends AbstractTest {

    protected static final String VALID_APPKEY="test"
    protected static final String TEST_USERID1="testuser1"
    protected static final String TEST_USERID2="testuser2"
    protected static final String TEST_DEVICEID1="testdevice1"
    protected static final String TEST_DEVICEID2="testdevice2"
    protected static final String TEST_DEVICEID3="testdevice3"

    protected static final String TEST_PUSHTOKEN1="testpushtoken1"
    protected static final String TEST_PUSHTOKEN2="testpushtoken2"


    Map<String, Object> pushRequest(String url, Map<String, Object> param) {
        pushRequest(url,param,HttpStatus.OK)
    }
    /**
     * http请求
     * @param url 请求地址
     * @param param 参数
     * @param status 期待返回的状态码
     * @return 将http 请求返回的json字符串转换成map
     */
    Map<String, Object> pushRequest(String url, Map<String, Object> param,HttpStatus status) {
        String str=post(url,
            MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.APPLICATION_JSON_UTF8,
            HttpClient.getFormUrlEncodedData(param),
            status).getResponse()
            .getContentAsString();
        if(StringUtil.isEmpty(str)){
            return new HashMap<>()
        }

        return JSONUtil.parse(
            str,
            Map.class)
    }

}
