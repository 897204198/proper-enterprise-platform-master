package com.proper.enterprise.platform.pay.web.ali.service.impl;

import com.proper.enterprise.platform.api.pay.service.NoticeService;
import com.proper.enterprise.platform.pay.web.ali.entity.AliwebEntity;
import com.proper.enterprise.platform.pay.web.ali.repository.AliwebRepository;
import com.proper.enterprise.platform.pay.web.ali.service.AliwebPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 实现异步通知业务代码(需要在项目中实现,此处为示例测试代码).
 */
@Service("pay_notice_aliweb")
public class MockAliwebNoticeServiceImpl implements NoticeService<Map<String, String>>  {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockAliwebNoticeServiceImpl.class);

    @Autowired
    AliwebRepository aliwebRepository;

    @Autowired
    AliwebPayService aliwebPayService;

    /**
     * 异步通知业务处理代码
     *
     * @param params 支付宝异步通知处理参数
     */
    @Override
    public void saveNoticeProcess(Map<String, String>  params) {
        LOGGER.debug("-------------支付宝网页支付异步通知相关业务处理-----------------");
        AliwebEntity aliweb = new AliwebEntity();

        aliweb.setOutTradeNo("001");
        aliweb.setBody("异步通知相关业务处理");
        aliweb.setBuyerId("testNoticeBuyerId");

        aliweb.setNotifyTime("notifyTime");
        aliweb.setNotifyType("notifyType");
        aliweb.setNotifyId("notifyId");
        aliweb.setSignType("signType");
        aliweb.setSign("sign");
        aliweb.setTradeNo("tradeNo");
        aliweb.setTradeStatus("tradeStatus");
        aliweb.setSellerId("sellerId");
        aliweb.setSellerEmail("sellerEmail");
        aliweb.setBuyerLogonId("buyerLogonId");
        aliweb.setTotalAmount("0.01");
        aliweb.setVersion("1.0");
        aliweb.setCharset("charset");
        aliweb.setAppId("appId");

        aliwebPayService.save(aliweb);
    }
}
