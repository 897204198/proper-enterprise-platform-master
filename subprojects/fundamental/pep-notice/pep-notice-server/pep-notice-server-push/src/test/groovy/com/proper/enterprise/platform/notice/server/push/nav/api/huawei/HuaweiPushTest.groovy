package com.proper.enterprise.platform.notice.server.push.nav.api.huawei

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.proper.enterprise.platform.core.utils.AntResourceUtil
import com.proper.enterprise.platform.core.utils.http.HttpClient
import com.proper.enterprise.platform.notice.server.push.constant.HuaweiConstant
import com.proper.enterprise.platform.test.AbstractJPATest
import nsp.NSPClient
import nsp.OAuth2Client
import org.junit.Ignore
import org.junit.Test
import org.springframework.core.io.Resource
import org.springframework.http.MediaType

import java.text.MessageFormat
import java.text.SimpleDateFormat

class HuaweiPushTest extends AbstractJPATest {

    @Ignore
    @Test
    void "testHuaweiPushApp"() {
        // Token of test device
        def token = HuaweiConstant.TARGET_TO
        def msgTitle = System.getProperty('os.name')
        def msgContent = "$msgTitle ${System.getProperty('os.arch')} push this notification to test Huawei push app at ${new Date().format('yyyy-MM-dd HH:mm:ss')} in test case"

        OAuth2Client oAuth2Client = new OAuth2Client()
        Resource[] resources = AntResourceUtil.getResources(HuaweiConstant.CENT_PATH)
        oAuth2Client.initKeyStoreStream(resources[0].inputStream, HuaweiConstant.PASSWORD)
        String accessToken = oAuth2Client.getAccessToken('client_credentials', HuaweiConstant.CLIENT_ID, HuaweiConstant.CLIENT_SECRET).getAccess_token()

        NSPClient nspClient = new NSPClient(accessToken)
        nspClient.initKeyStoreStream(resources[0].inputStream, HuaweiConstant.PASSWORD)
        nspClient.setApiUrl("https://api.push.hicloud.com/pushsend.do")
        def attributes = [:]
        attributes['ver'] = '1'
        attributes['appId'] = HuaweiConstant.CLIENT_ID

        JSONObject msg = new JSONObject()
        msg.put("type", 3)
        JSONObject action = new JSONObject()
        action.put("type", 3)
        JSONObject param = new JSONObject()
        param.put("appPkgName", HuaweiConstant.PACKAGE_NAME)
        action.put("param", param)
        msg.put("action", action)
        //???????????????body??????
        JSONObject body = new JSONObject()
        //????????????
        body.put("title", msgTitle)
        //???????????????
        body.put("content", msgContent)
        msg.put("body", body.toString())
        //??????PUSH??????????????????
        JSONObject hps = new JSONObject()
        hps.put("msg", msg)
        //??????????????????BI???????????????????????????????????????????????????
        JSONObject extJson = new JSONObject()
        //????????????????????????????????????????????????????????????????????????CP???????????????????????????????????????????????????
        extJson.put("biTag", "Trump")
        // TODO ???????????????????????????????????????????????? extJson ????????? icon ?????????value ??????????????????????????????URL
        hps.put("ext", extJson)

        JSONObject payload = new JSONObject()
        payload.put("hps", hps)

        def nspparam = [:]
        JSONArray deviceTokens = new JSONArray()
        deviceTokens.add(token)
        nspparam['device_token_list'] = deviceTokens.toString()
        nspparam['payload'] = payload.toString()
        nspparam['expire_time'] = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date(System.currentTimeMillis() + 3600 * 1000))
        String response = nspClient.call("openpush.message.api.send", nspparam, String.class, attributes)

