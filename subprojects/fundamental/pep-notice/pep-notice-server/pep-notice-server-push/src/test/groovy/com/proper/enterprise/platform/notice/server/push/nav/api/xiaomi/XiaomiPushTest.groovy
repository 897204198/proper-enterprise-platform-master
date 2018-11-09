package com.proper.enterprise.platform.notice.server.push.nav.api.xiaomi

import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.utils.JSONUtil
import com.xiaomi.xmpush.server.Message
import com.xiaomi.xmpush.server.Result
import com.xiaomi.xmpush.server.Sender
import com.xiaomi.xmpush.server.Tracer
import org.junit.Ignore
import org.junit.Test

import static com.proper.enterprise.platform.test.utils.JSONUtil.toJSON
import static com.xiaomi.push.sdk.ErrorCode.Success

import static com.proper.enterprise.platform.notice.server.push.constant.XiaomiConstant.PUSHTOKEN
import static com.proper.enterprise.platform.notice.server.push.constant.XiaomiConstant.APPSECRET
import static com.proper.enterprise.platform.notice.server.push.constant.XiaomiConstant.PACKAGENAME

@Ignore
class XiaomiPushTest extends AbstractJPATest {


    @Test
    void testPushMessage() {
        def mCustomer = new HashMap()
        mCustomer.put("gdpr_mpage", "examList")
        def mCustomerString = toJSON(mCustomer)
        Message.Builder msgBuilder = new Message.Builder()
        msgBuilder.title("test")
        msgBuilder.description('test')
        msgBuilder.passThrough(0)
        msgBuilder.restrictedPackageName(PACKAGENAME)
        msgBuilder.payload(mCustomerString)
        Message build = msgBuilder.build()

        def sender = new Sender(APPSECRET)
        Result result = sender.send(build, PUSHTOKEN, 1)
        println("MessageID :" + result.getMessageId())
        assert result.getErrorCode() == Success
    }

    /**
     * 追踪Api不好用
     */
    @Test
    void testFailTracer() {
        Tracer tracer = new Tracer(APPSECRET)
        def string = tracer.getMessageStatus("qweqew", 1)
        Map<String, Object> map = JSONUtil.parse(string, Map.class)
        assert "ok" == map.get("result")
    }

    /**
     * 获取时间段内的前一百条消息状态
     */
    @Test
    void testFailTracer2() {
        Tracer tracer = new Tracer(APPSECRET)
        long start = DateUtil.toDate("2018-09-09").getTime()
        long end = DateUtil.toDate("2018-09-11").getTime()
        def getMessageStatus = tracer.getMessageStatus(start, end, 1)
        println(getMessageStatus)
    }
}
