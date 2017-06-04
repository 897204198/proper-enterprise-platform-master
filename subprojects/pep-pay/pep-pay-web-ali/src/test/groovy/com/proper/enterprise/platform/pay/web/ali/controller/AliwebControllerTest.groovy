package com.proper.enterprise.platform.pay.web.ali.controller

import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.pay.web.ali.entity.AliwebEntity
import com.proper.enterprise.platform.pay.web.ali.repository.AliwebRepository
import com.proper.enterprise.platform.pay.web.ali.service.AliwebPayService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class AliwebControllerTest extends AbstractTest {

    @Autowired
    AliwebRepository aliwebRepo;

    @Autowired
    AliwebPayService aliwebPayService;

    @Test
    public void noticeInfo() {

        post("/pay/aliweb/noticeAliwebPayInfo?total_amount=0.03&buyer_id=2088902928162096&trade_no=2017052521001004090228267041&body=test&notify_time=2017-05-25 21:14:43&subject=subject&sign_type=RSA2&buyer_logon_id=dre***@gmail.com&auth_app_id=2017052207311336&charset=UTF-8&notify_type=trade_status_sync&invoice_amount=0.03&out_trade_no=20170525211121474&trade_status=TRADE_SUCCESS&gmt_payment=2017-05-25 21:14:43&version=1.0&point_amount=0.00&sign=okFUHcQp6S+tY76MCII+8r/Ic0oPnzyLHrZkGbhA3wryDOUTTUgYZPeYL4/RxljkxhseN1ip1Ub49ZznBP1YoY7LAELpNJRiRXmsSx30HtacRGQX/yLXq5BYGL9HoT4SWThtY7vkH+hGKWO3Sx9SE9Kbu1N1lJxqJAKKqoEWapk/qD7KL0xxw6gQBoU5NJiyFaufeVdy5uYGv436fGFOajM35myWd6lsLVPrUhmcSy5r4DfktjfM34cxp7/0qrOLPZNZgBXGWAo0hV2luSvthbJ/zr1imrksN54xJaOxowuE27UPdwLsrBlIrJmuK5sMdbPxQlfv/3o8D+wcDJitng==&gmt_create=2017-05-25 21:14:42&buyer_pay_amount=0.03&receipt_amount=0.03&fund_bill_list=%5b%7b\"amount\":\"0.03\",\"fundChannel\":\"ALIPAYACCOUNT\"%7d%5d&app_id=2017052207311336&seller_id=2088621974132508&notify_id=3108bb342fbc7f361955d0d9709230cgp2&seller_email=18940252603@163.com", '', HttpStatus.CREATED)

        waitExecutorDone()
        AliwebEntity noticeInfo = aliwebPayService.findByOutTradeNo("20170525211121474")
        AliwebEntity businessInfo = aliwebPayService.findByOutTradeNo("001")

        assert noticeInfo.getBody().equals("test")
        assert noticeInfo.getTotalAmount().equals("0.03")
        assert noticeInfo.getBuyerId().equals("2088902928162096")

        assert businessInfo.getBody().equals("异步通知相关业务处理")
        assert businessInfo.getBuyerId().equals("testNoticeBuyerId")
    }
}
