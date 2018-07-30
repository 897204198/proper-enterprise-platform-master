package com.proper.enterprise.platform.push.service.impl;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.jpa.service.impl.AbstractJpaServiceSupport;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.push.api.PushMsgStatistic;
import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus;
import com.proper.enterprise.platform.push.entity.PushMsgStatisticEntity;
import com.proper.enterprise.platform.push.repository.PushMsgStatisticRepository;
import com.proper.enterprise.platform.push.service.PushMsgStatisticService;
import com.proper.enterprise.platform.push.vo.PushMsgStatisticVO;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class PushMsgStatisticServiceImpl extends AbstractJpaServiceSupport<PushMsgStatistic, PushMsgStatisticRepository, String>
    implements PushMsgStatisticService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushMsgStatisticServiceImpl.class);
    @Autowired
    private PushMsgStatisticRepository pushMsgStatisticRepository;

    public PushMsgStatisticRepository getRepository() {
        return pushMsgStatisticRepository;
    }

    @Override
    public List<PushMsgStatisticVO> findByDateTypeAndAppkey(String dateType, String appkey) {
        final String day = "day";
        final String week = "week";
        final String month = "month";
        LOGGER.info("dateType:{}; appkey:{}", dateType, appkey);
        if (day.equals(dateType)) {
            return findPushStatisticByDay(appkey);
        } else if (week.equals(dateType)) {
            return findPushStatisticByWeek(appkey);
        } else if (month.equals(dateType)) {
            return findPushStatisticByMonth(appkey);
        } else {
            return null;
        }
    }

    @Override
    public List<PushMsgStatisticVO> saveStatisticOfSomeday(String date) {

        List<PushMsgStatisticEntity> entityList = new ArrayList<PushMsgStatisticEntity>();

        Date dateStart = DateUtil.toDate(date, PEPConstants.DEFAULT_DATE_FORMAT);
        Date dateEnd = DateUtils.addDays(dateStart, 1);

        List<Object[]> statisticList = pushMsgStatisticRepository.getPushStatistic(DateUtil.toString(dateStart,
            PEPConstants.DEFAULT_DATETIME_FORMAT), DateUtil.toString(dateEnd, PEPConstants.DEFAULT_DATETIME_FORMAT));
        List<PushMsgStatisticVO> voList = new ArrayList<>();
        if (statisticList.size() > 0) {
            convertObjToEntity(statisticList, entityList, voList);
            pushMsgStatisticRepository.deleteByMsendedDate(DateUtil.toString(dateStart,
                PEPConstants.DEFAULT_DATETIME_FORMAT));
            pushMsgStatisticRepository.save(entityList);
        }
        return voList;
    }

    public List<PushMsgStatisticVO> findPushStatisticByDay(String appkey) {
        List list = new ArrayList<PushMsgStatisticVO>();
        Date msendDate = DateUtils.ceiling(DateUtils.addDays(new Date(), -8), Calendar.DAY_OF_MONTH);
        String dateStr = DateUtil.toString(msendDate, PEPConstants.DEFAULT_DATE_FORMAT);
        if (appkey == null) {
            List result = pushMsgStatisticRepository.findByAndMsendedDateAfterOrderByMsendedDate(dateStr);
            toEntity(result, list);
        } else {
            List result = pushMsgStatisticRepository.findByAppkeyAndMsendedDateAfterOrderByMsendedDate(appkey, dateStr);
            toEntity(result, list);
        }
        return list;
    }

    public List<PushMsgStatisticVO> findPushStatisticByWeek(String appkey) {

        Calendar cale = DateUtils.toCalendar(new Date());
        if (cale.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            cale.add(Calendar.DAY_OF_MONTH, -1);
        }
        cale.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cale.add(Calendar.WEEK_OF_YEAR, -6);
        String dateStr = DateUtil.toString(cale.getTime(), PEPConstants.DEFAULT_DATE_FORMAT);

        List list = new ArrayList<PushMsgStatisticVO>();
        if (appkey == null) {
            List result = pushMsgStatisticRepository.findByMsendedDateAfterGroupByWeekOfYear(dateStr);
            toEntity(result, list);
        } else {
            List result = pushMsgStatisticRepository.findByAppkeyAndMsendedDateAfterGroupByWeekOfYear(appkey, dateStr);
            toEntity(result, list);
        }
        return list;
    }

    public List<PushMsgStatisticVO> findPushStatisticByMonth(String appkey) {
        Date date = DateUtils.addYears(DateUtils.ceiling(new Date(), Calendar.YEAR), -1);
        String dateStr = DateUtil.toString(date, PEPConstants.DEFAULT_DATE_FORMAT);
        List list = new ArrayList<PushMsgStatisticVO>();
        if (appkey == null) {
            List result = pushMsgStatisticRepository.findByMsendedDateAfterGroupByMonthOfYear(dateStr);
            toEntity(result, list);
        } else {
            List result = pushMsgStatisticRepository.findByAppkeyAndMsendedDateAfterGroupByMonthOfYear(appkey, dateStr);
            toEntity(result, list);
        }
        return list;
    }

    private void toEntity(List list, List<PushMsgStatisticVO> entityList) {
        for (Object row : list) {
            Object[] cells = (Object[]) row;
            PushMsgStatisticVO entity = new PushMsgStatisticVO();
            entity.setMnum(Integer.valueOf(cells[0].toString()));
            entity.setMstatus(cells[1].toString());
            entity.setPushMode(cells[2].toString());
            entity.setMsendedDate(cells[3].toString());
            entityList.add(entity);
        }
    }

    private void convertObjToEntity(List<Object[]> statisticList, List<PushMsgStatisticEntity> entityList,
                                    List<PushMsgStatisticVO> voList) {
        try {
            for (Object[] rows : statisticList) {
                PushMsgStatisticEntity entity = new PushMsgStatisticEntity();
                entity.setAppkey((String) rows[0]);
                entity.setPushMode((String) rows[1]);
                Date msendDate = null;
                msendDate = DateUtils.parseDate((String) rows[2], PEPConstants.DEFAULT_DATE_FORMAT);
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

                PushMsgStatisticVO vo = new PushMsgStatisticVO();
                vo.setPushMode(entity.getPushMode());
                vo.setMsendedDate(entity.getMsendedDate());
                vo.setWeek(entity.getWeek());
                vo.setMonth(entity.getMonth());
                vo.setMnum(entity.getMnum());
                vo.setMstatus(entity.getMstatus().toString());
                voList.add(vo);
            }
        } catch (ParseException e) {
            LOGGER.error("statistic init error:{}", e, e.getStackTrace());
        }
    }
}
