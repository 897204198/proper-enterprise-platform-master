package com.proper.enterprise.platform.pay.ali.service.impl;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.pay.ali.model.AliPayTradeQueryRes;
import com.proper.enterprise.platform.pay.ali.model.AliRefundRes;
import com.proper.enterprise.platform.pay.ali.model.AliRefundTradeQueryRes;
import com.proper.enterprise.platform.pay.ali.service.AliPayResService;
import org.slf4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

@Primary
@Service
public class MockAliPayResServiceImpl extends AliPayResServiceImpl implements AliPayResService {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MockAliPayResServiceImpl.class);

    @Override
    public Object convertMap2AliPayRes(String strRes, String responseKey, Object res) throws Exception {
        // 统一收单线下交易查询
        if (responseKey.equals("alipay_trade_query_response")) {
            strRes = "{\"alipay_trade_query_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"buyer_logon_id\":\"dre***@gmail.com\","
                + "\"buyer_pay_amount\":\"0.00\",\"buyer_user_id\":\"2088902928162096\",\"invoice_amount\":\"0.00\","
                + "\"open_id\":\"20881007251907009745429510918909\",\"out_trade_no\":\"201512191483583349219\","
                + "\"point_amount\":\"0.00\",\"receipt_amount\":\"0.00\",\"send_pay_date\":\"2017-01-05 10:29:43\","
                + "\"total_amount\":\"0.05\",\"trade_no\":\"2017010521001004090235281478\",\"trade_status\":\"TRADE_SUCCESS\"},"
                + "\"sign\":\"io1ljN3iWahN6PesvlI4AYdEf5jQRX/05IOFu1duD+lifrPN1CdMS86Yf784fYAqPbhGBl8b6eC9NrnyUOZijmW38UX1Rg19Wt"
                + "7rqHK9KDBTDUWLK7q51v7FpxNg9aIiZdCSvfenuMZAyIWusQgx9iOXopaHpr03MtpErhHAxmY=\"}";
            // 统一收单交易退款查询
        } else if (responseKey.equals("alipay_trade_fastpay_refund_query_response")) {
            strRes = "{\"alipay_trade_fastpay_refund_query_response\":{\"code\":\"10000\",\"msg\":\"Success\","
                + "\"out_request_no\":\"20151219148358334921901\",\"out_trade_no\":\"201512191483583349219\","
                + "\"refund_amount\":\"0.01\",\"total_amount\":\"0.05\",\"trade_no\":\"2017010521001004090235281478\"},"
                + "\"sign\":\"p9d/1izYNTQxdxbNJ0tprJqT2tsc6dc40k68KyyYLV5UY1E4qgbvbZc+EkHVVPy7wooPSF9gl7aGAYGy7mI2YjabZAt3WKPjctsqTWLCmr"
                + "CB9m49lSL1uv/hr9YyktHkmh3z4aj0AoAcqPKDulyJyzJtDdVrLAZg7vKu06dNbCA=\"}";
            // 统一收单交易退款接口
        } else if (responseKey.equals("alipay_trade_refund_response")) {
            strRes = "{\"alipay_trade_refund_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"buyer_logon_id\":\"dre***@gmail.com\","
                + "\"buyer_user_id\":\"2088902928162096\",\"fund_change\":\"Y\",\"gmt_refund_pay\":\"2017-01-05 11:08:09\","
                + "\"open_id\":\"20881007251907009745429510918909\",\"out_trade_no\":\"201512191483583349219\","
                + "\"refund_fee\":\"0.01\",\"send_back_fee\":\"0.00\",\"trade_no\":\"2017010521001004090235281478\"},"
                + "\"sign\":\"ZWve4zrFeZ7nOL2IxigXFnmxXxvr6L08LJM5ibIryvMZ9czdwIeVqnbNWRcvdgihoiWPrra2w3rtJSC6lLzXKCcAq3DtGnrY4oNuxs2iH"
                + "5p+bbf6hhyEtMl7YmjLdLrc3M7EhfpMHPinZ3NcyXzQ/vp+xCESWUPoMf6RBOFRzIY=\"}";
        }
        LOGGER.debug("strRes:{}", strRes);
        Map<String, Object> queryMap = JSONUtil.parse(strRes, Map.class);
        Map<String, Object> resMap = (Map<String, Object>) queryMap.get(responseKey);
        if (res instanceof AliPayTradeQueryRes) {
            res = new AliPayTradeQueryRes();
        } else if (res instanceof AliRefundRes) {
            res = new AliRefundRes();
        } else if (res instanceof AliRefundTradeQueryRes) {
            res = new AliRefundTradeQueryRes();
        }
        Field[] fields = res.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals("serialVersionUID") || field.getName().equals("$jacocoData")) {
                continue;
            }
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), res.getClass());
            Method setMethod = pd.getWriteMethod();
            // 执行get方法返回一个Object
            setMethod.invoke(res, resMap.get(StringUtil.camelToSnake(field.getName())));
        }
        return res;
    }

    @Override
    public String checkUrl(String urlvalue) throws IOException {
        return "true";
    }
}
