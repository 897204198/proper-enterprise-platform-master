package com.proper.enterprise.platform.pay.cmb.service.impl

import com.proper.enterprise.platform.pay.cmb.model.CmbBillRecordRes
import com.proper.enterprise.platform.pay.cmb.model.CmbCommonHeadRes
import com.proper.enterprise.platform.pay.cmb.model.CmbQueryRefundBodyRes
import com.proper.enterprise.platform.pay.cmb.model.CmbQueryRefundRes
import com.proper.enterprise.platform.pay.cmb.model.CmbQuerySingleOrderBodyRes
import com.proper.enterprise.platform.pay.cmb.model.CmbQuerySingleOrderRes
import com.proper.enterprise.platform.pay.cmb.model.CmbRefundNoDupBodyRes
import com.proper.enterprise.platform.pay.cmb.model.CmbRefundNoDupRes
import com.proper.enterprise.platform.pay.cmb.service.CmbPayResService
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Service
@Primary
class MockCmbPayResServiceImpl extends CmbPayResServiceImpl implements CmbPayResService {

    /**
     * 向一网通服务器发送http请求
     *
     * @param url 请求地址
     * @param beanId 实例Bean
     * @param requestXML 请求报文
     * @param <T> 泛型
     * @return 请求结果
     * @throws Exception
     */
    @Override
    public <T> T getCmbApiRes(String url, String beanId, String requestXML) throws Exception {
        CmbCommonHeadRes headRes = new CmbCommonHeadRes()
        // 查询
        if(beanId.equals("unmarshallCmbQuerySingleOrderRes")) {
            CmbQuerySingleOrderRes queryRes = new CmbQuerySingleOrderRes()
            CmbQuerySingleOrderBodyRes bodyRes = new CmbQuerySingleOrderBodyRes()

            if(requestXML.contains("1481260647")) {
                bodyRes.setBillNo("1481260647")
                bodyRes.setBillAmount("0.02")
                bodyRes.setAmount("0.02")
                bodyRes.setAcceptDate("20161209")
                bodyRes.setAcceptTime("131752")
                bodyRes.setStatus("0")
                bodyRes.setCardType("02")
                bodyRes.setCardNo("621485******3559")
                bodyRes.setFee("0.00")
                bodyRes.setBankSeqNo("16320960400000004220")
                bodyRes.setMerchantPara("pno%3d20161209131727305537%7cuserid%3d57ee2736ae65e2531aad70fa%7intent%3dyuyueguahaojiaofei")
            } else {
                headRes.setCode("error")
            }

            queryRes.setHead(headRes)
            queryRes.setBody(bodyRes)
            return (T)queryRes

            // 退款查询
        } else if(beanId.equals("unmarshallCmbQueryRefundRes")) {
            CmbQueryRefundRes queryRefundRes = new CmbQueryRefundRes()
            CmbQueryRefundBodyRes bodyRes = new CmbQueryRefundBodyRes()
            List<CmbBillRecordRes> recordList = new ArrayList<>()
            CmbBillRecordRes recordRes = new CmbBillRecordRes()

            recordRes.setAmount("0.01")

            recordList.add(recordRes)
            bodyRes.setBillRecord(recordList)
            queryRefundRes.setHead(headRes)
            queryRefundRes.setBody(bodyRes)
            return (T)queryRefundRes

            // 退款
        } else if(beanId.equals("unmarshallCmbRefundNoDupRes")) {
            CmbRefundNoDupRes cmbRefundNoDupRes = new CmbRefundNoDupRes()
            CmbRefundNoDupBodyRes bodyRes = new CmbRefundNoDupBodyRes()

            bodyRes.setRefundNo("20161209148125442201")
            bodyRes.setBankSeqNo("17211817000000007600")
            bodyRes.setAmount("0.01")
            bodyRes.setDate("20170118")
            bodyRes.setTime("150930")

            cmbRefundNoDupRes.setHead(headRes)
            cmbRefundNoDupRes.setBody(bodyRes)
            return (T)cmbRefundNoDupRes
        }
    }
}
