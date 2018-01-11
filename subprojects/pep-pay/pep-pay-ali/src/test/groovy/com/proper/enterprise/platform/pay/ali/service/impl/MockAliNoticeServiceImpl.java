package com.proper.enterprise.platform.pay.ali.service.impl;

import com.proper.enterprise.platform.api.pay.service.NoticeService;
import com.proper.enterprise.platform.pay.ali.entity.AliEntity;
import com.proper.enterprise.platform.pay.ali.repository.AliRepository;
import com.proper.enterprise.platform.pay.ali.service.AliPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 实现异步通知业务代码(需要在项目中实现,此处为示例测试代码)
 */
@Service("pay_notice_ali")
public class MockAliNoticeServiceImpl implements NoticeService<Map<String, String>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockAliNoticeServiceImpl.class);

    @Autowired
    AliRepository aliRepository;

    @Autowired
    AliPayService aliPayService;

    /**
     * 异步通知业务处理代码
     *
     * @param params 支付宝异步通知处理参数
     */
    @Async
    @Override
    public void saveNoticeProcessAsync(Map<String, String> params) {
        LOGGER.debug("-------------Async notice business-----------------");
        AliEntity ali = new AliEntity();

        ali.setOutTradeNo("001");
        ali.setBody("Async notice business");
        ali.setBuyerId("testNoticeBuyerId");

        ali.setNotifyTime("notifyTime");
        ali.setNotifyType("notifyType");
        ali.setNotifyId("notifyId");
        ali.setSignType("signType");
        ali.setSign("sign");
        ali.setTradeNo("tradeNo");
        ali.setTradeStatus("tradeStatus");
        ali.setSellerId("sellerId");
        ali.setSellerEmail("sellerEmail");
        ali.setBuyerEmail("buyerEmail");
        ali.setTotalFee("0.01");

        aliPayService.save(ali);
    }

    @Override
    public boolean isDuplicate(String orderNo) {
        return true;
    }

}
