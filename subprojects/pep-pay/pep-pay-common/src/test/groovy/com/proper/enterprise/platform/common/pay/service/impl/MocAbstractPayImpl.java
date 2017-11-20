package com.proper.enterprise.platform.common.pay.service.impl;

import com.proper.enterprise.platform.api.pay.enums.PayResType;
import com.proper.enterprise.platform.api.pay.model.*;
import com.proper.enterprise.platform.api.pay.service.PayService;
import com.proper.enterprise.platform.common.pay.OrderReqTest;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service("pay_way_ali")
@Primary
public class MocAbstractPayImpl extends AbstractPayImpl implements PayService {

    /**
     * 查询各种支付方式的订单退款信息
     *
     * @param orderNo 订单号
     * @param refundNo 退款订单号
     * @param <T> 查询各种支付方式退款结果泛型
     * @return 查询各种支付方式退款结果
     */
    @Override
    protected  <T> T getRefundQueryRes(String orderNo, String refundNo){
        return (T)"queryRefundSuccess";
    }

    /**
     * 获取各种支付方式的退款请求对象
     *
     * @param refundBody 各种支付方式退款请求对象
     * @param <T> 各种支付方式退款结果泛型
     * @return 各种支付方式退款结果
     */
    @Override
    protected  <T> T saveRefundProcess(T refundBody) throws Exception {
        String ret = "";
        RefundReq refundReq = (RefundReq)refundBody;
        if(StringUtil.isNotEmpty(refundReq.getOutRequestNo())) {
            ret = "refundPaySuccess";
        } else {
            throw new Exception();
        }
        return (T)ret;
    }

    /**
     * 获取各种支付方式的退款请求对象
     *
     * @param refundReq 退款请求对象
     * @param <T> 转换后的各种支付方式退款请求对象
     * @return 各种支付方式退款请求对象
     */
    protected  <T> T getRefundReq(RefundReq refundReq){
        return (T)refundReq;
    }

    /**
     * 根据订单号进行交易查询
     *
     * @param outTradeNo 订单号(系统内)
     * @param <T> 返回对象泛型
     * @return 查询结果
     */
    protected  <T> T getPayQueryRes(String outTradeNo){
        return (T)"queryPaySuccess";
    }

    /**
     * 预支付请求对象转换
     *
     * @param req 请求对象
     * @return 处理结果
     * @throws Exception
     */
    protected  OrderReq reqPrepay(PrepayReq req) throws Exception {
        OrderReqTest orderReq = new OrderReqTest();
        orderReq.setValue(req.getOutTradeNo());
        return orderReq;
    }

    /**
     * 支付宝预支付
     *
     * @param req 请求对象
     * @return resObj 处理结果
     * @throws Exception
     */
    @Override
    protected  <T extends PayResultRes, R extends OrderReq> T savePrepayImpl(R req) throws Exception{
        PayResultRes res = new PayResultRes();
        OrderReqTest reqTest = (OrderReqTest)req;
        if(reqTest.getValue().equals("12345678901234567890")) {
            res.setResultCode(PayResType.SUCCESS);
            res.setResultMsg("success");
        } else {
            throw new Exception();
        }
        return (T)res;
    }
    @Override
    protected <T> T getBillProcess(BillReq billBodyReq) throws Exception {
        return null;
    }
}
