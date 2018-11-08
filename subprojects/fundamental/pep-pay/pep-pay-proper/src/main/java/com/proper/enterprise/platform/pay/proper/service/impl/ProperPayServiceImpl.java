package com.proper.enterprise.platform.pay.proper.service.impl;

import com.proper.enterprise.platform.api.pay.PayApiErrorProperties;
import com.proper.enterprise.platform.api.pay.enums.PayResType;
import com.proper.enterprise.platform.api.pay.factory.PayFactory;
import com.proper.enterprise.platform.api.pay.model.*;
import com.proper.enterprise.platform.api.pay.service.PayService;
import com.proper.enterprise.platform.common.pay.service.impl.AbstractPayImpl;
import com.proper.enterprise.platform.common.pay.utils.PayUtils;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.pay.proper.entity.ProperEntity;
import com.proper.enterprise.platform.pay.proper.entity.ProperRefundEntity;
import com.proper.enterprise.platform.pay.proper.model.*;
import com.proper.enterprise.platform.pay.proper.repository.ProperRefundRepository;
import com.proper.enterprise.platform.pay.proper.repository.ProperRepository;
import com.proper.enterprise.platform.pay.proper.service.ProperPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * 模拟支付综合支付ServiceImpl
 */
@Profile({"dev", "test"})
@Service("pay_way_proper")
public class ProperPayServiceImpl extends AbstractPayImpl implements PayService, ProperPayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProperPayServiceImpl.class);

    @Autowired
    PayFactory payFactory;

    @Autowired
    ProperRepository properRepo;

    @Autowired
    ProperRefundRepository properRefundRepo;

    @Autowired
    private PayApiErrorProperties payApiErrorProperties;

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
            ProperOrderReq properPrepay = new ProperOrderReq();
            properPrepay.setOutTradeNo(req.getOutTradeNo());
            properPrepay.setSubject(req.getPayIntent());
            properPrepay.setTotalFee(PayUtils.convertMoneyFen2Yuan(req.getTotalFee()));
            return properPrepay;
        } catch (Exception e) {
            LOGGER.debug("ProperPayServiceImpl.reqPrepay[Exception]:{}", e);
            throw e;
        }
    }

    @Override
    protected <T extends PayResultRes, R extends OrderReq> T savePrepayImpl(R req) {
        // 返回给请求客户端处理结果
        ProperPayResultRes resObj = new ProperPayResultRes();
        ProperOrderReq uoReq = (ProperOrderReq)req;
        try {
            // -------------返回给客户端调用支付接口需要的参数-----------------------
            resObj.setResultCode(PayResType.SUCCESS);
            resObj.setResultMsg("SUCCESS");
            StringBuilder sb = new StringBuilder();
            sb.append(JSONUtil.toJSONIgnoreException(uoReq));
            resObj.setOrderNo(uoReq.getOutTradeNo());
            resObj.setPayInfo(sb.toString());
            LOGGER.debug("payInfo:{}", sb.toString());
        } catch (Exception e) {
            LOGGER.debug("ProperPayServiceImpl.savePrepayImpl[Exception]:{}", e);
            resObj.setResultCode(PayResType.SYSERROR);
            resObj.setResultMsg(payApiErrorProperties.getSystem());
        }
        return (T)resObj;
    }

    /**
     * 模拟支付根据订单号查询结果
     *
     * @param outTradeNo 订单号
     * @return 处理结果
     */
    @Override
    protected <T> T getPayQueryRes(String outTradeNo) {
        ProperQueryRes res = new ProperQueryRes();
        res.setCode("10000");
        res.setMsg("SUCCESS");
        return (T)res;
    }

    /**
     * 获取模拟支付的退款请求对象
     *
     * @param refundReq 退款请求对象
     * @param <T> 转换后的模拟支付退款请求对象
     * @return 模拟支付退款请求对象
     */
    @Override
    protected <T> T getRefundReq(RefundReq refundReq) {
        ProperRefundReq aliRefundReq = new ProperRefundReq();
        aliRefundReq.setRefundNo(refundReq.getOutRequestNo());
        aliRefundReq.setOutTradeNo(refundReq.getOutTradeNo());
        aliRefundReq.setAmount(PayUtils.convertMoneyFen2Yuan(refundReq.getRefundAmount()));
        return (T)aliRefundReq;
    }

    /**
     * 模拟支付退款操作
     *
     * @param refundBody 退款请求对象泛型
     * @param <T> 模拟支付退款结果泛型
     * @return 模拟支付退款结果
     */
    @Override
    protected <T> T saveRefundProcess(T refundBody) throws Exception {
        ProperRefundRes res = new ProperRefundRes();
        ProperRefundEntity refund = new ProperRefundEntity();
        res.setCode("10000");
        res.setMsg("SUCCESS");
        refund.setCode("10000");
        refund.setMsg("SUCCESS");
        ProperRefundReq properRefundReq = (ProperRefundReq) refundBody;
        refund.setOutTradeNo(properRefundReq.getOutTradeNo());
        refund.setRefundFee(properRefundReq.getAmount());
        refund.setRefundNo(properRefundReq.getRefundNo());
        refund.setRefundTime(DateUtil.getTimestamp(false));
        ProperEntity properInfo = properRepo.findByOutTradeNo(properRefundReq.getOutTradeNo());
        if (properInfo != null) {
            refund.setTradeNo(properRepo.findByOutTradeNo(properRefundReq.getOutTradeNo()).getTradeNo());
        }
        properRefundRepo.save(refund);
        return (T)res;
    }

    /**
     * 查询模拟支付的订单退款信息
     *
     * @param orderNo 订单号
     * @param refundNo 退款订单号
     * @param <T> 查询模拟支付退款结果泛型
     * @return 查询模拟支付退款结果
     */
    @Override
    protected <T> T getRefundQueryRes(String orderNo, String refundNo) {
        ProperRefundTradeQueryRes res = new ProperRefundTradeQueryRes();
        res.setCode("10000");
        res.setMsg("SUCCESS");
        return (T)res;
    }

    @Override
    protected <T> T getBillProcess(BillReq billReq) throws Exception {
        ProperBillRes properBillRes = new ProperBillRes();
        properBillRes.setCode("10000");
        properBillRes.setMsg("SUCCESS");
        properBillRes.setDownLoadUrl("http://www.xxx.com/proper/bills");
        return (T)properBillRes;
    }
    //-------------------------重写抽象类中的共通处理函数-----------------END------------------

    /**
     * 保存模拟支付支付信息
     *
     * @param proper 模拟支付对象
     * @return Proper
     */
    public ProperEntity save(ProperEntity proper) {
        return properRepo.save(proper);
    }

    /**
     * 保存模拟支付退款信息
     *
     * @param properRefund 模拟支付退款对象
     * @return ProperRefund
     */
    public ProperRefundEntity save(ProperRefundEntity properRefund) {
        return properRefundRepo.save(properRefund);
    }

    /**
     * 通过订单号查询模拟支付信息
     *
     * @param outTradeNo 商户内部订单号
     * @return Proper
     */
    public ProperEntity findByOutTradeNo(String outTradeNo) {
        return properRepo.findByOutTradeNo(outTradeNo);
    }

    /**
     * 通过模拟支付订单号查询模拟支付信息
     *
     * @param tradeNo 模拟支付订单号
     * @return Proper
     */
    public ProperEntity getByTradeNo(String tradeNo) {
        return properRepo.getByTradeNo(tradeNo);
    }

    /**
     * 通过退款单号查询模拟支付退款信息
     *
     * @param refundNo 退款单号
     * @return ProperRefund
     */
    public ProperRefundEntity findByRefundNo(String refundNo) {
        return properRefundRepo.findByRefundNo(refundNo);
    }
}
