package com.proper.enterprise.platform.pay.ali.service.impl

import com.proper.enterprise.platform.pay.ali.model.AliPayTradeQueryRes
import com.proper.enterprise.platform.pay.ali.model.AliRefundRes
import com.proper.enterprise.platform.pay.ali.model.AliRefundTradeQueryRes
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test

class AliPayResServiceImplTest extends AbstractJPATest {

    @Test
    public void testAliPayTradeQueryRes() {
        AliPayResServiceImpl aliPayResServiceImpl = new AliPayResServiceImpl();
        AliPayTradeQueryRes queryRes = new AliPayTradeQueryRes()
        String strRese = "{\"alipay_trade_query_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"buyer_logon_id\":\"dre***@gmail.com\",\"buyer_pay_amount\":\"0.00\",\"buyer_user_id\":\"2088902928162096\",\"invoice_amount\":\"0.00\",\"open_id\":\"20881007251907009745429510918909\",\"out_trade_no\":\"201512191483583349219\",\"point_amount\":\"0.00\",\"receipt_amount\":\"0.00\",\"send_pay_date\":\"2017-01-05 10:29:43\",\"total_amount\":\"0.05\",\"trade_no\":\"2017010521001004090235281478\",\"trade_status\":\"TRADE_SUCCESS\"},\"sign\":\"io1ljN3iWahN6PesvlI4AYdEf5jQRX/05IOFu1duD+lifrPN1CdMS86Yf784fYAqPbhGBl8b6eC9NrnyUOZijmW38UX1Rg19Wt7rqHK9KDBTDUWLK7q51v7FpxNg9aIiZdCSvfenuMZAyIWusQgx9iOXopaHpr03MtpErhHAxmY=\"}"
        String responsekey = "alipay_trade_query_response"
        queryRes = (AliPayTradeQueryRes)aliPayResServiceImpl.convertMap2AliPayRes(strRese, responsekey, queryRes)

        assert queryRes.getCode().equals("10000")
        assert queryRes.getOutTradeNo().equals("201512191483583349219")
        assert queryRes.getTotalAmount().equals("0.05")
    }

    @Test
    public void testAliRefundRes() {
        AliPayResServiceImpl aliPayResServiceImpl = new AliPayResServiceImpl();
        AliRefundRes refundRes = new AliRefundRes()
        String strRese = "{\"alipay_trade_refund_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"buyer_logon_id\":\"dre***@gmail.com\",\"buyer_user_id\":\"2088902928162096\",\"fund_change\":\"Y\",\"gmt_refund_pay\":\"2017-01-05 11:08:09\",\"open_id\":\"20881007251907009745429510918909\",\"out_trade_no\":\"201512191483583349219\",\"refund_fee\":\"0.01\",\"send_back_fee\":\"0.00\",\"trade_no\":\"2017010521001004090235281478\"},\"sign\":\"ZWve4zrFeZ7nOL2IxigXFnmxXxvr6L08LJM5ibIryvMZ9czdwIeVqnbNWRcvdgihoiWPrra2w3rtJSC6lLzXKCcAq3DtGnrY4oNuxs2iH5p+bbf6hhyEtMl7YmjLdLrc3M7EhfpMHPinZ3NcyXzQ/vp+xCESWUPoMf6RBOFRzIY=\"}"
        String responsekey = "alipay_trade_refund_response"
        refundRes = (AliRefundRes)aliPayResServiceImpl.convertMap2AliPayRes(strRese, responsekey, refundRes)

        assert refundRes.getCode().equals("10000")
        assert refundRes.getOutTradeNo().equals("201512191483583349219")
        assert refundRes.getRefundFee().equals("0.01")
    }

    @Test
    public void testAliRefundTradeQueryRes() {
        AliPayResServiceImpl aliPayResServiceImpl = new AliPayResServiceImpl();
        AliRefundTradeQueryRes refundQueryRes = new AliRefundTradeQueryRes()
        String strRese = "{\"alipay_trade_fastpay_refund_query_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"out_request_no\":\"20151219148358334921901\",\"out_trade_no\":\"201512191483583349219\",\"refund_amount\":\"0.01\",\"total_amount\":\"0.05\",\"trade_no\":\"2017010521001004090235281478\"},\"sign\":\"p9d/1izYNTQxdxbNJ0tprJqT2tsc6dc40k68KyyYLV5UY1E4qgbvbZc+EkHVVPy7wooPSF9gl7aGAYGy7mI2YjabZAt3WKPjctsqTWLCmrCB9m49lSL1uv/hr9YyktHkmh3z4aj0AoAcqPKDulyJyzJtDdVrLAZg7vKu06dNbCA=\"}"
        String responsekey = "alipay_trade_fastpay_refund_query_response"
        refundQueryRes = (AliRefundTradeQueryRes)aliPayResServiceImpl.convertMap2AliPayRes(strRese, responsekey, refundQueryRes)

        assert refundQueryRes.getCode().equals("10000")
        assert refundQueryRes.getOutTradeNo().equals("201512191483583349219")
        assert refundQueryRes.getTotalAmount().equals("0.05")
        assert refundQueryRes.getRefundAmount().equals("0.01")
    }

    @Test
    public void testCheckUrl() {
        AliPayResServiceImpl aliPayResServiceImpl = new AliPayResServiceImpl();
        String ret = aliPayResServiceImpl.checkUrl("https://mapi.alipay.com/gateway.do?service=notify_verify&partner=2088021705112890&notify_id=da1c584202b9be0b95ac4c957fb4905gp2")

        assert ret.equals("false")
    }
}
