package com.proper.enterprise.platform.push.repository;

import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import com.proper.enterprise.platform.push.entity.PushMsgStatisticEntity;
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
        + "WHERE S.msendedDate >= :msendDate GROUP BY S.month, S.mstatus, S.pushMode ORDER BY S.month")
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

}
