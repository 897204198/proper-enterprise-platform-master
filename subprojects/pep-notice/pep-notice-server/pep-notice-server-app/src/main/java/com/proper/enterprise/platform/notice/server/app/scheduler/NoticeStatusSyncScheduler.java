package com.proper.enterprise.platform.notice.server.app.scheduler;

import com.proper.enterprise.platform.notice.server.api.sender.NoticeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service("noticeStatusSyncScheduler")
public class NoticeStatusSyncScheduler {

    private NoticeSender noticeSendService;

    @Autowired
    public NoticeStatusSyncScheduler(NoticeSender noticeSendService) {
        this.noticeSendService = noticeSendService;
    }

    public void syncPending() {
        noticeSendService.syncPendingNoticesStatusAsync(LocalDateTime.now().minus(Duration.ofMinutes(10)), LocalDateTime.now());
    }

    public void syncRetry() {
        noticeSendService.retryNoticesAsync(LocalDateTime.now().minus(Duration.ofMinutes(10)), LocalDateTime.now());
    }
}
