package com.proper.enterprise.platform.notice.service


import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.notice.model.NoticeModel
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@Sql("/com/proper/enterprise/platform/notice/sql/datadics.sql")
class NoticeServiceImplTest extends AbstractTest {

    @Autowired
    NoticeService noticeService

    @Test
    void sendNotice() {
        Map<String, Object> custom = new HashMap<>(1)
        custom.put("pageUrl", "messages")

        NoticeModel noticeModel1 = new NoticeModel();
        noticeModel1.setSystemId("ihos")
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
