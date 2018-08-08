package com.proper.enterprise.platform.push.openapi.service.impl

import com.proper.enterprise.platform.push.api.openapi.service.AppServerRequestService
import com.proper.enterprise.platform.push.entity.PushMsgEntity
import com.proper.enterprise.platform.push.repository.PushMsgRepository
import com.proper.enterprise.platform.push.test.PushAbstractTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

@Sql([
    "/com/proper/enterprise/platform/push/push-users.sql",
    "/com/proper/enterprise/platform/push/push-devices.sql",
    "/com/proper/enterprise/platform/push/push-msgs.sql",
    "/com/proper/enterprise/platform/push/datadisc.sql"
])
class AppServerRequestServiceImplTest extends PushAbstractTest {
    @Autowired
    AppServerRequestService appServerRequestService
    @Autowired
    private PushMsgRepository pushMsgRepository;

    def vo

    @Before
    void beforeInit() {
        vo = initData()
    }

    @After
    void afterData() {
        delete(vo.getId())
    }

    @Test
    void sendMsgTest() {
        String pushId = "m_id_1"
        PushMsgEntity entity = pushMsgRepository.findById(pushId).orElse(null)
        assert entity != null
        assert entity.getSendCount() == 1
        List<String> list = new ArrayList<>()
        list.add(pushId)
        appServerRequestService.sendMsg(list)
        entity = pushMsgRepository.findById(pushId).orElse(null)
        assert entity.getSendCount() == 2
    }
}
