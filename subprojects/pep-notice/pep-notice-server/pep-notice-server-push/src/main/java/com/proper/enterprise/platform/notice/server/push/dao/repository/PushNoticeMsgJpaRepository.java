package com.proper.enterprise.platform.notice.server.push.dao.repository;

import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import com.proper.enterprise.platform.notice.server.push.dao.entity.PushNoticeMsgEntity;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PushNoticeMsgJpaRepository extends BaseJpaRepository<PushNoticeMsgEntity, String> {

    /**
     * 分页查询推送消息
     *
     * @param content     消息内容
     * @param status      消息状态
     * @param appKey      系统唯一标识
     * @param pushChannel 推送渠道
     * @param pageable    分页参数
     * @return 分页集合
     */
    @Query("SELECT p FROM PushNoticeMsgEntity p WHERE (p.content=:content or :content is null)"
        + " and (p.status=:status or :status is null)"
        + " and (p.appKey=:appKey or :appKey is null)"
        + " and (p.pushChannel=:pushChannel or :pushChannel is null)")
    Page<PushNoticeMsgEntity> findPagination(@Param("content") String content,
                                             @Param("status") NoticeStatus status,
                                             @Param("appKey") String appKey,
                                             @Param("pushChannel") PushChannelEnum pushChannel,
                                             Pageable pageable);
}
