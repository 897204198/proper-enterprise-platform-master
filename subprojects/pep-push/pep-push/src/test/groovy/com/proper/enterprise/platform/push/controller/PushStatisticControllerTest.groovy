package com.proper.enterprise.platform.push.controller

import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.push.client.PusherApp
import com.proper.enterprise.platform.push.client.service.IPushApiServiceRequest
import com.proper.enterprise.platform.push.common.model.enums.PushMode
import com.proper.enterprise.platform.push.entity.PushMsgEntity
import com.proper.enterprise.platform.push.repository.PushMsgRepository
import com.proper.enterprise.platform.push.repository.PushMsgStatisticRepository
import com.proper.enterprise.platform.push.service.PushMsgService
import com.proper.enterprise.platform.push.service.PushMsgStatisticService
import com.proper.enterprise.platform.push.test.PushAbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.jdbc.Sql

import java.text.SimpleDateFormat

@Sql([
    "/com/proper/enterprise/platform/push/push-users.sql",
    "/com/proper/enterprise/platform/push/push-devices.sql",
    "/com/proper/enterprise/platform/push/push-msgs.sql",
    "/com/proper/enterprise/platform/push/push-statistic.sql"
])
class PushStatisticControllerTest extends PushAbstractTest {
    @Autowired
    PushMsgStatisticRepository statisticRepository;
    @Autowired
    PushMsgRepository msgRepository
    @Autowired
    PushMsgService pushMsgService
    @Autowired
    PushMsgStatisticService pushMsgStatisticService
    PusherApp pusherApp

    @Before
    void init() {
        IPushApiServiceRequest pushApiRequestMock = new AppServerRequestMockService()
        pusherApp = new PusherApp("/push/statistic", VALID_APPKEY, "wanchina")
        pusherApp.setPushApiRequest(pushApiRequestMock)
        pusherApp.setAsync(false)
    }

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

    @Test
    void testGetPushList() {
        pusherApp.setPushUrl("/push/list")
        Pageable pageable = new PageRequest(0, 3)

        PushMsgEntity entity = new PushMsgEntity()
        entity.setMcontent("content1")
        entity.setAppkey("test")
        entity.setPushMode(PushMode.valueOf("xiaomi"))

        Example<PushMsgEntity> example = Example.of(entity)
        Page<PushMsgEntity> page1 = msgRepository.findAll(example, pageable)

        assert page1.getTotalElements() == 1

        pushMsgService.findByDateTypeAndAppkey(example, pageable)

        try {
            HashMap<String, Object> params = new LinkedHashMap<String, Object>()
            params.put("pageNo", 1)
            params.put("pageSize", 3)
            params.put("mcontent", "content1")
            params.put("appkey", "test")
            params.put("pushMode", "xiaomi")
//            pusherApp.doRequestApis(params)
        } catch (Exception e) {
        }

    }

    @Test
    void testStatisticInit() {
        pusherApp.setPushUrl("/push/statistic/init")
        String msendDate = "2018-07-25"
        String msendDateEnd = "2018-07-26"

        pushMsgStatisticService.saveStatisticOfSomeday(msendDate)
        List list = statisticRepository.findAll()
        assert list.size() == 16
        statisticRepository.deleteByMsendedDate(msendDate)
        List listAfterDelete = statisticRepository.findAll()
        assert listAfterDelete.size() == 15

        try {
            HashMap<String, Object> params = new LinkedHashMap<String, Object>()
            params.put("date", msendDate)
            pusherApp.doRequestApi(params)
        } catch (Exception e) {
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
