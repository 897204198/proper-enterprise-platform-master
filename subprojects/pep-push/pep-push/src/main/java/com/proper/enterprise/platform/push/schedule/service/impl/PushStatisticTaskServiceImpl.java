package com.proper.enterprise.platform.push.schedule.service.impl;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus;
import com.proper.enterprise.platform.push.common.schedule.service.PushStatisticTaskService;
import com.proper.enterprise.platform.push.entity.PushMsgStatisticEntity;
import com.proper.enterprise.platform.push.repository.PushMsgRepository;
import com.proper.enterprise.platform.push.repository.PushMsgStatisticRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

        Date dateEnd = new Date();
        Date dateStart = DateUtil.addDay(dateEnd, -1);
        List<Object[]> statisticList = msgStatisticRepositoryRepo.getPushStatistic(
            DateUtil.toString(dateStart, PEPConstants.DEFAULT_DATE_FORMAT),
            DateUtil.toString(dateEnd, PEPConstants.DEFAULT_DATE_FORMAT));
        for (Object[] rows : statisticList) {
            PushMsgStatisticEntity entity = new PushMsgStatisticEntity();
            entity.setAppkey((String) rows[0]);
            entity.setPushMode((String) rows[1]);
            Date msendDate = DateUtil.toDate((String) rows[2], PEPConstants.DEFAULT_DATE_FORMAT);
            entity.setMsendedDate(DateUtil.toString(msendDate, PEPConstants.DEFAULT_DATE_FORMAT));


            StringBuffer sb = new StringBuffer();
            Date mondayOfThisWeek = DateUtil.getDayOfWeek(msendDate, 1);
            Date sundayOfThisWeek = DateUtil.getDayOfWeek(msendDate, 7);
            sb.append(DateUtil.toString(mondayOfThisWeek, PEPConstants.DEFAULT_DATE_FORMAT))
                .append("～")
                .append(DateUtil.toString(sundayOfThisWeek, PEPConstants.DEFAULT_DATE_FORMAT));
            entity.setWeek(sb.toString());

            entity.setMonth(DateUtil.toString(msendDate, PEPConstants.DEFAULT_MONTH_FORMAT));
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
        msgStatisticRepositoryRepo.deleteByMsendedDate(DateUtil.toString(dateStart, PEPConstants.DEFAULT_DATE_FORMAT));
        msgStatisticRepositoryRepo.save(entityList);
    }
}
