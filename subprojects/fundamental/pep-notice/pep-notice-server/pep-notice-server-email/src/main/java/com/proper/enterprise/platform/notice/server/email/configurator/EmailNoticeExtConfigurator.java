package com.proper.enterprise.platform.notice.server.email.configurator;

import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator;
import org.springframework.mail.javamail.JavaMailSender;

public interface EmailNoticeExtConfigurator extends NoticeConfigurator {

    /**
     * 获取邮件发送服务
     *
     * @param appKey 唯一标识
     * @return 邮件发送服务
     */
    JavaMailSender getJavaMailSender(String appKey);
}
