package com.proper.enterprise.platform.notice.server.app.controller

import com.proper.enterprise.platform.api.auth.service.AccessTokenService
import com.proper.enterprise.platform.notice.server.app.handler.MockNoticeSender
import com.proper.enterprise.platform.notice.server.app.vo.AppVO
import com.proper.enterprise.platform.notice.server.app.AbstractServerAppTest
import com.proper.enterprise.platform.notice.server.app.dao.repository.NoticeRepository
import com.proper.enterprise.platform.notice.server.app.scheduler.NoticeStatusSyncScheduler
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeRequest
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeTarget
import com.proper.enterprise.platform.sys.i18n.I18NUtil
import com.proper.enterprise.platform.test.annotation.NoTx
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class ApiNoticeSendControllerTest extends AbstractServerAppTest {

    @Autowired
    private AccessTokenService accessTokenService

    @Autowired
    private NoticeStatusSyncScheduler noticeStatusSyncScheduler

    @Autowired
    private NoticeRepository noticeRepository

    @Test
    public void testSendPermission() {
        NoticeRequest noticeRequest = new NoticeRequest()
        noticeRequest.setBatchId("sendNoticeTest")
        noticeRequest.setNoticeType(NoticeType.MOCK)
        noticeRequest.setContent("text")
        List<NoticeTarget> targets = new ArrayList<>()
        NoticeTarget target = new NoticeTarget()
        target.setTo("toMe")
        target.setTargetExtMsg("targetExt", "targetExt")
        targets.add(target)
        noticeRequest.setTargets(targets)
        noticeRequest.setNoticeExtMsg("noticeExt", "noticeExt")
        //token验证
        AppVO appVO = initAppReturnVO('sendNoticeTest')
        post("/notice/server/send?access_token=" + appVO.getAppToken(), JSONUtil.toJSON(noticeRequest), HttpStatus.CREATED)
        enableApp(appVO.getId(), false)
        "sendNoticeTest is disabled" == post("/notice/server/send?access_token=" + appVO.getAppToken(), JSONUtil.toJSON(noticeRequest), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()
        enableApp(appVO.getId(), true)
        post("/notice/server/send?access_token=" + appVO.getAppToken(), JSONUtil.toJSON(noticeRequest), HttpStatus.CREATED)
        deleteApp(appVO.getId())
        post("/notice/server/send?access_token=" + appVO.getAppToken(), JSONUtil.toJSON(noticeRequest), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @Test
    public void sendNoticeValidTest() {
        NoticeRequest noticeRequest = new NoticeRequest()
        noticeRequest.setBatchId("sendNoticeTest")
        noticeRequest.setNoticeType(NoticeType.MOCK)
        noticeRequest.setContent("text")
        List<NoticeTarget> targets = new ArrayList<>()
        NoticeTarget target = new NoticeTarget()
        target.setTo("toMe")
        target.setTargetExtMsg("targetExt", "targetExt")
        targets.add(target)
        noticeRequest.setTargets(targets)
        noticeRequest.setNoticeExtMsg("noticeExt", "noticeExt")

        def accessToken = initApp('sendNoticeTest')
        noticeRequest.setBatchId("sendNoticeTestsendNoticeTestsendNoticeTestsendNoticeTestsendNoticeTestsendNoticeTestsendNoticeTestsendNoticeTest")
        //测批次号长度
        assert I18NUtil.getMessage("notice.server.param.batchId.isTooLong") == post("/notice/server/send?access_token=" + accessToken,
            JSONUtil.toJSON(noticeRequest), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()
        //测target扩展信息长度
        noticeRequest.setBatchId("sendNoticeTest")
        for (int i = 0; i < 2048; i++) {
            target.setTargetExtMsg("targetExt" + i, "targetExt")
        }
        assert I18NUtil.getMessage("notice.server.param.targetExtMsg.isTooLong") == post("/notice/server/send?access_token=" + accessToken,
            JSONUtil.toJSON(noticeRequest), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()
        //测notice扩展信息长度
        Map targetExtMap = new HashMap()
        targetExtMap.put("targetExt", "targetExt")
        target.setTargetExtMsg(targetExtMap)
        for (int i = 0; i < 2048; i++) {
            noticeRequest.setNoticeExtMsg("noticeExt" + i, "noticeExt")
        }
        assert I18NUtil.getMessage("notice.server.param.noticeExtMsg.isTooLong") == post("/notice/server/send?access_token=" + accessToken,
            JSONUtil.toJSON(noticeRequest), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()
        //测targetTo为空
        Map noticeExtMap = new HashMap()
        noticeExtMap.put("noticeExt", "noticeExt")
        noticeRequest.setNoticeExtMsg(noticeExtMap)
        target.setTo("")
        assert I18NUtil.getMessage("notice.server.param.target.cantBeEmpty") == post("/notice/server/send?access_token=" + accessToken,
            JSONUtil.toJSON(noticeRequest), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()
    }

    @Test
    @NoTx
    public void sendNoticeSuccessTest() {
        noticeRepository.deleteAll()
        String appKey = MockNoticeSender.MOCK_PINDING_SEND + "0"
        AppVO appVO = initAppReturnVO(appKey)
        def accessToken = appVO.getAppToken()
        NoticeRequest noticeRequest = new NoticeRequest()
        noticeRequest.setBatchId(appKey)
        noticeRequest.setNoticeType(NoticeType.MOCK)
        noticeRequest.setContent("text")
        List<NoticeTarget> targets = new ArrayList<>()
        NoticeTarget target = new NoticeTarget()
        target.setTo("toMe")
        target.setTargetExtMsg("targetExt", "targetExt")
        targets.add(target)
        noticeRequest.setTargets(targets)
        noticeRequest.setNoticeExtMsg("noticeExt", "noticeExt")
        post("/notice/server/send?access_token=" + accessToken, JSONUtil.toJSON(noticeRequest), HttpStatus.CREATED)
        waitExecutorDone()
        List searchPendingList = JSONUtil.parse(get("/notice/server/msg?appKey="
            + appKey + "&batchId=" + appKey,
            HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert searchPendingList.size() == 1
        assert searchPendingList.get(0).status == NoticeStatus.PENDING.name()
        noticeStatusSyncScheduler.syncPending()
        waitExecutorDone()
        List searchSuccessList = JSONUtil.parse(get("/notice/server/msg?appKey="
            + appKey + "&batchId=" + appKey,
            HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert searchSuccessList.size() == 1
        assert searchSuccessList.get(0).status == NoticeStatus.SUCCESS.name()
        noticeRepository.deleteAll()
        deleteApp(appVO.getId())
    }

    @Test
    @NoTx
    public void sendNoticeErrTest() {
        noticeRepository.deleteAll()
        def accessToken = initApp(MockNoticeSender.MOCK_ERR_SEND)
        NoticeRequest noticeRequest = new NoticeRequest()
        noticeRequest.setBatchId(MockNoticeSender.MOCK_ERR_SEND)
        noticeRequest.setNoticeType(NoticeType.MOCK)
        noticeRequest.setContent("text")
        List<NoticeTarget> targets = new ArrayList<>()
        NoticeTarget target = new NoticeTarget()
        target.setTo("toMe")
        target.setTargetExtMsg("targetExt", "targetExt")
        targets.add(target)
        noticeRequest.setTargets(targets)
        noticeRequest.setNoticeExtMsg("noticeExt", "noticeExt")
        post("/notice/server/send?access_token=" + accessToken, JSONUtil.toJSON(noticeRequest), HttpStatus.CREATED)
        waitExecutorDone()
        mockUser("1", "admin")
        List searchErrList = JSONUtil.parse(get("/notice/server/msg?appKey=mockErrSend&batchId=mockErrSend",
            HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert searchErrList.size() == 1
        assert searchErrList.get(0).status == NoticeStatus.FAIL.name()
        assert searchErrList.get(0).errorMsg != null
    }

    @Test
    @NoTx
    public void sendNoticeRetryTest() {
        noticeRepository.deleteAll()
        def accessToken = initApp(MockNoticeSender.MOCK_RETRY_STATUS)
        NoticeRequest noticeRequest = new NoticeRequest()
        noticeRequest.setBatchId(MockNoticeSender.MOCK_RETRY_STATUS)
        noticeRequest.setNoticeType(NoticeType.MOCK)
        noticeRequest.setContent("text")
        List<NoticeTarget> targets = new ArrayList<>()
        NoticeTarget target = new NoticeTarget()
        target.setTo("toMe")
        target.setTargetExtMsg("targetExt", "targetExt")
        targets.add(target)
        noticeRequest.setTargets(targets)
        noticeRequest.setNoticeExtMsg("noticeExt", "noticeExt")
        post("/notice/server/send?access_token=" + accessToken, JSONUtil.toJSON(noticeRequest), HttpStatus.CREATED)
        waitExecutorDone()
        List searchPendingList = JSONUtil.parse(get("/notice/server/msg?appKey="
            + MockNoticeSender.MOCK_RETRY_STATUS + "&batchId=" + MockNoticeSender.MOCK_RETRY_STATUS,
            HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert searchPendingList.size() == 1
        assert searchPendingList.get(0).status == NoticeStatus.RETRY.name()
        assert searchPendingList.get(0).retryCount == 0
        noticeStatusSyncScheduler.syncRetry()
        List searchRetry1List = JSONUtil.parse(get("/notice/server/msg?appKey=" + MockNoticeSender.MOCK_RETRY_STATUS
            + "&batchId=" + MockNoticeSender.MOCK_RETRY_STATUS,
            HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert searchRetry1List.size() == 1
        assert searchRetry1List.get(0).status == NoticeStatus.RETRY.name()
        assert searchRetry1List.get(0).retryCount == 1
        noticeRepository.deleteAll()
    }


}
