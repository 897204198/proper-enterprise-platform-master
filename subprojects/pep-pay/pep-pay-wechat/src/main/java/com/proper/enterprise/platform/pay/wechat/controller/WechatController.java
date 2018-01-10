package com.proper.enterprise.platform.pay.wechat.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.api.pay.constants.PayConstants;
import com.proper.enterprise.platform.api.pay.enums.PayResType;
import com.proper.enterprise.platform.api.pay.factory.PayFactory;
import com.proper.enterprise.platform.api.pay.model.PayResultRes;
import com.proper.enterprise.platform.api.pay.model.PrepayReq;
import com.proper.enterprise.platform.api.pay.service.NoticeService;
import com.proper.enterprise.platform.api.pay.service.PayService;
import com.proper.enterprise.platform.common.pay.utils.PayUtils;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.ConfCenter;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.pay.wechat.entity.WechatEntity;
import com.proper.enterprise.platform.pay.wechat.model.WechatNoticeRes;
import com.proper.enterprise.platform.pay.wechat.model.WechatOrderReq;
import com.proper.enterprise.platform.pay.wechat.model.WechatPayResultRes;
import com.proper.enterprise.platform.pay.wechat.service.WechatPayService;
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
 * 微信支付Controller
 */
@RestController
@RequestMapping(value = "/pay/wechat")
public class WechatController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WechatController.class);

    @Autowired
    Map<String, Unmarshaller> unmarshallerMap;

    @Autowired
    WechatPayService wechatPayService;

    @Autowired
    PayFactory payFactory;

    /**
     * 微信预支付处理.
     *
     * @param wechatReq 微信预支付请求对象.
     * @return 处理结果.
     * @throws Exception 异常.
     */
    @PostMapping(value = "/prepay", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<WechatPayResultRes> prepayWechat(@RequestBody WechatOrderReq wechatReq) throws Exception {
        LOGGER.debug(DateUtil.getTimestamp(true) + "------------- 微信支付 预支付业务--------开始------------");
        WechatPayResultRes resObj = new WechatPayResultRes();
        try {
            // 预支付
            PrepayReq prepayReq = new PrepayReq();
            PayService payService = (PayService) wechatPayService;
            // 预支付业务处理
            PayResultRes checkRes = payService.savePrepayBusiness(ConfCenter.get("pay.way.wechat"), prepayReq, wechatReq);
            if (checkRes.getResultCode() != null && checkRes.getResultCode().equals(PayResType.SYSERROR)) {
                BeanUtils.copyProperties(checkRes, resObj);
                return responseOfPost(resObj);
            }
            // 订单号
            prepayReq.setOutTradeNo(wechatReq.getOutTradeNo());
            // 订单金额
            prepayReq.setTotalFee(String.valueOf(wechatReq.getTotalFee()));
            // 支付用途
            prepayReq.setPayIntent(wechatReq.getBody());
            // 支付方式
            prepayReq.setPayWay(ConfCenter.get("pay.way.wechat"));
            // 获取预支付信息
            PayResultRes res = payService.savePrepay(prepayReq);
            // 判断预支付结果
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
        // 返回结果
        LOGGER.debug(DateUtil.getTimestamp(true) + "------------- 微信支付 预支付业务--------结束------------");
        return responseOfPost(resObj);
    }

    /**
     * 接收微信异步通知结果
     *
     * @param request 异步通知请求
     * @param resp 异步响应
     * @throws Exception
     */
    @AuthcIgnore
    @PostMapping(value = "/noticeWechatPayInfo")
    public void receiveWeixinNoticeInfo(HttpServletRequest request, HttpServletResponse resp) throws Exception {
        boolean ret = false;

        request.setCharacterEncoding("UTF-8");
        InputStream inStream = request.getInputStream();
        WechatNoticeRes noticeRes = (WechatNoticeRes) unmarshallerMap.get("unmarshallWechatNoticeRes")
            .unmarshal(new StreamSource(inStream));
        inStream.close();

        LOGGER.info("------------- 微信异步通知 {} : {} : {} ---------------", noticeRes.getOutTradeNo(), noticeRes.getTransactionId(), noticeRes.getTotalFee());

        // 进行签名验证操作
        if (wechatPayService.isValid(noticeRes) && "SUCCESS".equalsIgnoreCase(noticeRes.getReturnCode())
            && "SUCCESS".equalsIgnoreCase(noticeRes.getResultCode())) {
            LOGGER.debug("sign_verify:SUCCESS & result_code:SUCCESS");
            // 保存异步通知信息flag
            boolean saveNoticeFlag = false;
            try {
                WechatEntity wechatInfo = wechatPayService.getWechatNoticeInfo(noticeRes);
                if(wechatInfo != null) {
                    // 输出异步通知结果到log
                    PayUtils.logEntity(wechatInfo);
                    // 保存微信异步通知信息
                    WechatEntity wechat = wechatPayService.findByOutTradeNo(wechatInfo.getOutTradeNo());
                    if(wechat == null) {
                        wechatPayService.save(wechatInfo);
                    }
                    saveNoticeFlag = true;
                }
            } catch (Exception e) {
                LOGGER.debug("微信异步通知信息保存异常", e);
                saveNoticeFlag = false;
            }
            // 启用线程处理业务相关
            if(saveNoticeFlag) {
                LOGGER.debug("微信异步通知业务相关Notice,异步通知结果已经保存并新起线程进行业务处理");
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

}
