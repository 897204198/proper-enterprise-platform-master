package com.proper.enterprise.platform.notice.server.push.dao.repository;

import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import com.proper.enterprise.platform.notice.server.push.dao.entity.PushNoticeMsgStatisticEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PushNoticeMsgStatisticRepository extends BaseJpaRepository<PushNoticeMsgStatisticEntity, String> {

    /**
     * 对消息按 应用、渠道、时间、状态 分组查询
     *
     * @param startDate 消息时间范围的开始
     * @param endDate   消息时间范围的结束
     * @return List 统计数据
     */
    @Query(value = "SELECT A.APP_KEY, A.PUSH_CHANNEL, SUBSTRING(A.LAST_MODIFY_TIME, 1, 10), A.STATUS, COUNT(A.APP_KEY) "
        + "FROM PEP_PUSH_NOTICE_MSG A"
        + " WHERE A.LAST_MODIFY_TIME >= ?1 AND A.LAST_MODIFY_TIME < ?2 "
        + " GROUP BY  A.APP_KEY, A.PUSH_CHANNEL, SUBSTRING(A.LAST_MODIFY_TIME, 1, 10), A.STATUS", nativeQuery = true)
    List<Object[]> getPushStatistic(String startDate, String endDate);


    /**
     * 清除指定日期的统计数据
     *
     * @param sendDate 发送时间
     * @return int
     */
    @Modifying
    @Query("delete from PushNoticeMsgStatisticEntity s where s.sendDate = :sendDate")
    int deleteBySendDate(@Param("sendDate") String sendDate);


    /**
     * 查询指定日期之后的消息推送统计
     *
     * @param sendDate 消息推送开始时间
     * @return List 统计实体集合
     */
    @Query("SELECT SUM(S.msgCount) as msgCount, S.status, S.pushChannel, S.sendDate  "
        + "FROM PushNoticeMsgStatisticEntity S "
        + "WHERE S.sendDate >= :sendDate "
        + "GROUP BY S.sendDate, S.status, S.pushChannel "
        + "ORDER BY S.sendDate DESC ")
    List<Object[]> findAllGroupDay(@Param("sendDate") String sendDate);

    /**
     * 查询指定日期之后的消息推送统计
     *
     * @param sendDate 消息推送的开始时间
     * @param appKey   应用
     * @return List 统计实体集合
     */
    @Query("SELECT SUM(S.msgCount), S.status, S.pushChannel, S.sendDate  "
        + "FROM PushNoticeMsgStatisticEntity S "
        + "WHERE S.appKey = :appKey AND S.sendDate >= :sendDate "
        + "GROUP BY S.sendDate, S.status, S.pushChannel "
        + "ORDER BY S.sendDate DESC ")
    List<Object[]> findAllGroupDay(@Param("appKey") String appKey, @Param("sendDate") String sendDate);


    /**
     * 查询指定日期之后的消息推送统计；按周分组
     *
     * @param sendDate 消息推送时间
     * @return List 统计实体集合
     */
    @Query("SELECT SUM(S.msgCount), S.status, S.pushChannel, S.week  FROM PushNoticeMsgStatisticEntity S "
        + "WHERE S.sendDate >= :sendDate GROUP BY S.week, S.status, S.pushChannel ORDER BY S.week")
    List<Object[]> findAllGroupWeek(@Param("sendDate") String sendDate);


    /**
     * 查询指定日期之后的消息推送统计；按周分组
     *
     * @param sendDate 消息推送时间
     * @param appKey   应用唯一标识
     * @return List 统计实体集合
     */
    @Query("SELECT SUM(S.msgCount), S.status, S.pushChannel, S.week  "
        + "FROM PushNoticeMsgStatisticEntity S "
        + "WHERE S.appKey = :appKey AND S.sendDate >= :sendDate "
        + "GROUP BY S.week, S.status, S.pushChannel ORDER BY S.week")
    List<Object[]> findAllGroupWeek(@Param("appKey") String appKey, @Param("sendDate") String sendDate);


    /**
     * 查询指定日期之后的消息推送统计；按月份分组
     *
     * @param sendDate 消息推送时间
     * @return List 统计实体集合
     */
    @Query("SELECT SUM(S.msgCount), S.status, S.pushChannel, S.month  FROM PushNoticeMsgStatisticEntity S "
        + "WHERE S.month >= :sendDate GROUP BY S.month, S.status, S.pushChannel ORDER BY S.month")
    List<Object[]> findAllGroupMonth(@Param("sendDate") String sendDate);

    /**
     * 查询指定日期之后的消息推送统计；按月份分组
     *
     * @param sendDate 消息推送时间
     * @param appKey   应用唯一标识
     * @return List 统计实体集合
     */
    @Query("SELECT SUM(S.msgCount), S.status, S.pushChannel, S.month  "
        + "FROM PushNoticeMsgStatisticEntity S "
        + "WHERE S.appKey = :appKey AND S.sendDate >= :sendDate "
        + "GROUP BY S.month, S.status, S.pushChannel ORDER BY S.month")
    List<Object[]> findAllGroupMonth(@Param("appKey") String appKey, @Param("sendDate") String sendDate);

}
