package com.proper.enterprise.platform.pay.wechat.service.impl;

import com.proper.enterprise.platform.api.pay.constants.PayConstants;
import com.proper.enterprise.platform.api.pay.enums.PayResType;
import com.proper.enterprise.platform.api.pay.model.*;
import com.proper.enterprise.platform.api.pay.service.PayService;
import com.proper.enterprise.platform.common.pay.service.impl.AbstractPayImpl;
import com.proper.enterprise.platform.common.pay.utils.PayUtils;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.pay.wechat.adapter.SignAdapter;
import com.proper.enterprise.platform.pay.wechat.constants.WechatConstants;
import com.proper.enterprise.platform.pay.wechat.entity.WechatEntity;
import com.proper.enterprise.platform.pay.wechat.entity.WechatRefundEntity;
import com.proper.enterprise.platform.pay.wechat.model.*;
import com.proper.enterprise.platform.pay.wechat.repository.WechatRefundRepository;
import com.proper.enterprise.platform.pay.wechat.repository.WechatRepository;
import com.proper.enterprise.platform.pay.wechat.service.WechatPayResService;
import com.proper.enterprise.platform.pay.wechat.service.WechatPayService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.stereotype.Service;

import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 微信支付ServiceImpl
 */
@Service("pay_way_wechat")
public class WechatPayServiceImpl extends AbstractPayImpl implements PayService, WechatPayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WechatPayServiceImpl.class);
    private static final String STR_SUCCESS = "SUCCESS";

    @Autowired
    Marshaller marshaller;

    @Autowired
    WechatRepository weixinRepo;

    @Autowired
    WechatRefundRepository weixinRefundRepo;

    @Autowired
    WechatPayResService wechatPayResService;

    //-------------------------重写抽象类中的共通处理函数-------------------START----------------
    /**
     * 预支付请求对象转换
     *
     * @param req 请求对象
     * @return 处理结果
     */
    @Override
    protected OrderReq reqPrepay(PrepayReq req) throws Exception {
        try {
            WechatOrderReq wechatPrepay = new WechatOrderReq();
            wechatPrepay.setOutTradeNo(req.getOutTradeNo());
            wechatPrepay.setDetail(req.getPayIntent());
            wechatPrepay.setBody(req.getPayIntent());
            wechatPrepay.setAttach(req.getPayIntent());
            wechatPrepay.setTotalFee(Integer.parseInt(req.getTotalFee()));
            if (StringUtil.isNotNull(req.getPayTime()) && StringUtil.isNotNull(req.getOverMinuteTime())) {
                wechatPrepay.setTimeStart(req.getPayTime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.MINUTE, Integer.parseInt(req.getOverMinuteTime()));
                wechatPrepay.setTimeExpire(DateUtil.toString(cal.getTime(), "yyyyMMddHHmmss"));
            }
            return wechatPrepay;
        } catch (Exception e) {
            LOGGER.debug("WechatPayServiceImpl.reqPrepay[Exception]:{}", e);
            throw e;
        }
    }

    /**
     * 微信预支付
     *
     * @param req 请求对象
     * @return 处理结果
     * @throws Exception 保存异常
     */
    @Override
    protected <T extends PayResultRes, R extends OrderReq> T savePrepayImpl(R req)  throws Exception {
        // 返回给请求客户端处理结果
        WechatPayResultRes resObj = new WechatPayResultRes();
        WechatOrderReq uoReq = (WechatOrderReq) req;
        try {
            uoReq.setNonceStr(RandomStringUtils.randomAlphabetic(WechatConstants.WECHAT_PAY_RANDOM_LEN));
            uoReq.setSpbillCreateIp(WechatConstants.WECHAT_SPBILL_CREATE_IP);
            // 微信支付异步通知地址
            uoReq.setNotifyUrl(WechatConstants.WECHAT_PAY_URL_NOTICE);
            // 请求微信API接口
            WechatOrderRes res = getWechatRes(uoReq, WechatConstants.WECHAT_PAY_URL_UNIFIED,
                "unmarshallWechatOrderRes", false);
            // 返回对象
            WechatPayRes payObj = new WechatPayRes();
            LOGGER.debug("return_msg:" + res.getReturnMsg());
            // 以下字段在return_code为SUCCESS的时候有返回
            if (STR_SUCCESS.equals(res.getReturnCode()) && STR_SUCCESS.equals(res.getResultCode())) {
                LOGGER.debug("result_code:SUCCESS");
                // 返回给请求客户端处理结果
                resObj.setResultCode(PayResType.SUCCESS);
                // 返回给请求客户端处理结果消息
                resObj.setResultMsg("SUCCESS");

                // 设置签名参数
                // 预支付Id
                payObj.setPrepayid(res.getPrepayId());
                // 随机字符串
                payObj.setNoncestr(RandomStringUtils.randomAlphabetic(WechatConstants.WECHAT_PAY_RANDOM_LEN));
                // 时间戳
                payObj.setTimestamp(String.valueOf(System.currentTimeMillis() / 1000));
                // 进行签名
                SignAdapter signAdapter = new SignAdapter();
                String retSign = signAdapter.marshalObject(payObj, WechatPayRes.class);

                // -------------返回给客户端调用支付接口需要的参数-----------------------
                // 应用ID
                resObj.setAppid(res.getAppId());
                // 商户号
                resObj.setPartnerid(res.getMchId());
                // 预支付交易会话ID
                resObj.setPrepayid(payObj.getPrepayid());
                // 扩展字段
                resObj.setWxpackage(payObj.getPapackage());
                // 随机字符串
                resObj.setNoncestr(payObj.getNoncestr());
                // 时间戳
                resObj.setTimestamp(payObj.getTimestamp());
                // 签名
                resObj.setSign(retSign);
            } else {
                // 返回给请求客户端处理结果
                resObj.setResultCode(PayResType.SYSERROR);
                // 返回给请求客户端处理结果消息
                resObj.setResultMsg(res.getErrCode().concat("|").concat(res.getErrCodeDes()));
            }
        } catch (Exception e) {
            LOGGER.debug("WechatPayServiceImpl.savePrepayImpl[Exception]:{}", e);
            resObj.setResultCode(PayResType.SYSERROR);
            resObj.setResultMsg(PayConstants.APP_SYSTEM_ERR);
        }
        PayUtils.logEntity(resObj);
        return (T) resObj;
    }

    /**
     * 微信根据订单号查询结果
     *
     * @param outTradeNo 订单号
     * @return 处理结果
     */
    @Override
    protected <T> T getPayQueryRes(String outTradeNo) {
        WechatPayQueryReq req = new WechatPayQueryReq();
        req.setOutTradeNo(outTradeNo);
        req.setNonceStr(RandomStringUtils.randomAlphabetic(WechatConstants.WECHAT_PAY_RANDOM_LEN));
        SignAdapter signAdapter = new SignAdapter();
        WechatPayQueryRes queryRes = null;
        try {
            String sign = signAdapter.marshalObject(req, WechatPayQueryReq.class);
            req.setSign(sign);
            queryRes = getWechatRes(req, WechatConstants.WECHAT_PAY_URL_ORDER_QUERY, "unmarshallWechatPayQueryRes", false);
        } catch (Exception e) {
            LOGGER.debug("Wechat query error!", e);
            return null;
        }
        return (T)queryRes;
    }

    /**
     * 获取微信的退款请求对象
     *
     * @param refundReq 退款请求对象
     * @param <T> 转换后的微信退款请求对象
     * @return 微信退款请求对象
     */
    @Override
    protected <T> T getRefundReq(RefundReq refundReq) {
        WechatRefundReq wechatRefundReq = new WechatRefundReq();
        wechatRefundReq.setOutRefundNo(refundReq.getOutRequestNo());
        wechatRefundReq.setRefundFee(Integer.parseInt(refundReq.getRefundAmount()));
        wechatRefundReq.setTotalFee(Integer.parseInt(refundReq.getTotalFee()));
        wechatRefundReq.setOutTradeNo(refundReq.getOutTradeNo());

        return (T)wechatRefundReq;
    }

    /**
     * 微信退款操作
     *
     * @param refundBody 退款请求对象泛型
     * @param <T> 微信退款结果泛型
     * @return 微信退款结果
     */
    @Override
    protected <T> T saveRefundProcess(T refundBody) throws Exception {
        // 请求退款对象
        WechatRefundReq wechatRefundReq = (WechatRefundReq) refundBody;
        // 随机字符串
        wechatRefundReq.setNonceStr(RandomStringUtils.randomAlphabetic(WechatConstants.WECHAT_PAY_RANDOM_LEN));
        // 签名
        SignAdapter signAdapter = new SignAdapter();
        // 返回对象
        WechatRefundRes res = null;
        try {
            String sign = signAdapter.marshalObject(wechatRefundReq, WechatRefundReq.class);
            wechatRefundReq.setSign(sign);
            // 使用httsClient通过证书请求微信退款
            res = getWechatRes(wechatRefundReq, WechatConstants.WECHAT_PAY_URL_REFUND, "unmarshallWechatRefundRes", true);
            // 输出LOG
            PayUtils.logEntity(res);
            // 以下字段在return_code为SUCCESS的时候有返回
            if (STR_SUCCESS.equalsIgnoreCase(res.getReturnCode()) && STR_SUCCESS.equalsIgnoreCase(res.getResultCode())) {
                // 保存退款信息
                WechatRefundEntity oldRefundInfo = this.findByRefundNo(res.getOutRefundNo());
                if (oldRefundInfo == null) {
                    WechatRefundEntity wechatRefundInfo = new WechatRefundEntity();
                    wechatRefundInfo.setRefundNo(res.getOutRefundNo());
                    BeanUtils.copyProperties(res, wechatRefundInfo);
                    this.save(wechatRefundInfo);
                }
            }
        } catch (Exception e) {
            LOGGER.debug("Wechat refund Error!", e);
            return null;
        }
        return (T)res;
    }

    /**
     * 查询微信的订单退款信息
     *
     * @param orderNo 订单号
     * @param refundNo 退款订单号
     * @param <T> 查询微信退款结果泛型
     * @return 查询微信退款结果
     */
    @Override
    protected <T> T getRefundQueryRes(String orderNo, String refundNo) {
        WechatPayQueryReq req = new WechatPayQueryReq();
        // 退款单号
        req.setOutRefundNo(refundNo);
        // 随机字符串
        req.setNonceStr(RandomStringUtils.randomAlphabetic(WechatConstants.WECHAT_PAY_RANDOM_LEN));
        // 签名
        SignAdapter signAdapter = new SignAdapter();
        WechatRefundQueryRes queryRes = null;
        try {
            String sign = signAdapter.marshalObject(req, WechatPayQueryReq.class);
            req.setSign(sign);
            queryRes = getWechatRes(req, WechatConstants.WECHAT_PAY_URL_REFUND_QUERY, "unmarshallWechatRefundQueryRes", false);
        } catch (Exception e) {
            LOGGER.debug("Wechat refund query error!", e);
            return null;
        }
        return (T)queryRes;
    }
    //-------------------------重写抽象类中的共通处理函数--------------------END------------------

    /**
     * 保存微信支付信息
     *
     * @param wechatInfo 微信支付对象
     * @return wechatInfo
     */
    @Override
    public WechatEntity save(WechatEntity wechatInfo) {
        return weixinRepo.save(wechatInfo);
    }

    /**
     * 保存微信退款信息
     *
     * @param weixinRefundInfo 微信退款对象
     * @return weixinRefundInfo
     */
    @Override
    public WechatRefundEntity save(WechatRefundEntity weixinRefundInfo) {
        return weixinRefundRepo.save(weixinRefundInfo);
    }

    /**
     * 通过订单号查询微信支付信息
     *
     * @param outTradeNo 商户内部订单号
     * @return WechatInfo
     */
    @Override
    public WechatEntity findByOutTradeNo(String outTradeNo) {
        return weixinRepo.findByOutTradeNo(outTradeNo);
    }

    /**
     * 通过微信订单号查询微信支付信息
     *
     * @param tradeNo 微信订单号
     * @return WechatInfo
     */
    @Override
    public WechatEntity getByTradeNo(String tradeNo) {
        return weixinRepo.getByTransactionId(tradeNo);
    }

    /**
     * 通过退款单号查询微信退款信息
     *
     * @param refundNo 退款单号
     * @return WechatRefundInfo
     */
    @Override
    public WechatRefundEntity findByRefundNo(String refundNo) {
        return weixinRefundRepo.findByRefundNo(refundNo);
    }

    /**
     * 创建微信支付信息Entity
     *
     * @param wechatNoticeInfo 微信异步通知对象
     * @return wechatInfo 转换后的微信异步通知支付信息
     */
    @Override
    public WechatEntity getWechatNoticeInfo(WechatNoticeRes wechatNoticeInfo) {
        WechatEntity wechatInfo = new WechatEntity();
        BeanUtils.copyProperties(wechatNoticeInfo, wechatInfo);
        return wechatInfo;
    }

    /**
     * 微信异步通知验签
     *
     * @param noticeRes 异步通知对象
     * @return 验签结果
     */
    @Override
    public boolean isValid(WechatNoticeRes noticeRes) {
        String sign;
        try {
            sign = new SignAdapter().marshalObject(noticeRes, WechatNoticeRes.class);
            LOGGER.debug("notice_sign: {}; verify_sign: {}", noticeRes.getSign(), sign);
            return noticeRes.getSign().equals(sign);
        } catch (Exception e) {
            LOGGER.debug("Check validation of sign occurs error!", e);
            return false;
        }
    }

    /**
     * 获取微信接口请求结果
     *
     * @param obj 请求对象
     * @param url 请求地址
     * @param beanId 实例Bean
     * @param isHttpsRequest true : http请求 ; false : https请求
     * @param <T> 泛型
     * @return 请求结果
     * @throws Exception 获取异常
     */
    private <T> T getWechatRes(Object obj, String url, String beanId, boolean isHttpsRequest) throws Exception {
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, new StreamResult(writer));
        String requestXML = writer.toString();
        LOGGER.debug("{}_requestXML:{}", beanId, requestXML);
        if (isHttpsRequest) {
            return (T)wechatPayResService.getWechatApiRes(url, beanId, requestXML, true);
        } else {
            return (T)wechatPayResService.getWechatApiRes(url, beanId, requestXML, false);
        }
    }

    @Override
    protected <T> T getBillProcess(BillReq billReq) throws Exception {
        DateFormat dft = new SimpleDateFormat("yyyyMMdd");
        try {
            WechatBillReq wechatBillReq = new WechatBillReq();
            wechatBillReq.setBillDate(dft.format(billReq.getDate()));
            // 随机字符串
            wechatBillReq.setNonceStr(RandomStringUtils.randomAlphabetic(WechatConstants.WECHAT_PAY_RANDOM_LEN));
            // 签名
            SignAdapter signAdapter = new SignAdapter();
            String sign = signAdapter.marshalObject(wechatBillReq, WechatBillReq.class);
            wechatBillReq.setSign(sign);
            // 使用httsClient通过证书请求微信退款
            return (T) getWechatRes(wechatBillReq, WechatConstants.WECHAT_PAY_URL_BILL, "unmarshallWechatBillRes", true);
        } catch (Exception e) {
            LOGGER.error("Export wechat bill failed! {},{}", dft.format(billReq.getDate()), e);
            throw e;
        }
    }

}
