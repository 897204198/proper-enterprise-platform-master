package com.proper.enterprise.platform.notice.server.sdk.request

import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType
import spock.lang.Specification

class NoticeRequestSpec extends Specification {

    def beanTest() {
        NoticeRequest noticeRequest = new NoticeRequest()
        noticeRequest.setBatchId("batchId")
        noticeRequest.setContent("content")
        noticeRequest.setNoticeType(NoticeType.EMAIL)
        noticeRequest.setTitle("title")
        Map noticeExtMsg = new HashMap()
        noticeExtMsg.put("n", "n")
        noticeRequest.setNoticeExtMsg(noticeExtMsg)
        noticeRequest.setNoticeExtMsg("n1", "n1")
        NoticeTarget noticeTarget = new NoticeTarget()
        noticeTarget.setTo("to")
        Map targetExtMsg = new HashMap()
        targetExtMsg.put("a", "a")
        noticeTarget.setTargetExtMsg(targetExtMsg)
        noticeTarget.setTargetExtMsg("b", "b")
        noticeRequest.addTarget(noticeTarget)
        expect:
        assert noticeRequest.batchId == "batchId"
        assert noticeRequest.content == "content"
        assert noticeRequest.title == "title"
        assert noticeRequest.noticeType == NoticeType.EMAIL
        assert noticeRequest.noticeExtMsg.n == "n"
        assert noticeRequest.noticeExtMsg.n1 == "n1"
        assert noticeRequest.targets.get(0).to == "to"
        assert noticeRequest.targets.get(0).targetExtMsg.b == "b"
    }
}
