package com.proper.enterprise.platform.push.schedule.service.impl

import com.proper.enterprise.platform.push.common.schedule.service.PushStatisticTaskService
import com.proper.enterprise.platform.push.entity.PushMsgEntity
import com.proper.enterprise.platform.push.repository.PushMsgRepository
import com.proper.enterprise.platform.push.repository.PushMsgStatisticRepository
import com.proper.enterprise.platform.push.service.PushMsgStatisticService
import com.proper.enterprise.platform.push.test.PushAbstractTest
import com.proper.enterprise.platform.push.vo.PushMsgStatisticVO
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

@Sql([
    "/com/proper/enterprise/platform/push/push-users.sql",
    "/com/proper/enterprise/platform/push/push-devices.sql",
    "/com/proper/enterprise/platform/push/push-msgs.sql"
])
class PushStatisticTaskServiceTest extends PushAbstractTest {
    @Autowired
    PushMsgRepository msgRepository
    @Autowired
    PushMsgStatisticRepository statisticRepository
    @Autowired
    PushStatisticTaskService pushStatisticTaskService
    @Autowired
    PushMsgStatisticService pushMsgStatisticService

    @Test
    void pushStatisticTaskTest() {
        assert msgRepository.count() == 4
        assert statisticRepository.count() == 0

        pushStatisticTaskService.saveYesterdayPushStatistic()
        PushMsgEntity msgEntity = msgRepository.getOne("m_id_1")
        printf msgEntity.getLastModifyTime()
        List<PushMsgStatisticVO> array = pushMsgStatisticService.findByDateTypeAndAppkey("day", null)

        //测试以下数据；时需要修改push-msgs.sql里的last_modify_time改为昨天的日期
        //assert array.size() == 1
        //PushMsgStatisticVO vo = array.get(0)
        //assert vo.getMnum() == 4
        //assert "2018-07-25".equals(vo.getMsendedDate())
    }
}
