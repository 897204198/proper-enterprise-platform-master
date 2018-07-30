package com.proper.enterprise.platform.push.service;

import com.proper.enterprise.platform.core.jpa.service.BaseJpaService;
import com.proper.enterprise.platform.push.api.PushMsgStatistic;
import com.proper.enterprise.platform.push.vo.PushMsgStatisticVO;

import java.util.List;

public interface PushMsgStatisticService extends BaseJpaService<PushMsgStatistic, String> {

    /**
     * 获取推送统计数据
     *
     * @param dateType 日期类型
     * @param appkey   应用
     * @return List 推送集合
     */
    List<PushMsgStatisticVO> findByDateTypeAndAppkey(String dateType, String appkey);

    /**
     * 统计某一天的推送数据
     *
     * @param date 日期
     * @return List 当天的推送数据
     */
    List<PushMsgStatisticVO> saveStatisticOfSomeday(String date);
}
