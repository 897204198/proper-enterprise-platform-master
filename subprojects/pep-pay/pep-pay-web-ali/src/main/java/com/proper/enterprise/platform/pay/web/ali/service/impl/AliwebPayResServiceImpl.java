package com.proper.enterprise.platform.pay.web.ali.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.proper.enterprise.platform.pay.web.ali.service.AliwebPayResService;
import com.proper.enterprise.platform.pay.web.ali.constants.AliwebConstants;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 支付宝网页支付统一接口响应ServiceImpl.
 */
@Service
public class AliwebPayResServiceImpl implements AliwebPayResService {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AliwebPayResServiceImpl.class);

    /**
     * 获取支付宝网页支付各个接口的结果.
     *
     * @param req 请求对象.
     * @param res 响应对象.
     * @return res 各个接口响应结果.
     * @throws Exception 异常.
     */
    @Override
    public Object getAliInterfaceRes(Object req, Object res) throws Exception {
        LOGGER.debug("aliwebReq:{}", req);
        AlipayClient client = new DefaultAlipayClient(AliwebConstants.ALI_WEBPAY_URL, AliwebConstants.ALI_WEBPAY_APPID,
            AliwebConstants.ALI_WEBPAY_PRIVATEKEY, AliwebConstants.ALI_WEBPAY_FORMAT, AliwebConstants.ALI_WEBPAY_CHARSET,
            AliwebConstants.ALI_WEBPAY_PUBLICKEY, AliwebConstants.ALI_WEBPAY_SIGNTYPE);

        if (req instanceof AlipayTradeWapPayRequest) {
            AlipayTradeWapPayRequest alipayRequest = (AlipayTradeWapPayRequest)req;
            // 调用SDK生成表单
            res = client.pageExecute(alipayRequest).getBody();
        } else if (req instanceof AlipayTradeQueryRequest) {
            AlipayTradeQueryRequest alipayRequest = (AlipayTradeQueryRequest)req;
            // 调用SDK生成表单
            res = client.execute(alipayRequest);
        } else if (req instanceof AlipayTradeRefundRequest) {
            AlipayTradeRefundRequest alipayRequest = (AlipayTradeRefundRequest)req;
            // 调用SDK生成表单
            res = client.execute(alipayRequest);
        } else if (req instanceof AlipayTradeFastpayRefundQueryRequest) {
            AlipayTradeFastpayRefundQueryRequest alipayRequest = (AlipayTradeFastpayRefundQueryRequest)req;
            // 调用SDK生成表单
            res = client.execute(alipayRequest);
        }
        return res;
    }

    /**
     * 支付宝网页支付验证.
     *
     * @param params 验证参数.
     * @return 验证结果.
     * @throws Exception 异常.
     */
    @Override
    public boolean noticeVerify(Map<String, String> params) throws Exception {
        return AlipaySignature.rsaCheckV1(params, AliwebConstants.ALI_WEBPAY_PUBLICKEY,
            AliwebConstants.ALI_WEBPAY_CHARSET, AliwebConstants.ALI_WEBPAY_SIGNTYPE);
    }

}
