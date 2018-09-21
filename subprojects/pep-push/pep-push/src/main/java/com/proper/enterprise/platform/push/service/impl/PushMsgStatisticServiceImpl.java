package com.proper.enterprise.platform.push.service.impl;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.jpa.service.impl.AbstractJpaServiceSupport;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.push.api.PushMsgStatistic;
import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus;
import com.proper.enterprise.platform.push.entity.PushChannelEntity;
import com.proper.enterprise.platform.push.entity.PushMsgStatisticEntity;
import com.proper.enterprise.platform.push.repository.PushChannelRepository;
import com.proper.enterprise.platform.push.repository.PushMsgStatisticRepository;
import com.proper.enterprise.platform.push.service.PushMsgStatisticService;
import com.proper.enterprise.platform.push.vo.PushMsgPieVO;
import com.proper.enterprise.platform.push.vo.PushMsgStatisticVO;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PushMsgStatisticServiceImpl extends AbstractJpaServiceSupport<PushMsgStatistic, PushMsgStatisticRepository, String>
    implements PushMsgStatisticService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushMsgStatisticServiceImpl.class);
    private static final String DATE_RANGE_DAY = "day";
    private static final String DATE_RANGE_WEEK = "week";
    private static final String DATE_RANGE_MONTH = "month";
    private static final int WEEK_RANGE = 8;
    @Autowired
    private PushMsgStatisticRepository pushMsgStatisticRepository;
    @Autowired
    DataDicService dataDicService;
    @Autowired
    private PushChannelRepository pushChannelRepository;

    public PushMsgStatisticRepository getRepository() {
        return pushMsgStatisticRepository;
    }

    @Override
    public List<PushMsgStatisticVO> findByDateTypeAndAppkey(String dateType, String appkey) {

        LOGGER.info("dateType:{}; appkey:{}", dateType, appkey);
        if (DATE_RANGE_DAY.equals(dateType)) {
            return findPushStatisticByDay(appkey);
        } else if (DATE_RANGE_WEEK.equals(dateType)) {
            return findPushStatisticByWeek(appkey);
        } else if (DATE_RANGE_MONTH.equals(dateType)) {
            return findPushStatisticByMonth(appkey);
        } else {
            return null;
        }
    }

    @Override
    public List<PushMsgStatisticVO> saveStatisticOfSomeday(String date) {

        List<PushMsgStatisticEntity> entityList = new ArrayList<PushMsgStatisticEntity>();

        Date dateStart = DateUtil.toDate(date, PEPConstants.DEFAULT_DATE_FORMAT);
        Date dateEnd = DateUtil.addDay(dateStart, 1);

        List<Object[]> statisticList = pushMsgStatisticRepository.getPushStatistic(DateUtil.toString(dateStart,
            PEPConstants.DEFAULT_DATE_FORMAT), DateUtil.toString(dateEnd, PEPConstants.DEFAULT_DATE_FORMAT));
        List<PushMsgStatisticVO> voList = new ArrayList<>();
        if (statisticList.size() > 0) {
            convertObjToEntity(statisticList, entityList, voList);
            pushMsgStatisticRepository.deleteByMsendedDate(DateUtil.toString(dateStart,
                PEPConstants.DEFAULT_DATE_FORMAT));
            pushMsgStatisticRepository.saveAll(entityList);
        }
        return voList;
    }

    @Override
    public Map<String, Object> findAllWithPie(String startDate, String endDate, String appKey) {
        if (StringUtil.isNull(appKey)) {
            throw new ErrMsgException("appkey is not null");
        }
        List list;
        List<String> appKeys = Arrays.asList(appKey.split(","));
        boolean hasDate = StringUtil.isNotNull(startDate) && StringUtil.isNotNull(endDate);

        if (hasDate) {
            list = pushMsgStatisticRepository.findByBetweenStartDataEndDate(startDate, endDate, appKeys);
        } else {
            Date sendDate = DateUtil.addDay(new Date(), -1);
            String dateStr = DateUtil.toString(sendDate, PEPConstants.DEFAULT_DATE_FORMAT);
            list = pushMsgStatisticRepository.findPushMsgByDefault(dateStr, appKeys);
        }
        List<String> appKeyList = new ArrayList<>();
        List appKeysOrder = pushMsgStatisticRepository.findOrderByMsgNumberDesc(appKeys);
        if (null == appKeysOrder) {
            throw new ErrMsgException("appKey is illegal");
        }
        for (Object row : appKeysOrder) {
            Object[] cells = (Object[]) row;
            appKeyList.add(String.valueOf(cells[1].toString()));
        }
        if (list == null) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.push.data.exception"));
        }
        List<PushMsgPieVO> pieVOS = new ArrayList<>();
        List<PushChannelEntity> channelAll = pushChannelRepository.findAll();
        //封装数据,并且数据补全
        orgData(list, pieVOS, channelAll, appKeys, appKeyList);
        Map<String, Object> result = new HashMap<>(2);
        result.put("appKeyOrder", appKeyList);
        result.put("pieData", pieVOS);
        return result;
    }

    private void orgData(List list, List<PushMsgPieVO> pieVOS, List<PushChannelEntity> channelAll, List<String> appKeys, List<String> appKeyList) {

        if (null != channelAll && channelAll.size() == 0) {
            throw new ErrMsgException("channel is not Configuration");
        }

        for (Object row : list) {
            Object[] cells = (Object[]) row;
            PushMsgPieVO pieVO = new PushMsgPieVO();
            pieVO.setMsgSum(Integer.valueOf(cells[0].toString()));
            pieVO.setMsgStatus(String.valueOf(cells[1].toString()));
            for (PushChannelEntity channel : channelAll) {
                if (String.valueOf(cells[2].toString()).equals(channel.getChannelName())) {
                    pieVO.setAppName(channel.getChannelDesc());
                    pieVO.setAppKey(channel.getChannelName());
                }
            }
            pieVOS.add(pieVO);
        }
        //补全数据
        List<String> haveAppKeys = new ArrayList<>();
        for (PushMsgPieVO pieVO : pieVOS) {
            haveAppKeys.add(pieVO.getAppKey());
        }
        for (String key : appKeys) {
            if (!haveAppKeys.contains(key)) {
                PushMsgPieVO pushMsgPieVO = new PushMsgPieVO();
                pushMsgPieVO.setAppKey(key);
                for (PushChannelEntity channelEntity : channelAll) {
                    if (key.equalsIgnoreCase(channelEntity.getChannelName())) {
                        pushMsgPieVO.setAppName(channelEntity.getChannelDesc());
                    }
                }
                pushMsgPieVO.setMsgStatus("SENDED");
                pushMsgPieVO.setMsgSum(0);
                pieVOS.add(pushMsgPieVO);
                appKeyList.add(key);
            }
        }
    }

    public List<PushMsgStatisticVO> findPushStatisticByDay(String appkey) {
        List list = new ArrayList<PushMsgStatisticVO>();
        Date msendDate = DateUtil.addDay(new Date(), -7);
        String dateStr = DateUtil.toString(msendDate, PEPConstants.DEFAULT_DATE_FORMAT);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("findPushStatisticByDay dateStr:{}, nowStr:{}, now:{}",
                DateUtil.toString(msendDate, PEPConstants.DEFAULT_DATETIME_FORMAT),
                DateUtil.toString(new Date(), PEPConstants.DEFAULT_DATETIME_FORMAT),
                new Date());
        }
        if (appkey == null) {
            List result = pushMsgStatisticRepository.findByAndMsendedDateAfterOrderByMsendedDate(dateStr);
            toEntity(result, list);
        } else {
            List result = pushMsgStatisticRepository.findByAppkeyAndMsendedDateAfterOrderByMsendedDate(appkey, dateStr);
            toEntity(result, list);
        }
        List<PushMsgStatisticVO> pushList = supplement(list, DATE_RANGE_DAY);
        return pushList;
    }

    public List<PushMsgStatisticVO> findPushStatisticByWeek(String appkey) {
        Date msendDate = DateUtil.getDayOfWeek(DateUtil.addWeek(new Date(), -6), 1);
        String dateStr = DateUtil.toString(msendDate, PEPConstants.DEFAULT_DATE_FORMAT);

        List list = new ArrayList<PushMsgStatisticVO>();
        if (appkey == null) {
            List result = pushMsgStatisticRepository.findByMsendedDateAfterGroupByWeekOfYear(dateStr);
            toEntity(result, list);
        } else {
            List result = pushMsgStatisticRepository.findByAppkeyAndMsendedDateAfterGroupByWeekOfYear(appkey, dateStr);
            toEntity(result, list);
        }
        List<PushMsgStatisticVO> pushList = supplement(list, DATE_RANGE_WEEK);
        return pushList;
    }

    public List<PushMsgStatisticVO> findPushStatisticByMonth(String appkey) {
        Date date = new Date();
        Date beginMonth = DateUtil.addMonth(date, -7);
        String dateStr = DateUtil.toString(beginMonth, PEPConstants.DEFAULT_MONTH_FORMAT);
        List list = new ArrayList<PushMsgStatisticVO>();
        if (appkey == null) {
            List result = pushMsgStatisticRepository.findByMsendedDateAfterGroupByMonthOfYear(dateStr);
            toEntity(result, list);
        } else {
            List result = pushMsgStatisticRepository.findByAppkeyAndMsendedDateAfterGroupByMonthOfYear(appkey, dateStr);
            toEntity(result, list);
        }
        List<PushMsgStatisticVO> pushList = supplement(list, DATE_RANGE_MONTH);
        return pushList;
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

                PushMsgStatisticVO vo = new PushMsgStatisticVO();
                vo.setPushMode(entity.getPushMode());
                vo.setMsendedDate(entity.getMsendedDate());
                vo.setWeek(entity.getWeek());
                vo.setMonth(entity.getMonth());
                vo.setMnum(entity.getMnum());
                vo.setMstatus(entity.getMstatus().toString());
                voList.add(vo);
            }
        } catch (Exception e) {
            LOGGER.error("statistic init error:{}", e, e.getStackTrace());
        }
    }

    private List<PushMsgStatisticVO> supplement(List<PushMsgStatisticVO> list, String dateType) {

        Map<String, List<PushMsgStatisticVO>> listMap = new LinkedHashMap<>(50);
        for (PushMsgStatisticVO push : list) {
            if (listMap.containsKey(push.getMsendedDate())) {
                List<PushMsgStatisticVO> pushList = listMap.get(push.getMsendedDate());
                pushList.add(push);
            } else {
                List<PushMsgStatisticVO> pushList = new ArrayList<>();
                pushList.add(push);
                listMap.put(push.getMsendedDate(), pushList);
            }
        }

        //补齐当前分组无数据的情况
        Map<String, List<PushMsgStatisticVO>> supplementMap = addVacancy(listMap, dateType);

        List<PushMsgStatisticVO> pushAllList = new ArrayList<>();

        for (Map.Entry<String, List<PushMsgStatisticVO>> entry : supplementMap.entrySet()) {
            pushAllList.addAll(completion(entry.getValue()));
        }
        return pushAllList;
    }

    private List<PushMsgStatisticVO> completion(List<PushMsgStatisticVO> list) {

        List<DataDic> dataDics = (List<DataDic>) DataDicUtil.findByCatalog("PEP_PUSH_CHANNEL_TYPE");
        Map<String, PushMsgStatisticVO> pushMap = new HashMap<>(50);
        Set<String> pushSet = new HashSet<>();

        for (PushMsgStatisticVO push : list) {
            pushSet.add(push.getPushMode());
            if (!pushMap.containsKey(push.getPushMode())) {
                pushMap.put(push.getPushMode(), push);
            } else {
                pushMap.remove(push.getPushMode());
            }

        }

        for (DataDic dataDic : dataDics) {
            if (!pushSet.contains(dataDic.getCode())) {
                PushMsgStatisticVO pushVO = new PushMsgStatisticVO();
                pushVO.setMnum(0);
                pushVO.setMstatus(PushMsgStatus.UNSEND.name());
                pushVO.setPushMode(dataDic.getCode());
                pushVO.setMsendedDate(list.get(0).getMsendedDate());

                PushMsgStatisticVO pushVO2 = new PushMsgStatisticVO();
                pushVO2.setMnum(0);
                pushVO2.setMstatus(PushMsgStatus.SENDED.name());
                pushVO2.setPushMode(dataDic.getCode());
                pushVO2.setMsendedDate(list.get(0).getMsendedDate());
                list.add(pushVO);
                list.add(pushVO2);
            }
        }


        for (Map.Entry<String, PushMsgStatisticVO> entry : pushMap.entrySet()) {

            PushMsgStatisticVO pushVO = new PushMsgStatisticVO();
            pushVO.setMnum(0);
            if ("UNSEND".equals(entry.getValue().getMstatus())) {
                pushVO.setMstatus(PushMsgStatus.SENDED.name());
            } else {
                pushVO.setMstatus(PushMsgStatus.UNSEND.name());
            }

            pushVO.setPushMode(entry.getKey());
            pushVO.setMsendedDate(list.get(0).getMsendedDate());
            list.add(pushVO);
        }
        return list;
    }

    /**
     * 获取根据每周分组集合
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 每周分组集合(2018 - 07 - 23 ~ 2018 - 07 - 29)
     */
    private static List<String> getBetweenWeeks(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(PEPConstants.DEFAULT_DATE_FORMAT);
        List<Map<String, String>> result = new ArrayList<>();
        try {
            //定义起始日期
            Date start = sdf.parse(startDate);
            //定义结束日期
            Date end = sdf.parse(endDate);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            while (tempStart.before(tempEnd) || tempStart.equals(tempEnd)) {
                Map<String, String> map = new HashMap<>(10);
                int we = tempStart.get(Calendar.DAY_OF_WEEK);
                if (we == 2) {
                    map.put("mon", sdf.format(tempStart.getTime()));
                }
                //检测map是否为空
                if (map.isEmpty()) {
                    tempStart.add(Calendar.DAY_OF_YEAR, 1);
                } else {
                    tempStart.add(Calendar.DAY_OF_YEAR, 6);
                    map.put("week", sdf.format(tempStart.getTime()));
                    result.add(map);
                }
            }
        } catch (Exception e) {
            LOGGER.error("supplement error:{}", e, e.getStackTrace());
        }
        List<String> weekList = new ArrayList<>();
        for (Map<String, String> weekMap : result) {
            weekList.add(weekMap.get("mon") + "～" + weekMap.get("week"));
        }
        if (weekList.size() == WEEK_RANGE) {
            weekList.remove(0);
        }
        return weekList;
    }

    /**
     * 获取月份或天日期范围
     *
     * @param minDate 开始日期
     * @param maxDate 结束日期
     * @param month   是否按照月份显示
     * @return 按月显示月份(2018 - 08)
     */
    private List<String> getMonthOrDayBetween(String minDate, String maxDate, boolean month) {
        List<String> result = new ArrayList<>();
        //定义起始日期
        try {
            Date d1 = new SimpleDateFormat(PEPConstants.DEFAULT_DATE_FORMAT).parse(minDate);
            //定义结束日期
            Date d2 = new SimpleDateFormat(PEPConstants.DEFAULT_DATE_FORMAT).parse(maxDate);

            //定义日期实例
            Calendar dd = Calendar.getInstance();
            //设置日期起始时间
            dd.setTime(d1);
            //判断是否到结束日期
            while (dd.getTime().before(d2)) {
                if (month) {
                    SimpleDateFormat sdf = new SimpleDateFormat(PEPConstants.DEFAULT_MONTH_FORMAT);
                    String str = sdf.format(dd.getTime());
                    result.add(str);
                    dd.add(Calendar.MONTH, 1);
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat(PEPConstants.DEFAULT_DATE_FORMAT);
                    String str = sdf.format(dd.getTime());
                    result.add(str);
                    dd.add(Calendar.DATE, 1);
                }

            }
        } catch (Exception e) {
            LOGGER.error("supplement error:{}", e, e.getStackTrace());
        }
        if (month) {
            result.add(DateUtil.toString(new Date(), PEPConstants.DEFAULT_MONTH_FORMAT));
        }
        return result;
    }

    private Map<String, List<PushMsgStatisticVO>> addVacancy(Map<String, List<PushMsgStatisticVO>> listMap, String type) {
        List<String> dayRange = new ArrayList<>();
        if (type.equals(DATE_RANGE_DAY)) {
            Date start = DateUtil.addDay(new Date(), -6);
            String startStr = DateUtil.toString(start, PEPConstants.DEFAULT_DATE_FORMAT);
            Date end = DateUtil.addDay(new Date(), 1);
            String endStr = DateUtil.toString(end, PEPConstants.DEFAULT_DATE_FORMAT);
            dayRange = getMonthOrDayBetween(startStr, endStr, false);
        }

        if (type.equals(DATE_RANGE_WEEK)) {
            Date start = DateUtil.getDayOfWeek(DateUtil.addWeek(new Date(), -7), 1);
            String startStr = DateUtil.toString(start, PEPConstants.DEFAULT_DATE_FORMAT);
            Date end = new Date();
            String endStr = DateUtil.toString(end, PEPConstants.DEFAULT_DATE_FORMAT);
            dayRange = getBetweenWeeks(startStr, endStr);
        }

        if (type.equals(DATE_RANGE_MONTH)) {
            Date date = new Date();
            Date beginMonth = DateUtil.addMonth(date, -6);
            String startStr = DateUtil.toString(beginMonth, PEPConstants.DEFAULT_DATE_FORMAT);
            Date end = new Date();
            String endStr = DateUtil.toString(end, PEPConstants.DEFAULT_DATE_FORMAT);
            dayRange = getMonthOrDayBetween(startStr, endStr, true);

        }

        Map<String, List<PushMsgStatisticVO>> tempListMap = new LinkedHashMap<>();

        for (String range : dayRange) {
            if (!listMap.containsKey(range)) {
                PushMsgStatisticVO pushVO = new PushMsgStatisticVO();
                pushVO.setMnum(0);
                pushVO.setMstatus(PushMsgStatus.UNSEND.name());
                pushVO.setPushMode("apns");
                pushVO.setMsendedDate(range);
                List<PushMsgStatisticVO> pushMsgStatisticVOS = new ArrayList<>();
                pushMsgStatisticVOS.add(pushVO);
                tempListMap.put(range, pushMsgStatisticVOS);
            } else {
                tempListMap.put(range, listMap.get(range));
            }
        }
        return tempListMap;

    }

}
