package com.proper.enterprise.platform.push.schedule.service.impl;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus;
import com.proper.enterprise.platform.push.common.schedule.service.PushStatisticTaskService;
import com.proper.enterprise.platform.push.entity.PushMsgStatisticEntity;
import com.proper.enterprise.platform.push.repository.PushMsgRepository;
import com.proper.enterprise.platform.push.repository.PushMsgStatisticRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 每日定时任务：统计前一天的推送数据
 *
 * @author guozhimin
 */
@Component("pushStatisticTask")
public class PushStatisticTaskServiceImpl implements PushStatisticTaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushStatisticTaskServiceImpl.class);
    @Autowired
    PushMsgStatisticRepository msgStatisticRepositoryRepo;
    @Autowired
    PushMsgRepository msgRepository;

    @Override
    public void saveYesterdayPushStatistic() throws Exception {
        List<PushMsgStatisticEntity> entityList = new ArrayList<PushMsgStatisticEntity>();

        Date dateStart = DateUtils.ceiling(DateUtils.addDays(new Date(), -2), Calendar.DAY_OF_MONTH);
        Date dateEnd = DateUtils.ceiling(DateUtils.addDays(new Date(), -1), Calendar.DAY_OF_MONTH);
        List<Object[]> statisticList = msgStatisticRepositoryRepo.getPushStatistic(DateUtil.toString(dateStart, PEPConstants.DEFAULT_DATETIME_FORMAT),
            DateUtil.toString(dateEnd, PEPConstants.DEFAULT_DATETIME_FORMAT));
        for (Object[] rows : statisticList) {
            PushMsgStatisticEntity entity = new PushMsgStatisticEntity();
            entity.setAppkey((String) rows[0]);
            entity.setPushMode((String) rows[1]);
            Date msendDate = DateUtils.parseDate((String) rows[2], PEPConstants.DEFAULT_DATE_FORMAT);
            entity.setMsendedDate(DateUtil.toString(msendDate, PEPConstants.DEFAULT_DATE_FORMAT));

            Calendar cale = DateUtils.toCalendar(msendDate);
            if (cale.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                cale.add(Calendar.DAY_OF_MONTH, -1);
            }
            cale.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            StringBuffer sb = new StringBuffer();
            sb.append(DateUtil.toString(cale.getTime(), PEPConstants.DEFAULT_DATE_FORMAT)).append("～");
            cale.add(Calendar.DAY_OF_MONTH, 6);
            sb.append(DateUtil.toString(cale.getTime(), PEPConstants.DEFAULT_DATE_FORMAT));

            entity.setWeek(sb.toString());
            entity.setMonth(DateUtil.toString(msendDate, "yyyy-MM"));
            if ((int) rows[3] == PushMsgStatus.SENDED.ordinal()) {
                entity.setMstatus(PushMsgStatus.SENDED);
            } else if ((int) rows[3] == PushMsgStatus.UNSEND.ordinal()) {
                entity.setMstatus(PushMsgStatus.UNSEND);
            }
            String obj4 = rows[4].toString();
            entity.setMnum(Integer.valueOf(obj4));
            entityList.add(entity);
        }
        LOGGER.info("startDate:{} endDate:{} entityList:{}", DateUtil.toString(dateStart, PEPConstants.DEFAULT_DATETIME_FORMAT),
            DateUtil.toString(dateEnd, PEPConstants.DEFAULT_DATETIME_FORMAT), entityList);
        msgStatisticRepositoryRepo.save(entityList);
    }
}
