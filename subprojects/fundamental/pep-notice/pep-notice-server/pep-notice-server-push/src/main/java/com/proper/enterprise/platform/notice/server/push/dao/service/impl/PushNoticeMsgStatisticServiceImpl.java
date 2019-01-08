package com.proper.enterprise.platform.notice.server.push.dao.service.impl;

import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.model.App;
import com.proper.enterprise.platform.notice.server.api.service.AppDaoService;
import com.proper.enterprise.platform.notice.server.push.dao.entity.PushNoticeMsgStatisticEntity;
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushNoticeMsgStatisticRepository;
import com.proper.enterprise.platform.notice.server.push.dao.service.PushNoticeMsgStatisticService;
import com.proper.enterprise.platform.notice.server.push.enums.PushDataAnalysisDateRangeEnum;
import com.proper.enterprise.platform.notice.server.push.vo.AppVO;
import com.proper.enterprise.platform.notice.server.push.vo.PushMsgPieDataVO;
import com.proper.enterprise.platform.notice.server.push.vo.PushNoticeMsgPieVO;
import com.proper.enterprise.platform.notice.server.push.vo.PushServiceDataAnalysisVO;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class PushNoticeMsgStatisticServiceImpl implements PushNoticeMsgStatisticService {

    private PushNoticeMsgStatisticRepository pushNoticeMsgStatisticRepository;

    private AppDaoService appDaoService;

    private CoreProperties coreProperties;

    @Autowired
    public PushNoticeMsgStatisticServiceImpl(PushNoticeMsgStatisticRepository pushNoticeMsgStatisticRepository,
                                             AppDaoService appDaoService,
                                             CoreProperties coreProperties) {
        this.pushNoticeMsgStatisticRepository = pushNoticeMsgStatisticRepository;
        this.appDaoService = appDaoService;
        this.coreProperties = coreProperties;
    }

    @Override
    public List<PushNoticeMsgStatisticEntity> getPushStatistic(Date startDate, Date endDate) {
        List<Object[]> statisticList = pushNoticeMsgStatisticRepository.getPushStatistic(
                DateUtil.toString(DateUtil.toLocalDateTime(startDate), coreProperties.getDefaultDateFormat()),
                DateUtil.toString(DateUtil.toLocalDateTime(endDate), coreProperties.getDefaultDateFormat()));
        List<PushNoticeMsgStatisticEntity> entityList = new ArrayList<>();
        if (CollectionUtil.isEmpty(statisticList)) {
            return new ArrayList<>();
        }
        for (Object[] rows : statisticList) {
            PushNoticeMsgStatisticEntity entity = new PushNoticeMsgStatisticEntity();
            entity.setAppKey((String) rows[0]);
            entity.setPushChannel(PushChannelEnum.valueOf((String) rows[1]));
            LocalDate localDate = DateUtil.toLocalDate((String) rows[2], coreProperties.getDefaultDateFormat());
            Date sendDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            entity.setSendDate(DateUtil.toString(DateUtil.toLocalDateTime(sendDate), coreProperties.getDefaultDateFormat()));
            entity.setWeek(buildWeekRange(sendDate));
            entity.setMonth(DateUtil.toString(DateUtil.toLocalDateTime(sendDate), coreProperties.getDefaultMonthFormat()));
            entity.setStatus(NoticeStatus.valueOf((String) rows[3]));
            String obj4 = rows[4].toString();
            entity.setMsgCount(Integer.valueOf(obj4));
            entityList.add(entity);
        }
        return entityList;
    }

    @Override
    public void saveAll(List<PushNoticeMsgStatisticEntity> pushMsgStatistics) {
        pushNoticeMsgStatisticRepository.saveAll(pushMsgStatistics);
    }

    @Override
    public void deleteBySendDate(Date sendDate) {
        pushNoticeMsgStatisticRepository.deleteBySendDate(DateUtil.toString(DateUtil.toLocalDateTime(sendDate),
                coreProperties.getDefaultDateFormat()));
    }

    @Override
    public List<PushServiceDataAnalysisVO> findByDateTypeAndAppKey(Date startDate, PushDataAnalysisDateRangeEnum dateType, String appKey) {
        switch (dateType) {
            case DAY:
                return findPushStatisticByDay(startDate, appKey);
            case WEEK:
                return findPushStatisticByWeek(startDate, appKey);
            case MONTH:
                return findPushStatisticByMonth(startDate, appKey);
            default:
                return null;
        }
    }

    @Override
    public void saveStatisticSomeday(String date) {
        LocalDateTime dateStart = DateUtil.toLocalDateTime(date, coreProperties.getDefaultDateFormat());
        LocalDateTime dateEnd = DateUtil.addDay(dateStart, 1);
        List<PushNoticeMsgStatisticEntity> pushNoticeMsgStatistics = this.getPushStatistic(DateUtil.toDate(dateStart), DateUtil.toDate(dateEnd));
        this.deleteBySendDate(DateUtil.toDate(dateStart));
        this.saveAll(pushNoticeMsgStatistics);
    }

    @Override
    public PushNoticeMsgPieVO findPieDataByDateAndAppKey(String startDate, String endDate, String appKey) {
        if (StringUtil.isNull(appKey)) {
            throw new ErrMsgException("appkey is not null");
        }
        List<String> appKeys = Arrays.asList(appKey.split(","));
        List<Object[]> pieItems = pushNoticeMsgStatisticRepository.findPieItems(startDate, endDate, appKeys);
        List<PushMsgPieDataVO> pushMsgPieDataVOS = new ArrayList<>();
        List<String> appkeys = new ArrayList<>();
        //获取推送总数
        for (Object[] pieItem : pieItems) {
            PushMsgPieDataVO pushMsgPieDataVO = new PushMsgPieDataVO();
            pushMsgPieDataVO.setAppKey(pieItem[1].toString());
            pushMsgPieDataVO.setTotalNum(Integer.valueOf(pieItem[0].toString()));
            appkeys.add(pieItem[1].toString());
            pushMsgPieDataVOS.add(pushMsgPieDataVO);
        }
        //获取推送成功和失败数量
        List<Object[]> pieData = pushNoticeMsgStatisticRepository.getPieData(startDate, endDate, appKeys);
        for (Object[] pieDatum : pieData) {
            for (PushMsgPieDataVO pieDataVO : pushMsgPieDataVOS) {
                if (pieDatum[1].toString().equals(pieDataVO.getAppKey())) {
                    if (NoticeStatus.SUCCESS.name().equals(pieDatum[2].toString())) {
                        pieDataVO.setSuccessNum(Integer.valueOf(pieDatum[0].toString()));
                    } else {
                        pieDataVO.setFailNum(Integer.valueOf(pieDatum[0].toString()));
                    }
                }

            }

        }
        List<App> apps = appDaoService.findByApp();
        for (App app : apps) {
            for (PushMsgPieDataVO pushMsgPieDataVO : pushMsgPieDataVOS) {
                if (pushMsgPieDataVO.getAppKey().equals(app.getAppKey())) {
                    pushMsgPieDataVO.setAppName(app.getAppName());
                }
            }
        }

        //排序
        pushMsgPieDataVOS.sort((o1, o2) -> {
            if (o1.getTotalNum() < o2.getTotalNum()) {
                return 1;
            } else if (o1.getTotalNum() > o2.getTotalNum()) {
                return -1;
            } else {
                return o1.getAppKey().compareTo(o2.getAppKey());
            }
        });
        PushNoticeMsgPieVO msgPieVO = new PushNoticeMsgPieVO();
        msgPieVO.setPieData(pushMsgPieDataVOS);
        msgPieVO.setAppKeyOrder(appkeys);
        return msgPieVO;
    }

    @Override
    public List<PushMsgPieDataVO> findPieItems(String startDate, String endDate) {
        List<PushMsgPieDataVO> pieDataVOS = new ArrayList<>();
        List<App> apps = appDaoService.findByApp();
        for (App app : apps) {
            PushMsgPieDataVO pieDataVO = new PushMsgPieDataVO();
            pieDataVO.setAppKey(app.getAppKey());
            pieDataVO.setAppName(app.getAppName());
            pieDataVOS.add(pieDataVO);
        }
        List<Object[]> findPieItems = pushNoticeMsgStatisticRepository.findPieItems(startDate, endDate);
        for (Object[] findPieItem : findPieItems) {
            for (PushMsgPieDataVO pieDataVO : pieDataVOS) {
                if (findPieItem[1].toString().equals(pieDataVO.getAppKey())) {
                    pieDataVO.setTotalNum(Integer.valueOf(findPieItem[0].toString()));
                }
            }
        }
        return pieDataVOS;
    }

    @Override
    public DataTrunk<App> findApp(String appKey, String appName, String appDesc, Boolean enable, PageRequest pageRequest) {
        DataTrunk<App> result = appDaoService.findAll(appKey, appName, appDesc, enable, pageRequest);
        String endDate = DateUtil.toLocalDateString(LocalDateTime.now());
        String startDate = DateUtil.toLocalDateString(DateUtil.addDay(LocalDateTime.now(), -7));
        List<Object[]> itemsTotalNum = pushNoticeMsgStatisticRepository.getItemsTotalNum(startDate, endDate);
        List<AppVO> appVOs = new ArrayList<>(BeanUtil.convert(result.getData(), AppVO.class));
        for (AppVO app : appVOs) {
            for (Object[] item : itemsTotalNum) {
                if (app.getAppKey().equals(item[1].toString())) {
                    app.setChannelCount(Integer.valueOf(item[0].toString()));
                }
            }
        }
        result.setData(new ArrayList<>(appVOs));
        return result;
    }


    /**
     * 根据天数统计 基础日期及前六天数据
     *
     * @param startDate 基础日期
     * @param appKey    应用唯一标识
     * @return 统计结果
     */
    private List<PushServiceDataAnalysisVO> findPushStatisticByDay(Date startDate, String appKey) {
        Map<String, PushServiceDataAnalysisVO> pushServiceDataAnalysisMap = new HashMap<>(16);
        //构造每天视图 共七天
        String oneDate = DateUtil.toString(DateUtil.toLocalDateTime(startDate), coreProperties.getDefaultDateFormat());
        PushServiceDataAnalysisVO one = new PushServiceDataAnalysisVO();
        one.setDataAnalysisDate(oneDate);
        pushServiceDataAnalysisMap.put(oneDate, one);

        String twoDate = DateUtil.toString(DateUtil.addDay(DateUtil.toLocalDateTime(startDate), -1), coreProperties.getDefaultDateFormat());
        PushServiceDataAnalysisVO two = new PushServiceDataAnalysisVO();
        two.setDataAnalysisDate(twoDate);
        pushServiceDataAnalysisMap.put(twoDate, two);

        String threeDate = DateUtil.toString(DateUtil.addDay(DateUtil.toLocalDateTime(startDate), -2), coreProperties.getDefaultDateFormat());
        PushServiceDataAnalysisVO three = new PushServiceDataAnalysisVO();
        three.setDataAnalysisDate(threeDate);
        pushServiceDataAnalysisMap.put(threeDate, three);

        String fourDate = DateUtil.toString(DateUtil.addDay(DateUtil.toLocalDateTime(startDate), -3), coreProperties.getDefaultDateFormat());
        PushServiceDataAnalysisVO four = new PushServiceDataAnalysisVO();
        four.setDataAnalysisDate(fourDate);
        pushServiceDataAnalysisMap.put(fourDate, four);

        String fiveDate = DateUtil.toString(DateUtil.addDay(DateUtil.toLocalDateTime(startDate), -4), coreProperties.getDefaultDateFormat());
        PushServiceDataAnalysisVO five = new PushServiceDataAnalysisVO();
        five.setDataAnalysisDate(fiveDate);
        pushServiceDataAnalysisMap.put(fiveDate, five);

        String sixDate = DateUtil.toString(DateUtil.addDay(DateUtil.toLocalDateTime(startDate), -5), coreProperties.getDefaultDateFormat());
        PushServiceDataAnalysisVO six = new PushServiceDataAnalysisVO();
        six.setDataAnalysisDate(sixDate);
        pushServiceDataAnalysisMap.put(sixDate, six);

        String sevenDate = DateUtil.toString(DateUtil.addDay(DateUtil.toLocalDateTime(startDate), -6), coreProperties.getDefaultDateFormat());
        PushServiceDataAnalysisVO seven = new PushServiceDataAnalysisVO();
        seven.setDataAnalysisDate(sevenDate);
        pushServiceDataAnalysisMap.put(sevenDate, seven);

        //获取七天内统计数据
        Date sendDate = DateUtil.toDate(DateUtil.addDay(DateUtil.toLocalDateTime(startDate), -6));
        String sendDateStr = DateUtil.toString(DateUtil.toLocalDateTime(sendDate), coreProperties.getDefaultDateFormat());
        List<PushNoticeMsgStatisticEntity> result;
        if (StringUtil.isEmpty(appKey)) {
            result = convert(pushNoticeMsgStatisticRepository.findAllGroupDay(sendDateStr));
        } else {
            result = convert(pushNoticeMsgStatisticRepository.findAllGroupDay(appKey, sendDateStr));
        }

        //根据统计数据 填充天数视图
        buildDataAnalysisView(result, pushServiceDataAnalysisMap);

        List<PushServiceDataAnalysisVO> list = new ArrayList<>();
        list.add(seven);
        list.add(six);
        list.add(five);
        list.add(four);
        list.add(three);
        list.add(two);
        list.add(one);
        return list;
    }


    /**
     * 根据周统计 基础日期及前六周数据
     *
     * @param startDate 基础日期
     * @param appKey    应用唯一标识
     * @return 统计结果
     */
    private List<PushServiceDataAnalysisVO> findPushStatisticByWeek(Date startDate, String appKey) {

        Map<String, PushServiceDataAnalysisVO> pushServiceDataAnalysisMap = new HashMap<>(16);
        //构造每周视图 共七周
        String oneDate = buildWeekRange(startDate);

        PushServiceDataAnalysisVO one = new PushServiceDataAnalysisVO();
        one.setDataAnalysisDate(oneDate);
        pushServiceDataAnalysisMap.put(oneDate, one);

        String twoDate = buildWeekRange(DateUtil.toDate(DateUtil.addWeek(DateUtil.toLocalDateTime(startDate), -1)));
        PushServiceDataAnalysisVO two = new PushServiceDataAnalysisVO();
        two.setDataAnalysisDate(twoDate);
        pushServiceDataAnalysisMap.put(twoDate, two);

        String threeDate = buildWeekRange(DateUtil.toDate(DateUtil.addWeek(DateUtil.toLocalDateTime(startDate), -2)));
        PushServiceDataAnalysisVO three = new PushServiceDataAnalysisVO();
        three.setDataAnalysisDate(threeDate);
        pushServiceDataAnalysisMap.put(threeDate, three);

        String fourDate = buildWeekRange(DateUtil.toDate(DateUtil.addWeek(DateUtil.toLocalDateTime(startDate), -3)));
        PushServiceDataAnalysisVO four = new PushServiceDataAnalysisVO();
        four.setDataAnalysisDate(fourDate);
        pushServiceDataAnalysisMap.put(fourDate, four);

        String fiveDate = buildWeekRange(DateUtil.toDate(DateUtil.addWeek(DateUtil.toLocalDateTime(startDate), -4)));
        PushServiceDataAnalysisVO five = new PushServiceDataAnalysisVO();
        five.setDataAnalysisDate(fiveDate);
        pushServiceDataAnalysisMap.put(fiveDate, five);

        String sixDate = buildWeekRange(DateUtil.toDate(DateUtil.addWeek(DateUtil.toLocalDateTime(startDate), -5)));
        PushServiceDataAnalysisVO six = new PushServiceDataAnalysisVO();
        six.setDataAnalysisDate(sixDate);
        pushServiceDataAnalysisMap.put(sixDate, six);

        String sevenDate = buildWeekRange(DateUtil.toDate(DateUtil.addWeek(DateUtil.toLocalDateTime(startDate), -6)));
        PushServiceDataAnalysisVO seven = new PushServiceDataAnalysisVO();
        seven.setDataAnalysisDate(sevenDate);
        pushServiceDataAnalysisMap.put(sevenDate, seven);


        //获取七天内统计数据
        LocalDateTime sendDate = DateUtil.addWeek(DateUtil.toLocalDateTime(startDate), -6);
        String sendDateStr = DateUtil.toString(sendDate, coreProperties.getDefaultDateFormat());
        List<PushNoticeMsgStatisticEntity> result;
        if (StringUtil.isEmpty(appKey)) {
            result = convert(pushNoticeMsgStatisticRepository.findAllGroupWeek(sendDateStr));
        } else {
            result = convert(pushNoticeMsgStatisticRepository.findAllGroupWeek(appKey, sendDateStr));
        }

        //根据统计数据 填充每周视图
        buildDataAnalysisView(result, pushServiceDataAnalysisMap);

        List<PushServiceDataAnalysisVO> list = new ArrayList<>();
        list.add(seven);
        list.add(six);
        list.add(five);
        list.add(four);
        list.add(three);
        list.add(two);
        list.add(one);
        return list;
    }

    /**
     * 根据月统计 基础日期及前六月数据
     *
     * @param startDate 基础日期
     * @param appKey    应用唯一标识
     * @return 统计结果
     */
    public List<PushServiceDataAnalysisVO> findPushStatisticByMonth(Date startDate, String appKey) {
        Map<String, PushServiceDataAnalysisVO> pushServiceDataAnalysisMap = new HashMap<>(16);
        //构造每月视图 共七月
        String oneDate = DateUtil.toString(DateUtil.toLocalDateTime(startDate), coreProperties.getDefaultMonthFormat());
        PushServiceDataAnalysisVO one = new PushServiceDataAnalysisVO();
        one.setDataAnalysisDate(oneDate);
        pushServiceDataAnalysisMap.put(oneDate, one);

        String twoDate = DateUtil.toString(DateUtil.addMonth(DateUtil.toLocalDateTime(startDate), -1), coreProperties.getDefaultMonthFormat());
        PushServiceDataAnalysisVO two = new PushServiceDataAnalysisVO();
        two.setDataAnalysisDate(twoDate);
        pushServiceDataAnalysisMap.put(twoDate, two);

        String threeDate = DateUtil.toString(DateUtil.addMonth(DateUtil.toLocalDateTime(startDate), -2), coreProperties.getDefaultMonthFormat());
        PushServiceDataAnalysisVO three = new PushServiceDataAnalysisVO();
        three.setDataAnalysisDate(threeDate);
        pushServiceDataAnalysisMap.put(threeDate, three);

        String fourDate = DateUtil.toString(DateUtil.addMonth(DateUtil.toLocalDateTime(startDate), -3), coreProperties.getDefaultMonthFormat());
        PushServiceDataAnalysisVO four = new PushServiceDataAnalysisVO();
        four.setDataAnalysisDate(fourDate);
        pushServiceDataAnalysisMap.put(fourDate, four);

        String fiveDate = DateUtil.toString(DateUtil.addMonth(DateUtil.toLocalDateTime(startDate), -4), coreProperties.getDefaultMonthFormat());
        PushServiceDataAnalysisVO five = new PushServiceDataAnalysisVO();
        five.setDataAnalysisDate(fiveDate);
        pushServiceDataAnalysisMap.put(fiveDate, five);

        String sixDate = DateUtil.toString(DateUtil.addMonth(DateUtil.toLocalDateTime(startDate), -5), coreProperties.getDefaultMonthFormat());
        PushServiceDataAnalysisVO six = new PushServiceDataAnalysisVO();
        six.setDataAnalysisDate(sixDate);
        pushServiceDataAnalysisMap.put(sixDate, six);

        String sevenDate = DateUtil.toString(DateUtil.addMonth(DateUtil.toLocalDateTime(startDate), -6), coreProperties.getDefaultMonthFormat());
        PushServiceDataAnalysisVO seven = new PushServiceDataAnalysisVO();
        seven.setDataAnalysisDate(sevenDate);
        pushServiceDataAnalysisMap.put(sevenDate, seven);

        //获取七月内统计数据
        LocalDateTime beginMonth = DateUtil.addMonth(DateUtil.toLocalDateTime(startDate), -7);
        String sendDateStr = DateUtil.toString(beginMonth, coreProperties.getDefaultMonthFormat());
        List<PushNoticeMsgStatisticEntity> result;
        if (StringUtil.isEmpty(appKey)) {
            result = convert(pushNoticeMsgStatisticRepository.findAllGroupMonth(sendDateStr));
        } else {
            result = convert(pushNoticeMsgStatisticRepository.findAllGroupMonth(appKey, sendDateStr));
        }

        //根据统计数据 填充每月视图
        buildDataAnalysisView(result, pushServiceDataAnalysisMap);

        List<PushServiceDataAnalysisVO> list = new ArrayList<>();
        list.add(seven);
        list.add(six);
        list.add(five);
        list.add(four);
        list.add(three);
        list.add(two);
        list.add(one);
        return list;
    }

    /**
     * 根据统计结果和视图集合 构造视图对象
     * 引用传递修改存放于视图集合中的视图对象
     *
     * @param result                     统计结果
     * @param pushServiceDataAnalysisMap 视图集合
     */
    private void buildDataAnalysisView(List<PushNoticeMsgStatisticEntity> result, Map<String, PushServiceDataAnalysisVO> pushServiceDataAnalysisMap) {

        for (PushNoticeMsgStatisticEntity pushNoticeMsgStatisticEntity : result) {
            PushServiceDataAnalysisVO pushServiceDataAnalysisVO = pushServiceDataAnalysisMap.get(pushNoticeMsgStatisticEntity.getSendDate());
            switch (pushNoticeMsgStatisticEntity.getPushChannel()) {
                case XIAOMI:
                    if (NoticeStatus.SUCCESS == pushNoticeMsgStatisticEntity.getStatus()) {
                        pushServiceDataAnalysisVO.getXiaomiDataAnalysis().setSuccessCount(pushNoticeMsgStatisticEntity.getMsgCount());
                    }
                    if (NoticeStatus.FAIL == pushNoticeMsgStatisticEntity.getStatus()) {
                        pushServiceDataAnalysisVO.getXiaomiDataAnalysis().setFailCount(pushNoticeMsgStatisticEntity.getMsgCount());
                    }
                    break;
                case HUAWEI:
                    if (NoticeStatus.SUCCESS == pushNoticeMsgStatisticEntity.getStatus()) {
                        pushServiceDataAnalysisVO.getHuaweiDataAnalysis().setSuccessCount(pushNoticeMsgStatisticEntity.getMsgCount());
                    }
                    if (NoticeStatus.FAIL == pushNoticeMsgStatisticEntity.getStatus()) {
                        pushServiceDataAnalysisVO.getHuaweiDataAnalysis().setFailCount(pushNoticeMsgStatisticEntity.getMsgCount());
                    }
                    break;
                case APNS:
                    if (NoticeStatus.SUCCESS == pushNoticeMsgStatisticEntity.getStatus()) {
                        pushServiceDataAnalysisVO.getIosDataAnalysis().setSuccessCount(pushNoticeMsgStatisticEntity.getMsgCount());
                    }
                    if (NoticeStatus.FAIL == pushNoticeMsgStatisticEntity.getStatus()) {
                        pushServiceDataAnalysisVO.getIosDataAnalysis().setFailCount(pushNoticeMsgStatisticEntity.getMsgCount());
                    }
                    break;
                default:
                    break;
            }

        }
    }

    /**
     * 根据时间 获取时间周范围
     *
     * @param date 给定时间
     * @return 时间周范围 例如:2018-07-02~2018-07-08
     */
    private String buildWeekRange(Date date) {

        StringBuilder sb = new StringBuilder();
        LocalDateTime mondayOfThisWeek = DateUtil.getDayOfWeek(DateUtil.toLocalDateTime(date), 1);
        LocalDateTime sundayOfThisWeek = DateUtil.getDayOfWeek(DateUtil.toLocalDateTime(date), 7);
        sb.append(DateUtil.toString(mondayOfThisWeek, coreProperties.getDefaultDateFormat()))
                .append("～").append(DateUtil.toString(sundayOfThisWeek, coreProperties.getDefaultDateFormat()));
        return sb.toString();
    }

    private List<PushNoticeMsgStatisticEntity> convert(List<Object[]> list) {
        List<PushNoticeMsgStatisticEntity> pushNoticeMsgStatistics = new ArrayList<>();
        if (CollectionUtil.isEmpty(list)) {
            return pushNoticeMsgStatistics;
        }
        for (Object row : list) {
            Object[] cells = (Object[]) row;
            PushNoticeMsgStatisticEntity pushNoticeMsgStatisticEntity = new PushNoticeMsgStatisticEntity();
            pushNoticeMsgStatisticEntity.setMsgCount(Integer.valueOf(cells[0].toString()));
            pushNoticeMsgStatisticEntity.setStatus(NoticeStatus.valueOf(cells[1].toString()));
            pushNoticeMsgStatisticEntity.setPushChannel(PushChannelEnum.valueOf(cells[2].toString()));
            pushNoticeMsgStatisticEntity.setSendDate(cells[3].toString());
            pushNoticeMsgStatistics.add(pushNoticeMsgStatisticEntity);
        }
        return pushNoticeMsgStatistics;
    }

}
