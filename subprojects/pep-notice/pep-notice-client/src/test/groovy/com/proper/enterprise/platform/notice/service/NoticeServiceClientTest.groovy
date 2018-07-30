package com.proper.enterprise.platform.notice.service


import com.proper.enterprise.platform.notice.entity.NoticeSetDocument
import com.proper.enterprise.platform.notice.repository.NoticeSetRepository
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class NoticeServiceClientTest extends AbstractTest {

    @Autowired
    NoticeSetService noticeSetService

    @Autowired
    NoticeSetRepository noticeSetRepository

    @Test
    void findByNoticeTypeAndUserId() {
        NoticeSetDocument noticeSetEntity = noticeSetService.findByNoticeTypeAndUserId("BPM", "test_bpm_user01")
        assert noticeSetEntity.email == true
        assert noticeSetEntity.push == false
        assert noticeSetEntity.sms == true
    }

    @Before
    void init() {
        NoticeSetDocument noticeSetDocument = new NoticeSetDocument()
        noticeSetDocument.setUserId("test_bpm_user01")
        noticeSetDocument.setNoticeType("BPM")
        noticeSetDocument.setPush(false)
        noticeSetDocument.setSms(true)
        noticeSetDocument.setEmail(true)
        noticeSetRepository.save(noticeSetDocument)
    }

}
