package com.proper.enterprise.platform.notice.server.push.dao.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.notice.server.api.model.App;
import com.proper.enterprise.platform.notice.server.push.dao.entity.PushNoticeMsgStatisticEntity;
import com.proper.enterprise.platform.notice.server.push.enums.PushDataAnalysisDateRangeEnum;
import com.proper.enterprise.platform.notice.server.push.vo.PushMsgPieDataVO;
import com.proper.enterprise.platform.notice.server.push.vo.PushNoticeMsgPieVO;
import com.proper.enterprise.platform.notice.server.push.vo.PushServiceDataAnalysisVO;
import org.springframework.data.domain.PageRequest;

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
     * 数据分析视图
     *
     * @param startDate 基础日期
     * @param dateType  日期类型
     * @param appKey    应用
     * @return List 推送集合
     */
    List<PushServiceDataAnalysisVO> findByDateTypeAndAppKey(Date startDate, PushDataAnalysisDateRangeEnum dateType, String appKey);

    /**
     * 统计某一天的推送数据
     *
     * @param date 日期
     */
    void saveStatisticSomeday(String date);

    /**
     * 根据日期 获取饼图数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param appKey 需要查询的具体app
     * @return Vo
     */
    PushNoticeMsgPieVO  findPieDataByDateAndAppKey(String startDate, String endDate, String appKey);

    /**
     * 获取饼图左侧项目相关数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return list
     */
    List<PushMsgPieDataVO> findPieItems(String startDate, String endDate);

    /**
     * 分页查询app
     *
     * @param appKey      应用唯一标识
     * @param appName     应用名称
     * @param appDesc    应用描述
     * @param enable      启用停用
     * @param pageRequest 分页参数
     * @return 分页对象
     */
    DataTrunk<App> findApp(String appKey, String appName, String appDesc, Boolean enable, PageRequest pageRequest);
}
