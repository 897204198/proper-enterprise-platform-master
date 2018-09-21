package com.proper.enterprise.platform.push.controller

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.exception.ErrMsgException
import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.push.api.PushMsg
import com.proper.enterprise.platform.push.client.PusherApp
import com.proper.enterprise.platform.push.client.service.IPushApiServiceRequest
import com.proper.enterprise.platform.push.common.model.enums.PushMode
import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus
import com.proper.enterprise.platform.push.entity.PushMsgEntity
import com.proper.enterprise.platform.push.entity.PushMsgStatisticEntity
import com.proper.enterprise.platform.push.repository.PushMsgRepository
import com.proper.enterprise.platform.push.repository.PushMsgStatisticRepository
import com.proper.enterprise.platform.push.service.PushMsgService
import com.proper.enterprise.platform.push.service.PushMsgStatisticService
import com.proper.enterprise.platform.push.test.PushAbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.*
import org.springframework.http.HttpStatus
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
    @Autowired
    private PushMsgStatisticRepository pushMsgStatisticRepository;

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
    void getPushMsgListTest() {
        pusherApp.setPushUrl("/push/list")
        Pageable pageable = new PageRequest(0, 3)

        PushMsgEntity entity = new PushMsgEntity()
        entity.setMcontent("content1abcdef")
        entity.setAppkey("test")
        entity.setPushMode(PushMode.valueOf("xiaomi"))

        Example<PushMsgEntity> example = Example.of(entity)
        Page<PushMsgEntity> page1 = msgRepository.findAll(example, pageable)

        assert page1.getTotalElements() == 1

        DataTrunk<? extends PushMsg> dataTrunk = pushMsgService.findByDateTypeAndAppkey(example, pageable)
        assert dataTrunk.count == 1
        assert dataTrunk.getData().findAll().get(0).mcontent == "content1ab"
        assert dataTrunk.getData().findAll().get(0).userid == "123****8901"

        try {
            HashMap<String, Object> params = new LinkedHashMap<String, Object>()
            params.put("pageNo", 1)
            params.put("pageSize", 3)
            params.put("mcontent", "content1abcdef")
            params.put("appkey", "test")
            params.put("pushMode", "xiaomi")
//            pusherApp.doRequestApis(params)
        } catch (Exception e) {
        }

    }

    @Test
    void getTest() {
        pusherApp.setPushUrl("/push/statistic/init")
        String msendDate = "2018-07-25"
        String msendDateEnd = "2018-07-26"

        pushMsgStatisticService.saveStatisticOfSomeday(msendDate)
        List list = statisticRepository.findAll()
        assert list.size() == 19
        statisticRepository.deleteByMsendedDate(msendDate)
        List listAfterDelete = statisticRepository.findAll()
        assert listAfterDelete.size() == 18

        try {
            HashMap<String, Object> params = new LinkedHashMap<String, Object>()
            params.put("date", msendDate)
            pusherApp.doRequestApi(params)
        } catch (Exception e) {
        }

    }

    @Test
    void orderByLastModifyTimeTest() {
        //测试查询排序

        PushMsgEntity entity = new PushMsgEntity()
        Sort sort = new Sort(Sort.Direction.DESC, "lastModifyTime")
        PageRequest pageRequest = new PageRequest(0, 30, sort)
        Example<PushMsgEntity> example = Example.of(entity)
        List<PushMsgEntity> list = msgRepository.findAll(example, pageRequest).getContent()
        String lastDate = null
        for (PushMsgEntity en : list) {
            if (lastDate != null) {
                assert lastDate > en.getLastModifyTime()
            }
            lastDate = en.getLastModifyTime()
        }
    }

    @Test
    void findByAppkeyTest() {
        //测试按应用查询

        PushMsgEntity entity = new PushMsgEntity()
        entity.setAppkey("test")
        Sort sort = new Sort(Sort.Direction.DESC, "lastModifyTime")
        PageRequest pageRequest = new PageRequest(0, 30, sort)
        Example<PushMsgEntity> example = Example.of(entity)
        List<PushMsgEntity> list = msgRepository.findAll(example, pageRequest).getContent()
        for (PushMsgEntity en : list) {
            assert "test".equals(en.getAppkey())
        }
    }

    @Test
    void findByMstatusTest() {
        //测试按状态查询

        PushMsgEntity entity = new PushMsgEntity()
        entity.setMstatus(PushMsgStatus.UNSEND)
        Sort sort = new Sort(Sort.Direction.DESC, "lastModifyTime")
        PageRequest pageRequest = new PageRequest(0, 30, sort)
        Example<PushMsgEntity> example = Example.of(entity)
        List<PushMsgEntity> list = msgRepository.findAll(example, pageRequest).getContent()
        for (PushMsgEntity en : list) {
            assert PushMsgStatus.UNSEND.ordinal() == en.getMstatus().ordinal()
        }
    }

    @Test
    void findByMcontentTest() {
        //测试按状态查询

        PushMsgEntity entity = new PushMsgEntity()
        entity.setMcontent("content1")
        Sort sort = new Sort(Sort.Direction.DESC, "lastModifyTime")
        PageRequest pageRequest = new PageRequest(0, 30, sort)
        Example<PushMsgEntity> example = Example.of(entity)
        List<PushMsgEntity> list = msgRepository.findAll(example, pageRequest).getContent()
        for (PushMsgEntity en : list) {
            assert "content1".equals(en.getMcontent())
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

    @Sql(["/com/proper/enterprise/platform/push/push-channel.sql",
        "/com/proper/enterprise/platform/push/push-statistic.sql"])
    @Test
    void testPushMsgPieHasDate() {
        Map haveData = JSONUtil.parse(get('/push/statistic/pie?startDate=2018-07-08&endDate=2018-07-22&appKey=test,MobileOADev', HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert null != haveData && haveData.size() > 0
        try {
            get('/push/statistic/pie?startDate=2018-07-08&endDate=2018-07-22&appKey=aaa', HttpStatus.OK)
        } catch (ErrMsgException e) {
            assert "appKey is illegal" == e.getMessage()
        }
    }

    @Sql("/com/proper/enterprise/platform/push/push-channel.sql")
    @Test
    void testPushMsgPieNotDate() {
        initDataPie()
        Map<String, Object> haveData = JSONUtil.parse(get('/push/statistic/pie?appKey=MobileOAPr', HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert null != haveData && haveData.size() > 0
    }

    private void initDataPie() {
        PushMsgStatisticEntity pushFail = new PushMsgStatisticEntity()
        pushFail.setAppkey('MobileOAPr')
        pushFail.setMnum(100)
        pushFail.setMsendedDate(DateUtil.toDateString(DateUtil.addDay(new Date(), -1)))
        pushFail.setMstatus(PushMsgStatus.UNSEND)
        pushFail.setPushMode('xiaomi')

        PushMsgStatisticEntity pushSuccess = new PushMsgStatisticEntity()
        pushSuccess.setAppkey('MobileOAPr')
        pushSuccess.setMnum(100)
        pushSuccess.setMsendedDate(DateUtil.toDateString(DateUtil.addDay(new Date(), -1)))
        pushSuccess.setMstatus(PushMsgStatus.SENDED)
        pushSuccess.setPushMode('xiaomi')

        pushMsgStatisticRepository.save(pushFail)
        pushMsgStatisticRepository.save(pushSuccess)
    }
}
