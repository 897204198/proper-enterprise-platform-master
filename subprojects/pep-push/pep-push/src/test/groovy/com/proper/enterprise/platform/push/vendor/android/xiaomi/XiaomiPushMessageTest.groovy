package com.proper.enterprise.platform.push.vendor.android.xiaomi

import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.test.AbstractTest
import com.xiaomi.push.sdk.ErrorCode
import com.xiaomi.xmpush.server.Message
import com.xiaomi.xmpush.server.Result
import com.xiaomi.xmpush.server.Sender
import com.xiaomi.xmpush.server.Tracer
import org.junit.Test

import static com.proper.enterprise.platform.test.utils.JSONUtil.toJSON

/**
 * @Author: LeiLei
 * @Date: 2018/9/10 9:37
 */
class XiaomiPushMessageTest extends AbstractTest{

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
        Tracer tracer = new Tracer("RGW+NA+T2ucpEX0a6bxyhA==")
        def string = tracer.getMessageStatus(result.getMessageId(), 1)
        println("tracer :" + string)
        assert result.getErrorCode() == ErrorCode.Success
    }

    /**
     * 查询错误的msgId
     */
    @Test
    void testFailTracer(){
        Tracer tracer = new Tracer("RGW+NA+T2ucpEX0a6bxyhA==")
        def string = tracer.getMessageStatus("sdm02973536556973925t", 1)
        println("tracer :" + string)
    }

    @Test
    void testFailTracer2(){
        Tracer tracer = new Tracer("RGW+NA+T2ucpEX0a6bxyhA==")
//        long start = DateUtil.toDateString(DateUtil.toDate("2018-09-10 08:00:00", PEPConstants.DEFAULT_DATETIME_FORMAT)) as long
//        long end = DateUtil.toDateString(DateUtil.toDate("2018-09-10 13:40:00", PEPConstants.DEFAULT_DATETIME_FORMAT)) as long


        long start = Date.parse(DateUtil.toDateString(DateUtil.addDay(new Date(), -1)))
        long end = Date.parse(DateUtil.toDateString(new Date()))
        def getMessageStatus = tracer.getMessageStatus(start, end, 1)
        println("getMessageStatus" + getMessageStatus)
        def string = tracer.getMessageStatus("sdm02973536556973925t", 1)
        println("tracer :" + string)
    }
}
