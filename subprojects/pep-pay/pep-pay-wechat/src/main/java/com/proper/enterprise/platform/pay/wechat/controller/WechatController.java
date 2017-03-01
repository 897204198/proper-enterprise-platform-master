package com.proper.enterprise.platform.pay.wechat.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.api.pay.factory.PayFactory;
import com.proper.enterprise.platform.api.pay.service.NoticeService;
import com.proper.enterprise.platform.common.pay.task.PayNotice2BusinessTask;
import com.proper.enterprise.platform.common.pay.utils.PayUtils;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.pay.wechat.entity.WechatEntity;
import com.proper.enterprise.platform.pay.wechat.model.WechatNoticeRes;
import com.proper.enterprise.platform.pay.wechat.service.WechatPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Autowired
    PayNotice2BusinessTask payNoticeTask;

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
        LOGGER.debug("-------------微信异步通知---------------");
        boolean ret = false;

        request.setCharacterEncoding("UTF-8");
        InputStream inStream = request.getInputStream();
        WechatNoticeRes noticeRes = (WechatNoticeRes) unmarshallerMap.get("unmarshallWechatNoticeRes")
            .unmarshal(new StreamSource(inStream));
        inStream.close();

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
                payNoticeTask.run(noticeRes, noticeService);
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
