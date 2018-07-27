package com.proper.enterprise.platform.notice.service

import com.proper.enterprise.platform.notice.entity.NoticeSetEntity
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

@Sql("/com/proper/enterprise/platform/notice/sql/datadics.sql")
class NoticeServiceSetImplTest extends AbstractTest {

    @Autowired
    NoticeSetService noticeSetService

    @Test
    void queryResultWithoutType() {
        NoticeSetEntity noticeSetEntity = noticeSetService.findNoticeSetsByNoticeTypeAndUserId("BPM", "test_bpm_user01")
        assert noticeSetEntity.email == true
        assert noticeSetEntity.push == false
        assert noticeSetEntity.sms == true
    }
}
