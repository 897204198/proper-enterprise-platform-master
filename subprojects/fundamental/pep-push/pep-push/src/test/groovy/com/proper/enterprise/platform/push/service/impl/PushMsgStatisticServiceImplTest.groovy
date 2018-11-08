package com.proper.enterprise.platform.push.service.impl

import com.proper.enterprise.platform.core.exception.ErrMsgException
import com.proper.enterprise.platform.push.service.PushMsgStatisticService
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

@Sql([
    "/com/proper/enterprise/platform/push/push-statistic.sql",
    "/com/proper/enterprise/platform/push/push-channel.sql"
])
class PushMsgStatisticServiceImplTest extends AbstractJPATest {


    @Autowired
    private PushMsgStatisticService pushMsgStatisticService

    @Test
    void testFindAllWithPie() {

        try {
            pushMsgStatisticService.findAllWithPie(null, null, null)
        } catch (ErrMsgException e) {
            assert "appkey is not null" == e.getMessage()
        }

        String illegalAppKey = "ppp"
        try {
            pushMsgStatisticService.findAllWithPie(null, null, illegalAppKey)
        } catch (ErrMsgException e) {
            assert "appKey is illegal" == e.getMessage()
        }

        String appkey = "test"
        def pie = pushMsgStatisticService.findAllWithPie(null, null, appkey)
        assert null != pie && pie.size() > 0

        String appkey1 = "MobileOADev,test"
        def pie1 = pushMsgStatisticService.findAllWithPie("2018-07-22", "2018-07-22", appkey1)
        assert null != pie1 && pie1.size() > 0

        String appkey2 = "MobileOADev,test,MobileOADemo"
        def pie2 = pushMsgStatisticService.findAllWithPie("2018-07-22", "2018-07-22", appkey2)
        List<String> appkeys = (List) pie2.get("appKeyOrder")
        assert appkeys.size() == 3
        assert null != pie2 && pie2.size() > 0
    }
}
