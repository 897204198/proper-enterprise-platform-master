package com.proper.enterprise.platform.notice.server.api.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import com.proper.enterprise.platform.notice.server.api.model.Notice;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Validated
public interface NoticeDaoService {

    /**
     * 获得新消息对象
     *
     * @return 具有操作权限的操作对象
     */
    Notice newNotice();

    /**
     * 保存消息
     *
     * @param notice 消息内容
     * @return 保存后的消息VO
     */
    Notice save(@Valid Notice notice);

    /**
     * 更新消息状态
     *
     * @param noticeId 消息Id
     * @param status   消息状态
     * @return 更新后的消息VO
     */
    Notice updateStatus(String noticeId, NoticeStatus status);

    /**
     * 消息发送失败
     *
     * @param noticeId 消息Id
     * @param errCode  异常编码
     * @param errMsg   消息异常
     * @return 更新后的消息VO
     */
    Notice updateToFail(String noticeId, String errCode, String errMsg);

    /**
     * 增加重试次数
     *
     * @param noticeId 消息id
     */
    void addRetryCount(String noticeId);

    /**
     * 查询所有消息
     *
     * @param id         消息id
     * @param appKey     应用唯一标识
     * @param batchId    批次号
     * @param targetTo   发送目标
     * @param content    消息内容
     * @param noticeType 消息类型
     * @param errorCode  异常编码
     * @param status     消息状态
     * @return 消息VO集合
     */
    List<Notice> findAll(String id,
                         String appKey,
                         String batchId,
                         String targetTo,
                         String content,
                         NoticeType noticeType,
                         String errorCode,
                         NoticeStatus status);


    /**
     * 分页查询
     *
     * @param id         消息id
     * @param appKey     应用唯一标识
     * @param batchId    批次号
     * @param targetTo   发送目标
     * @param content    消息内容
     * @param noticeType 消息类型
     * @param errorCode  异常编码
     * @param status     消息状态
     * @param pageable   分页参数
     * @return 分页VO对象
     */
    DataTrunk<Notice> findAll(String id,
                              String appKey,
                              String batchId,
                              String targetTo,
                              String content,
                              NoticeType noticeType,
                              String errorCode,
                              NoticeStatus status,
                              Pageable pageable);

    /**
     * 根据修改时间查询发送中状态的消息
     *
     * @param startModifyTime 从修改时间
     * @param endModifyTime   至修改时间
     * @return 满足条件的消息集合
     */
    List<Notice> findPendingNotices(LocalDateTime startModifyTime, LocalDateTime endModifyTime);


    /**
     * 根据修改时间查询需要重试的消息
     *
     * @param startModifyTime 从修改时间
     * @param endModifyTime   至修改时间
     * @param maxRetryCount   最大重试次数
     * @return 满足条件的消息集合
     */
    List<Notice> findRetryNotices(LocalDateTime startModifyTime, LocalDateTime endModifyTime, Integer maxRetryCount);

    /**
     * 获得单条消息
     *
     * @param noticeId 消息id
     * @return 消息信息
     */
    Notice get(String noticeId);
}
