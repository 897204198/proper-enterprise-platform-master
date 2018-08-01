package com.proper.enterprise.platform.notice.service


import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.notice.model.NoticeModel
import com.proper.enterprise.platform.push.vo.PushChannelVO
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@Sql("/com/proper/enterprise/platform/notice/sql/NoticeTest.sql")
class NoticeServiceImplTest extends AbstractTest {

    @Autowired
    NoticeService noticeService

    def url = "/push/channels"

    @Before
    void init() {
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

    @Test
    void sendNotice() {
        Map<String, Object> custom = new HashMap<>(1)
        custom.put("pageUrl", "messages")

        NoticeModel noticeModel1 = new NoticeModel();
        noticeModel1.setSystemId("test")
        noticeModel1.setBusinessId("testBpm")
        noticeModel1.setBusinessName("测试流程")
        noticeModel1.setNoticeType("BPM")
        noticeModel1.setTarget("ihos1")
        noticeModel1.setTitle("bpm1")
        noticeModel1.setContent("noticeModel1")
        noticeModel1.setCustom(custom)
        noticeModel1.setNoticeChannel("PUSH")
        post('/notice', JSONUtil.toJSON(noticeModel1), HttpStatus.CREATED)

        def result = JSONUtil.parse(get('/notice/PUSH', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result.size() == 1
    }

}
