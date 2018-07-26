package com.proper.enterprise.platform.push.controller

import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.push.client.PusherApp
import com.proper.enterprise.platform.push.client.service.IPushApiServiceRequest
import com.proper.enterprise.platform.push.repository.PushMsgStatisticRepository
import com.proper.enterprise.platform.push.test.PushAbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

import java.text.SimpleDateFormat

@Sql([
    "/com/proper/enterprise/platform/push/push-statistic.sql"
])
class PushStatisticControllerTest extends PushAbstractTest {
    @Autowired
    PushMsgStatisticRepository statisticRepository;

    PusherApp pusherApp

    @Before
    void init() {
        IPushApiServiceRequest pushApiRequestMock = new AppServerRequestMockService()
        pusherApp = new PusherApp("/push/statistic", VALID_APPKEY, "wanchina")
        pusherApp.setPushApiRequest(pushApiRequestMock)
        pusherApp.setAsync(false)
    }

    @Test
    void doRequestMethodTest() {
        assert statisticRepository.count() == 15


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")
        Date date1 = sdf.parse("2018-07-21")
        assert statisticRepository.findByMsendedDateAfterOrderByMsendedDate(DateUtil.toDateString(date1)).size() == 6

        Date date2 = sdf.parse("2018-06-01")
        assert statisticRepository.findByMsendedDateAfterGroupByWeekOfYear(DateUtil.toDateString(date2)).size() == 15


        Date date3 = sdf.parse("2018-01-01")
        List list = statisticRepository.findByMsendedDateAfterGroupByMonthOfYear(DateUtil.toDateString(date3))
        assert list.size() == 9


        Runnable r = new Runnable() {
            @Override
            public void run() {

            }
        }
        pusherApp.startRunTask(r, true)

        try {
            HashMap<String, Object> params = new LinkedHashMap<String, Object>()
            params.put("dateType", "day")
            pusherApp.doRequestApi(params)
            params.put("appkey", "test")
            pusherApp.doRequestApi(params)

            params.clear()

            params.put("dateType", "week")
            pusherApp.doRequestApi(params)
            params.put("appkey", "test")
            pusherApp.doRequestApi(params)

            params.clear()

            params.put("dateType", "month")
            pusherApp.doRequestApi(params)
            params.put("appkey", "test")
            pusherApp.doRequestApi(params)
        } catch (Exception ex) {
        }

    }

    private class AppServerRequestMockService implements IPushApiServiceRequest {
        @Override
        String requestServiceServer(String baseUrl, String methodName, Map<String, Object> params, int timeout)
            throws Exception {
            return null
        }

        @Override
        String requestServiceServer(String baseUrl, Map<String, Object> params, int timeout) throws Exception {
            return JSONUtil.toJSON(apiRequest(baseUrl, params))
        }
    }
}
