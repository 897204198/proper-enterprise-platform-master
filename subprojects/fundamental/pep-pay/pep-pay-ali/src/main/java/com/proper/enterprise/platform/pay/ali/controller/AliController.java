package com.proper.enterprise.platform.pay.ali.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.api.pay.PayConstants;
import com.proper.enterprise.platform.api.pay.enums.PayResType;
import com.proper.enterprise.platform.api.pay.enums.PayWay;
import com.proper.enterprise.platform.api.pay.factory.PayFactory;
import com.proper.enterprise.platform.api.pay.model.OrderReq;
import com.proper.enterprise.platform.api.pay.model.PayResultRes;
import com.proper.enterprise.platform.api.pay.model.PrepayReq;
import com.proper.enterprise.platform.api.pay.service.NoticeService;
import com.proper.enterprise.platform.api.pay.service.PayService;
import com.proper.enterprise.platform.common.pay.utils.PayUtils;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.pay.ali.PayAliProperties;
import com.proper.enterprise.platform.pay.ali.entity.AliEntity;
import com.proper.enterprise.platform.pay.ali.model.AliOrderReq;
import com.proper.enterprise.platform.pay.ali.model.AliPayResultRes;
import com.proper.enterprise.platform.pay.ali.service.AliPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝Controller
 */
@RestController
@RequestMapping(value = "/pay/ali")
@Api(tags = "/pay/ali")
public class AliController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliController.class);

    @Autowired
    AliPayService aliService;

    @Autowired
    PayFactory payFactory;

    @Autowired
    PayAliProperties payAliProperties;

    /**
     * 支付宝预支付处理.
     *
     * @param aliReq 支付宝预支付请求对象.
     * @return 处理结果.
     * @throws Exception 异常.
     */
    @PostMapping(value = "/prepay", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation("‍支付宝预支付")
    public ResponseEntity<AliPayResultRes> prepayAli(@RequestBody AliOrderReq aliReq) throws Exception {
        LOGGER.debug("------------- Ali prepay business--------begin------------");
        AliPayResultRes resObj = new AliPayResultRes();
        try {
            // 预支付
            PrepayReq prepayReq = new PrepayReq();
            PayService payService = (PayService) aliService;
            // ALI预支付业务处理
            com.proper.enterprise.platform.pay.ali.model.AliOrderReq aliOrderReq =
                new com.proper.enterprise.platform.pay.ali.model.AliOrderReq();
            BeanUtil.copyProperties(aliReq, aliOrderReq);
            PayResultRes checkRes = payService.savePrepayBusiness(PayWay.ALI.toString(), prepayReq, aliOrderReq);
            if (checkRes.getResultCode() != null && checkRes.getResultCode().equals(PayResType.SYSERROR)) {
                BeanUtils.copyProperties(checkRes, resObj);
                return responseOfPost(resObj);
            }
            // 订单号
            prepayReq.setOutTradeNo(aliOrderReq.getOutTradeNo());
            // 订单金额
            prepayReq.setTotalFee(aliOrderReq.getTotalFee());
            // 支付用途
            prepayReq.setPayIntent(aliOrderReq.getBody());
            // 支付方式
            prepayReq.setPayWay(PayWay.ALI.toString());
            // 获取预支付信息
            PayResultRes res = payService.savePrepay(prepayReq);
            // 判断预支付结果
            if (res.getResultCode().equals(PayResType.SUCCESS)) {
                resObj = (AliPayResultRes) res;
            } else {
                resObj.setResultCode(res.getResultCode());
                resObj.setResultMsg(res.getResultMsg());
            }
        } catch (Exception e) {
            LOGGER.error("AliController.prepayAli[Exception]:{}", e);
            resObj.setResultCode(PayResType.SYSERROR);
            resObj.setResultMsg(PayConstants.APP_SYSTEM_ERR);
        }
        // 返回结果
        LOGGER.debug("------------- Ali prepay business--------end------------");
        return responseOfPost(resObj);
    }

    /**
     * 支付宝支付结果异步通知
     *
     * @param request 请求
     * @return 处理结果
     * @throws Exception 抛出验证通知参数异常
     */
    @AuthcIgnore
    @PostMapping(value = "/noticeAliPayInfo")
    @ApiOperation("‍支付宝支付结果异步通知")
    public ResponseEntity<String> dealAliNoticePay(HttpServletRequest request) throws Exception {
        LOGGER.debug("-----------Ali async notice--------begin-------------");

        // 返回给支付宝服务器的异步通知结果
        boolean ret = false;

        // 获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>(16);
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
            String name = entry.getKey();
            params.put(name, StringUtil.join(entry.getValue(), ","));
        }
        LOGGER.debug("notice_msg:{}", params.toString());
        // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        // 商户订单号
        String outTradeNo = request.getParameter("out_trade_no");
        // 交易状态
        String tradeStatus = request.getParameter("trade_status");
        // 退款状态
        String refundStatus = request.getParameter("refund_status");
        // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        // 验证成功
        if (aliService.verify(params) && StringUtil.isNotNull(outTradeNo)) {
            // 取得交易状态
            if (StringUtil.isNull(refundStatus)
                && tradeStatus.equals(payAliProperties.getNoticeTradeStatusTradeSuccess())) {
                // 保存异步通知信息flag
                boolean saveNoticeFlag = false;
                try {
                    // 保存异步通知结果
                    AliEntity aliInfo = aliService.getAliNoticeInfo(params);
                    if (aliInfo != null) {
                        // 输出异步通知结果到log
                        PayUtils.logEntity(aliInfo);
                        // 保存支付宝异步通知信息
                        AliEntity ali = aliService.findByOutTradeNo(aliInfo.getOutTradeNo());
                        if (ali == null) {
                            aliService.save(aliInfo);
                        }
                    }
                    saveNoticeFlag = true;
                } catch (Exception e) {
                    LOGGER.debug("Ali async notice error!", e);
                    saveNoticeFlag = false;
                }

                // 启用线程处理业务相关
                if (saveNoticeFlag) {
                    LOGGER.debug("Ali async notice result has bean saved and start a new thread to deal with business!");
                    NoticeService noticeService = payFactory.newNoticeService("ali");
                    noticeService.saveNoticeProcessAsync(params);
                    ret = true;
                }
            } else {
                LOGGER.debug("Useless Ali async notice info!Return SUCCESS directly!");
                ret = true;
            }
        }
        LOGGER.debug("-----------Ali async notice--------end-------------");
        return responseOfPost(ret ? "SUCCESS" : "FAIL");
    }

    public static class AliOrderReq implements OrderReq {
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

        /**
         * 商品名称
         */
        @ApiModelProperty(name = "‍商品名称")
        private String subject;

        /**
         * 服务器异步通知页面路径
         */
        @ApiModelProperty(name = "‍‍商品名称")
        private String notifyUrl;

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

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getNotifyUrl() {
            return notifyUrl;
        }

        public void setNotifyUrl(String notifyUrl) {
            this.notifyUrl = notifyUrl;
        }
    }
}
