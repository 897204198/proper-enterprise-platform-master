package com.proper.enterprise.platform.common.pay.service.impl;

import com.proper.enterprise.platform.api.pay.enums.PayResType;
import com.proper.enterprise.platform.api.pay.model.PayResultRes;
import com.proper.enterprise.platform.api.pay.service.NoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("pay_notice_ali")
public class MocAbstractNoticeImpl implements NoticeService<PayResultRes> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MocAbstractNoticeImpl.class);

    /**
     * 实现类
     * @param payResultRes payResultRes
     */
    @Override
    @Async
    public void saveNoticeProcessAsync(PayResultRes payResultRes) throws Exception {
        if (payResultRes.getResultCode() == PayResType.SUCCESS) {
            LOGGER.debug("execute async notice normally!");
        } else {
            throw new Exception();
        }
    }

}
