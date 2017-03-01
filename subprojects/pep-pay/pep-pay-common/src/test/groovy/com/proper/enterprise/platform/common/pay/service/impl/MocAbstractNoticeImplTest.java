package com.proper.enterprise.platform.common.pay.service.impl;

import com.proper.enterprise.platform.api.pay.enums.PayResType;
import com.proper.enterprise.platform.api.pay.model.PayResultRes;
import com.proper.enterprise.platform.api.pay.service.NoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("pay_notice_ali")
public class MocAbstractNoticeImplTest implements NoticeService<PayResultRes> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MocAbstractNoticeImplTest.class);

    /**
     * 实现类
     * @param payResultRes payResultRes
     */
    @Override
    public void saveNoticeProcess(PayResultRes payResultRes) throws Exception {
        if(payResultRes.getResultCode() == PayResType.SUCCESS) {
            LOGGER.debug("正常执行异步通知实现类");
        } else {
            throw new Exception();
        }

    }
}
