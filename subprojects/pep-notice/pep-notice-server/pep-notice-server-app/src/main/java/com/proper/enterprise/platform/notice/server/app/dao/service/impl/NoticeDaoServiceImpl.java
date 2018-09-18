package com.proper.enterprise.platform.notice.server.app.dao.service.impl;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;

import com.proper.enterprise.platform.notice.server.api.model.Notice;
import com.proper.enterprise.platform.notice.server.api.service.NoticeDaoService;
import com.proper.enterprise.platform.notice.server.app.vo.NoticeVO;
import com.proper.enterprise.platform.notice.server.app.dao.entity.NoticeEntity;
import com.proper.enterprise.platform.notice.server.app.dao.repository.NoticeRepository;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeDaoServiceImpl implements NoticeDaoService {

    private NoticeRepository noticeRepository;

    @Autowired
    public NoticeDaoServiceImpl(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    @Override
    public Notice newNotice() {
        return new NoticeEntity();
    }

    @Override
    public Notice save(Notice notice) {
        if (null == notice.getStatus()) {
            notice.setStatus(NoticeStatus.PENDING);
        }
        if (null == notice.getRetryCount()) {
            notice.setRetryCount(0);
        }
        NoticeEntity noticeEntity = BeanUtil.convert(notice, NoticeEntity.class);
        noticeEntity.setStatus(NoticeStatus.PENDING);
        noticeEntity.setRetryCount(0);
        return BeanUtil.convert(noticeRepository.save(noticeEntity), NoticeVO.class);
    }

    @Override
    public Notice updateStatus(String noticeId, NoticeStatus status) {
        NoticeEntity updateNotice = noticeRepository.findOne(noticeId);
        updateNotice.setStatus(status);
        return BeanUtil.convert(noticeRepository.updateForSelective(updateNotice), NoticeVO.class);
    }

    @Override
    public Notice updateToFail(String noticeId, String errMsg) {
        NoticeEntity updateNotice = noticeRepository.findOne(noticeId);
        updateNotice.setStatus(NoticeStatus.FAIL);
        updateNotice.setErrorMsg(errMsg);
        return BeanUtil.convert(noticeRepository.updateForSelective(updateNotice), NoticeVO.class);
    }

    @Override
    public void addRetryCount(String noticeId) {
        NoticeEntity updateNotice = noticeRepository.findOne(noticeId);
        updateNotice.setRetryCount(updateNotice.getRetryCount() + 1);
        noticeRepository.updateForSelective(updateNotice);
    }


    @Override
    public List<Notice> findAll(String id,
                                String appKey,
                                String batchId,
                                String targetTo,
                                String content,
                                NoticeType noticeType,
                                NoticeStatus status) {
        return new ArrayList<>(BeanUtil.convert(noticeRepository.findAll(id, appKey, batchId,
            targetTo, content, noticeType, status), NoticeVO.class));
    }

    @Override
    public DataTrunk<Notice> findAll(String id,
                                     String appKey,
                                     String batchId,
                                     String targetTo,
                                     String content,
                                     NoticeType noticeType,
                                     NoticeStatus status,
                                     Pageable pageable) {
        DataTrunk<Notice> data = new DataTrunk<>();
        Page<NoticeEntity> page = noticeRepository.findAll(id, appKey, batchId,
            targetTo, content, noticeType, status, pageable);
        data.setData(new ArrayList<>(BeanUtil.convert(page.getContent(), NoticeVO.class)));
        data.setCount(page.getTotalElements());
        return data;
    }

    @Override
    public List<Notice> findPendingNotices(LocalDateTime startModifyTime, LocalDateTime endModifyTime) {
        return noticeRepository.findPendingNotices(startModifyTime
                .format(DateTimeFormatter.ofPattern(PEPConstants.DEFAULT_DATETIME_FORMAT)),
            endModifyTime
                .format(DateTimeFormatter.ofPattern(PEPConstants.DEFAULT_DATETIME_FORMAT)));
    }

    @Override
    public List<Notice> findRetryNotices(LocalDateTime startModifyTime, LocalDateTime endModifyTime, Integer maxRetryCount) {
        return noticeRepository
            .findRetryNotices(startModifyTime
                    .format(DateTimeFormatter.ofPattern(PEPConstants.DEFAULT_DATETIME_FORMAT)),
                endModifyTime
                    .format(DateTimeFormatter.ofPattern(PEPConstants.DEFAULT_DATETIME_FORMAT)), maxRetryCount);
    }

    @Override
    public Notice get(String noticeId) {
        return BeanUtil.convert(noticeRepository.findOne(noticeId), NoticeVO.class);
    }
}
