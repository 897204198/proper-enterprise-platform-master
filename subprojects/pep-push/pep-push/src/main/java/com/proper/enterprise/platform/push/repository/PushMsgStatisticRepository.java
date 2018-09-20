package com.proper.enterprise.platform.push.repository;

import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import com.proper.enterprise.platform.push.entity.PushMsgStatisticEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PushMsgStatisticRepository extends BaseJpaRepository<PushMsgStatisticEntity, String> {

    /**
     * 对消息按 应用、渠道、时间、状态 分组查询
     *
     * @param mstartDate 消息时间范围的开始
     * @param mendDate   消息时间范围的结束
     * @return List 统计数据
     */
    @Query(value = "SELECT A.APPKEY, B.PUSH_MODE, SUBSTRING(A.LAST_MODIFY_TIME, 1, 10), A.MSTATUS, COUNT(A.APPKEY) "
        + "FROM PEP_PUSH_MSG AS A LEFT JOIN PEP_PUSH_DEVICE AS B ON A.DEVICE_PK_ID = B.ID "
        + "WHERE A.LAST_MODIFY_TIME > ?1 AND A.LAST_MODIFY_TIME < ?2 "
        + "GROUP BY  A.APPKEY, B.PUSH_MODE, SUBSTRING(A.LAST_MODIFY_TIME, 1, 10), A.MSTATUS", nativeQuery = true)
    List getPushStatistic(String mstartDate, String mendDate);

    /**
     * 查询指定日期之后的消息推送统计
     *
     * @param msendDate 消息推送的开始时间
     * @return List 统计实体集合
     */
    List<PushMsgStatisticEntity> findByMsendedDateAfterOrderByMsendedDate(Date msendDate);

    /**
     * 查询指定日期之后的消息推送统计
     *
     * @param msendDate 消息推送的开始时间
     * @return List 统计实体集合
     */
    List<PushMsgStatisticEntity> findByMsendedDateAfterOrderByMsendedDate(String msendDate);

    /**
     * 查询指定日期之后的消息推送统计；按周分组
     *
     * @param msendDate 消息推送的开始时间
     * @return List 统计实体集合
     */
    @Query("SELECT SUM(S.mnum), S.mstatus, S.pushMode, S.week  FROM PushMsgStatisticEntity S "
        + "WHERE S.msendedDate >= :msendDate GROUP BY S.week, S.mstatus, S.pushMode ORDER BY S.week")
    List findByMsendedDateAfterGroupByWeekOfYear(@Param("msendDate") String msendDate);

    /**
     * 查询指定日期之后的消息推送统计；按月份分组
     *
     * @param msendDate 消息推送的开始时间
     * @return List 统计实体集合
     */
    @Query("SELECT SUM(S.mnum), S.mstatus, S.pushMode, S.month  FROM PushMsgStatisticEntity S "
        + "WHERE S.month >= :msendDate GROUP BY S.month, S.mstatus, S.pushMode ORDER BY S.month")
    List findByMsendedDateAfterGroupByMonthOfYear(@Param("msendDate") String msendDate);

    /**
     * 查询指定日期之后的消息推送统计
     *
     * @param msendDate 消息推送的开始时间
     * @return List 统计实体集合
     */
    @Query("SELECT SUM(S.mnum), S.mstatus, S.pushMode, S.msendedDate  "
        + "FROM PushMsgStatisticEntity S "
        + "WHERE S.msendedDate >= :msendDate "
        + "GROUP BY S.msendedDate, S.mstatus, S.pushMode "
        + "ORDER BY S.msendedDate")
    List findByAndMsendedDateAfterOrderByMsendedDate(@Param("msendDate") String msendDate);

    /**
     * 查询指定日期之后的消息推送统计
     *
     * @param msendDate 消息推送的开始时间
     * @param appkey    应用
     * @return List 统计实体集合
     */
    @Query("SELECT SUM(S.mnum), S.mstatus, S.pushMode, S.msendedDate  "
        + "FROM PushMsgStatisticEntity S "
        + "WHERE S.appkey = :appkey AND S.msendedDate >= :msendDate "
        + "GROUP BY S.msendedDate, S.mstatus, S.pushMode "
        + "ORDER BY S.msendedDate")
    List findByAppkeyAndMsendedDateAfterOrderByMsendedDate(@Param("appkey") String appkey, @Param("msendDate") String msendDate);

    /**
     * 查询指定日期之后的消息推送统计；按周分组
     *
     * @param msendDate 消息推送的开始时间
     * @param appkey    应用
     * @return List 统计实体集合
     */
    @Query("SELECT SUM(S.mnum), S.mstatus, S.pushMode, S.week  "
        + "FROM PushMsgStatisticEntity S "
        + "WHERE S.appkey = :appkey AND S.msendedDate >= :msendDate "
        + "GROUP BY S.week, S.mstatus, S.pushMode ORDER BY S.week")
    List findByAppkeyAndMsendedDateAfterGroupByWeekOfYear(@Param("appkey") String appkey, @Param("msendDate") String msendDate);

    /**
     * 查询指定日期之后的消息推送统计；按月份分组
     *
     * @param msendDate 消息推送的开始时间
     * @param appkey    应用
     * @return List 统计实体集合
     */
    @Query("SELECT SUM(S.mnum), S.mstatus, S.pushMode, S.month  "
        + "FROM PushMsgStatisticEntity S "
        + "WHERE S.appkey = :appkey AND S.msendedDate >= :msendDate "
        + "GROUP BY S.month, S.mstatus, S.pushMode ORDER BY S.month")
    List findByAppkeyAndMsendedDateAfterGroupByMonthOfYear(@Param("appkey") String appkey, @Param("msendDate") String msendDate);


    /**
     * 清除指定日期的统计数据
     *
     * @param msendedDate 应用标识
     * @return int
     */
    @Modifying
    @Query("delete from PushMsgStatisticEntity s where s.msendedDate = :msendedDate")
    public int deleteByMsendedDate(@Param("msendedDate") String msendedDate);

    /**
     * 默认展示的饼状图统计数据
     * @param msendedDate 前一天的时间
     * @param appkeys appkey 集合
     * @return 饼状图统计数据
     */
    @Query("SELECT SUM(S.mnum), S.mstatus, S.appkey "
        + "FROM PushMsgStatisticEntity S "
        + "where S.msendedDate = :msendedDate and S.appkey in :appKeys "
        + "group by S.mstatus, S.appkey")
    List findPushMsgByDefault(@Param("msendedDate") String msendedDate, @Param("appKeys") List<String> appkeys);

    /**
     * 查询区间内的数据
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param appkeys appkey 集合
     * @return 查询区间内饼状图数据
     */
    @Query("SELECT SUM(S.mnum), S.mstatus, S.appkey "
        + "FROM PushMsgStatisticEntity S "
        + "where S.msendedDate  between :startDate and :endDate and S.appkey in :appKeys "
        + "group by S.mstatus, S.appkey")
    List findByBetweenStartDataEndDate(@Param("startDate") String startDate,
                                       @Param("endDate") String endDate,
                                       @Param("appKeys") List<String> appkeys);

    /**
     * 查询渠道统计总数
     * @param appKey 渠道名称
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    @Query("SELECT SUM(b.mnum) FROM PushMsgStatisticEntity b "
        + "where b.appkey = :appKey "
        + "and b.msendedDate between :startDate and :endDate group by b.appkey")
    String findPushCount(@Param("appKey") String appKey,
                         @Param("startDate") String startDate,
                         @Param("endDate") String endDate);

    /**
     * 按照项目消息总数量进行排序
     * @param appKeys appkeys
     * @return result
     */
    @Query("SELECT SUM(b.mnum) , b.appkey FROM PushMsgStatisticEntity b "
        + "WHERE b.appkey IN :appKeys GROUP BY b.appkey ORDER BY SUM(b.mnum) DESC ")
    List findOrderByMsgNumberDesc(@Param("appKeys") List<String> appKeys);
}
