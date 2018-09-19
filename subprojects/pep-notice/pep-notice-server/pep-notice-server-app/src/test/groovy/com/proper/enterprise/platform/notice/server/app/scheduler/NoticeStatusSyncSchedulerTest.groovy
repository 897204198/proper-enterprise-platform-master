package com.proper.enterprise.platform.notice.server.app.scheduler

import com.proper.enterprise.platform.notice.server.api.sender.NoticeSender
import com.proper.enterprise.platform.notice.server.app.dao.entity.NoticeEntity
import com.proper.enterprise.platform.notice.server.app.dao.repository.NoticeRepository
import com.proper.enterprise.platform.notice.server.app.vo.NoticeVO
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class NoticeStatusSyncSchedulerTest extends AbstractTest {

    @Autowired
    private NoticeStatusSyncScheduler noticeStatusSyncScheduler

    @Autowired
    private NoticeSender noticeSender

    @Autowired
    private NoticeRepository noticeRepository

    @Test
    public void syncPending() {
        NoticeVO noticeVO = new NoticeVO()
        String appKey = UUID.randomUUID()
        noticeVO.setAppKey(appKey)
        noticeVO.setBatchId("batchId")
        noticeVO.setNoticeType(NoticeType.MOCK)
        noticeVO.setContent("content")
        noticeVO.setTargetTo("aa")
        noticeSender.sendAsync(noticeVO)
        NoticeVO noticeErrVO = new NoticeVO()
        noticeErrVO.setAppKey("mockErrException")
        noticeErrVO.setBatchId("batchId")
        noticeErrVO.setNoticeType(NoticeType.MOCK)
        noticeErrVO.setContent("content")
        noticeErrVO.setTargetTo("aa")
        noticeSender.sendAsync(noticeErrVO)
        NoticeVO noticeRetryVO = new NoticeVO()
        noticeRetryVO.setAppKey("mockRetryStatus")
        noticeRetryVO.setBatchId("batchId")
        noticeRetryVO.setNoticeType(NoticeType.MOCK)
        noticeRetryVO.setContent("content")
        noticeRetryVO.setTargetTo("aa")
        noticeSender.sendAsync(noticeRetryVO)
        Thread.sleep(3000)
        noticeStatusSyncScheduler.syncPending()
        Thread.sleep(3000)
        List<NoticeEntity> notices = noticeRepository.findAll()
        assert notices.size() == 3
        for (NoticeEntity notice : notices) {
            if (appKey.equals(notice.getAppKey())) {
                assert notice.getTargetTo() == "aa"
                assert notice.getStatus() == NoticeStatus.SUCCESS
                continue
            }
            if ("mockErrException".equals(notice.getAppKey())) {
                assert notice.getStatus() == NoticeStatus.FAIL
                assert notice.getErrorMsg() == "error"
                continue
            }
            assert notice.getAppKey() == "mockRetryStatus"
            assert notice.getStatus() == NoticeStatus.RETRY
        }
    }

    @Test
    public void syncRetry() {
        NoticeEntity noticePending = new NoticeEntity()
        String appKey = UUID.randomUUID()
        noticePending.setAppKey(appKey)
        noticePending.setBatchId("batchId")
        noticePending.setNoticeType(NoticeType.MOCK)
        noticePending.setContent("content")
        noticePending.setTargetTo("aa")
        noticePending.setStatus(NoticeStatus.RETRY)
        noticePending.setRetryCount(1)
        noticeRepository.save(noticePending)
        NoticeEntity noticeFail = new NoticeEntity()
        noticeFail.setAppKey("mockErrSend")
        noticeFail.setBatchId("batchId")
        noticeFail.setNoticeType(NoticeType.MOCK)
        noticeFail.setContent("content")
        noticeFail.setTargetTo("aa")
        noticeFail.setStatus(NoticeStatus.RETRY)
        noticeFail.setRetryCount(1)
        noticeRepository.save(noticeFail)
        Thread.sleep(3000)
        noticeStatusSyncScheduler.syncRetry()
        Thread.sleep(3000)
        List<NoticeEntity> notices = noticeRepository.findAll()
        assert notices.size() == 2
        for (NoticeEntity notice : notices) {
            if (appKey.equals(notice.getAppKey())) {
                assert notice.getTargetTo() == "aa"
                assert notice.getStatus() == NoticeStatus.PENDING
                continue
            }
            assert notice.getStatus() == NoticeStatus.FAIL
        }

    }

}
