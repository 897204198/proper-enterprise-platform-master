package com.proper.enterprise.platform.notice.service

import com.proper.enterprise.platform.notice.entity.NoticeSetDocument
import com.proper.enterprise.platform.notice.repository.NoticeSetRepository
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class NoticeServiceClientTest extends AbstractJPATest {

    @Autowired
    NoticeSetService noticeSetService

    @Autowired
    NoticeSetRepository noticeSetRepository

    @Test
    void findByNoticeTypeAndUserId() {
        Set<String> userIds = new HashSet<>(3)
        userIds.add("test_bpm_user01")
        userIds.add("test_bpm_user02")
        userIds.add("test_bpm_user03")
        Map<String, NoticeSetDocument> result = noticeSetService.findMapByNoticeTypeAndUserIds("BPM", userIds)
        NoticeSetDocument noticeSetDocument = result.get("test_bpm_user01")
        assert noticeSetDocument.email == true
        assert noticeSetDocument.push == false
        assert noticeSetDocument.sms == true
        noticeSetDocument = result.get("test_bpm_user02")
        assert noticeSetDocument.email == true
        assert noticeSetDocument.push == false
        assert noticeSetDocument.sms == true
        noticeSetDocument = result.get("test_bpm_user03")
        assert noticeSetDocument == null
    }

    @Before
    void init() {
        NoticeSetDocument noticeSetDocument1 = new NoticeSetDocument()
        noticeSetDocument1.setUserId("test_bpm_user01")
        noticeSetDocument1.setNoticeType("BPM")
        noticeSetDocument1.setPush(false)
        noticeSetDocument1.setSms(true)
        noticeSetDocument1.setEmail(true)
        noticeSetRepository.save(noticeSetDocument1)

        NoticeSetDocument noticeSetDocument2 = new NoticeSetDocument()
        noticeSetDocument2.setUserId("test_bpm_user02")
        noticeSetDocument2.setNoticeType("BPM")
        noticeSetDocument2.setPush(false)
        noticeSetDocument2.setSms(true)
        noticeSetDocument2.setEmail(true)
        noticeSetRepository.save(noticeSetDocument2)
    }

}
