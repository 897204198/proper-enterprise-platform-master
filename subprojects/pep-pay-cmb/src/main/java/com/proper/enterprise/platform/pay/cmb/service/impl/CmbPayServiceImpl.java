package com.proper.enterprise.platform.pay.cmb.service.impl;

import cmb.netpayment.Security;
import com.cmb.b2b.B2BResult;
import com.cmb.b2b.Base64;
import com.cmb.b2b.FirmbankCert;
import com.proper.enterprise.platform.api.pay.constants.PayConstants;
import com.proper.enterprise.platform.api.pay.enums.PayResType;
import com.proper.enterprise.platform.api.pay.model.OrderReq;
import com.proper.enterprise.platform.api.pay.model.PayResultRes;
import com.proper.enterprise.platform.api.pay.model.PrepayReq;
import com.proper.enterprise.platform.api.pay.model.RefundReq;
import com.proper.enterprise.platform.api.pay.service.PayService;
import com.proper.enterprise.platform.common.pay.service.impl.AbstractPayImpl;
import com.proper.enterprise.platform.common.pay.utils.PayUtils;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.PinyinUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.pay.cmb.constants.CmbConstants;
import com.proper.enterprise.platform.pay.cmb.document.CmbProtocolDocument;
import com.proper.enterprise.platform.pay.cmb.entity.CmbProtocolEntity;
import com.proper.enterprise.platform.pay.cmb.entity.CmbPayEntity;
import com.proper.enterprise.platform.pay.cmb.entity.CmbRefundEntity;
import com.proper.enterprise.platform.pay.cmb.model.*;
import com.proper.enterprise.platform.pay.cmb.repository.CmbPayNoticeRepository;
import com.proper.enterprise.platform.pay.cmb.repository.CmbProtocolNoticeRepository;
import com.proper.enterprise.platform.pay.cmb.repository.CmbProtocolRepository;
import com.proper.enterprise.platform.pay.cmb.repository.CmbRefundRepository;
import com.proper.enterprise.platform.pay.cmb.service.CmbPayResService;
import com.proper.enterprise.platform.pay.cmb.service.CmbPayService;
import com.proper.enterprise.platform.pay.cmb.utils.CmbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;

/**
 * 一网通支付ServiceImpl
 */
