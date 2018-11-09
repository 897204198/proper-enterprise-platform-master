package com.proper.enterprise.platform.notice.server.app.dao.service

import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.notice.server.api.model.Notice
import com.proper.enterprise.platform.notice.server.api.service.NoticeDaoService
import com.proper.enterprise.platform.notice.server.app.vo.NoticeVO
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NoticeDaoServiceImplTest extends AbstractJPATest {

    @Autowired
    private NoticeDaoService noticeDaoService


    @Test
    public void saveTest() {
        NoticeVO noticeVO = new NoticeVO()
        noticeVO.setAppKey("appKey")
        noticeVO.setBatchId("batchId")
        noticeVO.setNoticeType(NoticeType.EMAIL)
        noticeVO.setNoticeExtMsg("a", "a")
        noticeVO.setTargetExtMsg("b", "b")
        noticeVO.setContent("content")
        noticeVO.setTargetTo("to")
        noticeDaoService.save(noticeVO)
        assert noticeVO.getStatus() == NoticeStatus.PENDING
        assert noticeVO.getRetryCount() == 0
    }


    @Test
    public void updateTest() {
        NoticeVO noticeVO = new NoticeVO()
        noticeVO.setAppKey("appKey")
        noticeVO.setBatchId("batchId")
        noticeVO.setNoticeType(NoticeType.EMAIL)
        noticeVO.setNoticeExtMsg("a", "a")
        noticeVO.setTargetExtMsg("b", "b")
        noticeVO.setContent("content")
        noticeVO.setTargetTo("to")
        Notice save = noticeDaoService.save(noticeVO)
        assert save.getStatus() == NoticeStatus.PENDING
        assert save.getRetryCount() == 0
        assert noticeDaoService.updateStatus(save.getId(),
            NoticeStatus.SUCCESS).getStatus() == NoticeStatus.SUCCESS
        assert noticeDaoService.updateToFail(save.getId(),"errMsg",
            "errMsg").getErrorMsg() == "errMsg"
    }

    @Test
    public void addRetryCount() {
        NoticeVO noticeVO = new NoticeVO()
        noticeVO.setAppKey("appKey")
        noticeVO.setBatchId("batchId")
        noticeVO.setNoticeType(NoticeType.EMAIL)
        noticeVO.setNoticeExtMsg("a", "a")
        noticeVO.setTargetExtMsg("b", "b")
        noticeVO.setContent("content")
        noticeVO.setTargetTo("to")
        Notice save = noticeDaoService.save(noticeVO)
        noticeDaoService.addRetryCount(save.getId())
        assert noticeDaoService.get(save.getId()).getRetryCount() == 1
    }

    @Test
    @Sql("/com/proper/enterprise/platform/notice/server/app/dao/sql/pendingNotices.sql")
    public void findPendingNoticesTest() {
        DateTimeFormatter dfm = DateTimeFormatter.ofPattern(PEPConstants.DEFAULT_DATETIME_FORMAT)
        List<Notice> notices = noticeDaoService.findPendingNotices(LocalDateTime.parse("2018-09-03 17:30:21", dfm),
            LocalDateTime.parse("2018-09-03 17:32:22", dfm))
        assert notices.size() == 2
    }

    @Test
    @Sql("/com/proper/enterprise/platform/notice/server/app/dao/sql/retryNotices.sql")
    public void findRetryNoticesTest() {
        DateTimeFormatter dfm = DateTimeFormatter.ofPattern(PEPConstants.DEFAULT_DATETIME_FORMAT)
        List<Notice> notices = noticeDaoService
            .findRetryNotices(LocalDateTime.parse("2018-09-03 17:30:21", dfm),
            LocalDateTime.parse("2018-09-03 17:32:22", dfm)
            , 3)
        assert notices.size() == 1
    }
}
