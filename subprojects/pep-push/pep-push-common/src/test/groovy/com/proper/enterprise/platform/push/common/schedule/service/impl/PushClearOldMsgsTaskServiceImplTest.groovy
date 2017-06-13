package com.proper.enterprise.platform.push.common.schedule.service.impl

import com.proper.enterprise.platform.push.common.db.repository.PushMsgRepository
import com.proper.enterprise.platform.push.common.schedule.service.PushClearOldMsgsTaskService
import com.proper.enterprise.platform.push.test.PushAbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

@Sql([
    "/com/proper/enterprise/platform/push/common/push-users.sql",
    "/com/proper/enterprise/platform/push/common/push-devices.sql",
    "/com/proper/enterprise/platform/push/common/push-msgs.sql"
])
class PushClearOldMsgsTaskServiceImplTest extends PushAbstractTest {
    @Autowired
    PushClearOldMsgsTaskService service

    @Autowired
    PushMsgRepository msgRepository
    @Test
    void deleteOldMsgsTest() {
        assert msgRepository.count()>0
        service.deleteOldMsgs()
        assert msgRepository.count()==0
    }
}
