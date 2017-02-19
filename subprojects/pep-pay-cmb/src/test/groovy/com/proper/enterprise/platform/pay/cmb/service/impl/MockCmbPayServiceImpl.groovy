package com.proper.enterprise.platform.pay.cmb.service.impl

import com.cmb.b2b.Base64
import com.proper.enterprise.platform.api.pay.service.PayService
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.pay.cmb.constants.CmbConstants
import com.proper.enterprise.platform.pay.cmb.document.CmbProtocolDocument
import com.proper.enterprise.platform.pay.cmb.model.CmbBusinessRes
import com.proper.enterprise.platform.pay.cmb.service.CmbPayService
import com.proper.enterprise.platform.pay.cmb.utils.CmbUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.oxm.Unmarshaller
import org.springframework.stereotype.Service

import javax.xml.transform.stream.StreamSource

@Primary
@Service
class MockCmbPayServiceImpl extends CmbPayServiceImpl implements PayService, CmbPayService{

    @Autowired
    Map<String, Unmarshaller> unmarshallerMap;

    /**
     * 一网通支付结果异步通知验签
     *
     * @param notice 异步通知的字符串
     * @return 验签是否成功
     */
    @Override
    public boolean isValid(String notice) {
        return true;
    }

    @Override
    boolean saveNoticeProtocol(String reqData) throws Exception {
        boolean ret = false
        reqData = reqData.replace(' ', '+');
        Map<String, String> reqObject = JSONUtil.parse(reqData, Map.class);
        String busdat = reqObject.get("BUSDAT");
        byte[] bt = Base64.decode(busdat);
        CmbBusinessRes res = (CmbBusinessRes) unmarshallerMap.get("unmarshallCmbBusinessRes")
            .unmarshal(new StreamSource(new ByteArrayInputStream(bt)));
        Map<String, String> paramObj = CmbUtils.getParamObj(res.getNoticepara());
        String userId = paramObj.get("userid");
        String pno = paramObj.get("pno");
        CmbProtocolDocument protocolInfo = getUserProtocolInfo(userId);
        if (protocolInfo != null) {
            if (protocolInfo.getSign().equals(CmbConstants.CMB_PAY_PROTOCOL_UNSIGNED)) {
                if (CmbConstants.CMB_PAY_PROTOCOL_SUCCESS.equals(res.getRespcod())
                    && pno.equals(res.getCustArgno())) {
                    protocolInfo.setProtocolNo(res.getCustArgno());
                    protocolInfo.setSign(CmbConstants.CMB_PAY_PROTOCOL_SIGNED);
                    saveUserProtocolInfo(protocolInfo);
                    res.setRespmsg("protocol_sign_success");
                    saveBusinessInfo(userId, reqObject, res);
                    ret = true
                }
            }
        }
        return ret;
    }
}
