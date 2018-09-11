package com.proper.enterprise.platform.notice.server.push.handler.xiaomi

import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.test.AbstractTest
import com.xiaomi.xmpush.server.Message
import com.xiaomi.xmpush.server.Result
import com.xiaomi.xmpush.server.Sender
import com.xiaomi.xmpush.server.Tracer
import org.junit.Ignore
import org.junit.Test

import static com.proper.enterprise.platform.test.utils.JSONUtil.toJSON
import static com.xiaomi.push.sdk.ErrorCode.Success

class XiaomiPushMessageTest extends AbstractTest{

    @Ignore
    @Test
    void testPushMessage(){
        def mCustomer = new HashMap()
        mCustomer.put("gdpr_mpage", "examList")
        def mCustomerString = toJSON(mCustomer)

        Message.Builder msgBuilder = new Message.Builder()
        msgBuilder.title("test zhanglei")
        msgBuilder.description('test zahnglei')
        msgBuilder.passThrough(0)
        msgBuilder.restrictedPackageName("com.proper.icmp.dev")
        msgBuilder.payload(mCustomerString)
        Message build = msgBuilder.build()

        def sender = new Sender("RGW+NA+T2ucpEX0a6bxyhA==")
        Result result = sender.send(build, "o4gN1LuTsk6/CM7TKf0tTbj2MIWimxTGxRo8yZFQTJAhNsGlEeZLbMIeYnZ9BshJ", 1)
        println("MessageID :" + result.getMessageId())
        assert result.getErrorCode() == Success
    }

    @Ignore
    @Test
    void testFailTracer(){
        Tracer tracer = new Tracer("RGW+NA+T2ucpEX0a6bxyhA==")
        def string = tracer.getMessageStatus(" ", 1)
        println("tracer :" + string)
    }

    @Ignore
    @Test
    void testFailTracer2(){
        Tracer tracer = new Tracer("RGW+NA+T2ucpEX0a6bxyhA==")
        long start = DateUtil.toDate("2018-09-09").getTime()
        long end = DateUtil.toDate("2018-09-11").getTime()
        def getMessageStatus = tracer.getMessageStatus(start, end, 1)
        println("getMessageStatus" + getMessageStatus)
    }
}
