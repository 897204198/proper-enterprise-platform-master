package com.proper.enterprise.platform.notice.server.app.dao.repository;

import com.proper.enterprise.platform.notice.server.api.model.Notice;
import com.proper.enterprise.platform.notice.server.app.dao.entity.NoticeEntity;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeRepository extends BaseJpaRepository<NoticeEntity, String> {

    /**
     * 根据修改时间倒叙查询列表
     *
     * @param id         消息id
     * @param appKey     应用唯一标识
     * @param batchId    批次号
     * @param targetTo   发送目标
     * @param content    消息内容
     * @param noticeType 消息类型
     * @param status     消息状态
     * @return 实体集合
     */
    @Query("select t from NoticeEntity t"
        + " where (t.id=:id or :id is null)"
        + " and (t.appKey=:appKey or :appKey is null)"
        + " and (t.batchId=:batchId or :batchId is null)"
        + " and (t.targetTo=:targetTo or :targetTo is null)"
        + " and (t.content=:content or :content is null)"
        + " and (t.noticeType=:noticeType or :noticeType is null)"
        + " and (t.status=:status or :status is null)"
        + " order by t.createTime desc")
    List<NoticeEntity> findAll(@Param("id") String id,
                               @Param("appKey") String appKey,
                               @Param("batchId") String batchId,
                               @Param("targetTo") String targetTo,
                               @Param("content") String content,
                               @Param("noticeType") NoticeType noticeType,
                               @Param("status") NoticeStatus status);

    /**
     * 根据修改时间倒叙分页查询
     *
     * @param id         消息id
     * @param appKey     应用唯一标识
     * @param batchId    批次号
     * @param targetTo   发送目标
     * @param content    消息内容
     * @param noticeType 消息类型
     * @param status     消息状态
     * @param pageable   分页参数
     * @return 分页实体集合
     */
    @Query("select t from NoticeEntity t"
        + " where (t.id=:id or :id is null)"
        + " and (t.appKey=:appKey or :appKey is null)"
        + " and (t.batchId=:batchId or :batchId is null)"
        + " and (t.targetTo=:targetTo or :targetTo is null)"
        + " and (t.content=:content or :content is null)"
        + " and (t.noticeType=:noticeType or :noticeType is null)"
        + " and (t.status=:status or :status is null)"
        + " order by t.createTime desc")
    Page<NoticeEntity> findAll(@Param("id") String id,
                               @Param("appKey") String appKey,
                               @Param("batchId") String batchId,
                               @Param("targetTo") String targetTo,
                               @Param("content") String content,
                               @Param("noticeType") NoticeType noticeType,
                               @Param("status") NoticeStatus status,
                               Pageable pageable);

    /**
     * 根据修改时间查询发送中状态的消息
     *
     * @param startModifyTime 从修改时间
     * @param endModifyTime   至修改时间
     * @return 满足条件的消息集合
     */
    @Query("select t from NoticeEntity t where t.status='PENDING'"
        + " AND t.lastModifyTime>:startModifyTime"
        + " AND t.lastModifyTime<=:endModifyTime")
    List<Notice> findPendingNotices(@Param("startModifyTime") String startModifyTime,
                                    @Param("endModifyTime") String endModifyTime);


    /**
     * 根据修改时间查询需要重试的消息
     *
     * @param startModifyTime 从修改时间
     * @param endModifyTime   至修改时间
     * @param maxRetryCount   最大重试次数
     * @return 满足条件的消息集合
     */
    @Query("select t from NoticeEntity t where t.status='RETRY'"
        + " AND t.retryCount<:maxRetryCount"
        + " AND t.lastModifyTime>:startModifyTime"
        + " AND t.lastModifyTime<=:endModifyTime")
    List<Notice> findRetryNotices(@Param("startModifyTime") String startModifyTime,
                                  @Param("endModifyTime") String endModifyTime,
                                  @Param("maxRetryCount") Integer maxRetryCount);
}
