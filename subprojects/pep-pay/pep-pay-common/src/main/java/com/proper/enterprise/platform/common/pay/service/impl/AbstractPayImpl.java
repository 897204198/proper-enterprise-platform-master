package com.proper.enterprise.platform.common.pay.service.impl;

import com.proper.enterprise.platform.api.pay.constants.PayConstants;
import com.proper.enterprise.platform.api.pay.enums.PayResType;
import com.proper.enterprise.platform.api.pay.model.*;
import com.proper.enterprise.platform.api.pay.service.PayService;
import com.proper.enterprise.platform.common.pay.service.PayBusinessService;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 支付抽象类
 */
public abstract class AbstractPayImpl implements PayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPayImpl.class);

    @Autowired
    private PayBusinessService payBusinessService;

    /**
     * 预支付业务处理
     *
     * @return 业务处理结果
     * @throws Exception 异常
     */
    @Override
    public PayResultRes savePrepayBusiness(String payWay, PrepayReq prepayReq, OrderReq orderReq) throws Exception {
        return payBusinessService.savePrepayBusiness(payWay, prepayReq, orderReq);
    }

    /**
     * 生成支付信息
     *
     * @param req 预支付请求对象
     * @return 预支付请求处理结果对象
     */
    @Override
    public <T extends PayResultRes> T savePrepay(PrepayReq req) {
        PayResultRes resObj =  new PayResultRes();
        try {
            resObj = getPrepayRes(req);
            // 校验预支付请求参数
            if(resObj.getResultCode().getCode() != PayResType.SUCCESS.getCode()) {
                return (T)resObj;
            } else {
                // 预支付处理
                OrderReq orderReq =  reqPrepay(req);
                resObj = savePrepayImpl(orderReq);
            }
        } catch (Exception e) {
            LOGGER.info("AbstractPayImpl.savePrepay[Exception]:{}", e);
            resObj.setResultCode(PayResType.SYSERROR);
            resObj.setResultMsg(PayConstants.APP_SYSTEM_ERR);
        }
        return (T)resObj;
    }

    /**
     * 查询订单信息
     *
     * @param outTradeNo 订单号(系统内)
     * @return 查询结果
     */
    @Override
    public <T> T queryPay(String outTradeNo) {
        return (T)getPayQueryRes(outTradeNo);
    }

    /**
     * 退款
     *
     * @param refundReq 退款请求对象
     * @return 退款结果
     */
    @Override
    public <T> T refundPay(RefundReq refundReq) {
        try {
            T refundBody = getRefundReq(refundReq);
            return saveRefundProcess(refundBody);
        } catch (Exception e) {
            LOGGER.info("AbstractPayImpl.refundPay[Exception]:{}", e);
            return null;
        }
    }

    /**
     * 退款查询
     *
     * @param orderNo 原订单号
     * @param refundNo 退款单号
     * @return 查询结果
     */
    @Override
    public <T> T queryRefund(String orderNo, String refundNo) {
        return (T)getRefundQueryRes(orderNo, refundNo);
    }

    /**
     * 查询各种支付方式的订单退款信息
     *
     * @param orderNo 订单号
     * @param refundNo 退款订单号
     * @param <T> 查询各种支付方式退款结果泛型
     * @return 查询各种支付方式退款结果
     */
    protected abstract <T> T getRefundQueryRes(String orderNo, String refundNo);

    /**
     * 获取各种支付方式的退款请求对象
     *
     * @param refundBody 各种支付方式退款请求对象
     * @param <T> 各种支付方式退款结果泛型
     * @return 各种支付方式退款结果
     */
    protected abstract <T> T saveRefundProcess(T refundBody) throws Exception;

    /**
     * 获取各种支付方式的退款请求对象
     *
     * @param refundReq 退款请求对象
     * @param <T> 转换后的各种支付方式退款请求对象
     * @return 各种支付方式退款请求对象
     */
    protected abstract <T> T getRefundReq(RefundReq refundReq);

    /**
     * 根据订单号进行交易查询
     *
     * @param outTradeNo 订单号(系统内)
     * @param <T> 返回对象泛型
     * @return 查询结果
     */
    protected abstract <T> T getPayQueryRes(String outTradeNo);

    /**
     * 预支付请求对象转换
     *
     * @param req 请求对象
     * @return 处理结果
     * @throws Exception
     */
    protected abstract OrderReq reqPrepay(PrepayReq req) throws Exception;

    /**
     * 支付宝预支付
     *
     * @param req 请求对象
     * @return resObj 处理结果
     * @throws Exception
     */
    protected abstract <T extends PayResultRes, R extends OrderReq> T savePrepayImpl(R req) throws Exception;

    /**
     * 校验请求预支付参数
     *
     * @param req 请求对象
     * @return ret 校验结果
     * @throws Exception
     */
    protected <T extends PayResultRes> T getPrepayRes(PrepayReq req) throws Exception{
        // 返回值
        PayResultRes resObj = new PayResultRes();
        // 请求对象为空
        if(req == null) {
            resObj.setResultCode(PayResType.REQERROR);
            resObj.setResultMsg(PayConstants.APP_PAY_REQ_ERR);
            // 请求对象订单号为空
        } else if (StringUtil.isEmpty(req.getOutTradeNo())) {
            resObj.setResultCode(PayResType.ORDERNUMERROR);
            resObj.setResultMsg(PayConstants.APP_PAY_ORDERNO_ERR);
            // 支付方式不正确
        } else if(StringUtil.isEmpty(req.getPayWay())) {
            resObj.setResultCode(PayResType.PAYWAYERROR);
            resObj.setResultMsg(PayConstants.APP_PAY_PAYWAY_ERR);
            // 支付金额为空
        } else if(StringUtil.isEmpty(req.getTotalFee())) {
            resObj.setResultCode(PayResType.MONEYERROR);
            resObj.setResultMsg(PayConstants.APP_PAY_MONEY_ERR);
            // 支付用途为空
        } else if(StringUtil.isEmpty(req.getPayIntent())) {
            resObj.setResultCode(PayResType.PAYINTENTERROR);
            resObj.setResultMsg(PayConstants.APP_PAY_PAYINTENT_ERR);
            // 校验金额
        } else {
            BigDecimal bigDecimal = new BigDecimal(req.getTotalFee());
            // 小数点后最多两位并且大于零正则表达式
            Pattern pattern = Pattern.compile("^([1-9]\\d*)?$");
            Matcher matcher = pattern.matcher(String.valueOf(bigDecimal));
            // 校验金额小于0 或 不满足小数点后最多两位并且大于零
            if(bigDecimal.compareTo(new BigDecimal("0")) <= 0 || !matcher.matches()) {
                resObj.setResultCode(PayResType.MONEYERROR);
                resObj.setResultMsg(PayConstants.APP_PAY_MONEY_ERR);
            } else {
                resObj.setResultCode(PayResType.SUCCESS);
                resObj.setResultMsg("success");
            }
        }
        return (T)resObj;
    }

    /**
     * 获取对账单
     * @param billReq
     * @param <T>
     * @return
     */
    @Override
    public <T> T getBill(BillReq billReq) {
        try {
            return this.getBillProcess(billReq);
        } catch (Exception e) {
            LOGGER.info("AbstractPayImpl.getBill[Exception]:{}", e);
            return null;
        }
    }

    /**
     * 获取对账单
     */
    protected abstract <T> T getBillProcess(BillReq billBodyReq) throws Exception;
}
