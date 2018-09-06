package com.proper.enterprise.platform.notice.server.app.sender

import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.notice.server.api.model.Notice
import com.proper.enterprise.platform.notice.server.api.sender.NoticeSender
import com.proper.enterprise.platform.notice.server.app.dao.repository.NoticeRepository
import com.proper.enterprise.platform.notice.server.app.global.SingletonMap
import com.proper.enterprise.platform.notice.server.app.vo.NoticeVO
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeRequest
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeTarget
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NoticeSenderTest extends AbstractTest {

    @Autowired
    private NoticeSender noticeSender

    @Autowired
    private NoticeRepository noticeRepository

    @Test
    public void beforeSendTest() {
        NoticeRequest noticeRequest = new NoticeRequest()
        noticeRequest.setBatchId("batchId")
        noticeRequest.setNoticeType(NoticeType.MOCK)
        noticeRequest.setContent("content")
        List<NoticeTarget> targets = new ArrayList<>()
        NoticeTarget target = new NoticeTarget()
        target.setTo("1")
        NoticeTarget target2 = new NoticeTarget()
        target2.setTo("2")
        targets.add(target)
        targets.add(target2)
        noticeRequest.setTargets(targets)
        List<Notice> notices = noticeSender.beforeSend("qqq", noticeRequest)
        assert notices.size() == 2
        assert notices.get(0).getTitle() == "bbb"
    }

    @Test
    public void afterSendTest() {
        SingletonMap.getSingletMap().put("mockErrAfter", "a")
        NoticeVO noticeVO = new NoticeVO()
        noticeVO.setAppKey("mockErrAfter")
        noticeVO.setNoticeType(NoticeType.MOCK)
        noticeSender.afterSend(noticeVO)
        assert "a" == SingletonMap.getSingletMap().get("mockErrAfter")
        Thread.sleep(3000)
        assert "b" == SingletonMap.getSingletMap().get("mockErrAfter")
    }

    @Test
    public void sendAsyncTest() {
        noticeRepository.deleteAll()
        NoticeVO noticeVO = new NoticeVO()
        String appKey = UUID.randomUUID()
        noticeVO.setAppKey(appKey)
        noticeVO.setBatchId("batchId")
        noticeVO.setNoticeType(NoticeType.MOCK)
        noticeVO.setContent("content")
        noticeVO.setTargetTo("aa")
        noticeSender.sendAsync(noticeVO)
        Thread.sleep(5000)
        assert appKey == SingletonMap.getSingletMap().get(appKey)
        assert noticeRepository.findAll().size() == 1
        assert noticeRepository.findAll().get(0).getBatchId() == "batchId"
    }

    @Test
    @NoTx
    @Sql("/com/proper/enterprise/platform/notice/server/app/service/sql/syncPendingNoticesStatusAsync.sql")
    public void syncPendingNoticesStatusAsync() {
        DateTimeFormatter dfm = DateTimeFormatter.ofPattern(PEPConstants.DEFAULT_DATETIME_FORMAT)
        noticeSender.syncPendingNoticesStatusAsync(LocalDateTime.parse("2018-09-03 17:30:21", dfm),
            LocalDateTime.parse("2018-09-03 17:32:22", dfm))
        Thread.sleep(5000)
        assert noticeRepository.findOne('1-PENDING').getStatus() == NoticeStatus.SUCCESS
        assert noticeRepository.findOne('4-PENDING').getStatus() == NoticeStatus.RETRY
        assert noticeRepository.findOne('4-PENDING').getRetryCount() == 2
        assert noticeRepository.findOne('2-PENDING').getStatus() == NoticeStatus.FAIL
        assert noticeRepository.findOne('2-PENDING').getErrorMsg() == "Max retry"
        noticeRepository.deleteAll()
    }

    @Test
    @NoTx
    @Sql("/com/proper/enterprise/platform/notice/server/app/service/sql/retryNoticesAsync.sql")
    public void retryNoticesAsync() {
        DateTimeFormatter dfm = DateTimeFormatter.ofPattern(PEPConstants.DEFAULT_DATETIME_FORMAT)
        noticeSender.retryNoticesAsync(LocalDateTime.parse("2018-09-03 17:30:21", dfm),
            LocalDateTime.parse("2018-09-03 17:32:22", dfm))
        Thread.sleep(5000)
        assert noticeRepository.findOne('1-RETRY').getStatus() == NoticeStatus.PENDING
        assert noticeRepository.findOne('2-RETRY').getStatus() == NoticeStatus.PENDING
        assert noticeRepository.findOne('4-RETRY').getStatus() == NoticeStatus.FAIL
        noticeRepository.deleteAll()
    }
}
