package com.proper.enterprise.platform.push.schedule.service.impl;

import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus;
import com.proper.enterprise.platform.push.common.schedule.service.PushClearOldMsgsTaskService;
import com.proper.enterprise.platform.push.config.PushGlobalInfo;
import com.proper.enterprise.platform.push.repository.PushMsgRepository;
import org.nutz.mapl.Mapl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 清空指定天数之前的历史消息
 *
 * @author shen
 */
@Component("clearOldMsgsTask")
public class PushClearOldMsgsTaskServiceImpl implements PushClearOldMsgsTaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushClearOldMsgsTaskServiceImpl.class);
    @Autowired
    PushGlobalInfo globalInfo;
    @Autowired
    PushMsgRepository msgRepo;

    /**
     * 清空指定天数之前的发送成功消息
     */
    @Override
    public synchronized void deleteOldMsgs() {
        Map<String, Map<String, Object>> config = globalInfo.getPushConfigs();
        try {
            for (Map.Entry<String, Map<String, Object>> entry : config.entrySet()) {
                String appkey = entry.getKey();
                int msgSaveDays = Integer.parseInt(Mapl.cell(entry.getValue(), "msg_save_days").toString());
                Date date2 = DateUtil.addDay(new Date(), -1 * msgSaveDays);
                String strTimeStamp = DateUtil.toTimestamp(date2);
                int rtn = msgRepo.deleteByAppkeyAndCreateTimeLessThan(appkey, strTimeStamp, PushMsgStatus.SENDED);
                LOGGER.info("[" + appkey + "]: delete  msgs  before date of " + strTimeStamp);
                LOGGER.info("[" + appkey + "]: delete " + rtn + " rows.");
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString(), ex);
        }
    }

    /**
     * 清空指定天数之前的发送失败消息
     */
    @Override
    public synchronized void deleteOldUnsendMsgs() {
        Map<String, Map<String, Object>> config = globalInfo.getPushConfigs();
        try {
            for (Map.Entry<String, Map<String, Object>> entry : config.entrySet()) {
                String appkey = entry.getKey();
                int msgSaveDays = Integer.parseInt(Mapl.cell(entry.getValue(), "msg_save_days").toString());
                Date date2 = DateUtil.addDay(new Date(), -7 * msgSaveDays);
                String strTimeStamp = DateUtil.toTimestamp(date2);
                int rtn = msgRepo.deleteUnsendMsgByAppkeyAndCreateTimeLessThan(appkey, strTimeStamp, PushMsgStatus.UNSEND);
                LOGGER.info("[" + appkey + "]: delete  msgs  before date of " + strTimeStamp);
                LOGGER.info("[" + appkey + "]: delete " + rtn + " rows.");
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString(), ex);
        }
    }
}
