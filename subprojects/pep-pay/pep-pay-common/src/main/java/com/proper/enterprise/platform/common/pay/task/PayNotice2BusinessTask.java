package com.proper.enterprise.platform.common.pay.task;

import com.proper.enterprise.platform.api.pay.service.NoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 *
 * 异步通知异步处理业务流程
 *
 * @param <T> 各支付平台业务实体
 */
@Component
public class PayNotice2BusinessTask<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayNotice2BusinessTask.class);

    @Async
    public void run(T t, NoticeService noticeService) {
        try {
            LOGGER.debug("启动线程PayNotice2BusinessTask处理异步通知业务正常开始");
            noticeService.saveNoticeProcess(t);
            LOGGER.debug("启动线程PayNotice2BusinessTask处理异步通知业务正常结束");
        } catch (Exception e) {
            LOGGER.debug("启动线程PayNotice2BusinessTask处理异步通知业务异常", e);
        }
    }
}
