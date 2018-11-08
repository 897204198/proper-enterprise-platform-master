package com.proper.enterprise.platform.pay.wechat.service.impl

import com.proper.enterprise.platform.api.pay.service.NoticeService
import com.proper.enterprise.platform.pay.wechat.entity.WechatEntity
import com.proper.enterprise.platform.pay.wechat.model.WechatNoticeRes
import com.proper.enterprise.platform.pay.wechat.service.WechatPayService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

/**
 * 实现异步通知业务代码(需要在项目中实现,此处为示例测试代码)
 */
@Service("pay_notice_wechat")
class MockWechatNoticeServiceImpl implements NoticeService <WechatNoticeRes> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockWechatNoticeServiceImpl.class);

    @Autowired
    WechatPayService wechatPayService;

    /**
     * 异步通知业务处理代码
     *
     * @param noticeRes 微信异步通知处理参数
     */
    @Async
    @Override
    public void saveNoticeProcessAsync(WechatNoticeRes  noticeRes) {
        LOGGER.debug("-------------异步通知相关业务处理-----------------");

        WechatEntity wechatinfo = new WechatEntity();

        wechatinfo.setOutTradeNo("001")
        wechatinfo.setAttach("异步通知相关业务处理")
        wechatinfo.setDeviceInfo("test")

        wechatinfo.setReturnCode("returnCode")
        wechatinfo.setAppid("appid")
        wechatinfo.setMchId("mchId")
        wechatinfo.setNonceStr("nonceStr")
        wechatinfo.setSign("sign")
        wechatinfo.setResultCode("resultCode")
        wechatinfo.setOpenid("openid")
        wechatinfo.setTradeType("tradeType")
        wechatinfo.setBankType("bankType")
        wechatinfo.setTotalFee("123")
        wechatinfo.setCashFee("cashFee")
        wechatinfo.setTransactionId("transactionId")
        wechatinfo.setTimeEnd("timeEnd")

        wechatPayService.save(wechatinfo)

    }
}
