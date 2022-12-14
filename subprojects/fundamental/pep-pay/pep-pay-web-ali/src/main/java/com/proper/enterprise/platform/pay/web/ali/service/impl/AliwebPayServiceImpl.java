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
import com.proper.enterprise.platform.api.pay.PayConstants;
import com.proper.enterprise.platform.api.pay.enums.PayResType;
import com.proper.enterprise.platform.api.pay.factory.PayFactory;
import com.proper.enterprise.platform.api.pay.model.*;
import com.proper.enterprise.platform.api.pay.service.PayService;
import com.proper.enterprise.platform.common.pay.service.impl.AbstractPayImpl;
import com.proper.enterprise.platform.common.pay.utils.PayUtils;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.pay.web.ali.PayWebAliProperties;
import com.proper.enterprise.platform.pay.web.ali.entity.AliwebEntity;
import com.proper.enterprise.platform.pay.web.ali.entity.AliwebRefundEntity;
import com.proper.enterprise.platform.pay.web.ali.model.AliwebOrderReq;
import com.proper.enterprise.platform.pay.web.ali.model.AliwebPayResultRes;
import com.proper.enterprise.platform.pay.web.ali.model.AliwebRefundReq;
import com.proper.enterprise.platform.pay.web.ali.repository.AliwebRefundRepository;
import com.proper.enterprise.platform.pay.web.ali.repository.AliwebRepository;
import com.proper.enterprise.platform.pay.web.ali.service.AliwebPayResService;
import com.proper.enterprise.platform.pay.web.ali.service.AliwebPayService;
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
 * ?????????web??????ServiceImpl.
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

    @Autowired
    PayWebAliProperties payWebAliProperties;

    //-------------------------???????????????????????????????????????-------------------START----------------
    /**
     * ?????????????????????????????????
     *
     * @param req ????????????
     * @return ????????????
     */
    @Override
    protected OrderReq reqPrepay(PrepayReq req) throws Exception {
        try {
            AliwebOrderReq aliPrepay = new AliwebOrderReq();
            aliPrepay.setOutTradeNo(req.getOutTradeNo());
            aliPrepay.setSubject(req.getPayIntent());
            aliPrepay.setBody(req.getPayIntent());
            aliPrepay.setTotalFee(PayUtils.convertMoneyFen2Yuan(req.getTotalFee()));
            // ??????????????????
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
     * ????????????????????????????????????
     *
     * @param req ????????????
     * @return ????????????
     * @throws Exception ????????????
     */
    @Override
    @SuppressWarnings("unchecked")
    protected <T extends PayResultRes, R extends OrderReq> T savePrepayImpl(R req)  throws Exception {
        // ????????????????????????????????????
        AliwebPayResultRes resObj = new AliwebPayResultRes();
        AliwebOrderReq webReq = (AliwebOrderReq)req;
        // form????????????
        String form = "";
        try {
            // ????????????????????????
            AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
            model.setOutTradeNo(webReq.getOutTradeNo());
            model.setSubject(webReq.getSubject());
            model.setTotalAmount(webReq.getTotalFee());
            model.setBody(webReq.getBody());
            model.setTimeoutExpress(webReq.getTimeoutExpress());
            model.setProductCode(payWebAliProperties.getPayWebAliProductCode());
            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
            alipayRequest.setBizModel(model);
            // ????????????????????????
            alipayRequest.setNotifyUrl(payWebAliProperties.getPayWebAliNotifyUrl());
            // ??????????????????
            alipayRequest.setReturnUrl(payWebAliProperties.getPayWebAliReturnUrl());
            // ????????????
            form = (String)aliwebResService.getAliInterfaceRes(alipayRequest, form);
            // -------------???????????????????????????????????????????????????-----------------------
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
     * ????????????????????????????????????
     *
     * @param outTradeNo ?????????
     * @return ????????????
     */
    @Override
    @SuppressWarnings("unchecked")
    protected <T> T getPayQueryRes(String outTradeNo) {
        AlipayTradeQueryResponse res = new AlipayTradeQueryResponse();
        // SDK ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        try {
            AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
            // ????????????
            AlipayTradeQueryModel model = new AlipayTradeQueryModel();
            model.setOutTradeNo(outTradeNo);
            alipayRequest.setBizModel(model);
            // ????????????
            res = (AlipayTradeQueryResponse)aliwebResService.getAliInterfaceRes(alipayRequest, res);
        } catch (Exception e) {
            LOGGER.debug("AliwebPayServiceImpl.getPayQueryRes[Exception]:{}", e);
            res = null;
        }

        return (T)res;
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param refundReq ??????????????????
     * @param <T> ???????????????????????????????????????
     * @return ???????????????????????????
     */
    @Override
    @SuppressWarnings("unchecked")
    protected <T> T getRefundReq(RefundReq refundReq) {
        AliwebRefundReq aliRefundReq = new AliwebRefundReq();
        aliRefundReq.setRefundNo(refundReq.getOutRequestNo());
        aliRefundReq.setOutTradeNo(refundReq.getOutTradeNo());
        aliRefundReq.setAmount(PayUtils.convertMoneyFen2Yuan(refundReq.getRefundAmount()));
        return (T)aliRefundReq;
    }

    /**
     * ?????????????????????
     *
     * @param refundBody ????????????????????????
     * @param <T> ???????????????????????????
     * @return ?????????????????????
     */
    @Override
    @SuppressWarnings("unchecked")
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
            // ????????????
            AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
            alipayRequest.setBizModel(model);
            // ????????????
            res = (AlipayTradeRefundResponse)aliwebResService.getAliInterfaceRes(alipayRequest, res);

            // ????????????
            String refundNo = aliRefundReq.getRefundNo();
            BeanUtils.copyProperties(res, refund);
            // ???????????????????????????
            refund.setRefundDetailItem(JSONUtil.toJSON(res.getRefundDetailItemList()));
            // ????????????
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
     * ????????????????????????????????????????????????
     *
     * @param orderNo ?????????
     * @param refundNo ???????????????
     * @param <T> ?????????????????????????????????
     * @return ???????????????????????????
     */
    @Override
    @SuppressWarnings("unchecked")
    protected <T> T getRefundQueryRes(String orderNo, String refundNo) {
        AlipayTradeFastpayRefundQueryResponse res = new AlipayTradeFastpayRefundQueryResponse();

        try {
            AlipayTradeFastpayRefundQueryRequest alipayRequest = new AlipayTradeFastpayRefundQueryRequest();
            // ????????????
            AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
            model.setOutTradeNo(orderNo);
            model.setOutRequestNo(refundNo);
            alipayRequest.setBizModel(model);
            // ????????????
            res = (AlipayTradeFastpayRefundQueryResponse)aliwebResService.getAliInterfaceRes(alipayRequest, res);
        } catch (Exception e) {
            LOGGER.debug("AliwebPayServiceImpl.getRefundQueryRes[Exception]:{}", e);
            res = null;
        }
        return (T)res;
    }
    //-------------------------???????????????????????????????????????-----------------END------------------

    /**
     * ?????????????????????????????????
     *
     * @param ali ???????????????????????????
     * @return AliwebEntity
     */
    public AliwebEntity save(AliwebEntity ali) {
        return aliwebRepo.save(ali);
    }

    /**
     * ???????????????????????????????????????
     *
     * @param aliRefund ?????????????????????????????????
     * @return AliRefund
     */
    public AliwebRefundEntity save(AliwebRefundEntity aliRefund) {
        return aliwebRefundRepo.save(aliRefund);
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param outTradeNo ?????????????????????
     * @return AliwebEntity
     */
    public AliwebEntity findByOutTradeNo(String outTradeNo) {
        return aliwebRepo.findByOutTradeNo(outTradeNo);
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param refundNo ????????????
     * @return AliRefund
     */
    public AliwebRefundEntity findByRefundNo(String refundNo) {
        return aliwebRefundRepo.findByRefundNo(refundNo);
    }



    /**
     * ?????????????????????????????????Entity
     *
     * @param params
     *            ??????
     * @return alipayinfo ????????????
     * @throws Exception ????????????
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
                // ???????????? 0:??????
                if ("trade_status".equals(paramName) && StringUtil.isNull(params.get("trade_status"))) {
                    aliwebpayinfo.setTradeStatus(payWebAliProperties.getPayWebAliNoticeTradeStatusUnknown());
                    // ???????????????????????????????????????????????????????????????
                } else if ("total_amount".equals(paramName) && StringUtil.isNull(params.get("total_amount"))) {
                    aliwebpayinfo.setTotalAmount("0");
                    // ??????????????????????????????????????????????????????
                } else if ("receipt_amount".equals(paramName) && StringUtil.isNull(params.get("receipt_amount"))) {
                    aliwebpayinfo.setReceiptAmount("0");
                    // ????????????????????????????????????????????????
                } else if ("invoice_amount".equals(paramName) && StringUtil.isNull(params.get("invoice_amount"))) {
                    aliwebpayinfo.setInvoiceAmount("0");
                    // ?????????????????????????????????
                } else if ("buyer_pay_amount".equals(paramName) && StringUtil.isNull(params.get("buyer_pay_amount"))) {
                    aliwebpayinfo.setBuyerPayAmount("0");
                    // ??????????????????????????????
                } else if ("point_amount".equals(paramName) && StringUtil.isNull(params.get("point_amount"))) {
                    aliwebpayinfo.setPointAmount("0");
                    // ???????????????
                } else if ("refund_fee".equals(paramName) && StringUtil.isNull(params.get("refund_fee"))) {
                    aliwebpayinfo.setRefundFee("0");
                    // ????????????
                } else {
                    AliwebEntity.class.getMethod("set" + StringUtil.capitalize(fieldName), String.class).invoke(
                        aliwebpayinfo, value);
                }
            }
        }
        return aliwebpayinfo;
    }

}
