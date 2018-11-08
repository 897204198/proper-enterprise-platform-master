package com.proper.enterprise.platform.pay.web.ali.service.impl

import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest
import com.alipay.api.request.AlipayTradeQueryRequest
import com.alipay.api.request.AlipayTradeRefundRequest
import com.alipay.api.request.AlipayTradeWapPayRequest
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse
import com.alipay.api.response.AlipayTradeQueryResponse
import com.alipay.api.response.AlipayTradeRefundResponse
import com.proper.enterprise.platform.pay.web.ali.service.AliwebPayResService
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Primary
@Service
class MockAliwebPayResServiceImpl extends AliwebPayResServiceImpl implements AliwebPayResService {

    @Override
    public Object getAliInterfaceRes(Object req, Object res) throws Exception {
        if(req instanceof AlipayTradeWapPayRequest) {
            res = "<form name=\"punchout_form\" method=\"post\" action=\"https://openapi.alipay.com/gateway.do?charset=UTF-8&method=alipay.trade.wap.pay&sign=jkbu948nn3p5ktkHcNxjhxlFPS9vMpq7QryBdcVfhC38Ofr0Lo3kIaf4w9KW1Tfuvbomi9V6uLSA%2FPt85EquO6LlWyz4iZ%2BK5iAzHjrzlBdYgZqHupDE58KfcaE7fO8NS3k7cjynFBCPuWDRko4R%2BMRXTGnUwLFw20tfx6FG%2FvItm2MakU%2F8ba6VD1B97UeE0ETlT1KvyeS7dxaDM8pzGFHA33tkmW%2B2yHDqusd5Ur4T%2FI0sn8ydQvg67NeLuPn9HrRPHT0K0zECaeg3Ix5miP4HDgV3x3yy7UXelokQ1cu9Bw7cR3xox0yrPW7Ddb2jJ9%2BDyGiqOr47Zy3jeM7ZBQ%3D%3D&return_url=http%3A%2F%2Ffoo%2Fbar%2Fpay%2Fali%2FnoticeAliPayInfo&notify_url=http%3A%2F%2F113.233.152.124%2Fpep%2Fpay%2Fali%2FnoticeAliPayInfo&version=1.0&app_id=2017052207311336&sign_type=RSA2&timestamp=2017-05-27+10%3A39%3A52&alipay_sdk=alipay-sdk-java-dynamicVersionNo&format=json\">\n" +
                "<input type=\"hidden\" name=\"biz_content\" value=\"{&quot;body&quot;:&quot;预约挂号&quot;,&quot;out_trade_no&quot;:&quot;12345678901234567890&quot;,&quot;product_code&quot;:&quot;QUICK_WAP_PAY&quot;,&quot;subject&quot;:&quot;预约挂号&quot;,&quot;timeout_express&quot;:&quot;31m&quot;,&quot;total_amount&quot;:&quot;1.23&quot;}\">\n" +
                "<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >\n" +
                "</form>\n" +
                "<script>document.forms[0].submit();</script>"
        } else if(req instanceof AlipayTradeQueryRequest) {
            AlipayTradeQueryResponse response = new AlipayTradeQueryResponse()
            response.setCode("10000")
            response.setMsg("Success")
            response.setBody("{\"alipay_trade_query_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"buyer_logon_id\":\"dre***@gmail.com\",\"buyer_pay_amount\":\"0.00\",\"buyer_user_id\":\"2088902928162096\",\"invoice_amount\":\"0.00\",\"open_id\":\"20881088054517700884948810915009\",\"out_trade_no\":\"20170525211121474\",\"point_amount\":\"0.00\",\"receipt_amount\":\"0.00\",\"send_pay_date\":\"2017-05-25 21:14:43\",\"total_amount\":\"0.03\",\"trade_no\":\"2017052521001004090228267041\",\"trade_status\":\"TRADE_SUCCESS\"},\"sign\":\"0FriYHlgTD/VO0JvgfdQiNkwKc91QqIto75Net0YtWR8h+1rzt2tHSKC5TKLoxxetPDOUCG2kiD8tLjypAPFaC9fdx/PzevX4kZ2USalGq4JBSWTuCET4LtD9S0TXWznvopacsTeqlKLeMSAvvp/AqvjJk+o2diJP2/nsVuEQOPA7Jtz1rVlxPTSLIzDAyu6VPwaJEPH5aSBfDsKVxrtEdBE2xOitrIFldDC9TNIuMi1Gay8TL0AclS/nCRo2yqToTT14tIM7oYA8YDFWrkbd6EnMMJsQulV+wPTMCFaMWoVXQA3VX2c1wYshBWYTjzFcQboa1U1d61Z9nt1jOm/7Q==\"}")
            Map<String, String> params = new HashMap<>()
            params.put("biz_content", "{\"out_trade_no\":\"20170525211121474\"}")
            response.setParams(params)
            response.setBuyerLogonId("dre***@gmail.com")
            response.setBuyerPayAmount("0.00")
            response.setBuyerUserId("userId")
            response.setInvoiceAmount("0.00")
            response.setOpenId("openId")
            response.setOutTradeNo("20170525211121474")
            response.setPointAmount("0.00")
            response.setReceiptAmount("0.00")
            response.setSendPayDate(new Date(1495718083000 * 1000))
            response.setTotalAmount("0.03")
            response.setTradeNo("2017052521001004090228267041")
            response.setTradeStatus("TRADE_SUCCESS")
            res = response
        } else if(req instanceof AlipayTradeRefundRequest) {
            AlipayTradeRefundResponse response = new AlipayTradeRefundResponse()
            response.setCode("10000")
            response.setMsg("Success")
            response.setBody("{\"alipay_trade_refund_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"buyer_logon_id\":\"dre***@gmail.com\",\"buyer_user_id\":\"2088902928162096\",\"fund_change\":\"Y\",\"gmt_refund_pay\":\"2017-05-27 14:59:21\",\"open_id\":\"20881088054517700884948810915009\",\"out_trade_no\":\"20170525211121474\",\"refund_fee\":\"0.01\",\"send_back_fee\":\"0.00\",\"trade_no\":\"2017052521001004090228267041\"},\"sign\":\"I3zfp3Pc94zojvMKV1LN3eUaPG2fycIx8ZgwokVI7aqhfcqjD3QVwQAAGJ28VCo+VehJj7H3z6H/gBzReOabgVcE7wDl51kOdEtIw5DkBbEvIvBjp0nfaaxsnGbT48zZJtxfS3N6zlIsyyieCYJeXedAZAvfZByn9RMRgkiOCnb6+350Evh66Jk5+3huJTSDL3loLrLqFwZIT8SixWVYkg0hiE5wz6TDmGaaajLGzmgZw+7nBlZqNkMZDeDlfDgM1LetBEeIutWgk32x0NVYkBCHwfsTq+Kx1qRgaH5v/ublDoejbuOKWoQvkIvkOdNQBpVvpSDYyQS1zGeOTPu9ZQ==\"}")
            Map<String, String> params = new HashMap<>()
            params.put("biz_content", "{\"out_request_no\":\"2017052521112147401\",\"out_trade_no\":\"20170525211121474\",\"refund_amount\":\"0.01\"}")
            response.setParams(params)
            response.setBuyerLogonId("dre***@gmail.com")
            response.setBuyerUserId("userId")
            response.setFundChange("Y")
            response.setGmtRefundPay(new Date(1495868361000 * 1000))
            response.setOpenId("openId")
            response.setOutTradeNo("20170525211121474")
            response.setRefundFee("0.01")
            response.setSendBackFee("0.00")
            response.setTradeNo("2017052521001004090228267041")
            res = response
        } else if(req instanceof AlipayTradeFastpayRefundQueryRequest) {
            AlipayTradeFastpayRefundQueryResponse response = new AlipayTradeFastpayRefundQueryResponse()
            response.setCode("10000")
            response.setMsg("Success")
            response.setBody("{\"alipay_trade_fastpay_refund_query_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"out_request_no\":\"2017052521112147401\",\"out_trade_no\":\"20170525211121474\",\"refund_amount\":\"0.01\",\"total_amount\":\"0.03\",\"trade_no\":\"2017052521001004090228267041\"},\"sign\":\"hh75XWSPjwvlROCvfpOmkwwbp35k0wAp0O1ihuOJH9OizlQ3jauCKOJco/MV23ITgBxhdCZK7xSSxyM2XlLSMXHSbcglAlUOn68HK9h5SW3t9yQ5SLNf1x+hki6MWHS6CnEI27zFmw4ApgeOuxAO7mNqzG8rVUWTEN4mkJp4bapSa+t0fUuit8PmxA7KChQC1JtWg8U4EHF2tFQRe5ZOJHD0VYJgQ8FXbemBYJcC4T/6AxTN+aQc0GDrPMwxST8SD6Ddqn3FdkbL6lkTTcuYSRq6+clImg7xp3eLq9VOfmf2sTLKqCFhfT+Bkh4Y16DO8aBFzyDec22VSetCaQ5KUQ==\"}")
            Map<String, String> params = new HashMap<>()
            params.put("biz_content", "{\"out_request_no\":\"2017052521112147401\",\"out_trade_no\":\"20170525211121474\"}")
            response.setParams(params)
            response.setOutRequestNo("2017052521112147401")
            response.setOutTradeNo("20170525211121474")
            response.setRefundAmount("0.01")
            response.setTotalAmount("0.03")
            response.setTradeNo("2017052521001004090228267041")
            res = response
        }
        return res;
    }

    @Override
    public boolean noticeVerify(Map<String, String> params) throws Exception {
        return true;
    }
}
