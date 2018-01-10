package com.proper.enterprise.platform.pay.cmb.service.impl

import com.proper.enterprise.platform.api.pay.service.NoticeService
import com.proper.enterprise.platform.pay.cmb.entity.CmbPayEntity
import com.proper.enterprise.platform.pay.cmb.service.CmbPayService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

/**
 * 实现异步通知业务代码(需要在项目中实现,此处为示例测试代码)
 */
@Service("pay_notice_cmb")
class MockCmbNoticeServiceImpl implements NoticeService <CmbPayEntity>{

    private static final Logger LOGGER = LoggerFactory.getLogger(MockCmbNoticeServiceImpl.class);

    @Autowired
    CmbPayService cmbPayService;

    /**
     * 异步通知业务处理代码
     *
     * @param cmbInfo 一网通异步通知处理参数
     */
    @Async
    @Override
    public void saveNoticeProcessAsync(CmbPayEntity  cmbInfo) {
        LOGGER.debug("-------------异步通知相关业务处理-----------------");

        CmbPayEntity tmpCmbInfo = new CmbPayEntity()

        tmpCmbInfo.setUserId("testUserId")
        tmpCmbInfo.setIntent("yuyueguahaojiaofei")
        tmpCmbInfo.setSucceed("testSucceed")
        tmpCmbInfo.setCoNo("testCoNo")
        tmpCmbInfo.setBillNo("1234567890")
        tmpCmbInfo.setAmount("testAmount")
        tmpCmbInfo.setDate("20170101")
        tmpCmbInfo.setTime("testTime")
        tmpCmbInfo.setMsg("001")

        cmbPayService.saveCmbPayNoticeInfo(tmpCmbInfo)

    }
}
