package com.proper.enterprise.platform.pay.wechat.controller;

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
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.pay.wechat.entity.WechatEntity;
import com.proper.enterprise.platform.pay.wechat.model.WechatNoticeRes;
import com.proper.enterprise.platform.pay.wechat.model.WechatOrderReq;
import com.proper.enterprise.platform.pay.wechat.model.WechatPayResultRes;
import com.proper.enterprise.platform.pay.wechat.service.WechatPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.oxm.Unmarshaller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;

/**
 * ????????????Controller
 */
@RestController
@RequestMapping(value = "/pay/wechat")
@Api(tags = "/pay/wechat")
public class WechatController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WechatController.class);
    private static final String STR_SUCCESS = "SUCCESS";

    @Autowired
    Map<String, Unmarshaller> unmarshallerMap;

    @Autowired
    WechatPayService wechatPayService;

    @Autowired
    PayFactory payFactory;

    /**
     * ?????????????????????.
     *
     * @param wechatReq ???????????????????????????.
     * @return ????????????.
     * @throws Exception ??????.
     */
    @PostMapping(value = "/prepay", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation("????????????????????????")
    public ResponseEntity<WechatPayResultRes> prepayWechat(@RequestBody WechatOrderReq wechatReq) throws Exception {
        LOGGER.debug(DateUtil.getTimestamp(true) + "------------- Wechat prepay business--------begin------------");
        WechatPayResultRes resObj = new WechatPayResultRes();
        try {
            // ?????????
            PrepayReq prepayReq = new PrepayReq();
            PayService payService = (PayService) wechatPayService;
            // ?????????????????????
            com.proper.enterprise.platform.pay.wechat.model.WechatOrderReq wechatOrderReq =
                new com.proper.enterprise.platform.pay.wechat.model.WechatOrderReq();
            BeanUtil.copyProperties(wechatReq, wechatOrderReq);
            PayResultRes checkRes = payService.savePrepayBusiness(PayWay.WECHAT.toString(), prepayReq, wechatOrderReq);
            if (checkRes.getResultCode() != null && checkRes.getResultCode().equals(PayResType.SYSERROR)) {
                BeanUtils.copyProperties(checkRes, resObj);
                return responseOfPost(resObj);
            }
            // ?????????
            prepayReq.setOutTradeNo(wechatOrderReq.getOutTradeNo());
            // ????????????
            prepayReq.setTotalFee(String.valueOf(wechatOrderReq.getTotalFee()));
            // ????????????
            prepayReq.setPayIntent(wechatOrderReq.getBody());
            // ????????????
            prepayReq.setPayWay(PayWay.WECHAT.toString());
            // ?????????????????????
            PayResultRes res = payService.savePrepay(prepayReq);
            // ?????????????????????
            if (PayResType.SUCCESS.equals(res.getResultCode())) {
                resObj = (WechatPayResultRes) res;
            } else {
                resObj.setResultCode(res.getResultCode());
                resObj.setResultMsg(res.getResultMsg());
            }
        } catch (Exception e) {
            LOGGER.error("WechatController.prepayWechat[Exception]:{}", e);
            resObj.setResultCode(PayResType.SYSERROR);
            resObj.setResultMsg(PayConstants.APP_SYSTEM_ERR);
        }
        // ????????????
        LOGGER.debug(DateUtil.getTimestamp(true) + "------------- Wechat prepay business--------end------------");
        return responseOfPost(resObj);
    }

    /**
     * ??????????????????????????????
     *
     * @param request ??????????????????
     * @param resp ????????????
     * @throws Exception ????????????
     */
    @AuthcIgnore
    @PostMapping(value = "/noticeWechatPayInfo")
    @ApiOperation("?????????????????????????????????")
    @SuppressWarnings("unchecked")
    public void receiveWeixinNoticeInfo(HttpServletRequest request, HttpServletResponse resp) throws Exception {
        boolean ret = false;

        request.setCharacterEncoding("UTF-8");
        InputStream inStream = request.getInputStream();
        WechatNoticeRes noticeRes = (WechatNoticeRes) unmarshallerMap.get("unmarshallWechatNoticeRes")
            .unmarshal(new StreamSource(inStream));
        inStream.close();

        LOGGER.info("--------- wechat async notice {} : {} : {} -----------", noticeRes.getOutTradeNo(),
            noticeRes.getTransactionId(), noticeRes.getTotalFee());

        // ????????????????????????
        if (wechatPayService.isValid(noticeRes) && STR_SUCCESS.equalsIgnoreCase(noticeRes.getReturnCode())
            && STR_SUCCESS.equalsIgnoreCase(noticeRes.getResultCode())) {
            LOGGER.debug("sign_verify:SUCCESS & result_code:SUCCESS");
            // ????????????????????????flag
            boolean saveNoticeFlag = false;
            try {
                WechatEntity wechatInfo = wechatPayService.getWechatNoticeInfo(noticeRes);
                if (wechatInfo != null) {
                    // ???????????????????????????log
                    PayUtils.logEntity(wechatInfo);
                    // ??????????????????????????????
                    WechatEntity wechat = wechatPayService.findByOutTradeNo(wechatInfo.getOutTradeNo());
                    if (wechat == null) {
                        wechatPayService.save(wechatInfo);
                    }
                    saveNoticeFlag = true;
                }
            } catch (Exception e) {
                LOGGER.debug("Wechat async notice error!", e);
                saveNoticeFlag = false;
            }
            // ??????????????????????????????
            if (saveNoticeFlag) {
                LOGGER.debug("Wechat async notice result has bean saved and start a new thread to deal with business!");
                NoticeService noticeService = payFactory.newNoticeService("wechat");
                noticeService.saveNoticeProcessAsync(noticeRes);
                ret = true;
            }
        }

        PrintWriter out = resp.getWriter();
        String resultMsg = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[ERROR]]></return_msg></xml> ";
        if (ret) {
            resultMsg = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml> ";
        }
        out.write(resultMsg);
        out.flush();
        out.close();
    }

    public static class WechatOrderReq implements OrderReq {
        /**
         * ???????????????
         */
        @ApiModelProperty(name = "??????????????????", required = true)
        private String nonceStr;

        /**
         * ????????????
         */
        @ApiModelProperty(name = "???????????????")
        private String detail;

        /**
         * ????????????
         */
        @ApiModelProperty(name = "???????????????")
        private String attach;

        /**
         * ??????IP ??????
         */
        @ApiModelProperty(name = "?????????IP", required = true)
        private String spbillCreateIp;

        /**
         * ??????????????????
         */
        @ApiModelProperty(name = "?????????????????????")
        private String timeStart;

        /**
         * ??????????????????
         */
        @ApiModelProperty(name = "?????????????????????")
        private String timeExpire;

        /**
         * ????????????
         */
        @ApiModelProperty(name = "???????????????")
        private String goodsTag;

        /**
         * ????????????
         */
        @ApiModelProperty(name = "???????????????", required = true)
        private String notifyUrl;

        /**
         * ??????????????????
         */
        @ApiModelProperty(name = "?????????????????????")
        private String limitPay;

        /**
         * ???????????????????????????
         */
        @ApiModelProperty(name = "??????????????????????????????", required = true)
        private String outTradeNo;

        /**
         * ????????????
         */
        @ApiModelProperty(name = "???????????????", required = true)
        private String body;

        /**
         * ???????????????
         */
        @ApiModelProperty(name = "??????????????????", required = true)
        private int totalFee;

        public String getNonceStr() {
            return nonceStr;
        }

        public void setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getAttach() {
            return attach;
        }

        public void setAttach(String attach) {
            this.attach = attach;
        }

        public String getSpbillCreateIp() {
            return spbillCreateIp;
        }

        public void setSpbillCreateIp(String spbillCreateIp) {
            this.spbillCreateIp = spbillCreateIp;
        }

        public String getTimeStart() {
            return timeStart;
        }

        public void setTimeStart(String timeStart) {
            this.timeStart = timeStart;
        }

        public String getTimeExpire() {
            return timeExpire;
        }

        public void setTimeExpire(String timeExpire) {
            this.timeExpire = timeExpire;
        }

        public String getGoodsTag() {
            return goodsTag;
        }

        public void setGoodsTag(String goodsTag) {
            this.goodsTag = goodsTag;
        }

        public String getNotifyUrl() {
            return notifyUrl;
        }

        public void setNotifyUrl(String notifyUrl) {
            this.notifyUrl = notifyUrl;
        }

        public String getLimitPay() {
            return limitPay;
        }

        public void setLimitPay(String limitPay) {
            this.limitPay = limitPay;
        }

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

        public int getTotalFee() {
            return totalFee;
        }

        public void setTotalFee(int totalFee) {
            this.totalFee = totalFee;
        }
    }

}