@Service("pay_way_cmb")
public class CmbPayServiceImpl extends AbstractPayImpl implements PayService, CmbPayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmbPayServiceImpl.class);

    @Autowired
    Marshaller marshaller;

    @Autowired
    Map<String, Unmarshaller> unmarshallerMap;

    @Autowired
    CmbProtocolRepository cmbRepo;

    @Autowired
    CmbProtocolNoticeRepository cmbNoticeRepo;

    @Autowired
    CmbPayNoticeRepository cmbPayNoticeRepo;

    @Autowired
    CmbRefundRepository cmbRefundRepo;

    @Autowired
    CmbPayResService cmbPayResService;

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
            CmbOrderReq cmbPrepay = new CmbOrderReq();
            cmbPrepay.setBillNo(req.getOutTradeNo());
            cmbPrepay.setMerchantPara(PinyinUtil.quanpin(req.getPayIntent()));
            cmbPrepay.setAmount(PayUtils.convertMoneyFen2Yuan(req.getTotalFee()));
            cmbPrepay.setPayUserId(req.getUserId());
            // 设置超时时间
            if(StringUtil.isNumeric(req.getOverMinuteTime())) {
                cmbPrepay.setExpireTimeSpan(req.getOverMinuteTime());
            }
            return cmbPrepay;
        } catch (Exception e) {
            LOGGER.debug("CmbPayServiceImpl.reqPrepay[Exception]:{}", e);
            throw e;
        }
    }

    /**
     * 一网通预支付
     *
     * @param req 请求对象
     * @return 处理结果
     * @throws Exception
     */
    @Override
    protected <T extends PayResultRes, R extends OrderReq> T savePrepayImpl(R req)  throws Exception {
        // 返回给请求客户端处理结果
        CmbPayResultRes resObj = new CmbPayResultRes();
        CmbOrderReq uoReq = (CmbOrderReq) req;
        try {
            // 获取用户ID
            String userId = uoReq.getPayUserId();
            // 校验用户ID
            if (StringUtil.isEmpty(userId)) {
                resObj.setResultCode(PayResType.SYSERROR);
                resObj.setResultMsg(CmbConstants.CMB_PAY_ERROR_USERID);
                // 订单号不足18位
            } else if(!checkBillNo(uoReq.getBillNo())) {
                resObj.setResultCode(PayResType.SYSERROR);
                resObj.setResultMsg(CmbConstants.CMB_PAY_ERROR_BILLNO_ERROR);
            } else {
                // 客户协议号
                // 17位的时间戳 + 3位随机数字
                CmbBusinessProReq tmpReq = new CmbBusinessProReq();
                boolean needSign = saveProtocolNeedSign(userId, tmpReq);
                // 交易时间
                String tradeDate = DateUtil.toString(new Date(), CmbConstants.CMB_PAY_DATE_FORMAT_YYYYMMDDHHMMSS);
                // merchantCode
                String requestXML = "";
                // 自定义请求参数
                StringBuilder requestParams = new StringBuilder();
                requestParams.append("pno=").append(tmpReq.getPno()).append("|").append("userid=").append(userId)
                    .append("|").append("intent=").append(uoReq.getMerchantPara());
                // writer
                StringWriter writer = new StringWriter();
                // 签约并支付
                if (needSign) {
                    // 生成协议信息
                    CmbBusinessProReq businessReq = new CmbBusinessProReq();
                    // 交易时间
                    businessReq.setTs(tradeDate);
                    //------以下字段“签约并支付”时必填----------
                    // 用户协议号
                    businessReq.setPno(tmpReq.getPno());
                    // 协议开通请求流水号
                    // 17位的时间戳 + 3为随机数字
                    businessReq.setSeq(CmbUtils.getTimeNo());
                    businessReq.setUrl(CmbConstants.CMB_PAY_PROTOCOL_URL);
                    businessReq.setPara(requestParams.toString());
                    marshaller.marshal(businessReq, new StreamResult(writer));

                    // 已签约并支付
                } else {
                    // 生成协议信息
                    CmbBusinessNoProReq cmbBusinessNoProReq = new CmbBusinessNoProReq();
                    // 交易时间
                    cmbBusinessNoProReq.setTs(tradeDate);
                    // 协议号
                    cmbBusinessNoProReq.setPno(tmpReq.getPno());
                    // 请求参数
                    cmbBusinessNoProReq.setPara(requestParams.toString());
                    marshaller.marshal(cmbBusinessNoProReq, new StreamResult(writer));
                }
                // 替换xml头
                requestXML = writer.toString().replace(CmbConstants.CMB_PAY_XML_HEADER, "");
                // 创建订单信息
                uoReq.setMerchantPara(requestParams.toString());
                // 根据参数生成支付请求对象
                uoReq = createOrderInfo(uoReq, requestXML);
                // 取得订单信息
                String orderInfo = getOrderInfo(uoReq, CmbOrderReq.class);
                // 设置APP端支付请求
                resObj.setPayInfo(CmbConstants.CMB_PAY_PAYMENT_URL.concat(orderInfo).replace("|", "%7C"));
                resObj.setResultCode(PayResType.SUCCESS);
                // 一网通10位订单号
                resObj.setCmbBillNo(uoReq.getBillNo());
                // 一网通订单支付日期
                resObj.setCmbDate(uoReq.getDate());
                // 一网通支付金额
                resObj.setAmout(PayUtils.convertMoneyYuan2Fen(uoReq.getAmount()));
                // 输出请求地址全上下文
                LOGGER.debug("requestURL:{}", resObj.getPayInfo());
            }
        } catch(Exception e) {
            LOGGER.debug("CmbPayServiceImpl.savePrepayImpl[Exception]:{}", e);
            resObj.setResultCode(PayResType.SYSERROR);
            resObj.setResultMsg(PayConstants.APP_SYSTEM_ERR);
        }
        PayUtils.logEntity(resObj);
        return (T) resObj;
    }

    /**
     * 一网通根据订单号查询结果
     *
     * @param outTradeNo 订单号
     * @return 处理结果
     */
    @Override
    protected <T> T getPayQueryRes(String outTradeNo) {
        CmbQuerySingleOrderRes res = null;
        try {
            CmbQuerySingleOrderReq queryReq = new CmbQuerySingleOrderReq();
            CmbQuerySingleOrderHeadReq headReq = new CmbQuerySingleOrderHeadReq();
            CmbQuerySingleOrderBodyReq bodyReq = new CmbQuerySingleOrderBodyReq();
            CmbPayEntity payInfo = getQueryInfo(outTradeNo);
            if (StringUtil.isNotNull(payInfo.getBillNo()) && StringUtil.isNotNull(payInfo.getDate())) {
                // 请求时间,精确到毫秒
                headReq.setTimeStamp(CmbUtils.getCmbReqTime());
                // 订单号
                bodyReq.setBillNo(payInfo.getBillNo());
                // 交易日期
                bodyReq.setDate(payInfo.getDate());
                // head
                queryReq.setHead(headReq);
                // body
                queryReq.setBody(bodyReq);
                // hash
                queryReq.setHash(getCmbHash(queryReq));
                // 生成请求参数
                res = getCmbtRes(queryReq, CmbConstants.CMB_PAY_DIRECT_REQUEST_X, "unmarshallCmbQuerySingleOrderRes");
                LOGGER.debug("getPayQueryRes[ErrMsg]:{}", res.getHead().getErrMsg());
            }
        } catch (Exception e) {
            LOGGER.debug("CmbPayServiceImpl.getPayQueryRes[Exception]:{}", e);
            return null;
        }
        return (T)res;
    }

    /**
     * 获取一网通的退款请求对象
     *
     * @param refundReq 退款请求对象
     * @param <T> 转换后的一网通退款请求对象
     * @return 一网通退款请求对象
     */
    @Override
    protected <T> T getRefundReq(RefundReq refundReq) {
        try {
            CmbPayEntity cmbPayInfo = getQueryInfo(refundReq.getOutTradeNo());
            CmbRefundNoDupBodyReq cmbRefundReq = new CmbRefundNoDupBodyReq();
            cmbRefundReq.setBillNo(cmbPayInfo.getBillNo());
            cmbRefundReq.setDate(cmbPayInfo.getDate());
            cmbRefundReq.setAmount(PayUtils.convertMoneyFen2Yuan(refundReq.getRefundAmount()));
            cmbRefundReq.setRefundNo(refundReq.getOutRequestNo());
            return (T)cmbRefundReq;
        }catch (Exception e) {
            return null;
        }
    }

    /**
     * 一网通退款操作
     *
     * @param refundBody 退款请求对象泛型
     * @param <T> 一网通退款结果泛型
     * @return 一网通退款结果
     */
    @Override
    protected <T> T saveRefundProcess(T refundBody) throws Exception {
        CmbRefundNoDupRes res = null;
        // 请求退款对象
        CmbRefundNoDupBodyReq refundInfo = (CmbRefundNoDupBodyReq) refundBody;
        CmbRefundNoDupReq refundReq = new CmbRefundNoDupReq();
        CmbRefundNoDupHeadReq headReq = new CmbRefundNoDupHeadReq();
        CmbRefundNoDupBodyReq bodyReq = new CmbRefundNoDupBodyReq();
        if (StringUtil.isNotNull(refundInfo.getBillNo()) && StringUtil.isNotNull(refundInfo.getDate())
            && StringUtil.isNotNull(refundInfo.getRefundNo()) && StringUtil.isNotNull(refundInfo.getAmount())) {

            // 退款金额
            bodyReq.setAmount(refundInfo.getAmount());
            // 请求时间,精确到毫秒
            headReq.setTimeStamp(CmbUtils.getCmbReqTime());
            // 订单号
            bodyReq.setBillNo(refundInfo.getBillNo());
            // 交易日期
            bodyReq.setDate(refundInfo.getDate());
            // 退款流水号
            // 退款流水号长度小于等于20 ,组成是英文字符与数字。
            bodyReq.setRefundNo(refundInfo.getRefundNo());
            // 备注
            bodyReq.setDesc(refundInfo.getRefundNo());
            // head
            refundReq.setHead(headReq);
            // body
            refundReq.setBody(bodyReq);

            // hash
            refundReq.setHash(getCmbHash(refundReq));
            // 生成请求参数
            res = getCmbtRes(refundReq, CmbConstants.CMB_PAY_DIRECT_REQUEST_X, "unmarshallCmbRefundNoDupRes");

            // 保存一网通退款信息
            if(StringUtil.isNull(res.getHead().getCode())) {
                CmbRefundEntity refund = new CmbRefundEntity();
                // 退款状态: 0 : 成功
                refund.setRefundCode("0");
                // 退款说明
                refund.setRefundMsg(res.getHead().getErrMsg());
                // 原订单号
                refund.setReqBillNo(bodyReq.getBillNo());
                // 原订单日期YYYYMMDD
                refund.setReqDate(bodyReq.getDate());
                // 退款流水号
                refund.setRefundCode(bodyReq.getRefundNo());
                // 退款金额
                refund.setReqAmount(bodyReq.getAmount());
                // 退款备注
                refund.setReqDesc(bodyReq.getDesc());
                // 银行退款流水号
                refund.setRefundNo(res.getBody().getRefundNo());
                // 银行流水号
                refund.setBankSeqNo(res.getBody().getBankSeqNo());
                // 退款金额
                refund.setAmount(res.getBody().getAmount());
                // 银行交易日期YYYYMMDD
                refund.setDate(res.getBody().getDate());
                // 银行交易时间hhmmss
                refund.setTime(res.getBody().getTime());
                // 保存退款信息
                cmbRefundRepo.save(refund);
            }
            LOGGER.debug("saveRefundProcess[ErrMsg]:{}", res.getHead().getErrMsg());
        }
        return (T)res;
    }

    /**
     * 查询一网通的订单退款信息
     *
     * @param orderNo 订单号
     * @param refundNo 退款订单号
     * @param <T> 查询一网通退款结果泛型
     * @return 查询一网通退款结果
     */
    @Override
    protected <T> T getRefundQueryRes(String orderNo, String refundNo) {
        CmbQueryRefundRes res = null;
        try {
            // 获取原订单号以及交易日期
            CmbPayEntity queryRefundInfo = getQueryInfo(orderNo);
            CmbQueryRefundReq queryReq = new CmbQueryRefundReq();
            CmbQueryRefundHeadReq headReq = new CmbQueryRefundHeadReq();
            CmbQueryRefundBodyReq bodyReq = new CmbQueryRefundBodyReq();
            if (StringUtil.isNotNull(queryRefundInfo.getBillNo()) && StringUtil.isNotNull(queryRefundInfo.getDate())
                && StringUtil.isNotNull(refundNo)) {
                // 请求时间,精确到毫秒
                headReq.setTimeStamp(CmbUtils.getCmbReqTime());
                // 订单号
                bodyReq.setBillNo(queryRefundInfo.getBillNo());
                // 订单日期
                bodyReq.setDate(queryRefundInfo.getDate());
                // 退款流水号
                bodyReq.setRefundNo(refundNo);
                // head
                queryReq.setHead(headReq);
                // body
                queryReq.setBody(bodyReq);
                // hash
                queryReq.setHash(getCmbHash(queryReq));
                // 生成请求参数
                res = getCmbtRes(queryReq, CmbConstants.CMB_PAY_DIRECT_REQUEST_X, "unmarshallCmbQueryRefundRes");
                LOGGER.debug("queryRefundResult[ErrMsg]:" + res.getHead().getErrMsg());
            }
        } catch (Exception e) {
            LOGGER.debug("CmbPayServiceImpl.getRefundQueryRes[Exception]:{}", e);
            return null;
        }
        return (T)res;
    }
    //-------------------------重写抽象类中的共通处理函数--------------------END------------------

    /**
     * 获取一网通Hash签名信息
     *
     * @param object 待签名对象
     * @return hash签名
     * @throws Exception
     */
    private String getCmbHash(Object object) throws Exception{
        StringWriter writer = new StringWriter();
        marshaller.marshal(object, new StreamResult(writer));
        String preXML = CmbUtils.getOriginSign(writer.toString());
        LOGGER.debug("preXML:" + preXML);
        return CmbUtils.encrypt(preXML, "SHA-1");
    }

    /**
     * 获取一网通接口请求结果
     *
     * @param obj 请求对象
     * @param url 请求地址
     * @param beanId 实例Bean
     * @param <T> 泛型
     * @return 请求结果
     * @throws Exception
     */
    private <T> T getCmbtRes(Object obj, String url, String beanId) throws Exception{
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, new StreamResult(writer));
        String requestXML = writer.toString();
        LOGGER.debug("{}_requestXML:{}", beanId, requestXML);
        return (T)cmbPayResService.getCmbApiRes(url, beanId, requestXML);
    }

    /**
     * 校验一网通订单号
     *
     * @param billNo 订单号
     * @return 校验结果
     */
    private boolean checkBillNo(String billNo) {
        try {
            CmbPayEntity cmbPayTempInfo = getQueryInfo(billNo);
            // 订单号不足18位 || 订单号错误:前八位不是数字 || 订单号不是数字 || 订单号的长度不为10
            return billNo.length() >= 18 && StringUtil.isNumeric(cmbPayTempInfo.getDate())
                && StringUtil.isNumeric(cmbPayTempInfo.getBillNo()) && cmbPayTempInfo.getBillNo().length() == 10;
        } catch (Exception e) {
            LOGGER.debug("解析订单号获取支付日期以及订单号异常{}", e);
            return false;
        }
    }

    /**
     * 获取用户协议信息
     *
     * @param userId
     *        用户ID
     * @return 用户协议信息
     * @throws Exception
     */
    @Override
    public CmbProtocolDocument getUserProtocolInfo(String userId) throws Exception {
        return cmbRepo.findByUserId(userId);
    }

    /**
     * 保存用户协议信息
     *
     * @param userProtocolInfo
     *        用户协议信息
     * @throws Exception
     */
    @Override
    public void saveUserProtocolInfo(CmbProtocolDocument userProtocolInfo) throws Exception {
        cmbRepo.save(userProtocolInfo);
    }

    /**
     * 保存支付结果异步通知结果
     *
     * @param payInfo
     *        支付结果异步通知
     * @throws Exception
     */
    @Override
    public void saveCmbPayNoticeInfo(CmbPayEntity payInfo) throws Exception {
        cmbPayNoticeRepo.save(payInfo);
    }

    /**
     * 根据银行通知商户的支付结果消息查询是否已经处理过支付结果异步通知
     *
     * @param msg
     *        支付结果
     * @throws Exception
     */
    @Override
    public CmbPayEntity getPayNoticeInfoByMsg(String msg) throws Exception {
        return cmbPayNoticeRepo.findByMsg(msg);
    }

    /**
     * 生成并保存用户协议号
     *
     * @param userId
     *        用户ID
     * @return 用户协议号
     */
    private String savePNo(String userId, CmbProtocolDocument protocolInfo) {
        String pNo = CmbUtils.getTimeNo();
        if(protocolInfo != null) {
            protocolInfo.setProtocolNo(pNo);
            protocolInfo.setSign(CmbConstants.CMB_PAY_PROTOCOL_UNSIGNED);
            cmbRepo.save(protocolInfo);
        } else {
            CmbProtocolDocument newProtocolInfo = new CmbProtocolDocument();
            newProtocolInfo.setUserId(userId);
            newProtocolInfo.setProtocolNo(pNo);
            newProtocolInfo.setSign(CmbConstants.CMB_PAY_PROTOCOL_UNSIGNED);
            cmbRepo.save(newProtocolInfo);
        }
        return pNo;
    }

    /**
     * 根据订单号获取一网通订单号及日期
     *
     * @param orderNo 订单号
     * @return 一网通对象
     * @throws Exception
     */
    @Override
    public CmbPayEntity getQueryInfo(String orderNo) throws Exception {
        CmbPayEntity payInfo = new CmbPayEntity();
        // 订单日期
        String date = orderNo.substring(0, 8);
        payInfo.setDate(date);
        // 订单号
        String billNo = orderNo.substring(8, 18);
        payInfo.setBillNo(billNo);
        return payInfo;
    }

    /**
     * 是否需要签约并支付
     *
     * @param userId 用户ID
     * @param businessReq 协议对象
     * @return true 需要签约并支付 | false 不需要签约,可以直接支付
     * @throws Exception
     */
    private boolean saveProtocolNeedSign(String userId, CmbBusinessProReq businessReq) throws Exception {
        boolean retValue = false;
        String pNo = "";
        CmbProtocolDocument protocolInfo = cmbRepo.findByUserId(userId);
        if(protocolInfo != null) {
            if(StringUtil.isNotNull(protocolInfo.getProtocolNo())
                && protocolInfo.getSign().equals(CmbConstants.CMB_PAY_PROTOCOL_SIGNED)) {
                pNo = protocolInfo.getProtocolNo();
            } else {
                pNo = savePNo(userId, protocolInfo);
                retValue = true;
            }
        } else {
            pNo = savePNo(userId, null);
            retValue = true;
        }
        businessReq.setPno(pNo);
        return retValue;
    }

    /**
     * 处理签约协议异步通知
     *
     * @param reqData 请求数据
     * @return 处理结果
     * @throws Exception
     */
    @Override
    public boolean saveNoticeProtocol(String reqData) throws Exception {
        boolean ret = false;
        //接收方需要兼容可能早期版本发送的未作URLEncoder.encode的方式
        reqData = reqData.replace(' ', '+');
        LOGGER.debug("cmb_reqData:{}", reqData);
        Map<String, String> reqObject = JSONUtil.parse(reqData, Map.class);
        // 初始化公钥,验证签名
        B2BResult bRet = FirmbankCert.initPublicKey(CmbConstants.CMB_PAY_PUBLICKEY);
        if (!bRet.isError()) {
            LOGGER.debug("验签成功!");
            // 业务数据包：报文数据必须经过base64编码。
            String busdat = reqObject.get("BUSDAT");
            byte[] bt = Base64.decode(busdat);
            CmbBusinessRes res = (CmbBusinessRes) unmarshallerMap.get("unmarshallCmbBusinessRes")
                .unmarshal(new StreamSource(new ByteArrayInputStream(bt)));
            // 取得用户请求的参数
            Map<String, String> paramObj = CmbUtils.getParamObj(res.getNoticepara());
            // 获取用户ID以及协议号
            String userId = paramObj.get("userid");
            String pno = paramObj.get("pno");
            // 获取用户协议信息
            CmbProtocolDocument protocolInfo = getUserProtocolInfo(userId);
            // 用户协议信息不为空
            if (protocolInfo != null) {
                // 用户没有签约过或者签约失败
                if (protocolInfo.getSign().equals(CmbConstants.CMB_PAY_PROTOCOL_UNSIGNED)) {
                    // 一网通异步通知为签约成功
                    if (CmbConstants.CMB_PAY_PROTOCOL_SUCCESS.equals(res.getRespcod())
                        && pno.equals(res.getCustArgno())) {
                        // 更新协议信息
                        // 协议号
                        protocolInfo.setProtocolNo(res.getCustArgno());
                        // 协议状态
                        protocolInfo.setSign(CmbConstants.CMB_PAY_PROTOCOL_SIGNED);
                        saveUserProtocolInfo(protocolInfo);
                        res.setRespmsg("protocol_sign_success");
                        // 保存异步通知信息
                        saveBusinessInfo(userId, reqObject, res);
                        ret = true;
                    }
                }
            }
        }
        return ret;
    }

    /**
     * 一网通支付结果异步通知验签
     *
     * @param notice 异步通知的字符串
     * @return 验签是否成功
     */
    @Override
    public boolean isValid(String notice) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(CmbConstants.CMB_PAY_KEYPATH);
        try {
            // 构造方法
            Security cmbSecurity = new Security(CmbUtils.readStream(inputStream));
            return cmbSecurity.checkInfoFromBank(notice);
        } catch (Exception ioe) {
            LOGGER.error("Check validation of CMB notice content ERROR!", ioe);
            return false;
        }
    }

    /**
     * 保存签约异步通知结果
     *
     * @param userId
     *        用户id
     * @param reqObject
     *        通知数据
     * @param businessInfo
     *        业务数据
     * @throws Exception
     */
    protected void saveBusinessInfo(String userId, Map<String, String> reqObject, CmbBusinessRes businessInfo) throws Exception {
        CmbProtocolEntity cmbBusinessInfo = new CmbProtocolEntity();
        BeanUtils.copyProperties(businessInfo, cmbBusinessInfo);
        // 用户id
        cmbBusinessInfo.setUserId(userId);
        // 企业网银编号
        cmbBusinessInfo.setNtbnbr(reqObject.get("NTBNBR"));
        // 功能交易码
        cmbBusinessInfo.setTrscod(reqObject.get("TRSCOD"));
        // 字段BUSDAT长度
        cmbBusinessInfo.setDatlen(reqObject.get("DATLEN"));
        // 通讯报文ID
        cmbBusinessInfo.setCommid(reqObject.get("COMMID"));
        // 业务数据包
        cmbBusinessInfo.setBusdat(reqObject.get("BUSDAT"));
        // 签名时间
        cmbBusinessInfo.setSigtim(reqObject.get("SIGTIM"));
        // 签名数据的BASE64编码
        cmbBusinessInfo.setSigdat(reqObject.get("SIGDAT"));
        // 保存签约异步通知结果
        cmbNoticeRepo.save(cmbBusinessInfo);
    }

    /**
     * 取得支付结果异步通知对象
     *
     * @param request 请求
     * @return 支付结果异步通知对象
     * @throws Exception
     */
    @Override
    public CmbPayEntity getCmbPayNoticeInfo(HttpServletRequest request) throws Exception {
        CmbPayEntity payInfo = new CmbPayEntity();
        // 支付结果。取值Y(成功)：系统只通知成功交易。
        payInfo.setSucceed(request.getParameter("Succeed"));
        // 商户号，6位长数字，由银行在商户开户时确定
        payInfo.setCoNo(request.getParameter("CoNo"));
        // 定单号(由支付命令送来)
        payInfo.setBillNo(request.getParameter("BillNo"));
        // 订单金额(由支付命令送来)
        payInfo.setAmount(request.getParameter("Amount"));
        // 交易日期(由支付命令送来)
        payInfo.setDate(request.getParameter("Date"));
        // 商户自定义参数(支付接口中MerchantPara送来)
        String parameter = request.getParameter("MerchantPara");
        payInfo.setMerchantPara(parameter);
        // 银行通知商户的支付结果消息；
        payInfo.setMsg(request.getParameter("Msg"));
        // 当前订单是否有优惠，Y:有优惠。
        payInfo.setDiscountFlag(request.getParameter("DiscountFlag"));
        // 优惠金额，格式：xxxx.xx
        payInfo.setDiscountAmt(request.getParameter("DiscountAmt"));
        // 银行用自己的Private Key对通知命令的签名
        payInfo.setSignature(request.getParameter("Signature"));
        // 设定时间 由异步通知的时间为准
        payInfo.setTime(DateUtil.toString(new Date(), "HH:mm:ss"));
        // --------------设定系统内参数-------------------------
        Map<String, String> paramObj = CmbUtils.getParamObj(parameter);
        // 获取用户ID
        payInfo.setUserId(paramObj.get("userid"));
        // 支付意图
        payInfo.setIntent(paramObj.get("intent"));
        // 返回对象
        return payInfo;
    }

    /**
     * 一网通查询单笔交易信息
     *
     * @param orderNo 订单号
     * @return 查询结果
     * @throws Exception
     */
    @Override
    public CmbPayResultRes querySingleOrder(String orderNo) throws Exception {
        CmbPayResultRes resObj = new CmbPayResultRes();
        CmbQuerySingleOrderRes res = getPayQueryRes(orderNo);
        if(StringUtil.isNull(res.getHead().getCode())) {
            String orderStatus = res.getBody().getStatus();
            // 订单状态
            // 0－已结帐，1－已撤销，2－部分结帐，4－未结帐，7-冻结交易-已经冻结金额已经全部结账 8-冻结交易，冻结金额只结帐了一部分
            // 订单状态
            resObj.setResultCode(orderStatus.equals("0") ? PayResType.SUCCESS : PayResType.SYSERROR);
            // 订单金额
            resObj.setAmout(res.getBody().getAmount());
        } else {
            resObj.setResultCode(PayResType.SYSERROR);
            resObj.setResultMsg(res.getHead().getErrMsg());
        }
        return resObj;
    }

    /**
     * 创建订单信息
     *
     * @param uoReq 一网通订单请求对象
     * @param strXml xml参数
     * @return 创建订单后的一网通对象
     * @throws Exception
     */
    private CmbOrderReq createOrderInfo(CmbOrderReq uoReq, String strXml) throws Exception {

        // 生成订单号
        // 10位长数字(通过截取订单号的 时分秒 + 毫秒 + 1位随机数)
        CmbPayEntity cmbPayInfo = getQueryInfo(uoReq.getBillNo());
        uoReq.setBillNo(cmbPayInfo.getBillNo());
        // 日期
        uoReq.setDate(DateUtil.toString(new Date(), CmbConstants.CMB_PAY_DATE_FORMAT_YYYYMMDD));
        // 异步通知地址
        uoReq.setMerchantUrl(URLEncoder.encode(CmbConstants.CMB_PAY_NOTICE_URL, "UTF-8"));
        // 商户密钥 测试环境为空， 生产环境见《一网通支付商户服务指南.doc》
        String sKey = CmbConstants.CMB_PAY_CMBKEY;
        // 生成请求校验码
        String strMerchantCode = cmb.MerchantCode.genMerchantCode(sKey, uoReq.getDate(), uoReq.getBranchID(),
            uoReq.getCono(), uoReq.getBillNo(), uoReq.getAmount(), uoReq.getMerchantPara(),
            CmbConstants.CMB_PAY_NOTICE_URL, "", "", "", "", strXml);
        uoReq.setMerchantCode(strMerchantCode);

        return uoReq;
    }

    /**
     * 取得订单信息
     *
     * @param t 对象
     * @param clz 对象类
     * @param <T> 对象泛型
     * @return 返回对象
     * @throws Exception
     */
    private <T> String getOrderInfo(T t, Class<T> clz) throws Exception {
        Field[] fields = clz.getDeclaredFields();
        Set<String> set = new HashSet<>();
        for (Field field : fields) {
            set.add(field.getName());
        }
        StringBuilder sb = new StringBuilder();
        Object value;
        for (String fieldName : set) {
            if (!StringUtil.startsWith(fieldName, "$") && !fieldName.equals("payUserId")) {
                value = clz.getMethod("get" + StringUtil.capitalize(fieldName)).invoke(t);
                if (value != null) {
                    sb.append("&").append(StringUtil.upperCase(fieldName).substring(0, 1))
                            .append(fieldName.substring(1, fieldName.length())).append("=").append(value);
                }
            }
        }
        return sb.deleteCharAt(0).toString();
    }

    /**
     * 通过订单号获取支付结果异步通知信息
     *
     * @param orderNo 订单号
     * @return 支付结果异步通知信息
     * @throws Exception
     */
    @Override
    public CmbPayEntity findByOutTradeNo(String orderNo) throws Exception {
        CmbPayEntity retCmbPayInfo = null;
        if(StringUtil.isNotEmpty(orderNo)) {
            CmbPayEntity cmbPayInfo = getQueryInfo(orderNo);
            retCmbPayInfo = cmbPayNoticeRepo.findByBillNoAndDate(cmbPayInfo.getBillNo(), cmbPayInfo.getDate());
        }
        return retCmbPayInfo;
    }

    /**
     * 通过退款单号查询一网通退款信息
     *
     * @param refundNo 退款单号
     * @return WechatRefundInfo
     */
    @Override
    public CmbRefundEntity findByRefundNo(String refundNo) {
        return cmbRefundRepo.findByRefundNo(refundNo);
    }
}
