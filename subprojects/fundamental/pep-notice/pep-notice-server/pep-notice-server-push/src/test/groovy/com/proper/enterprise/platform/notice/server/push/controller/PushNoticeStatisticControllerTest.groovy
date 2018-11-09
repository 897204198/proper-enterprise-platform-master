package com.proper.enterprise.platform.notice.server.push.controller

import com.proper.enterprise.platform.notice.server.push.dao.repository.PushNoticeMsgJpaRepository
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushNoticeMsgStatisticRepository
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class PushNoticeStatisticControllerTest extends AbstractJPATest {

    @Autowired
    private PushNoticeMsgStatisticRepository pushNoticeMsgStatisticRepository

    @Autowired
    private PushNoticeMsgJpaRepository pushNoticeMsgJpaRepository

    @Test
    public void get() {
        get("/notice/server/push/statistic/dataAnalysis/DAY", HttpStatus.OK)
    }

    @Test
    public void init() {
        get("/notice/server/push/statistic/init", HttpStatus.OK)
    }

    @Test
    public void getPie() {
        get("/notice/server/push/statistic/pieDataItems?startDate=2018-01-10&endDate=2018-12-10", HttpStatus.OK)
        get("/notice/server/push/statistic/pieDataAnalysis?startDate=2018-01-10&endDate=2018-12-10&appKey=test", HttpStatus.OK)
    }
}
