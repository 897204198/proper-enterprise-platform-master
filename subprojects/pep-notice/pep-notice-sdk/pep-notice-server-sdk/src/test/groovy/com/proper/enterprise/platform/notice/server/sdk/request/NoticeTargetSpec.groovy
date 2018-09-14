package com.proper.enterprise.platform.notice.server.sdk.request

import spock.lang.Specification

class NoticeTargetSpec extends Specification {

    def beanTest() {
        NoticeTarget noticeTarget = new NoticeTarget()
        noticeTarget.setTo("to")
        Map targetExtMsg = new HashMap()
        targetExtMsg.put("a", "a")
        noticeTarget.setTargetExtMsg(targetExtMsg)
        noticeTarget.setTargetExtMsg("b", "b")
        expect:
        assert noticeTarget.to == "to"
        assert noticeTarget.targetExtMsg.a == "a"
        assert noticeTarget.targetExtMsg.b == "b"
    }
}
