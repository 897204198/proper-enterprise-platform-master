package com.proper.enterprise.platform.pay.cmb.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.api.pay.constants.PayConstants;
import com.proper.enterprise.platform.api.pay.enums.PayResType;
import com.proper.enterprise.platform.api.pay.factory.PayFactory;
import com.proper.enterprise.platform.api.pay.model.RefundReq;
import com.proper.enterprise.platform.api.pay.service.NoticeService;
import com.proper.enterprise.platform.api.pay.service.PayService;
import com.proper.enterprise.platform.common.pay.utils.PayUtils;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.pay.cmb.entity.CmbPayEntity;
import com.proper.enterprise.platform.pay.cmb.model.CmbPayResultRes;
import com.proper.enterprise.platform.pay.cmb.model.CmbRefundNoDupBodyReq;
import com.proper.enterprise.platform.pay.cmb.model.CmbRefundNoDupRes;
import com.proper.enterprise.platform.pay.cmb.service.CmbPayService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 一网通支付Controller
 */
@RestController
@RequestMapping(value = "/pay/cmb")
public class CmbController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmbController.class);

    @Autowired
    Marshaller marshaller;

    @Autowired
    Map<String, Unmarshaller> unmarshallerMap;

    @Autowired
    CmbPayService cmbPayService;

    @Autowired
    PayFactory payFactory;

    private String payWay = "cmb";

    /**
     * 一网通签约异步通知
     *
     * @param request 请求
     * @return 处理结果
     * @throws Exception 异步通知异常
     */
    @AuthcIgnore
    @PostMapping(value = "/noticeCmbProtocolInfo")
    public ResponseEntity<String> dealCmbNoticeProtocolInfo(HttpServletRequest request) throws Exception {
        LOGGER.debug("------------- Cmb protocol async notice--------begin------------");
        // 返回给一网通服务器的异步通知结果
        boolean ret = false;
        try {
            // 获取一网通POST过来反馈信息
            // 唯一的元素
            String reqData = request.getParameter("RequestData");
            // 处理签约异步通知结果
            ret = cmbPayService.saveNoticeProtocol(reqData);
        } catch (Exception e) {
            LOGGER.debug("CmbController.dealCmbNoticeProtocolInfo[Exception]:", e);
            throw e;
        }
        if (ret) {
            LOGGER.debug("----------- Cmb protocol async notice--------end---------------");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } else {
            LOGGER.debug("----------- Cmb protocol async notice: handled or invalid protocol info------end-------------------");
            return new ResponseEntity<>("FAIL", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 一网通支付结果异步通知
     *
     * @param request 请求
     * @return 处理结果
     * @throws Exception 异步通知异常
     */
    @AuthcIgnore
    @GetMapping(value = "/noticeCmbPayInfo")
    public ResponseEntity<String> dealCmbNoticePay(HttpServletRequest request) throws Exception {
        LOGGER.debug("-----------Cmb async notice--------begin---------------");
        // 获取从银行返回的信息
        String queryStr = request.getQueryString();
        // 检验数字签名
        if (!cmbPayService.isValid(queryStr)) {
            LOGGER.debug("Verify sign failed!{}", queryStr);
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
        try {
            // 保存异步通知信息flag
            LOGGER.debug("Verify sign successful！{}", queryStr);
            // 取得一网通支付结果异步通知对象
            CmbPayEntity cmbInfo = cmbPayService.getCmbPayNoticeInfo(request);
            // 输出异步通知结果到log
            PayUtils.logEntity(cmbInfo);
            CmbPayEntity queryPayInfo = cmbPayService.getPayNoticeInfoByMsg(cmbInfo.getMsg());
            if (queryPayInfo == null) {
                // 保存异步通知结果
                cmbPayService.saveCmbPayNoticeInfo(cmbInfo);
            }
            // 启用线程处理业务相关
            LOGGER.debug("Cmb async notice result has bean saved and start a new thread to deal with business!");
            NoticeService noticeService = payFactory.newNoticeService("cmb");
            noticeService.saveNoticeProcessAsync(cmbInfo);
            LOGGER.debug("-----------Cmb async notice--------end normal---------------");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.debug("Error occurred while saving cmb async notice", e);
            LOGGER.debug("-----------Cmb async notice:save message error------end error-----------------");
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 查询一网通单笔交易支付结果
     *
     * @param payInfo 支付信息
     * @return 处理结果
     */
    @AuthcIgnore
    @PostMapping(value = "/queryCmbPay", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CmbPayResultRes> queryCmbPay(@RequestBody CmbPayEntity payInfo) {
        CmbPayResultRes resObj = new CmbPayResultRes();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(payInfo.getDate()).append(payInfo.getBillNo());
            resObj = cmbPayService.querySingleOrder(sb.toString());
        } catch (Exception e) {
            LOGGER.debug("CmbController.queryCmbPay[Exception]:", e);
            resObj.setResultCode(PayResType.SYSERROR);
            resObj.setResultMsg(PayConstants.APP_SYSTEM_ERR);
        }
        return responseOfPost(resObj);
    }

    /**
     * 一网通退款接口
     *
     * @param refundInfo 退款
     * @return 处理结果
     */
    @AuthcIgnore
    @PostMapping(value = "/refundCmbPay", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CmbRefundNoDupRes> refundPayInfo(@RequestBody CmbRefundNoDupBodyReq refundInfo) {
        CmbRefundNoDupRes resObj = new CmbRefundNoDupRes();
        try {
            RefundReq refundReq = new RefundReq();
            refundReq.setOutTradeNo(refundInfo.getBillNo());
            // 设定退款流水号(随机生成20位退款流水号)
            refundReq.setOutRequestNo(RandomStringUtils.randomNumeric(20));
            refundReq.setTotalFee(refundInfo.getAmount());
            refundReq.setRefundAmount(refundInfo.getAmount());
            // 调用退款接口
            PayService payService = payFactory.newPayService(payWay);
            resObj = payService.refundPay(refundReq);
        } catch (Exception e) {
            LOGGER.debug("PayController.refundCmbPay[Exception]:", e);
            LOGGER.debug("---------  Cmb refund error  --------------");
        }
        return responseOfPost(resObj);
    }
}
