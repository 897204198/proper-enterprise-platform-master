package com.proper.enterprise.platform.notice.server.push.controller

import com.proper.enterprise.platform.notice.server.push.dao.repository.PushNoticeMsgJpaRepository
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushNoticeMsgStatisticRepository
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class PushNoticeStatisticControllerTest extends AbstractTest {

    @Autowired
    private PushNoticeMsgStatisticRepository pushNoticeMsgStatisticRepository

    @Autowired
    private PushNoticeMsgJpaRepository pushNoticeMsgJpaRepository

    @Test
    public void get() {
        get("/notice/server/push/statistic/dataAnalysis", HttpStatus.OK)
    }

    @Test
    public void init() {
        get("/notice/server/push/statistic/init", HttpStatus.OK)
    }
}
