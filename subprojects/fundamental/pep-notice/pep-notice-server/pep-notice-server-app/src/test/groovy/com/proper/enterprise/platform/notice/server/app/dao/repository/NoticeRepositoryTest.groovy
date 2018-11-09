package com.proper.enterprise.platform.notice.server.app.dao.repository

import com.proper.enterprise.platform.notice.server.app.dao.entity.NoticeEntity
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.context.jdbc.Sql

class NoticeRepositoryTest extends AbstractJPATest {

    @Autowired
    private NoticeRepository noticeRepository

    @Test
    @NoTx
    public void saveTest() {
        NoticeEntity noticeEntity = new NoticeEntity()
        noticeEntity.setAppKey("appKey")
        noticeEntity.setBatchId("batchId")
        noticeEntity.setNoticeType(NoticeType.EMAIL)
        noticeEntity.setStatus(NoticeStatus.PENDING)
        noticeEntity.setRetryCount(1)
        noticeEntity.setNoticeExtMsg("a", "a")
        noticeEntity.setTargetExtMsg("b", "b")
        noticeEntity.setContent("content")
        noticeEntity.setTargetTo("to")
        NoticeEntity save = noticeRepository.save(noticeEntity)
        assert save.getAppKey() == "appKey"
        assert save.getBatchId() == "batchId"
        assert save.getContent() == "content"
        assert save.getTargetTo() == "to"
        assert save.getNoticeType() == NoticeType.EMAIL
        assert save.getStatus() == NoticeStatus.PENDING
        assert save.getRetryCount() == 1
        assert save.getNoticeExtMsgMap().get("a") == "a"
        assert save.getTargetExtMsgMap().get("b") == "b"
        noticeRepository.deleteAll()
    }

    @Test
    @Sql("/com/proper/enterprise/platform/notice/server/app/dao/sql/pendingNotices.sql")
    public void findPendingNoticesTest() {
        List<NoticeEntity> notices = noticeRepository.findPendingNotices("2018-09-03 17:30:21", "2018-09-03 17:32:22")
        assert notices.size() == 2
    }

    @Test
    @Sql("/com/proper/enterprise/platform/notice/server/app/dao/sql/retryNotices.sql")
    public void findRetryNoticesTest() {
        List<NoticeEntity> notices = noticeRepository
            .findRetryNotices("2018-09-03 17:30:21", "2018-09-03 17:32:22"
            , 3)
        assert notices.size() == 1
    }

    @Test
    @Sql("/com/proper/enterprise/platform/notice/server/app/dao/sql/queryNotices.sql")
    public void queryNoticesTest() {
        List<NoticeEntity> noticesNoParam = noticeRepository
            .findAll(null, null, null, null, null, null, null, null)
        assert noticesNoParam.size() == 5
        assert noticesNoParam.get(0).getId() == '1'
        List<NoticeEntity> noticesById = noticeRepository
            .findAll("1", null, null, null, null, null, null, null)
        assert noticesById.size() == 1
        assert noticesById.get(0).getStatus() == NoticeStatus.RETRY
        List<NoticeEntity> noticesByAppKey = noticeRepository
            .findAll(null, "1", null, null, null, null, null, null)
        assert noticesByAppKey.size() == 2
        List<NoticeEntity> notices = noticeRepository
            .findAll("1", "1", "1", "1", "1",
            NoticeType.EMAIL, null, NoticeStatus.RETRY)
        assert notices.size() == 1
    }


    @Test
    @Sql("/com/proper/enterprise/platform/notice/server/app/dao/sql/queryNotices.sql")
    public void queryNoticesPageTest() {
        PageRequest pageRequest = new PageRequest(0, 2)
        Page<NoticeEntity> noticesPage = noticeRepository
            .findAll(null,
            null,
            null,
            null,
            null,
            null,
            null,
            null, pageRequest)
        assert noticesPage.getTotalElements() == 5
        assert noticesPage.getContent().size() == 2
        assert noticesPage.getContent().get(0).getId() == '1'
        PageRequest pageRequest2 = new PageRequest(0, 2, new Sort(new Sort.Order(Sort.Direction.DESC,"id")))
        Page<NoticeEntity> noticesPage2 = noticeRepository
            .findAll(null,
            null,
            null,
            null,
            null,
            null,
            null,
            null, pageRequest2)
        assert noticesPage2.getTotalElements() == 5
        assert noticesPage2.getContent().size() == 2
        assert noticesPage2.getContent().get(1).getId() == '3'
    }
}
