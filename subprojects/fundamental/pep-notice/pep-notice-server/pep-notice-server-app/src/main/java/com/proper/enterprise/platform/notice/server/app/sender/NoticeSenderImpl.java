package com.proper.enterprise.platform.notice.server.app.sender;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.i18n.I18NUtil;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.factory.NoticeSenderFactory;
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNoticeResult;
import com.proper.enterprise.platform.notice.server.api.model.Notice;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.api.sender.NoticeSender;
import com.proper.enterprise.platform.notice.server.api.service.NoticeDaoService;
import com.proper.enterprise.platform.notice.server.api.util.AppUtil;
import com.proper.enterprise.platform.notice.server.api.util.ThrowableMessageUtil;
import com.proper.enterprise.platform.notice.server.app.NoticeServerAppProperties;
import com.proper.enterprise.platform.notice.server.app.convert.RequestConvert;
import com.proper.enterprise.platform.notice.server.app.vo.NoticeVO;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("pepNoticeServerSender")
public class NoticeSenderImpl implements NoticeSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeSenderImpl.class);

    private static final Integer MAX_TARGET_TO_SIZE = 255;

    private NoticeDaoService noticeDaoService;

    private NoticeServerAppProperties noticeServerAppProperties;

    @Autowired
    public NoticeSenderImpl(NoticeDaoService noticeDaoService, NoticeServerAppProperties noticeServerAppProperties) {
        this.noticeDaoService = noticeDaoService;
        this.noticeServerAppProperties = noticeServerAppProperties;
    }

    @Override
    public List<Notice> beforeSend(String appKey, NoticeRequest noticeRequest) {
        //已禁用或已删除的app不发送
        if (!AppUtil.isEnable(appKey)) {
            throw new ErrMsgException(appKey + " is disabled");
        }
        if (null != noticeRequest.getNoticeExtMsg()
            && JSONUtil.toJSONIgnoreException(noticeRequest.getNoticeExtMsg()).length() > 2048) {
            throw new ErrMsgException(I18NUtil.getMessage("notice.server.param.noticeExtMsg.isTooLong"));
        }
        List<Notice> notices = RequestConvert.convert(noticeRequest);
        for (Notice notice : notices) {
            notice.setAppKey(appKey);
            notice.setNoticeType(noticeRequest.getNoticeType());
        }
        return notices;
    }

    @Override
    public void sendAsync(List<Notice> notices) {
        for (Notice notice : notices) {
            sendAsync(notice);
        }
    }

    @Override
    public void sendAsync(Notice notice) {
        Notice saveNotice;
        try {
            if (null != notice.getTargetExtMsgMap()
                && JSONUtil.toJSONIgnoreException(notice.getTargetExtMsgMap()).length() > 2048) {
                notice.setErrorMsg(I18NUtil.getMessage("notice.server.param.targetExtMsg.isTooLong")
                    + ":" + JSONUtil.toJSONIgnoreException(notice.getTargetExtMsgMap()));
                notice.setErrorCode("notice.server.param.targetExtMsg.isTooLong");
                notice.setStatus(NoticeStatus.FAIL);
                NoticeVO noticeVO = BeanUtil.convert(notice, NoticeVO.class);
                noticeVO.setTargetExtMsg(null);
                noticeDaoService.save(noticeVO);
                return;
            }
            NoticeSendHandler noticeSendHandler = NoticeSenderFactory.product(notice.getNoticeType());
            BusinessNoticeResult businessNoticeResult = noticeSendHandler.beforeSend(notice);
            if (NoticeStatus.FAIL == businessNoticeResult.getNoticeStatus()) {
                notice.setErrorMsg(businessNoticeResult.getMessage());
                notice.setErrorCode(businessNoticeResult.getCode());
                notice.setStatus(NoticeStatus.FAIL);
                noticeDaoService.save(BeanUtil.convert(notice, NoticeVO.class));
                return;
            }
            if (StringUtil.isNotEmpty(notice.getTargetTo()) && notice.getTargetTo().length() > MAX_TARGET_TO_SIZE) {
                notice.setErrorMsg(I18NUtil.getMessage("notice.server.param.target.isTooLong")
                    + ":" + notice.getTargetTo());
                notice.setErrorCode("notice.server.param.target.isTooLong");
                notice.setStatus(NoticeStatus.FAIL);
                notice.setTargetTo("");
                noticeDaoService.save(BeanUtil.convert(notice, NoticeVO.class));
                return;
            }
            if (StringUtil.isEmpty(notice.getTargetTo())) {
                notice.setErrorMsg(I18NUtil.getMessage("notice.server.param.target.cantBeEmpty"));
                notice.setErrorCode("notice.server.param.target.cantBeEmpty");
                notice.setStatus(NoticeStatus.FAIL);
                notice.setTargetTo("");
                noticeDaoService.save(BeanUtil.convert(notice, NoticeVO.class));
                return;
            }
            saveNotice = noticeDaoService.save(BeanUtil.convert(notice, NoticeVO.class));
        } catch (Exception e) {
            LOGGER.error("notice persistence exception,batchId{},appKey:{}", notice.getBatchId(),
                notice.getAppKey(), e);
            throw new ErrMsgException("notice persistence exception");
        }
        try {
            NoticeSendHandler noticeSendHandler = NoticeSenderFactory.product(notice.getNoticeType());
            BusinessNoticeResult businessNoticeResult = noticeSendHandler.send(saveNotice);
            if (NoticeStatus.FAIL == businessNoticeResult.getNoticeStatus()) {
                noticeDaoService.updateToFail(saveNotice.getId(), businessNoticeResult.getCode(), businessNoticeResult.getMessage());
            }
            noticeDaoService.updateStatus(saveNotice.getId(), businessNoticeResult.getNoticeStatus());
        } catch (Exception e) {
            noticeDaoService.updateToFail(saveNotice.getId(),
                e.getMessage(), ThrowableMessageUtil.getStackTrace(e));
        }
    }

    @Override
    public void afterSend(ReadOnlyNotice notice) {
        try {
            NoticeSendHandler noticeSendHandler = NoticeSenderFactory.product(notice.getNoticeType());
            noticeSendHandler.afterSend(notice);
        } catch (Exception e) {
            LOGGER.error("afterSend error noticeType:{},noticeId:{}", notice.getNoticeType(), notice.getId());
        }
    }

    @Override
    public void syncPendingNoticesStatusAsync(LocalDateTime startModifyTime, LocalDateTime endModifyTime) {
        List<Notice> notices = noticeDaoService.findPendingNotices(startModifyTime, endModifyTime);
        if (CollectionUtil.isEmpty(notices)) {
            return;
        }
        for (Notice notice : notices) {
            syncPendingNoticeStatusAsync(notice);
        }
    }

    @Override
    public void syncPendingNoticeStatusAsync(ReadOnlyNotice notice) {
        try {
            if (NoticeStatus.PENDING != notice.getStatus()) {
                return;
            }
            NoticeSendHandler noticeSendHandler = NoticeSenderFactory.product(notice.getNoticeType());
            BusinessNoticeResult businessNoticeResult = noticeSendHandler.getStatus(notice);
            switch (businessNoticeResult.getNoticeStatus()) {
                case SUCCESS:
                    noticeDaoService.updateStatus(notice.getId(), NoticeStatus.SUCCESS);
                    return;
                case PENDING:
                    return;
                case FAIL:
                    noticeDaoService.updateToFail(notice.getId(), businessNoticeResult.getCode(), businessNoticeResult.getMessage());
                    return;
                case RETRY:
                    //超过最大重试次数 记异常不再重试
                    if (notice.getRetryCount() >= noticeServerAppProperties.getMaxRetryCount()) {
                        noticeDaoService.updateToFail(notice.getId(), "Max retry", "Max retry");
                        return;
                    }
                    //未超过最大重试次数 重试
                    noticeDaoService.addRetryCount(notice.getId());
                    noticeDaoService.updateStatus(notice.getId(), NoticeStatus.RETRY);
                    return;
                default:
                    noticeDaoService.updateToFail(notice.getId(), "business status is not support",
                        "business status is not support");
            }
        } catch (Exception e) {
            Notice updateNoticeVO = noticeDaoService.updateToFail(notice.getId(),
                e.getMessage(), ThrowableMessageUtil.getStackTrace(e));
            this.afterSend(updateNoticeVO);
        }
    }

    @Override
    public void retryNoticesAsync(LocalDateTime startModifyTime, LocalDateTime endModifyTime) {
        List<Notice> notices =
            noticeDaoService.findRetryNotices(startModifyTime, endModifyTime, noticeServerAppProperties.getMaxRetryCount());
        if (CollectionUtil.isEmpty(notices)) {
            return;
        }
        for (Notice notice : notices) {
            retryNoticeAsync(notice);
        }
    }

    @Override
    public void retryNoticeAsync(ReadOnlyNotice notice) {
        try {
            if (NoticeStatus.RETRY != notice.getStatus()) {
                return;
            }
            NoticeSendHandler noticeSendHandler = NoticeSenderFactory.product(notice.getNoticeType());
            noticeDaoService.addRetryCount(notice.getId());
            BusinessNoticeResult businessNoticeResult = noticeSendHandler.send(notice);
            if (NoticeStatus.FAIL == businessNoticeResult.getNoticeStatus()) {
                noticeDaoService.updateToFail(notice.getId(), businessNoticeResult.getCode(),
                    businessNoticeResult.getMessage());
                return;
            }
            noticeDaoService.updateStatus(notice.getId(), businessNoticeResult.getNoticeStatus());
        } catch (Exception e) {
            Notice updateNoticeVO = noticeDaoService.updateToFail(notice.getId(),
                e.getMessage(), ThrowableMessageUtil.getStackTrace(e));
            this.afterSend(updateNoticeVO);
        }
    }

}
