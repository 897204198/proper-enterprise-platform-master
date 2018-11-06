package com.proper.enterprise.platform.notice.server.push.dao.service.impl;

import com.proper.enterprise.platform.file.service.FileService;
import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator;
import com.proper.enterprise.platform.notice.server.push.dao.service.PushNoticeConfigService;
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.push.vo.PushConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PushNoticeConfigServiceImpl implements PushNoticeConfigService {

    private static final String CERT_ID = "certificateId";

    private NoticeConfigurator pushConfigurator;

    private FileService fileService;

    @Autowired
    public PushNoticeConfigServiceImpl(@Qualifier("pushNoticeConfigurator") NoticeConfigurator pushConfigurator,
                                       FileService fileService) {
        this.pushConfigurator = pushConfigurator;
        this.fileService = fileService;
    }

    @Override
    public void save(String appKey, PushConfigVO pushConfigVO) {
        if (null != pushConfigVO.getHuaweiConf()) {
            Map<String, Object> huaweiParam = new HashMap<>(16);
            huaweiParam.put("pushChannel", PushChannelEnum.HUAWEI.name());
            pushConfigurator.post(appKey, pushConfigVO.getHuaweiConf(), huaweiParam);
        }
        if (null != pushConfigVO.getXiaomiConf()) {
            Map<String, Object> xiaomiParam = new HashMap<>(16);
            xiaomiParam.put("pushChannel", PushChannelEnum.XIAOMI.name());
            pushConfigurator.post(appKey, pushConfigVO.getXiaomiConf(), xiaomiParam);
        }
        if (null != pushConfigVO.getIosConf()) {
            Map<String, Object> iosParam = new HashMap<>(16);
            iosParam.put("pushChannel", PushChannelEnum.APNS.name());
            pushConfigurator.post(appKey, pushConfigVO.getIosConf(), iosParam);
        }
    }

    @Override
    public void delete(String appKey) {
        Map<String, Object> huaweiParam = new HashMap<>(16);
        huaweiParam.put("pushChannel", PushChannelEnum.HUAWEI.name());
        pushConfigurator.delete(appKey, huaweiParam);
        Map<String, Object> xiaomiParam = new HashMap<>(16);
        xiaomiParam.put("pushChannel", PushChannelEnum.XIAOMI.name());
        pushConfigurator.delete(appKey, xiaomiParam);
        Map<String, Object> iosParam = new HashMap<>(16);
        iosParam.put("pushChannel", PushChannelEnum.APNS.name());
        pushConfigurator.delete(appKey, iosParam);
    }

    @Override
    public void update(String appKey, PushConfigVO pushConfigVO) {
        //处理华为配置
        Map<String, Object> huaweiParam = new HashMap<>(16);
        huaweiParam.put("pushChannel", PushChannelEnum.HUAWEI.name());
        Map oldHuaweiConf = pushConfigurator.get(appKey, huaweiParam);
        if (null == pushConfigVO.getHuaweiConf() && null != oldHuaweiConf) {
            pushConfigurator.delete(appKey, huaweiParam);
        }
        if (null != pushConfigVO.getHuaweiConf()) {
            if (null == oldHuaweiConf) {
                pushConfigurator.post(appKey, pushConfigVO.getHuaweiConf(), huaweiParam);
            }
            if (null != oldHuaweiConf) {
                pushConfigVO.getHuaweiConf().put("pushChannel", PushChannelEnum.HUAWEI);
                pushConfigurator.put(appKey, pushConfigVO.getHuaweiConf(), huaweiParam);
            }
        }
        //处理小米配置
        Map<String, Object> xiaomiParam = new HashMap<>(16);
        xiaomiParam.put("pushChannel", PushChannelEnum.XIAOMI.name());
        Map oldXiaomiConf = pushConfigurator.get(appKey, xiaomiParam);
        if (null == pushConfigVO.getXiaomiConf() && null != oldXiaomiConf) {
            pushConfigurator.delete(appKey, xiaomiParam);
        }
        if (null != pushConfigVO.getXiaomiConf()) {
            if (null == oldXiaomiConf) {
                pushConfigurator.post(appKey, pushConfigVO.getXiaomiConf(), xiaomiParam);
            }
            if (null != oldXiaomiConf) {
                pushConfigVO.getXiaomiConf().put("pushChannel", PushChannelEnum.XIAOMI);
                pushConfigurator.put(appKey, pushConfigVO.getXiaomiConf(), xiaomiParam);
            }
        }
        //处理IOS配置
        Map<String, Object> iosParam = new HashMap<>(16);
        iosParam.put("pushChannel", PushChannelEnum.APNS.name());
        Map oldIosConf = pushConfigurator.get(appKey, iosParam);
        if (null == pushConfigVO.getIosConf() && null != oldIosConf) {
            pushConfigurator.delete(appKey, iosParam);
        }
        if (null != pushConfigVO.getIosConf()) {
            if (null == oldIosConf) {
                pushConfigurator.post(appKey, pushConfigVO.getIosConf(), iosParam);
            }
            if (null != oldIosConf) {
                pushConfigVO.getIosConf().put("pushChannel", PushChannelEnum.APNS);
                pushConfigurator.put(appKey, pushConfigVO.getIosConf(), iosParam);
            }
        }
    }

    @Override
    public PushConfigVO get(String appKey) {
        PushConfigVO pushConfigVO = new PushConfigVO();
        Map<String, Object> huaweiParam = new HashMap<>(16);
        huaweiParam.put("pushChannel", PushChannelEnum.HUAWEI.name());
        pushConfigVO.setHuaweiConf(pushConfigurator.get(appKey, huaweiParam));
        Map<String, Object> xiaomiParam = new HashMap<>(16);
        xiaomiParam.put("pushChannel", PushChannelEnum.XIAOMI.name());
        pushConfigVO.setXiaomiConf(pushConfigurator.get(appKey, xiaomiParam));
        Map<String, Object> iosParam = new HashMap<>(16);
        iosParam.put("pushChannel", PushChannelEnum.APNS.name());
        Map<String, Object> iosConf = pushConfigurator.get(appKey, iosParam);
        if (null != iosConf && null != iosConf.get(CERT_ID)) {
            iosConf.put("certificateName", fileService.findOne(iosConf.get(CERT_ID).toString()).getFileName());
        }
        pushConfigVO.setIosConf(iosConf);
        return pushConfigVO;
    }
}
