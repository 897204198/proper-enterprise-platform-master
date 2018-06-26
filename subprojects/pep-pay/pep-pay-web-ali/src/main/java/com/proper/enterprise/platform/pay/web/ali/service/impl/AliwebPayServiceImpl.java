package com.proper.enterprise.platform.pay.web.ali.service.impl;

import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.proper.enterprise.platform.api.pay.constants.PayConstants;
import com.proper.enterprise.platform.api.pay.enums.PayResType;
import com.proper.enterprise.platform.api.pay.factory.PayFactory;
import com.proper.enterprise.platform.api.pay.model.*;
import com.proper.enterprise.platform.api.pay.service.PayService;
import com.proper.enterprise.platform.common.pay.service.impl.AbstractPayImpl;
import com.proper.enterprise.platform.common.pay.utils.PayUtils;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.pay.web.ali.entity.AliwebEntity;
import com.proper.enterprise.platform.pay.web.ali.model.AliwebOrderReq;
import com.proper.enterprise.platform.pay.web.ali.model.AliwebRefundReq;
import com.proper.enterprise.platform.pay.web.ali.repository.AliwebRepository;
import com.proper.enterprise.platform.pay.web.ali.service.AliwebPayResService;
import com.proper.enterprise.platform.pay.web.ali.service.AliwebPayService;
import com.proper.enterprise.platform.pay.web.ali.constants.AliwebConstants;
import com.proper.enterprise.platform.pay.web.ali.entity.AliwebRefundEntity;
import com.proper.enterprise.platform.pay.web.ali.model.AliwebPayResultRes;
import com.proper.enterprise.platform.pay.web.ali.repository.AliwebRefundRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 支付宝web支付ServiceImpl.
 */
