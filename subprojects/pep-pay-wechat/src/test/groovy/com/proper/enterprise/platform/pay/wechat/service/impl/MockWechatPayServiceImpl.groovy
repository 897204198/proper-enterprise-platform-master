package com.proper.enterprise.platform.pay.wechat.service.impl

import com.proper.enterprise.platform.pay.wechat.model.WechatNoticeRes
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Service
@Primary
class MockWechatPayServiceImpl extends WechatPayServiceImpl {

    @Override
    boolean isValid(WechatNoticeRes noticeRes) {
        true
    }

}
