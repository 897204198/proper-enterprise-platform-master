package com.proper.enterprise.platform.notice.server.api.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.notice.server.api.enums.NoticeStatus;
import com.proper.enterprise.platform.notice.server.api.vo.NoticeVO;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Validated
public interface NoticeServerService {

    /**
     * 保存消息
     *
     * @param notice 消息内容
     * @return 保存后的消息VO
     */
    NoticeVO save(@Valid NoticeVO notice);

    /**
     * 更新消息状态
     *
     * @param noticeId     消息Id
     * @param noticeStatus 消息状态
     * @return 更新后的消息VO
     */
    NoticeVO updateStatus(String noticeId, NoticeStatus noticeStatus);

    /**
     * 更新消失败
     *
     * @param noticeId 消息Id
     * @param errMsg   消息异常
     * @return 更新后的消息VO
     */
    NoticeVO updateStatusFail(String noticeId, String errMsg);

    /**
     * 查询所有消息
     *
     * @return 消息VO集合
     */
    List<NoticeVO> findAll();


    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 分页VO对象
     */
    DataTrunk<NoticeVO> findAll(Pageable pageable);

    /**
     * 获得单条消息
     *
     * @param noticeId 消息id
     * @return 消息信息
     */
    NoticeVO get(String noticeId);
}
