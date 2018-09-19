package com.proper.enterprise.platform.notice.server.app.sender;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.notice.server.api.exception.NoticeException;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNoticeResult;
import com.proper.enterprise.platform.notice.server.api.sender.NoticeSender;
import com.proper.enterprise.platform.notice.server.api.util.AppUtil;
import com.proper.enterprise.platform.notice.server.app.convert.RequestConvert;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler;

import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.api.model.Notice;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeRequest;
import com.proper.enterprise.platform.notice.server.api.service.NoticeDaoService;
import com.proper.enterprise.platform.notice.server.app.vo.NoticeVO;
import com.proper.enterprise.platform.notice.server.api.factory.NoticeSenderFactory;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

@Service("pepNoticeServerSender")
public class NoticeSenderImpl implements NoticeSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeSenderImpl.class);

    private NoticeDaoService noticeDaoService;

    @Value("${notice.server.app.maxRetryCount}")
    private Integer maxRetryCount;

    @Autowired
    public NoticeSenderImpl(NoticeDaoService noticeDaoService) {
        this.noticeDaoService = noticeDaoService;
    }

    @Override
    public List<Notice> beforeSend(String appKey, NoticeRequest noticeRequest) throws NoticeException {
        //已禁用或已删除的app不发送
        if (!AppUtil.isEnable(appKey)) {
            throw new ErrMsgException(appKey + " is disabled");
        }
        List<Notice> notices = RequestConvert.convert(noticeRequest);
        NoticeSendHandler noticeSendHandler = NoticeSenderFactory.product(noticeRequest.getNoticeType());
        for (Notice notice : notices) {
            if (null != notice.getNoticeExtMsgMap()
                && JSONUtil.toJSONIgnoreException(notice.getNoticeExtMsgMap()).length() > 2048) {
                throw new ErrMsgException(I18NUtil.getMessage("notice.server.param.noticeExtMsg.isTooLong"));
            }
            if (null != notice.getTargetExtMsgMap()
                && JSONUtil.toJSONIgnoreException(notice.getTargetExtMsgMap()).length() > 2048) {
                throw new ErrMsgException(I18NUtil.getMessage("notice.server.param.targetExtMsg.isTooLong"));
            }
            notice.setAppKey(appKey);
            notice.setNoticeType(noticeRequest.getNoticeType());
            noticeSendHandler.beforeSend(notice);
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
        String persistenceNoticeId;
        try {
            Notice saveNotice = noticeDaoService.save(BeanUtil.convert(notice, NoticeVO.class));
            persistenceNoticeId = saveNotice.getId();
        } catch (Exception e) {
            LOGGER.error("notice persistence exception,batchId{},appKey:{}", notice.getBatchId(),
                notice.getAppKey(), e);
            throw new ErrMsgException("notice persistence exception");
        }
        try {
            NoticeSendHandler noticeSendHandler = NoticeSenderFactory.product(notice.getNoticeType());
            noticeSendHandler.send(notice);
        } catch (Exception e) {
            String stackTrace = getStackTrace(e);
            Notice updateNoticeVO = noticeDaoService.updateToFail(persistenceNoticeId,
                e.getMessage() + ":" + (stackTrace.length() > 2000
                    ? stackTrace.substring(0, 2000)
                    : stackTrace));
            this.afterSend(updateNoticeVO);
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
                    noticeDaoService.updateToFail(notice.getId(), businessNoticeResult.getMessage());
                    return;
                case RETRY:
                    //超过最大重试次数 记异常不再重试
                    if (notice.getRetryCount() >= maxRetryCount) {
                        noticeDaoService.updateToFail(notice.getId(), "Max retry");
                        return;
                    }
                    //未超过最大重试次数 重试
                    noticeDaoService.addRetryCount(notice.getId());
                    noticeDaoService.updateStatus(notice.getId(), NoticeStatus.RETRY);
                    return;
                default:
                    noticeDaoService.updateToFail(notice.getId(), "business status is not support");
            }
        } catch (Exception e) {
            String stackTrace = getStackTrace(e);
            Notice updateNoticeVO = noticeDaoService.updateToFail(notice.getId(),
                e.getMessage() + ":" + (stackTrace.length() > 2000
                    ? stackTrace.substring(0, 2000)
                    : stackTrace));
            this.afterSend(updateNoticeVO);
        }
    }

    @Override
    public void retryNoticesAsync(LocalDateTime startModifyTime, LocalDateTime endModifyTime) {
        List<Notice> notices = noticeDaoService.findRetryNotices(startModifyTime, endModifyTime, maxRetryCount);
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
            noticeDaoService.updateStatus(notice.getId(), NoticeStatus.PENDING);
            noticeSendHandler.send(notice);
        } catch (Exception e) {
            String stackTrace = getStackTrace(e);
            Notice updateNoticeVO = noticeDaoService.updateToFail(notice.getId(),
                e.getMessage() + ":" + (stackTrace.length() > 2000
                    ? stackTrace.substring(0, 2000)
                    : stackTrace));
            this.afterSend(updateNoticeVO);
        }
    }

    private static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }
}
