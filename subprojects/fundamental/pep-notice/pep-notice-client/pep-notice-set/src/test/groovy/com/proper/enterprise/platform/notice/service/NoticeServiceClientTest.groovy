package com.proper.enterprise.platform.notice.service

import com.proper.enterprise.platform.notice.document.NoticeSetDocument
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
        Set<String> userIds = new HashSet<>(3)
        userIds.add("test_bpm_user01")
        userIds.add("test_bpm_user02")
        userIds.add("test_bpm_user03")
        Map<String, NoticeSetDocument> result = noticeSetService.findMapByCatalogAndUserIds("BPM", userIds)
        NoticeSetDocument noticeSetDocument = result.get("test_bpm_user01")
        assert noticeSetDocument.noticeChannel == ["sms","email"]
        noticeSetDocument = result.get("test_bpm_user02")
        assert noticeSetDocument.noticeChannel == ["sms","email"]
        noticeSetDocument = result.get("test_bpm_user03")
        assert noticeSetDocument.noticeChannel == ["push","email"]
    }

    @Before
    void init() {
        NoticeSetDocument noticeSetDocument1 = new NoticeSetDocument()
        noticeSetDocument1.setUserId("test_bpm_user01")
        noticeSetDocument1.setCatalog("BPM")
        noticeSetDocument1.noticeChannel = ["sms","email"]
        noticeSetRepository.save(noticeSetDocument1)

        NoticeSetDocument noticeSetDocument2 = new NoticeSetDocument()
        noticeSetDocument2.setUserId("test_bpm_user02")
        noticeSetDocument2.setCatalog("BPM")
        noticeSetDocument2.noticeChannel = ["sms","email"]
        noticeSetRepository.save(noticeSetDocument2)
    }

}
