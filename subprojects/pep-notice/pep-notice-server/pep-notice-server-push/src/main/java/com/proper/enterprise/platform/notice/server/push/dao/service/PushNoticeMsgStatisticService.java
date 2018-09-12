package com.proper.enterprise.platform.notice.server.push.dao.service;

import com.proper.enterprise.platform.notice.server.push.dao.entity.PushNoticeMsgStatisticEntity;
import com.proper.enterprise.platform.notice.server.push.vo.PushServiceDataAnalysisVO;

import java.util.Date;
import java.util.List;

public interface PushNoticeMsgStatisticService {

    /**
     * 对消息按 应用、渠道、时间、状态 分组查询
     *
     * @param startDate 消息时间范围的开始
     * @param endDate   消息时间范围的结束
     * @return 统计数据集合
     */
    List<PushNoticeMsgStatisticEntity> getPushStatistic(Date startDate, Date endDate);


    /**
     * 批量保存
     *
     * @param pushMsgStatistics 统计数据集合
     */
    void saveAll(List<PushNoticeMsgStatisticEntity> pushMsgStatistics);

    /**
     * 删除对应发送时间的所有统计记录
     *
     * @param sendDate 发送时间
     */
    void deleteBySendDate(Date sendDate);


    /**
     * 根据基础日期获取推送统计数据
     *
     * @param startDate 基础日期
     * @param dateType 日期类型
     * @param appKey   应用
     * @return List 推送集合
     */
    List<PushServiceDataAnalysisVO> findByDateTypeAndAppKey(Date startDate, String dateType, String appKey);
}
