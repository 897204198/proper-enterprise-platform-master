package com.proper.enterprise.platform.notice.server.push.scheduler;

import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.notice.server.push.dao.entity.PushNoticeMsgStatisticEntity;
import com.proper.enterprise.platform.notice.server.push.dao.service.PushNoticeMsgStatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 每日定时任务：统计前一天的推送数据
 *
 * @author guozhimin
 */
@Component("pushStatisticTask")
public class PushStatisticTaskScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushStatisticTaskScheduler.class);

    private PushNoticeMsgStatisticService pushNoticeMsgStatisticService;

    @Autowired
    private CoreProperties coreProperties;

    @Autowired
    public PushStatisticTaskScheduler(PushNoticeMsgStatisticService pushNoticeMsgStatisticService) {
        this.pushNoticeMsgStatisticService = pushNoticeMsgStatisticService;
    }

    public void saveYesterdayPushStatistic() {
        List<PushNoticeMsgStatisticEntity> entityList = new ArrayList<>();
        //当天时间
        LocalDateTime dateEnd = LocalDateTime.now();
        //昨天时间
        LocalDateTime dateStart = DateUtil.addDay(dateEnd, -1);
        List<PushNoticeMsgStatisticEntity> pushMsgStatistics = pushNoticeMsgStatisticService.getPushStatistic(DateUtil.toDate(dateStart),
                DateUtil.toDate(dateEnd));
        LOGGER.debug("startDate:{} endDate:{} entityList:{}", DateUtil.toString(dateStart, coreProperties.getDefaultDatetimeFormat()),
            DateUtil.toString(dateEnd, coreProperties.getDefaultDatetimeFormat()), entityList);
        pushNoticeMsgStatisticService.deleteBySendDate(DateUtil.toDate(dateStart));
        pushNoticeMsgStatisticService.saveAll(pushMsgStatistics);
    }
}
