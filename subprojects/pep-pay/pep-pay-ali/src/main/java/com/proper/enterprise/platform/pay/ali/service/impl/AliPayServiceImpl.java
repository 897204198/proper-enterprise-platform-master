package com.proper.enterprise.platform.pay.ali.service.impl;

import com.proper.enterprise.platform.api.pay.constants.PayConstants;
import com.proper.enterprise.platform.api.pay.enums.PayResType;
import com.proper.enterprise.platform.api.pay.factory.PayFactory;
import com.proper.enterprise.platform.api.pay.model.*;
import com.proper.enterprise.platform.api.pay.service.PayService;
import com.proper.enterprise.platform.common.pay.service.impl.AbstractPayImpl;
import com.proper.enterprise.platform.common.pay.utils.PayUtils;
import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.utils.ConfCenter;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.cipher.RSA;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.pay.ali.constants.AliConstants;
import com.proper.enterprise.platform.pay.ali.entity.AliEntity;
import com.proper.enterprise.platform.pay.ali.entity.AliRefundEntity;
import com.proper.enterprise.platform.pay.ali.model.*;
import com.proper.enterprise.platform.pay.ali.repository.AliRefundRepository;
import com.proper.enterprise.platform.pay.ali.repository.AliRepository;
import com.proper.enterprise.platform.pay.ali.service.AliPayResService;
import com.proper.enterprise.platform.pay.ali.service.AliPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 支付宝综合支付ServiceImpl
 */
