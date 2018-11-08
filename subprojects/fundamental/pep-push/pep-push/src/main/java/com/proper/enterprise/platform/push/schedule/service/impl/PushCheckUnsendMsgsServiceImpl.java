package com.proper.enterprise.platform.push.schedule.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.nutz.mapl.Mapl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.proper.enterprise.platform.push.config.PushGlobalInfo;
import com.proper.enterprise.platform.push.entity.PushMsgEntity;
import com.proper.enterprise.platform.push.repository.PushMsgRepository;
import com.proper.enterprise.platform.push.common.model.enums.PushDeviceType;
import com.proper.enterprise.platform.push.common.model.enums.PushMode;
import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus;
import com.proper.enterprise.platform.push.common.schedule.service.PushCheckUnsendMsgsService;
import com.proper.enterprise.platform.push.vendor.AbstractPushVendorService;
import com.proper.enterprise.platform.push.vendor.PushVendorFactory;

/**
 * 定时检查未发送的消息
 *
 * @author shen
 *
 */
@Component("checkUnsendMsgsTask")
public class PushCheckUnsendMsgsServiceImpl implements PushCheckUnsendMsgsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushCheckUnsendMsgsServiceImpl.class);

    @Autowired
    PushGlobalInfo globalInfo;
    @Autowired
    PushMsgRepository msgRepo;
    @Autowired
    PushVendorFactory pushVendorFactory;

    @Override
    public synchronized void saveCheckUnsendMsgs() {
        Map<String, Map<String, Object>> config = globalInfo.getPushConfigs();
        Set<String> appkeys = config.keySet();
        Map<String, Object> rtn = new LinkedHashMap<>();
        final int dbBatchSize = globalInfo.getDbBatchSize();
        try {
            for (String appkey : appkeys) {
                Map<String, Object> deviceConf = (Map<String, Object>) Mapl.cell(config, appkey + ".device");
                // 最大发送次数，超过最大发送次数的消息，将不会再进行重发操作
                Integer maxSendCount = Integer.valueOf(Mapl.cell(config, appkey + ".max_send_count").toString());
                for (Entry<String, Object> entry : deviceConf.entrySet()) {
                    String strDeviceType = entry.getKey();
                    PushDeviceType deviceType = Enum.valueOf(PushDeviceType.class, strDeviceType.trim());
                    Map<String, Object> mapPushModes = (Map<String, Object>) entry.getValue();
                    for (Entry<String, Object> pushModeEntry : mapPushModes.entrySet()) {
                        String strPushMode = pushModeEntry.getKey();
                        PushMode pushMode = Enum.valueOf(PushMode.class, strPushMode.trim());
                        AbstractPushVendorService pushService = pushVendorFactory.getPushVendorService(appkey,
                                deviceType, pushMode);
                        int totalSendCount = 0;
                        Pageable pageable = new PageRequest(0, dbBatchSize);
                        boolean hasData = false;
                        do {
                            Page<PushMsgEntity> page = msgRepo
                                    .findByAppkeyAndMstatusAndSendCountLessThanEqualAndDevicePushMode(appkey,
                                            PushMsgStatus.UNSEND, maxSendCount, pushMode, pageable);
                            pageable = pageable.next();
                            hasData = page.hasContent();
                            if (hasData) {
                                List<PushMsgEntity> lstMsgs = page.getContent();
                                totalSendCount += pushService.pushMsg(lstMsgs);
                            }
                        } while (hasData);
                        if (totalSendCount > 0) {
                            rtn.put(appkey + "_" + deviceType + "_" + pushMode, totalSendCount);
                        }
                    }
                }
            }
            LOGGER.trace(rtn.toString());
        } catch (Exception ex) {
            LOGGER.error(ex.toString(), ex);
        }
    }
}
