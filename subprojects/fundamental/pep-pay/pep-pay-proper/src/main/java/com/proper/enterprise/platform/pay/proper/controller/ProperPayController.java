package com.proper.enterprise.platform.pay.proper.controller;

import com.proper.enterprise.platform.api.pay.PayConstants;
import com.proper.enterprise.platform.api.pay.enums.PayResType;
import com.proper.enterprise.platform.api.pay.enums.PayWay;
import com.proper.enterprise.platform.api.pay.factory.PayFactory;
import com.proper.enterprise.platform.api.pay.model.OrderReq;
import com.proper.enterprise.platform.api.pay.model.PayResultRes;
import com.proper.enterprise.platform.api.pay.model.PrepayReq;
import com.proper.enterprise.platform.api.pay.service.NoticeService;
import com.proper.enterprise.platform.api.pay.service.PayService;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.pay.proper.entity.ProperEntity;
import com.proper.enterprise.platform.pay.proper.model.ProperPayResultRes;
import com.proper.enterprise.platform.pay.proper.repository.ProperRepository;
import com.proper.enterprise.platform.pay.proper.service.ProperPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 模拟支付Controller.
 */
@RestController
@RequestMapping(value = "/pay/proper")
@Api(tags = "/pay/proper")
public class ProperPayController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProperPayController.class);

    @Autowired
    private ProperPayService properPayService;

    @Autowired
    private ProperRepository properRepo;

    @Autowired
    PayFactory payFactory;

    /**
     * 模拟支付预支付处理.
     *
     * @param properReq 模拟支付预支付请求对象.
     * @return 处理结果.
     * @throws Exception 异常.
     */
    @PostMapping(value = "/prepay", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation("‍模拟支付预支付处理")
    public ResponseEntity<ProperPayResultRes> prepayProper(@RequestBody ProperOrderReq properReq) throws Exception {
        LOGGER.debug("------------- proper prepay business--------begin------------");
        ProperPayResultRes resObj = new ProperPayResultRes();
        try {
            // 预支付
            PrepayReq prepayReq = new PrepayReq();
            PayService payService = (PayService) properPayService;
            // 预支付业务处理
            PayResultRes checkRes = payService.savePrepayBusiness(PayWay.PROPER.toString(), prepayReq, properReq);
            if (checkRes.getResultCode() != null && checkRes.getResultCode().equals(PayResType.SYSERROR)) {
                BeanUtils.copyProperties(checkRes, resObj);
                return responseOfPost(resObj);
            }
            // 订单号
            prepayReq.setOutTradeNo(properReq.getOutTradeNo());
            // 订单金额
            prepayReq.setTotalFee(properReq.getTotalFee());
            // 支付用途
            prepayReq.setPayIntent(properReq.getBody());
            // 支付方式
            prepayReq.setPayWay(PayWay.PROPER.toString());
            // 获取预支付信息
            PayResultRes res = payService.savePrepay(prepayReq);
            // 判断预支付结果
            if (res.getResultCode().equals(PayResType.SUCCESS)) {
                resObj = (ProperPayResultRes) res;
            } else {
                resObj.setResultCode(res.getResultCode());
                resObj.setResultMsg(res.getResultMsg());
            }
        } catch (Exception e) {
            LOGGER.error("ProperPayController.prepayProper[Exception]:{}", e);
            resObj.setResultCode(PayResType.SYSERROR);
            resObj.setResultMsg(PayConstants.APP_SYSTEM_ERR);
        }
        // 返回结果
        LOGGER.debug("------------- proper prepay business--------end------------");
        return responseOfPost(resObj);
    }

    /**
     * 模拟支付结果查询并进行异步通知处理
     *
     * @param reqMap 请求
     * @return 处理结果
     * @throws Exception 处理异步通知异常
     */
    @PostMapping(value = "/query")
    @ApiOperation("‍模拟支付结果查询并进行异步通知处理")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Map<String, Object>> queryProperPay(@RequestBody ProperPayReq reqMap) throws Exception {
        LOGGER.debug("-----------Ali async notice--------begin------------");

        String orderNo = reqMap.getOrderNo();
        String subject = reqMap.getSubject();
        String totalFee = reqMap.getTotalFee();

        ProperEntity properInfo = new ProperEntity();
        properInfo.setOutTradeNo(orderNo);
        properInfo.setTradeNo(UUID.randomUUID().toString());
        properInfo.setNotifyTime(DateUtil.getTimestamp(false));
        properInfo.setSubject(subject);
        properInfo.setTotalFee(totalFee);
        properRepo.save(properInfo);

        Map<String, String> params = new HashMap<>(3);
        params.put("orderNo", orderNo);
        params.put("tradeNo", properInfo.getTradeNo());
        params.put("notifyTime", properInfo.getNotifyTime());

        NoticeService noticeService = payFactory.newNoticeService("proper");
        noticeService.saveNoticeProcessAsync(params);

        Map<String, Object> res = new HashMap<>(1);
        res.put("resultCode", "SUCCESS");
        LOGGER.debug("-----------Ali async notice--------end------------");
        return responseOfPost(res);
    }

    public static class ProperOrderReq implements OrderReq {
        /**
         * 商户网站唯一订单号
         */
        @ApiModelProperty(name = "‍商户网站唯一订单号", required = true)
        private String outTradeNo;

        /**
         * 商品详情
         */
        @ApiModelProperty(name = "‍商品详情", required = true)
        private String body;

        /**
         * 商品总金额
         */
        @ApiModelProperty(name = "‍商品总金额", required = true)
        private String totalFee;

        public String getOutTradeNo() {
            return outTradeNo;
        }

        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getTotalFee() {
            return totalFee;
        }

        public void setTotalFee(String totalFee) {
            this.totalFee = totalFee;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }

    public static class ProperPayReq {

        @ApiModelProperty(name = "‍商户网站唯一订单号", required = true)
        private String orderNo;

        @ApiModelProperty(name = "‍商品名称", required = true)
        private String subject;

        @ApiModelProperty(name = "‍商品总金额", required = true)
        private String totalFee;

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getTotalFee() {
            return totalFee;
        }

        public void setTotalFee(String totalFee) {
            this.totalFee = totalFee;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }

}
