package com.proper.enterprise.platform.notice.service

import com.proper.enterprise.platform.core.utils.JSONUtil
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
        Set<String> targets = new HashSet<>()
        targets.add("IHOS1")
        targets.add("IHOS2")
        //Default
        noticeService.sendNotice("ihos", "testBpm", "测试流程", "BPM", targets, "bpm1", "this is a bpm test.", custom , "PUSH")
        noticeService.sendNotice("ihos", "testBpm", "测试流程", "BPM", "IHOS3", "bpm2", "this is a bpm test.", custom , "PUSH")

        def result = JSONUtil.parse(get('/notice/PUSH', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result.size() == 3
    }

}
