package com.proper.enterprise.platform.notice.server.push.nav.api.huawei

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.proper.enterprise.platform.core.utils.AntResourceUtil
import com.proper.enterprise.platform.notice.server.push.constant.HuaweiConstant
import com.proper.enterprise.platform.test.AbstractTest
import nsp.NSPClient
import nsp.OAuth2Client
import org.junit.Ignore
import org.junit.Test
import org.springframework.core.io.Resource

import java.text.SimpleDateFormat

class HuaweiPushTest extends AbstractTest {

    //@Ignore
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
        //通知栏消息body内容
        JSONObject body = new JSONObject()
        //消息标题
        body.put("title", msgTitle)
        //消息内容体
        body.put("content", msgContent)
        msg.put("body", body.toString())
        //华为PUSH消息总结构体
        JSONObject hps = new JSONObject()
        hps.put("msg", msg)
        //扩展信息，含BI消息统计，特定展示风格，消息折叠。
        JSONObject extJson = new JSONObject()
        //设置消息标签，如果带了这个标签，会在回执中推送给CP用于检测某种类型消息的到达率和状态
        extJson.put("biTag", "Trump")
        // TODO 自定义推送消息在通知栏的图标可在 extJson 中加入 icon 属性，value 为一个公网可以访问的URL
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
}