        expect:
        assert response.contains("Success")
    }

    @Ignore
    @Test
    void "testHuaweiAccessToken"() {
        // Token of test device
        def token = HuaweiConstant.TARGET_TO
        def msgTitle = System.getProperty('os.name')
        def msgContent = "$msgTitle ${System.getProperty('os.arch')} push this notification to test Huawei push app at ${new Date().format('yyyy-MM-dd HH:mm:ss')} in test case"

        OAuth2Client oAuth2Client = new OAuth2Client()
        Resource[] resources = AntResourceUtil.getResources(HuaweiConstant.CENT_PATH)
        oAuth2Client.initKeyStoreStream(resources[0].inputStream, HuaweiConstant.PASSWORD)
        String accessToken = oAuth2Client.getAccessToken('client_credentials', HuaweiConstant.CLIENT_ID, HuaweiConstant.CLIENT_SECRET).getAccess_token()
        String accessToken2 = oAuth2Client.getAccessToken('client_credentials', HuaweiConstant.CLIENT_ID, HuaweiConstant.CLIENT_SECRET).getAccess_token()
        println(accessToken)
        println(accessToken2)

        def accessToken3 = 'CFuiHS2lNkvxO+RXj9tZdus4jnniaQBApHs01WxBjXz7j3cxBEp7oCehoKMzBjdhATfpZPqPivjDy9Y0GW7JWQ=='

        NSPClient nspClient = new NSPClient(accessToken)
        nspClient.initKeyStoreStream(resources[0].inputStream, HuaweiConstant.PASSWORD)
        nspClient.setApiUrl("https://api.push.hicloud.com/pushsend.do")

        NSPClient nspClient2 = new NSPClient(accessToken2)
        nspClient2.initKeyStoreStream(resources[0].inputStream, HuaweiConstant.PASSWORD)
        nspClient2.setApiUrl("https://api.push.hicloud.com/pushsend.do")

        NSPClient nspClient3 = new NSPClient(accessToken3)
        nspClient3.initKeyStoreStream(resources[0].inputStream, HuaweiConstant.PASSWORD)
        nspClient3.setApiUrl("https://api.push.hicloud.com/pushsend.do")

        def attributes = [:]
        attributes['ver'] = '1'
        attributes['appId'] = HuaweiConstant.CLIENT_ID

        JSONObject msg = new JSONObject()
        msg.put("type", 3)
        JSONObject action = new JSONObject()
        action.put("type", 3)
        JSONObject param = new JSONObject()
        param.put("appPkgName", HuaweiConstant.PACKAGE_NAME)
        action.put("param", param)
        msg.put("action", action)
        //???????????????body??????
        JSONObject body = new JSONObject()
        //????????????
        body.put("title", msgTitle)
        //???????????????
        body.put("content", msgContent)
        msg.put("body", body.toString())
        //??????PUSH??????????????????
        JSONObject hps = new JSONObject()
        hps.put("msg", msg)
        //??????????????????BI???????????????????????????????????????????????????
        JSONObject extJson = new JSONObject()
        //????????????????????????????????????????????????????????????????????????CP???????????????????????????????????????????????????
        extJson.put("biTag", "Trump")
        hps.put("ext", extJson)

        JSONObject payload = new JSONObject()
        payload.put("hps", hps)

        def nspparam = [:]
        JSONArray deviceTokens = new JSONArray()
        deviceTokens.add(token)
        nspparam['device_token_list'] = deviceTokens.toString()
        nspparam['payload'] = payload.toString()
        nspparam['expire_time'] = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date(System.currentTimeMillis() + 3600 * 1000))
        String response = nspClient.call("openpush.message.api.send", nspparam, String.class, attributes)
        String response2 = nspClient.call("openpush.message.api.send", nspparam, String.class, attributes)
        String response3 = nspClient.call("openpush.message.api.send", nspparam, String.class, attributes)

        expect:
        assert response.contains("Success")
        assert response2.contains("Success")
    }

    @Ignore
    @Test
    void "testNavHuaweiPushApp"() {
        // Token of test device
        def token = HuaweiConstant.TARGET_TO
        def msgTitle = System.getProperty('os.name')
        def msgContent = "$msgTitle ${System.getProperty('os.arch')} push this notification to test Huawei push app at ${new Date().format('yyyy-MM-dd HH:mm:ss')} in test case"

        // ??????access_token
        String tokenUrl = "https://login.cloud.huawei.com/oauth2/v2/token"
        String msgBody = MessageFormat.format(
            "grant_type=client_credentials&client_secret={0}&client_id={1}",
            URLEncoder.encode(HuaweiConstant.CLIENT_SECRET, "UTF-8"), HuaweiConstant.CLIENT_ID)
        String response = new String(HttpClient.post(tokenUrl, MediaType.APPLICATION_FORM_URLENCODED, msgBody).getBody(), "UTF-8")
        def accessToken = JSONObject.parseObject(response).getString("access_token")

        JSONObject msg = new JSONObject()
        //3: ???????????????????????????????????????????????????????????????
        msg.put("type", 3)
        JSONObject action = new JSONObject()
        //??????3?????????APP??????????????????????????????????????????
        action.put("type", 3)
        JSONObject param = new JSONObject()
        param.put("appPkgName", HuaweiConstant.PACKAGE_NAME)
        action.put("param", param)
        msg.put("action", action)
        //???????????????body??????
        JSONObject body = new JSONObject()
        //????????????
        body.put("title", msgTitle)
        //???????????????
        body.put("content", msgContent)
        msg.put("body", body.toString())
        //??????PUSH??????????????????
        JSONObject hps = new JSONObject()
        hps.put("msg", msg)
        //??????????????????BI???????????????????????????????????????????????????
        JSONObject extJson = new JSONObject()
        //????????????????????????????????????????????????????????????????????????CP???????????????????????????????????????????????????
        extJson.put("biTag", "Trump")
        hps.put("ext", extJson)

        JSONObject payload = new JSONObject()
        payload.put("hps", hps)

        JSONArray deviceTokens = new JSONArray()
        deviceTokens.add(token)

        String format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date(System.currentTimeMillis() + 3600 * 1000))
        String postBody = MessageFormat.format(
            "access_token={0}&nsp_svc={1}&nsp_ts={2}&device_token_list={3}&payload={4}&expire_time={5}",
            URLEncoder.encode(accessToken, "UTF-8"),
            URLEncoder.encode("openpush.message.api.send", "UTF-8"),
            URLEncoder.encode(String.valueOf(System.currentTimeMillis() / 1000), "UTF-8"),
            URLEncoder.encode(deviceTokens.toString(), "UTF-8"),
            URLEncoder.encode(payload.toString(), "UTF-8"),
            URLEncoder.encode(format, "UTF-8"))
        String postUrl = "https://api.push.hicloud.com/pushsend.do?nsp_ctx=" + URLEncoder.encode("{\"ver\":\"1\", \"appId\":\"" + HuaweiConstant.CLIENT_ID + "\"}", "UTF-8")
        def result = new String(HttpClient.post(postUrl, MediaType.APPLICATION_FORM_URLENCODED, postBody).getBody(), "UTF-8")

        System.setProperty('push_env', 'Fire in test')

        expect:
        assert result.contains("Success")
    }
}