@Service("pay_way_ali")
public class AliPayServiceImpl extends AbstractPayImpl implements PayService, AliPayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliPayServiceImpl.class);
    private static final String ALI_REQUEST_CODE = "10000";

    @Autowired
    PayFactory payFactory;

    @Autowired
    AliRepository aliRepo;

    @Autowired
    AliRefundRepository aliRefundRepo;

    @Autowired
    AliPayResService aliPayResService;

    @Autowired
    @Qualifier("aliRSAPay")
    RSA rsaPay;

    @Autowired
    @Qualifier("aliRSARefund")
    RSA rsaRefund;

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
            AliOrderReq aliPrepay = new AliOrderReq();
            aliPrepay.setOutTradeNo(req.getOutTradeNo());
            aliPrepay.setSubject(req.getPayIntent());
            aliPrepay.setBody(req.getPayIntent());
            aliPrepay.setTotalFee(PayUtils.convertMoneyFen2Yuan(req.getTotalFee()));
            // 设置超时时间
            if (StringUtil.isNumeric(req.getOverMinuteTime())) {
                aliPrepay.setItBPay(req.getOverMinuteTime().concat("m"));
            }
            return aliPrepay;
        } catch (Exception e) {
            LOGGER.debug("PayServiceImpl.queryPayInfo[Exception]:{}", e);
            throw e;
        }
    }

    /**
     * 支付宝预支付
     *
     * @param req 请求对象
     * @return 处理结果
     * @throws Exception 保存异常
     */
    @Override
    protected <T extends PayResultRes, R extends OrderReq> T savePrepayImpl(R req)  throws Exception {
        // 返回给请求客户端处理结果
        AliPayResultRes resObj = new AliPayResultRes();
        AliOrderReq uoReq = (AliOrderReq)req;
        try {
            // 异步通知地址
            uoReq.setNotifyUrl(AliConstants.ALI_PAY_NOTICE_URL);
            // 取得订单信息
            String orderInfo = getOrderInfo(uoReq, AliOrderReq.class);
            // 获取秘钥
            String privateKey = AliConstants.ALI_PAY_RSA_PRIVATE_PAY;
            // 对订单信息进行签名
            String sign = rsaPay.sign(orderInfo, privateKey);
            sign = URLEncoder.encode(sign, PEPConstants.DEFAULT_CHARSET.name());
            // 完整的符合支付宝参数规范的订单信息
            final StringBuilder payInfo = new StringBuilder();
            payInfo.append(orderInfo).append("&sign=\"").append(sign).append("\"&sign_type=\"RSA\"");
            // -------------返回给客户端调用支付接口需要的参数-----------------------
            resObj.setResultCode(PayResType.SUCCESS);
            resObj.setResultMsg("SUCCESS");
            resObj.setPayInfo(payInfo.toString());
            resObj.setSign(privateKey);
            LOGGER.debug("payInfo:{}", payInfo);
        } catch (Exception e) {
            LOGGER.debug("AliPayServiceImpl.savePrepayImpl[Exception]:{}", e);
            resObj.setResultCode(PayResType.SYSERROR);
            resObj.setResultMsg(PayConstants.APP_SYSTEM_ERR);
        }
        return (T)resObj;
    }

    /**
     * 支付宝根据订单号查询结果
     *
     * @param outTradeNo 订单号
     * @return 处理结果
     */
    @Override
    protected <T> T getPayQueryRes(String outTradeNo) {
        AliPayTradeQueryRes res = new AliPayTradeQueryRes();
        Map<String, String> bizContentMap = new HashMap<String, String>(1);
        bizContentMap.put("out_trade_no", outTradeNo);
        String method = ConfCenter.get("pay.ali.tradeQueryMethod");
        String responseKey = "alipay_trade_query_response";
        Object result = getAliRequestRes(res,
                                         bizContentMap,
                                         method,
                                         responseKey,
                                         AliConstants.ALI_PAY_RSA_PRIVATE_PAY,
                                         AliConstants.ALI_PAY_SIGN_TYPE_PAY,
                                         rsaPay);
        return (T)result;
    }

    /**
     * 获取支付宝的退款请求对象
     *
     * @param refundReq 退款请求对象
     * @param <T> 转换后的支付宝退款请求对象
     * @return 支付宝退款请求对象
     */
    @Override
    protected <T> T getRefundReq(RefundReq refundReq) {
        AliRefundReq aliRefundReq = new AliRefundReq();
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
        AliRefundReq aliRefundReq = (AliRefundReq) refundBody;
        AliRefundEntity refund = new AliRefundEntity();
        Map<String, String> bizContentMap = new HashMap<String, String>(3);
        // 订单号
        bizContentMap.put("out_trade_no", aliRefundReq.getOutTradeNo());
        // 退款单号
        String refundNo = aliRefundReq.getRefundNo();
        bizContentMap.put("out_request_no", refundNo);
        refund.setRefundNo(refundNo);
        // 退款金额
        bizContentMap.put("refund_amount", aliRefundReq.getAmount());
        String method = ConfCenter.get("pay.ali.tradeRefundMethod");
        String responseKey = "alipay_trade_refund_response";
        AliRefundRes res = new AliRefundRes();
        Object result = getAliRequestRes(res,
                                         bizContentMap,
                                         method,
                                         responseKey,
                                         AliConstants.ALI_PAY_RSA_PRIVATE_REFUND,
                                         AliConstants.ALI_PAY_SIGN_TYPE_REFUND,
                                         rsaRefund);
        if (result != null) {
            res = (AliRefundRes) result;
            BeanUtils.copyProperties(res, refund);
            AliRefundEntity oldRefund =  findByRefundNo(refundNo);
            if (ALI_REQUEST_CODE.equals(refund.getCode()) && oldRefund == null) {
                save(refund);
            }
        } else {
            res = null;
        }
        return (T)res;
    }

    /**
     * 查询支付宝的订单退款信息
     *
     * @param orderNo 订单号
     * @param refundNo 退款订单号
     * @param <T> 查询支付宝退款结果泛型
     * @return 查询支付宝退款结果
     */
    @Override
    protected <T> T getRefundQueryRes(String orderNo, String refundNo) {
        AliRefundTradeQueryRes res = new AliRefundTradeQueryRes();
        Map<String, String> bizContentMap = new HashMap<String, String>(2);
        bizContentMap.put("out_trade_no", orderNo);
        bizContentMap.put("out_request_no", refundNo);
        String method = ConfCenter.get("pay.ali.tradeRefundQueryMethod");
        String responseKey = "alipay_trade_fastpay_refund_query_response";
        Object result = getAliRequestRes(res,
                                         bizContentMap,
                                         method,
                                         responseKey,
                                         AliConstants.ALI_PAY_RSA_PRIVATE_REFUND,
                                         AliConstants.ALI_PAY_SIGN_TYPE_REFUND,
                                         rsaRefund);
        return (T)result;
    }
    //-------------------------重写抽象类中的共通处理函数-----------------END------------------

    /**
     * 保存支付宝支付信息
     *
     * @param ali 支付宝对象
     * @return Ali
     */

    public AliEntity save(AliEntity ali) {
        return aliRepo.save(ali);
    }

    /**
     * 保存支付宝退款信息
     *
     * @param aliRefund 支付宝退款对象
     * @return AliRefund
     */
    public AliRefundEntity save(AliRefundEntity aliRefund) {
        return aliRefundRepo.save(aliRefund);
    }

    /**
     * 通过订单号查询支付宝信息
     *
     * @param outTradeNo 商户内部订单号
     * @return Ali
     */
    public AliEntity findByOutTradeNo(String outTradeNo) {
        return aliRepo.findByOutTradeNo(outTradeNo);
    }

    /**
     * 通过订支付宝订单号查询支付宝信息
     *
     * @param tradeNo 支付宝订单号
     * @return Ali
     */
    public AliEntity getByTradeNo(String tradeNo) {
        return aliRepo.getByTradeNo(tradeNo);
    }

    /**
     * 通过退款单号查询支付宝退款信息
     *
     * @param refundNo 退款单号
     * @return AliRefund
     */
    public AliRefundEntity findByRefundNo(String refundNo) {
        return aliRefundRepo.findByRefundNo(refundNo);
    }

    /**
     * 验证消息是否是支付宝发出的合法消息
     *
     * @param params 通知返回来的参数数组
     * @return 验证结果
     */
    public boolean verify(Map<String, String> params) throws Exception {
        // 判断responsetTxt是否为true，isSign是否为true
        // responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
        // isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
        String responseTxt = "false";
        String notifyId = params.get("notify_id");
        if (notifyId != null) {
            responseTxt = verifyResponse(notifyId);
        }
        LOGGER.debug("Verify Ali notice info!Result:{}", responseTxt);
        String sign = params.get("sign");
        sign = sign != null ? sign : "";
        boolean isSign = getSignVeryfy(params, sign);
        LOGGER.debug("Verify Ali notice signature!Result:{}", isSign);

        return isSign && "true".equals(responseTxt);
    }

    /**
     * 取得支付宝处理后的请求结果
     *
     * @param res 支付宝平台应用对应接口的响应对象
     * @param bizContentMap 非公共请求参数
     * @param method 请求方法名
     * @param responseKey 响应结果关键字
     * @return 处理结果
     */
    protected Object getAliRequestRes(Object res,
                                      Map<String, String> bizContentMap,
                                      String method,
                                      String responseKey,
                                      String privateKey,
                                      String signType,
                                      RSA rsa) {
        String appId = AliConstants.ALI_PAY_APPID;
        String tradeUrl = ConfCenter.get("pay.ali.tradeUrl");
        StringBuilder reqUrl = new StringBuilder();
        StringBuilder paramStr = new StringBuilder();
        try {
            reqUrl = reqUrl.append(tradeUrl);
            paramStr.append("app_id=").append(appId);
            paramStr.append("&biz_content=");
            paramStr.append(JSONUtil.toJSON(bizContentMap));
            paramStr.append("&charset=");
            paramStr.append(PEPConstants.DEFAULT_CHARSET.name());
            paramStr.append("&method=");
            paramStr.append(method);
            paramStr.append("&sign_type=" + signType);
            paramStr.append("&timestamp=").append(DateUtil.toTimestamp(new Date()));
            paramStr.append("&version=1.0");
            String sign = rsa.sign(paramStr.toString(), privateKey);
            sign = URLEncoder.encode(sign, PEPConstants.DEFAULT_CHARSET.name());
            paramStr.append("&sign=").append(sign);
            reqUrl = reqUrl.append("?").append(paramStr);
            ResponseEntity<byte[]> entity = HttpClient.get(reqUrl.toString());
            byte[] content = entity.getBody();
            if (content != null && content.length > 0) {
                String strRead = new String(content, PEPConstants.DEFAULT_CHARSET.name());
                res = aliPayResService.convertMap2AliPayRes(strRead, responseKey, res);
            }
        } catch (Exception e) {
            LOGGER.debug("Error occurred while getting Ali pay notice results.{}", e);
            return null;
        }
        return res;
    }

    /**
     * 创建支付宝支付信息Entity
     *
     * @param params
     *            参数
     * @return alipayinfo 支付信息
     * @throws Exception 参数获取异常
     */
    public AliEntity getAliNoticeInfo(Map<String, String> params) throws Exception {
        Field[] fields = AliEntity.class.getDeclaredFields();
        Set<String> set = new HashSet<>();
        for (Field field : fields) {
            set.add(field.getName());
        }
        String value;
        AliEntity alipayinfo = new AliEntity();
        for (String fieldName : set) {
            if (!StringUtil.startsWith(fieldName, "$")) {
                String paramName = StringUtil.camelToSnake(fieldName);
                value = params.get(paramName);
                // 交易状态 0:未知
                if ("trade_status".equals(paramName) && StringUtil.isNull(params.get("trade_status"))) {
                    alipayinfo.setTradeStatus(AliConstants.ALI_PAY_NOTICE_TARDESTATUS_UNKONWN);
                    // 交易金额
                } else if ("total_fee".equals(paramName) && StringUtil.isNull(params.get("total_fee"))) {
                    alipayinfo.setTotalFee("0");
                    // 购买数量
                } else if ("quantity".equals(paramName) && StringUtil.isNull(params.get("quantity"))) {
                    alipayinfo.setTotalFee("1");
                    // 商品单价
                } else if ("price".equals(paramName) && StringUtil.isNull(params.get("price"))) {
                    alipayinfo.setTotalFee("0");
                    // 退款状态 0:未知
                } else if ("refund_status".equals(paramName) && StringUtil.isNull(params.get("refund_status"))) {
                    alipayinfo.setRefundStatus(AliConstants.ALI_PAY_NOTICE_TARDESTATUS_UNKONWN);
                    // 其他情况
                } else {
                    AliEntity.class.getMethod("set" + StringUtil.capitalize(fieldName), String.class).invoke(alipayinfo,
                        value);
                }
            }
        }
        return alipayinfo;
    }

    /**
     * 创建向支付宝请求时的订单信息
     *
     * @param t 对象
     * @param clz 对象class
     * @param <T> 泛型
     * @return 结果
     * @throws Exception 参数获取异常
     */
    public <T> String getOrderInfo(T t, Class<T> clz) throws Exception {
        Field[] fields = clz.getDeclaredFields();
        Set<String> set = new HashSet<>();
        for (Field field : fields) {
            set.add(field.getName());
        }
        StringBuilder sb = new StringBuilder();
        Object value;
        for (String fieldName : set) {
            if (!StringUtil.startsWith(fieldName, "$")) {
                value = clz.getMethod("get" + StringUtil.capitalize(fieldName)).invoke(t);
                if (value != null) {
                    if ("inputCharset".equals(fieldName)) {
                        sb.append("&_").append(StringUtil.camelToSnake(fieldName)).append("=").append("\"")
                                .append(value).append("\"");
                    } else {
                        sb.append("&").append(StringUtil.camelToSnake(fieldName)).append("=").append("\"").append(value)
                                .append("\"");
                    }
                }
            }
        }
        return sb.deleteCharAt(0).toString();
    }

    /**
     * 获取远程服务器ATN结果,验证返回URL
     *
     * @param notifyId 通知校验ID
     * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
     *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    private String verifyResponse(String notifyId) throws IOException {
        // 获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求
        String partner = AliConstants.ALI_PAY_PARTNER_ID;
        StringBuilder veryfyUrl = new StringBuilder();
        veryfyUrl.append(AliConstants.ALI_PAY_NOTICE_HTTPS_VERIFY_URL).append("partner=").append(partner)
            .append("&notify_id=").append(notifyId);
        return aliPayResService.checkUrl(veryfyUrl.toString());
    }

    /**
     * 根据反馈回来的信息，生成签名结果
     *
     * @param params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
    private boolean getSignVeryfy(Map<String, String> params, String sign) throws Exception {
        // 过滤空值、sign与sign_type参数
        Map<String, String> newParams = paraFilter(params);
        // 获取待签名字符串
        String preSignStr = createLinkString(newParams);
        LOGGER.debug("Ali's notice value to be signed:{}", preSignStr);
        LOGGER.debug("Ali's sign:{}", sign);
        // 获得签名验证结果
        return "RSA".equals(AliConstants.ALI_PAY_SIGN_TYPE_PAY)
                && rsaPay.verifySign(preSignStr, sign, AliConstants.ALI_PAY_RSA_PUBLIC);
    }

    /**
     * 除去数组中的空值和签名参数
     *
     * @param array 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public Map<String, String> paraFilter(Map<String, String> array) {
        Map<String, String> result = new HashMap<>(16);
        if (array == null || array.size() <= 0) {
            return result;
        }
        for (Map.Entry entry : array.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                    || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    private String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        StringBuilder prestr = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            // 拼接时，不包括最后一个&字符
            if (i == keys.size() - 1) {
                prestr.append(key);
                prestr.append("=");
                prestr.append(value);
            } else {
                prestr.append(key);
                prestr.append("=");
                prestr.append(value);
                prestr.append("&");
            }
        }
        return prestr.toString();
    }

    @Override
    protected <T> T getBillProcess(BillReq billReq) throws Exception {
        DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String method = "alipay.data.dataservice.bill.downloadurl.query";
            String responseKey = "alipay_data_dataservice_bill_downloadurl_query_response";

            AliBillRes aliBillRes = new AliBillRes();
            Map<String, String> bizContentMap = new HashMap<>(2);
            if (StringUtil.isEmpty(billReq.getBillType())) {
                bizContentMap.put("bill_type", "trade");
            } else {
                bizContentMap.put("bill_type", billReq.getBillType());
            }
            bizContentMap.put("bill_date", dft.format(billReq.getDate()));

            Object result = getAliRequestRes(aliBillRes,
                                             bizContentMap,
                                             method,
                                             responseKey,
                                             AliConstants.ALI_PAY_RSA_PRIVATE_PAY,
                                             AliConstants.ALI_PAY_SIGN_TYPE_PAY,
                                             rsaPay);
            if (result != null) {
                aliBillRes = (AliBillRes) result;
                if (ALI_REQUEST_CODE.equals(aliBillRes.getCode())) {
                    LOGGER.info("{}Ali pay bill download url：{}", dft.format(billReq.getDate()), ((AliBillRes) result).getBillDownloadUrl());
                    return (T) aliBillRes;
                } else {
                    LOGGER.error("Error occurred while getting Ali pay bill download url!");
                }
            }
            return null;
        } catch (Exception e) {
            LOGGER.error("Failed to download Ali pay bill：{}", dft.format(billReq.getDate()));
            throw e;
        }
    }
}
