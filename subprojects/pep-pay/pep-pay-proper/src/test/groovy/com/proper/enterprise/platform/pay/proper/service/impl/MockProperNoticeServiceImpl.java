package com.proper.enterprise.platform.pay.proper.service.impl;

import com.proper.enterprise.platform.api.pay.service.NoticeService;
import com.proper.enterprise.platform.pay.proper.entity.ProperEntity;
import com.proper.enterprise.platform.pay.proper.repository.ProperRepository;
import com.proper.enterprise.platform.pay.proper.service.ProperPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 实现异步通知业务代码(需要在项目中实现,此处为示例测试代码)
 */
@Service("pay_notice_proper")
public class MockProperNoticeServiceImpl implements NoticeService<Map<String, String>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockProperNoticeServiceImpl.class);

    @Autowired
    ProperRepository properRepository;

    @Autowired
    ProperPayService properPayService;

    /**
     * 异步通知业务处理代码
     *
     * @param params 支付宝异步通知处理参数
     */
    @Override
    public void saveNoticeProcess(Map<String, String>  params) {
        LOGGER.debug("-------------异步通知相关业务处理-----------------");
        ProperEntity proper = new ProperEntity();

        proper.setOutTradeNo("001");
        proper.setSubject("异步通知相关业务处理");
        proper.setNotifyTime("notifyTime");
        proper.setTradeNo("tradeNo");
        proper.setTotalFee("0.01");

        properPayService.save(proper);
    }
}
