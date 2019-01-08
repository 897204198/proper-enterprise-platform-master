package com.proper.enterprise.platform.pay.web.ali.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.api.pay.factory.PayFactory;
import com.proper.enterprise.platform.api.pay.service.NoticeService;
import com.proper.enterprise.platform.common.pay.utils.PayUtils;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.pay.web.ali.PayWebAliProperties;
import com.proper.enterprise.platform.pay.web.ali.entity.AliwebEntity;
import com.proper.enterprise.platform.pay.web.ali.service.AliwebPayResService;
import com.proper.enterprise.platform.pay.web.ali.service.AliwebPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝web支付Controller.
 */
@RestController
@Api(tags = "/pay/aliweb")
@RequestMapping(value = "/pay/aliweb")
public class AliwebController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliwebController.class);

    @Autowired
    AliwebPayService aliwebService;

    @Autowired
    AliwebPayResService aliwebResService;

    @Autowired
    PayFactory payFactory;

    @Autowired
    PayWebAliProperties payWebAliProperties;

    /**
     * 支付宝网页支付结果异步通知
     *
     * @param request 请求
     * @return 处理结果
     * @throws Exception 处理异常
     */
    @AuthcIgnore
    @ApiOperation("‍支付宝网页支付结果异步通知")
    @PostMapping(value = "/noticeAliwebPayInfo")
    @SuppressWarnings("unchecked")
    public ResponseEntity<String> dealAliwebNoticePay(HttpServletRequest request) throws Exception {
        LOGGER.debug("-----------Ali web async notice--------begin-------------");

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
        // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)
        if (aliwebResService.noticeVerify(params) && StringUtil.isNotNull(outTradeNo)) {
            // 验证成功,取得交易状态
            if (tradeStatus.equals(payWebAliProperties.getPayWebAliNoticeTradeStatusTradeSuccess())) {
                // 保存异步通知信息flag
                boolean saveNoticeFlag = false;
                try {
                    // 保存异步通知结果
                    AliwebEntity aliwebInfo = aliwebService.getAliwebNoticeInfo(params);
                    if (aliwebInfo != null) {
                        String signStr = requestParams.get("sign")[0];
                        String signTypeStr = requestParams.get("sign_type")[0];
                        if (StringUtil.isEmpty(signStr)) {
                            aliwebInfo.setSign("unknow_sign");
                        } else {
                            aliwebInfo.setSign(signStr);
                        }
                        if (StringUtil.isEmpty(signTypeStr)) {
                            aliwebInfo.setSign("unknow_sign_type");
                        } else {
                            aliwebInfo.setSignType(signTypeStr);
                        }
                        // 输出异步通知结果到log
                        PayUtils.logEntity(aliwebInfo);
                        // 保存支付宝异步通知信息
                        AliwebEntity aliweb = aliwebService.findByOutTradeNo(aliwebInfo.getOutTradeNo());
                        if (aliweb == null) {
                            aliwebService.save(aliwebInfo);
                        }
                    }
                    saveNoticeFlag = true;
                } catch (Exception e) {
                    LOGGER.debug("Ali async notice error!", e);
                    saveNoticeFlag = false;
                }

                // 启用线程处理业务相关
                if (saveNoticeFlag) {
                    LOGGER.debug("Ali web async notice result has bean saved and start a new thread to deal with business!");
                    NoticeService noticeService = payFactory.newNoticeService("aliweb");
                    noticeService.saveNoticeProcessAsync(params);
                    ret = true;
                }
            } else {
                LOGGER.debug("Useless Ali web async notice info!Return SUCCESS directly!");
                ret = true;
            }
        }
        LOGGER.debug("-----------Ali web async notice--------end-------------");
        return responseOfPost(ret ? "SUCCESS" : "FAIL");
    }

}
