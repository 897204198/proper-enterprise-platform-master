package com.proper.enterprise.platform.pay.proper.controller;

import com.proper.enterprise.platform.api.pay.constants.PayConstants;
import com.proper.enterprise.platform.api.pay.enums.PayResType;
import com.proper.enterprise.platform.api.pay.factory.PayFactory;
import com.proper.enterprise.platform.api.pay.model.PayResultRes;
import com.proper.enterprise.platform.api.pay.model.PrepayReq;
import com.proper.enterprise.platform.api.pay.service.NoticeService;
import com.proper.enterprise.platform.api.pay.service.PayService;
import com.proper.enterprise.platform.common.pay.task.PayNotice2BusinessTask;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.ConfCenter;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.pay.proper.entity.ProperEntity;
import com.proper.enterprise.platform.pay.proper.model.ProperOrderReq;
import com.proper.enterprise.platform.pay.proper.model.ProperPayResultRes;
import com.proper.enterprise.platform.pay.proper.repository.ProperRepository;
import com.proper.enterprise.platform.pay.proper.service.ProperPayService;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 模拟支付Controller.
 */
@RestController
@RequestMapping(value = "/pay/proper")
public class ProperPayController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProperPayController.class);

    @Autowired
    private ProperPayService properPayService;

    @Autowired
    private ProperRepository properRepo;

    @Autowired
    PayFactory payFactory;

    @Autowired
    PayNotice2BusinessTask payNoticeTask;

    /**
     * 模拟支付预支付处理.
     *
     * @param properReq 模拟支付预支付请求对象.
     * @return 处理结果.
     * @throws Exception 异常.
     */
    @PostMapping(value = "/prepay", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ProperPayResultRes> prepayProper(@RequestBody ProperOrderReq properReq) throws Exception {
        LOGGER.debug("------------- 模拟支付 预支付业务--------开始------------");
        ProperPayResultRes resObj = new ProperPayResultRes();
        try {
            // 预支付
            PrepayReq prepayReq = new PrepayReq();
            PayService payService = (PayService) properPayService;
            // 预支付业务处理
            PayResultRes checkRes = payService.savePrepayBusiness(ConfCenter.get("pay.way.proper"), prepayReq, properReq);
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
            prepayReq.setPayWay(ConfCenter.get("pay.way.proper"));
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
        LOGGER.debug("------------- 模拟支付 预支付业务--------结束------------");
        return responseOfPost(resObj);
    }

    /**
     * 模拟支付结果查询并进行异步通知处理
     *
     * @param reqMap 请求
     * @return 处理结果
     * @throws Exception
     */
    @PostMapping(value = "/query")
    public ResponseEntity<Map<String, Object>> queryProperPay(@RequestBody Map<String, Object> reqMap) throws Exception {
        LOGGER.debug("-----------模拟支付结果查询并进行异步通知处理--------开始------------");

        String orderNo = (String)reqMap.get("orderNo");
        String subject = (String)reqMap.get("subject");
        String totalFee = (String)reqMap.get("totalFee");

        ProperEntity properInfo = new ProperEntity();
        properInfo.setOutTradeNo(orderNo);
        properInfo.setTradeNo(UUID.randomUUID().toString());
        properInfo.setNotifyTime(DateUtil.getTimestamp(false));
        properInfo.setSubject(subject);
        properInfo.setTotalFee(totalFee);
        properRepo.save(properInfo);

        Map<String, String> params = new HashMap<>();
        params.put("orderNo", orderNo);
        params.put("subject", subject);
        params.put("notifyTime", properInfo.getNotifyTime());

        NoticeService noticeService = payFactory.newNoticeService("proper");
        payNoticeTask.run(params, noticeService);

        Map<String, Object> res = new HashMap<>();
        res.put("resultCode", "SUCCESS");
        return responseOfPost(res);
    }

}