@Service("pay_way_aliweb")
public class AliwebPayServiceImpl extends AbstractPayImpl implements PayService, AliwebPayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliwebPayServiceImpl.class);
    private static final String ALI_REQUEST_CODE = "10000";

    @Autowired
    PayFactory payFactory;

    @Autowired
    AliwebRepository aliwebRepo;

    @Autowired
    AliwebRefundRepository aliwebRefundRepo;

    @Autowired
    AliwebPayService aliwebService;

    @Autowired
    AliwebPayResService aliwebResService;


    //-------------------------重写抽象类中的共通处理函数-------------------START----------------
    /**
     * 网页预支付请求对象转换
     *
     * @param req 请求对象
     * @return 处理结果
     */
    @Override
    protected OrderReq reqPrepay(PrepayReq req) throws Exception {
        try {
            AliwebOrderReq aliPrepay = new AliwebOrderReq();
            aliPrepay.setOutTradeNo(req.getOutTradeNo());
            aliPrepay.setSubject(req.getPayIntent());
            aliPrepay.setBody(req.getPayIntent());
            aliPrepay.setTotalFee(PayUtils.convertMoneyFen2Yuan(req.getTotalFee()));
            // 设置超时时间
            if (StringUtil.isNumeric(req.getOverMinuteTime())) {
                aliPrepay.setTimeoutExpress(req.getOverMinuteTime().concat("m"));
            }
            return aliPrepay;
        } catch (Exception e) {
            LOGGER.debug("AliwebPayServiceImpl.reqPrepay[Exception]:{}", e);
            throw e;
        }
    }

    /**
     * 支付宝网页支付预支付处理
     *
     * @param req 请求对象
     * @return 处理结果
     * @throws Exception 保存异常
     */
    @Override
    protected <T extends PayResultRes, R extends OrderReq> T savePrepayImpl(R req)  throws Exception {
        // 返回给请求客户端处理结果
        AliwebPayResultRes resObj = new AliwebPayResultRes();
        AliwebOrderReq webReq = (AliwebOrderReq)req;
        // form表单生产
        String form = "";
        try {
            // 封装请求支付信息
            AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
            model.setOutTradeNo(webReq.getOutTradeNo());
            model.setSubject(webReq.getSubject());
            model.setTotalAmount(webReq.getTotalFee());
            model.setBody(webReq.getBody());
            model.setTimeoutExpress(webReq.getTimeoutExpress());
            model.setProductCode(AliwebConstants.ALI_WEBPAY_PRODUCT_CODE);
            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
            alipayRequest.setBizModel(model);
            // 设置异步通知地址
            alipayRequest.setNotifyUrl(AliwebConstants.ALI_WEBPAY_NOTIFY_URL);
            // 设置同步地址
            alipayRequest.setReturnUrl(AliwebConstants.ALI_WEBPAY_RETURN_URL);
            // 获取结果
            form = (String)aliwebResService.getAliInterfaceRes(alipayRequest, form);
            // -------------返回给客户端调用支付接口需要的参数-----------------------
            resObj.setResultCode(PayResType.SUCCESS);
            resObj.setResultMsg("SUCCESS");
            resObj.setForm(form);
            LOGGER.debug("form:{}", form);
        } catch (Exception e) {
            LOGGER.debug("AliwebPayServiceImpl.savePrepayImpl[Exception]:{}", e);
            resObj.setResultCode(PayResType.SYSERROR);
            resObj.setResultMsg(PayConstants.APP_SYSTEM_ERR);
        }
        return (T)resObj;
    }

    @Override
    protected <T> T getBillProcess(BillReq billBodyReq) throws Exception {
        return null;
    }

    /**
     * 支付宝根据订单号查询结果
     *
     * @param outTradeNo 订单号
     * @return 处理结果
     */
    @Override
    protected <T> T getPayQueryRes(String outTradeNo) {
        AlipayTradeQueryResponse res = new AlipayTradeQueryResponse();
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        try {
            AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
            // 设置参数
            AlipayTradeQueryModel model = new AlipayTradeQueryModel();
            model.setOutTradeNo(outTradeNo);
            alipayRequest.setBizModel(model);
            // 获取结果
            res = (AlipayTradeQueryResponse)aliwebResService.getAliInterfaceRes(alipayRequest, res);
        } catch (Exception e) {
            LOGGER.debug("AliwebPayServiceImpl.getPayQueryRes[Exception]:{}", e);
            res = null;
        }

        return (T)res;
    }

    /**
     * 获取支付宝的网页支付退款请求对象
     *
     * @param refundReq 退款请求对象
     * @param <T> 转换后的支付宝退款请求对象
     * @return 支付宝退款请求对象
     */
    @Override
    protected <T> T getRefundReq(RefundReq refundReq) {
        AliwebRefundReq aliRefundReq = new AliwebRefundReq();
        aliRefundReq.setRefundNo(refundReq.getOutRequestNo());
        aliRefundReq.setOutTradeNo(refundReq.getOutTradeNo());
        aliRefundReq.setAmount(PayUtils.convertMoneyFen2Yuan(refundReq.getRefundAmount()));
        return (T)aliRefundReq;
    }

    /**
     * 支付宝退款操作
     *
     * @param refundBody 退款请求对象泛型
     * @param <T> 支付宝退款结果泛型
     * @return 支付宝退款结果
     */
    @Override
    protected <T> T saveRefundProcess(T refundBody) throws Exception {
        AlipayTradeRefundResponse res = new AlipayTradeRefundResponse();
        AliwebRefundReq aliRefundReq = (AliwebRefundReq) refundBody;
        AliwebRefundEntity refund = new AliwebRefundEntity();
        try {
            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            model.setOutTradeNo(aliRefundReq.getOutTradeNo());
            model.setOutRequestNo(aliRefundReq.getRefundNo());
            model.setRefundReason(aliRefundReq.getRefundReason());
            model.setRefundAmount(aliRefundReq.getAmount());
            // 请求参数
            AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
            alipayRequest.setBizModel(model);
            // 获取结果
            res = (AlipayTradeRefundResponse)aliwebResService.getAliInterfaceRes(alipayRequest, res);

            // 退款单号
            String refundNo = aliRefundReq.getRefundNo();
            BeanUtils.copyProperties(res, refund);
            // 退款使用的资金渠道
            refund.setRefundDetailItem(JSONUtil.toJSON(res.getRefundDetailItemList()));
            // 退款单号
            refund.setRefundNo(refundNo);

            AliwebRefundEntity oldRefund = findByRefundNo(refundNo);
            if (ALI_REQUEST_CODE.equals(refund.getCode()) && oldRefund == null) {
                save(refund);
            }
        } catch (Exception e) {
            LOGGER.debug("AliwebPayServiceImpl.saveRefundProcess[Exception]:{}", e);
            res = null;
        }

        return (T)res;
    }

    /**
     * 查询支付宝网页支付的订单退款信息
     *
     * @param orderNo 订单号
     * @param refundNo 退款订单号
     * @param <T> 查询支付宝退款结果泛型
     * @return 查询支付宝退款结果
     */
    @Override
    protected <T> T getRefundQueryRes(String orderNo, String refundNo) {
        AlipayTradeFastpayRefundQueryResponse res = new AlipayTradeFastpayRefundQueryResponse();

        try {
            AlipayTradeFastpayRefundQueryRequest alipayRequest = new AlipayTradeFastpayRefundQueryRequest();
            // 请求参数
            AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
            model.setOutTradeNo(orderNo);
            model.setOutRequestNo(refundNo);
            alipayRequest.setBizModel(model);
            // 获取结果
            res = (AlipayTradeFastpayRefundQueryResponse)aliwebResService.getAliInterfaceRes(alipayRequest, res);
        } catch (Exception e) {
            LOGGER.debug("AliwebPayServiceImpl.getRefundQueryRes[Exception]:{}", e);
            res = null;
        }
        return (T)res;
    }
    //-------------------------重写抽象类中的共通处理函数-----------------END------------------

    /**
     * 保存支付宝网页支付信息
     *
     * @param ali 支付宝网页支付对象
     * @return AliwebEntity
     */
    public AliwebEntity save(AliwebEntity ali) {
        return aliwebRepo.save(ali);
    }

    /**
     * 保存支付宝网页支付退款信息
     *
     * @param aliRefund 支付宝网页支付退款对象
     * @return AliRefund
     */
    public AliwebRefundEntity save(AliwebRefundEntity aliRefund) {
        return aliwebRefundRepo.save(aliRefund);
    }

    /**
     * 通过订单号查询支付宝网页支付信息
     *
     * @param outTradeNo 商户内部订单号
     * @return AliwebEntity
     */
    public AliwebEntity findByOutTradeNo(String outTradeNo) {
        return aliwebRepo.findByOutTradeNo(outTradeNo);
    }

    /**
     * 通过退款单号查询支付宝退款信息
     *
     * @param refundNo 退款单号
     * @return AliRefund
     */
    public AliwebRefundEntity findByRefundNo(String refundNo) {
        return aliwebRefundRepo.findByRefundNo(refundNo);
    }



    /**
     * 创建支付宝网页支付信息Entity
     *
     * @param params
     *            参数
     * @return alipayinfo 支付信息
     * @throws Exception 获取异常
     */
    @Override
    public AliwebEntity getAliwebNoticeInfo(Map<String, String> params) throws Exception {
        Field[] fields = AliwebEntity.class.getDeclaredFields();
        Set<String> set = new HashSet<>();
        for (Field field : fields) {
            set.add(field.getName());
        }
        String value;
        AliwebEntity aliwebpayinfo = new AliwebEntity();
        for (String fieldName : set) {
            if (!StringUtil.startsWith(fieldName, "$")) {
                String paramName = StringUtil.camelToSnake(fieldName);
                value = params.get(paramName);
                // 交易状态 0:未知
                if ("trade_status".equals(paramName) && StringUtil.isNull(params.get("trade_status"))) {
                    aliwebpayinfo.setTradeStatus(AliwebConstants.ALI_WEBPAY_NOTICE_TARDESTATUS_UNKONWN);
                    // 本次交易支付的订单金额，单位为人民币（元）
                } else if ("total_amount".equals(paramName) && StringUtil.isNull(params.get("total_amount"))) {
                    aliwebpayinfo.setTotalAmount("0");
                    // 商家在交易中实际收到的款项，单位为元
                } else if ("receipt_amount".equals(paramName) && StringUtil.isNull(params.get("receipt_amount"))) {
                    aliwebpayinfo.setReceiptAmount("0");
                    // 用户在交易中支付的可开发票的金额
                } else if ("invoice_amount".equals(paramName) && StringUtil.isNull(params.get("invoice_amount"))) {
                    aliwebpayinfo.setInvoiceAmount("0");
                    // 用户在交易中支付的金额
                } else if ("buyer_pay_amount".equals(paramName) && StringUtil.isNull(params.get("buyer_pay_amount"))) {
                    aliwebpayinfo.setBuyerPayAmount("0");
                    // 使用集分宝支付的金额
                } else if ("point_amount".equals(paramName) && StringUtil.isNull(params.get("point_amount"))) {
                    aliwebpayinfo.setPointAmount("0");
                    // 总退款金额
                } else if ("refund_fee".equals(paramName) && StringUtil.isNull(params.get("refund_fee"))) {
                    aliwebpayinfo.setRefundFee("0");
                    // 其他情况
                } else {
                    AliwebEntity.class.getMethod("set" + StringUtil.capitalize(fieldName), String.class).invoke(
                        aliwebpayinfo, value);
                }
            }
        }
        return aliwebpayinfo;
    }

}
