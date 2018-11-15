package com.proper.enterprise.platform.notice.server.app.sender

import com.proper.enterprise.platform.core.CoreProperties
import com.proper.enterprise.platform.core.PEPPropertiesLoader
import com.proper.enterprise.platform.notice.server.api.model.Notice
import com.proper.enterprise.platform.notice.server.api.sender.NoticeSender
import com.proper.enterprise.platform.notice.server.app.AbstractServerAppTest
import com.proper.enterprise.platform.notice.server.app.dao.repository.NoticeRepository
import com.proper.enterprise.platform.notice.server.app.global.SingletonMap
import com.proper.enterprise.platform.notice.server.app.handler.MockNoticeSender
import com.proper.enterprise.platform.notice.server.app.vo.NoticeVO
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeRequest
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeTarget
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NoticeSenderTest extends AbstractServerAppTest {

    @Autowired
    private NoticeSender noticeSender

    @Autowired
    private NoticeRepository noticeRepository

    @Test
    void beforeSendTest() {
        initApp("qqq")
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
    void afterSendTest() {
        SingletonMap.getSingletMap().put("mockErrAfter", "a")
        NoticeVO noticeVO = new NoticeVO()
        noticeVO.setAppKey("mockErrAfter")
        noticeVO.setNoticeType(NoticeType.MOCK)
        noticeSender.afterSend(noticeVO)
        assert "a" == SingletonMap.getSingletMap().get("mockErrAfter")
        waitExecutorDone()
        assert "b" == SingletonMap.getSingletMap().get("mockErrAfter")
    }

    @Test
    void sendAsyncTest() {
        noticeRepository.deleteAll()
        NoticeVO noticeVO = new NoticeVO()
        String appKey = UUID.randomUUID()
        noticeVO.setAppKey(appKey)
        noticeVO.setBatchId("batchId")
        noticeVO.setNoticeType(NoticeType.MOCK)
        noticeVO.setContent("content")
        noticeVO.setTargetTo("aa")
        noticeSender.sendAsync(noticeVO)
        waitExecutorDone()
        Notice successNotice = noticeRepository
            .findAll(null, appKey, null, null, null, null, null, null)
            .get(0)
        assert NoticeStatus.SUCCESS == SingletonMap.getSingletMap().get(successNotice.getId())
        assert successNotice.getBatchId() == "batchId"

        NoticeVO noticeVO2 = new NoticeVO()
        noticeVO2.setAppKey(MockNoticeSender.MOCK_INVALID_TARGET)
        noticeVO2.setBatchId("batchId")
        noticeVO2.setNoticeType(NoticeType.MOCK)
        noticeVO2.setContent("content")
        noticeVO2.setTargetTo("aa")
        noticeSender.sendAsync(noticeVO2)
        waitExecutorDone()
        Notice invalidTarget = noticeRepository
            .findAll(null, MockNoticeSender.MOCK_INVALID_TARGET, null, null, null, null, null, null)
            .get(0)
        assert MockNoticeSender.MOCK_INVALID_TARGET == invalidTarget.getErrorCode()
    }

    @Test
    @NoTx
    @Sql("/com/proper/enterprise/platform/notice/server/app/service/sql/syncPendingNoticesStatusAsync.sql")
    void syncPendingNoticesStatusAsync() {
        DateTimeFormatter dfm = DateTimeFormatter.ofPattern(PEPPropertiesLoader.load(CoreProperties.class).getDefaultDatetimeFormat())
        noticeSender.syncPendingNoticesStatusAsync(LocalDateTime.parse("2018-09-03 17:30:21", dfm),
            LocalDateTime.parse("2018-09-03 17:32:22", dfm))
        waitExecutorDone()
        assert noticeRepository.findById('1-PENDING').get().getStatus() == NoticeStatus.SUCCESS
        assert noticeRepository.findById('4-PENDING').get().getStatus() == NoticeStatus.RETRY
        assert noticeRepository.findById('4-PENDING').get().getRetryCount() == 2
        assert noticeRepository.findById('2-PENDING').get().getStatus() == NoticeStatus.FAIL
        assert noticeRepository.findById('2-PENDING').get().getErrorMsg() == "Max retry"
        noticeRepository.deleteAll()
    }

    @Test
    @NoTx
    @Sql("/com/proper/enterprise/platform/notice/server/app/service/sql/retryNoticesAsync.sql")
    void retryNoticesAsync() {
        DateTimeFormatter dfm = DateTimeFormatter.ofPattern(PEPPropertiesLoader.load(CoreProperties.class).getDefaultDatetimeFormat())
        noticeSender.retryNoticesAsync(LocalDateTime.parse("2018-09-03 17:30:21", dfm),
            LocalDateTime.parse("2018-09-03 17:32:22", dfm))
        waitExecutorDone()
        assert noticeRepository.findById('1-RETRY').get().getStatus() == NoticeStatus.SUCCESS
        assert noticeRepository.findById('2-RETRY').get().getStatus() == NoticeStatus.SUCCESS
        assert noticeRepository.findById('4-RETRY').get().getStatus() == NoticeStatus.FAIL
        noticeRepository.deleteAll()
    }

}
