package com.proper.enterprise.platform.notice.server.push.dao.service.impl;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.model.App;
import com.proper.enterprise.platform.notice.server.api.service.AppDaoService;
import com.proper.enterprise.platform.notice.server.push.dao.entity.PushNoticeMsgStatisticEntity;
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushNoticeMsgStatisticRepository;
import com.proper.enterprise.platform.notice.server.push.dao.service.PushNoticeMsgStatisticService;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.push.enums.PushDataAnalysisDateRangeEnum;
import com.proper.enterprise.platform.notice.server.push.vo.PushMsgPieDataVO;
import com.proper.enterprise.platform.notice.server.push.vo.PushNoticeMsgPieVO;
import com.proper.enterprise.platform.notice.server.push.vo.PushServiceDataAnalysisVO;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PushNoticeMsgStatisticServiceImpl implements PushNoticeMsgStatisticService {

    private PushNoticeMsgStatisticRepository pushNoticeMsgStatisticRepository;

    private AppDaoService appDaoService;

    @Autowired
    public PushNoticeMsgStatisticServiceImpl(PushNoticeMsgStatisticRepository pushNoticeMsgStatisticRepository, AppDaoService appDaoService) {
        this.pushNoticeMsgStatisticRepository = pushNoticeMsgStatisticRepository;
        this.appDaoService = appDaoService;
    }

    @Override
    public List<PushNoticeMsgStatisticEntity> getPushStatistic(Date startDate, Date endDate) {
        List<Object[]> statisticList = pushNoticeMsgStatisticRepository.getPushStatistic(
            DateUtil.toString(startDate, PEPConstants.DEFAULT_DATE_FORMAT),
            DateUtil.toString(endDate, PEPConstants.DEFAULT_DATE_FORMAT));
        List<PushNoticeMsgStatisticEntity> entityList = new ArrayList<>();
        if (CollectionUtil.isEmpty(statisticList)) {
            return new ArrayList<>();
        }
        for (Object[] rows : statisticList) {
            PushNoticeMsgStatisticEntity entity = new PushNoticeMsgStatisticEntity();
            entity.setAppKey((String) rows[0]);
            entity.setPushChannel(PushChannelEnum.valueOf((String) rows[1]));
            Date sendDate = DateUtil.toDate((String) rows[2], PEPConstants.DEFAULT_DATE_FORMAT);
            entity.setSendDate(DateUtil.toString(sendDate, PEPConstants.DEFAULT_DATE_FORMAT));
            entity.setWeek(buildWeekRange(sendDate));
            entity.setMonth(DateUtil.toString(sendDate, PEPConstants.DEFAULT_MONTH_FORMAT));
            entity.setStatus(NoticeStatus.valueOf((String) rows[3]));
            String obj4 = rows[4].toString();
            entity.setMsgCount(Integer.valueOf(obj4));
            entityList.add(entity);
        }
        return entityList;
    }

    @Override
    public void saveAll(List<PushNoticeMsgStatisticEntity> pushMsgStatistics) {
        pushNoticeMsgStatisticRepository.save(pushMsgStatistics);
    }

    @Override
    public void deleteBySendDate(Date sendDate) {
        pushNoticeMsgStatisticRepository.deleteBySendDate(DateUtil.toString(sendDate, PEPConstants.DEFAULT_DATE_FORMAT));
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
        Date dateStart = DateUtil.toDate(date, PEPConstants.DEFAULT_DATE_FORMAT);
        Date dateEnd = DateUtil.addDay(dateStart, 1);
        List<PushNoticeMsgStatisticEntity> pushNoticeMsgStatistics = this.getPushStatistic(dateStart, dateEnd);
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
                    if (NoticeStatus.SUCCESS.name().equals(pieDatum[1].toString())) {
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
        String oneDate = DateUtil.toString(startDate, PEPConstants.DEFAULT_DATE_FORMAT);
        PushServiceDataAnalysisVO one = new PushServiceDataAnalysisVO();
        one.setDataAnalysisDate(oneDate);
        pushServiceDataAnalysisMap.put(oneDate, one);

        String twoDate = DateUtil.toString(DateUtil.addDay(startDate, -1), PEPConstants.DEFAULT_DATE_FORMAT);
        PushServiceDataAnalysisVO two = new PushServiceDataAnalysisVO();
        two.setDataAnalysisDate(twoDate);
        pushServiceDataAnalysisMap.put(twoDate, two);

        String threeDate = DateUtil.toString(DateUtil.addDay(startDate, -2), PEPConstants.DEFAULT_DATE_FORMAT);
        PushServiceDataAnalysisVO three = new PushServiceDataAnalysisVO();
        three.setDataAnalysisDate(threeDate);
        pushServiceDataAnalysisMap.put(threeDate, three);

        String fourDate = DateUtil.toString(DateUtil.addDay(startDate, -3), PEPConstants.DEFAULT_DATE_FORMAT);
        PushServiceDataAnalysisVO four = new PushServiceDataAnalysisVO();
        four.setDataAnalysisDate(fourDate);
        pushServiceDataAnalysisMap.put(fourDate, four);

        String fiveDate = DateUtil.toString(DateUtil.addDay(startDate, -4), PEPConstants.DEFAULT_DATE_FORMAT);
        PushServiceDataAnalysisVO five = new PushServiceDataAnalysisVO();
        five.setDataAnalysisDate(fiveDate);
        pushServiceDataAnalysisMap.put(fiveDate, five);

        String sixDate = DateUtil.toString(DateUtil.addDay(startDate, -5), PEPConstants.DEFAULT_DATE_FORMAT);
        PushServiceDataAnalysisVO six = new PushServiceDataAnalysisVO();
        six.setDataAnalysisDate(sixDate);
        pushServiceDataAnalysisMap.put(sixDate, six);

        String sevenDate = DateUtil.toString(DateUtil.addDay(startDate, -6), PEPConstants.DEFAULT_DATE_FORMAT);
        PushServiceDataAnalysisVO seven = new PushServiceDataAnalysisVO();
        seven.setDataAnalysisDate(sevenDate);
        pushServiceDataAnalysisMap.put(sevenDate, seven);

        //获取七天内统计数据
        Date sendDate = DateUtil.addDay(startDate, -6);
        String sendDateStr = DateUtil.toString(sendDate, PEPConstants.DEFAULT_DATE_FORMAT);
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

        String twoDate = buildWeekRange(DateUtil.addWeek(startDate, -1));
        PushServiceDataAnalysisVO two = new PushServiceDataAnalysisVO();
        two.setDataAnalysisDate(twoDate);
        pushServiceDataAnalysisMap.put(twoDate, two);

        String threeDate = buildWeekRange(DateUtil.addWeek(startDate, -2));
        PushServiceDataAnalysisVO three = new PushServiceDataAnalysisVO();
        three.setDataAnalysisDate(threeDate);
        pushServiceDataAnalysisMap.put(threeDate, three);

        String fourDate = buildWeekRange(DateUtil.addWeek(startDate, -3));
        PushServiceDataAnalysisVO four = new PushServiceDataAnalysisVO();
        four.setDataAnalysisDate(fourDate);
        pushServiceDataAnalysisMap.put(fourDate, four);

        String fiveDate = buildWeekRange(DateUtil.addWeek(startDate, -4));
        PushServiceDataAnalysisVO five = new PushServiceDataAnalysisVO();
        five.setDataAnalysisDate(fiveDate);
        pushServiceDataAnalysisMap.put(fiveDate, five);

        String sixDate = buildWeekRange(DateUtil.addWeek(startDate, -5));
        PushServiceDataAnalysisVO six = new PushServiceDataAnalysisVO();
        six.setDataAnalysisDate(sixDate);
        pushServiceDataAnalysisMap.put(sixDate, six);

        String sevenDate = buildWeekRange(DateUtil.addWeek(startDate, -6));
        PushServiceDataAnalysisVO seven = new PushServiceDataAnalysisVO();
        seven.setDataAnalysisDate(sevenDate);
        pushServiceDataAnalysisMap.put(sevenDate, seven);


        //获取七天内统计数据
        Date sendDate = DateUtil.addWeek(startDate, -6);
        String sendDateStr = DateUtil.toString(sendDate, PEPConstants.DEFAULT_DATE_FORMAT);
        List<PushNoticeMsgStatisticEntity> result;
        if (StringUtil.isEmpty(appKey)) {
            result = convert(pushNoticeMsgStatisticRepository.findAllGroupWeek(sendDateStr));
        } else {
            result = convert(pushNoticeMsgStatisticRepository.findAllGroupWeek(appKey, sendDateStr));
        }

        //根据统计数据 填充每周视图
        buildDataAnalysisView(result, pushServiceDataAnalysisMap);

        List<PushServiceDataAnalysisVO> list = new ArrayList<>();
        list.add(one);
        list.add(two);
        list.add(three);
        list.add(four);
        list.add(five);
        list.add(six);
        list.add(seven);
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
        String oneDate = DateUtil.toString(startDate, PEPConstants.DEFAULT_MONTH_FORMAT);
        PushServiceDataAnalysisVO one = new PushServiceDataAnalysisVO();
        one.setDataAnalysisDate(oneDate);
        pushServiceDataAnalysisMap.put(oneDate, one);

        String twoDate = DateUtil.toString(DateUtil.addMonth(startDate, -1), PEPConstants.DEFAULT_MONTH_FORMAT);
        PushServiceDataAnalysisVO two = new PushServiceDataAnalysisVO();
        two.setDataAnalysisDate(twoDate);
        pushServiceDataAnalysisMap.put(twoDate, two);

        String threeDate = DateUtil.toString(DateUtil.addMonth(startDate, -2), PEPConstants.DEFAULT_MONTH_FORMAT);
        PushServiceDataAnalysisVO three = new PushServiceDataAnalysisVO();
        three.setDataAnalysisDate(threeDate);
        pushServiceDataAnalysisMap.put(threeDate, three);

        String fourDate = DateUtil.toString(DateUtil.addMonth(startDate, -3), PEPConstants.DEFAULT_MONTH_FORMAT);
        PushServiceDataAnalysisVO four = new PushServiceDataAnalysisVO();
        four.setDataAnalysisDate(fourDate);
        pushServiceDataAnalysisMap.put(fourDate, four);

        String fiveDate = DateUtil.toString(DateUtil.addMonth(startDate, -4), PEPConstants.DEFAULT_MONTH_FORMAT);
        PushServiceDataAnalysisVO five = new PushServiceDataAnalysisVO();
        five.setDataAnalysisDate(fiveDate);
        pushServiceDataAnalysisMap.put(fiveDate, five);

        String sixDate = DateUtil.toString(DateUtil.addMonth(startDate, -5), PEPConstants.DEFAULT_MONTH_FORMAT);
        PushServiceDataAnalysisVO six = new PushServiceDataAnalysisVO();
        six.setDataAnalysisDate(sixDate);
        pushServiceDataAnalysisMap.put(sixDate, six);

        String sevenDate = DateUtil.toString(DateUtil.addMonth(startDate, -6), PEPConstants.DEFAULT_MONTH_FORMAT);
        PushServiceDataAnalysisVO seven = new PushServiceDataAnalysisVO();
        seven.setDataAnalysisDate(sevenDate);
        pushServiceDataAnalysisMap.put(sevenDate, seven);

        //获取七月内统计数据
        Date beginMonth = DateUtil.addMonth(startDate, -7);
        String sendDateStr = DateUtil.toString(beginMonth, PEPConstants.DEFAULT_MONTH_FORMAT);
        List<PushNoticeMsgStatisticEntity> result;
        if (StringUtil.isEmpty(appKey)) {
            result = convert(pushNoticeMsgStatisticRepository.findAllGroupMonth(sendDateStr));
        } else {
            result = convert(pushNoticeMsgStatisticRepository.findAllGroupMonth(appKey, sendDateStr));
        }

        //根据统计数据 填充每月视图
        buildDataAnalysisView(result, pushServiceDataAnalysisMap);

        List<PushServiceDataAnalysisVO> list = new ArrayList<>();
        list.add(one);
        list.add(two);
        list.add(three);
        list.add(four);
        list.add(five);
        list.add(six);
        list.add(seven);
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
                case IOS:
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
        Date mondayOfThisWeek = DateUtil.getDayOfWeek(date, 1);
        Date sundayOfThisWeek = DateUtil.getDayOfWeek(date, 7);
        sb.append(DateUtil.toString(mondayOfThisWeek, PEPConstants.DEFAULT_DATE_FORMAT))
            .append("～")
            .append(DateUtil.toString(sundayOfThisWeek, PEPConstants.DEFAULT_DATE_FORMAT));
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
