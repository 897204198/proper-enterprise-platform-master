package com.proper.enterprise.platform.notice.server.push.service

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.notice.server.api.model.App
import com.proper.enterprise.platform.notice.server.push.dao.entity.PushNoticeMsgStatisticEntity
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushNoticeMsgJpaRepository
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushNoticeMsgStatisticRepository
import com.proper.enterprise.platform.notice.server.push.dao.service.PushNoticeMsgStatisticService
import com.proper.enterprise.platform.notice.server.push.enums.PushDataAnalysisDateRangeEnum
import com.proper.enterprise.platform.notice.server.push.vo.PushMsgPieDataVO
import com.proper.enterprise.platform.notice.server.push.vo.PushNoticeMsgPieVO
import com.proper.enterprise.platform.notice.server.push.vo.PushServiceDataAnalysisVO
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.jdbc.Sql

class PushNoticeMsgStatisticServiceTest extends AbstractJPATest {

    @Autowired
    private PushNoticeMsgStatisticService pushNoticeMsgStatisticService

    @Autowired
    private PushNoticeMsgStatisticRepository pushNoticeMsgStatisticRepository

    @Autowired
    private PushNoticeMsgJpaRepository pushNoticeMsgJpaRepository


    @Test
    @Sql([
        "/com/proper/enterprise/platform/notice/server/push/statistic/push-msg-saveYesterdayPushStatistic.sql"
    ])
    public void getPushStatistic() {
        assert pushNoticeMsgJpaRepository.count() == 6
        assert pushNoticeMsgStatisticRepository.count() == 0
        List<PushNoticeMsgStatisticEntity> pushNoticeMsgStatistics = pushNoticeMsgStatisticService.getPushStatistic(DateUtil.toDate("2018-07-25"), DateUtil.toDate("2018-07-26"))
        assert pushNoticeMsgStatistics.size() == 5
    }

    @Test
    @Sql([
        "/com/proper/enterprise/platform/notice/server/push/statistic/push-msg-findByDateTypeAndAppKeyDay.sql"
    ])
    public void findByDateTypeAndAppKeyDayTest() {

        List<PushNoticeMsgStatisticEntity> pushNoticeMsgStatistics25 = pushNoticeMsgStatisticService.getPushStatistic(DateUtil.toDate("2018-07-25"), DateUtil.toDate("2018-07-26"))
        pushNoticeMsgStatisticService.saveAll(pushNoticeMsgStatistics25)
        pushNoticeMsgStatisticRepository.deleteBySendDate("2018-07-25")
        pushNoticeMsgStatisticService.saveAll(pushNoticeMsgStatisticService.getPushStatistic(DateUtil.toDate("2018-07-25"), DateUtil.toDate("2018-07-26")))

        List<PushNoticeMsgStatisticEntity> pushNoticeMsgStatistics24 = pushNoticeMsgStatisticService.getPushStatistic(DateUtil.toDate("2018-07-24"), DateUtil.toDate("2018-07-25"))
        pushNoticeMsgStatisticService.saveAll(pushNoticeMsgStatistics24)

        List<PushNoticeMsgStatisticEntity> pushNoticeMsgStatistics23 = pushNoticeMsgStatisticService.getPushStatistic(DateUtil.toDate("2018-07-23"), DateUtil.toDate("2018-07-24"))
        pushNoticeMsgStatisticService.saveAll(pushNoticeMsgStatistics23)


        List<PushNoticeMsgStatisticEntity> pushNoticeMsgStatistics21 = pushNoticeMsgStatisticService.getPushStatistic(DateUtil.toDate("2018-07-21"), DateUtil.toDate("2018-07-22"))
        pushNoticeMsgStatisticService.saveAll(pushNoticeMsgStatistics21)

        List<PushServiceDataAnalysisVO> day = pushNoticeMsgStatisticService.findByDateTypeAndAppKey(DateUtil.toDate("2018-07-25"),
            PushDataAnalysisDateRangeEnum.DAY, null)

        assert day.size() == 7
        //25号
        assert day.get(6).getIosDataAnalysis().getSuccessCount() == 1
        assert day.get(6).getHuaweiDataAnalysis().getFailCount() == 1
        assert day.get(6).getXiaomiDataAnalysis().getFailCount() == 1

        //24号
        assert day.get(5).getXiaomiDataAnalysis().getFailCount() == 1

        //23号
        assert day.get(4).getIosDataAnalysis().getFailCount() == 1

        //21号
        assert day.get(2).getHuaweiDataAnalysis().getFailCount() == 1

        //验证APP_KEY
        pushNoticeMsgStatisticRepository.deleteAll()
        List<PushNoticeMsgStatisticEntity> pushNoticeMsgStatistics25Again = pushNoticeMsgStatisticService.getPushStatistic(DateUtil.toDate("2018-07-25"), DateUtil.toDate("2018-07-26"))
        pushNoticeMsgStatisticService.saveAll(pushNoticeMsgStatistics25Again)

        List<PushServiceDataAnalysisVO> dayAppKeyTest = pushNoticeMsgStatisticService.findByDateTypeAndAppKey(DateUtil.toDate("2018-07-25"),
            PushDataAnalysisDateRangeEnum.DAY, "test")
        //25号
        assert dayAppKeyTest.get(6).getIosDataAnalysis().getSuccessCount() == 1
        assert dayAppKeyTest.get(6).getHuaweiDataAnalysis().getFailCount() == 1
        assert dayAppKeyTest.get(6).getXiaomiDataAnalysis().getFailCount() == 1

        List<PushServiceDataAnalysisVO> dayAppKeyTest2 = pushNoticeMsgStatisticService.findByDateTypeAndAppKey(DateUtil.toDate("2018-07-25"),
            PushDataAnalysisDateRangeEnum.DAY, "test2")

        assert dayAppKeyTest2.get(0).getIosDataAnalysis().getSuccessCount() == 0
        assert dayAppKeyTest2.get(0).getHuaweiDataAnalysis().getFailCount() == 0
        assert dayAppKeyTest2.get(0).getXiaomiDataAnalysis().getFailCount() == 0
    }

    @Test
    @Sql([
        "/com/proper/enterprise/platform/notice/server/push/statistic/push-msg-findByDateTypeAndAppKeyWeek.sql"
    ])
    public void findByDateTypeAndAppKeyWeekTest() {

        List<PushNoticeMsgStatisticEntity> pushNoticeMsgStatistics = pushNoticeMsgStatisticService.getPushStatistic(DateUtil.toDate("2018-07-06"), DateUtil.toDate("2018-07-26"))
        pushNoticeMsgStatisticService.saveAll(pushNoticeMsgStatistics)

        List<PushServiceDataAnalysisVO> week = pushNoticeMsgStatisticService.findByDateTypeAndAppKey(DateUtil.toDate("2018-07-25"),
            PushDataAnalysisDateRangeEnum.WEEK, null)

        assert week.size() == 7
        //第一周 23-29
        assert week.get(6).getIosDataAnalysis().getSuccessCount() == 1
        assert week.get(6).getHuaweiDataAnalysis().getFailCount() == 1
        assert week.get(6).getXiaomiDataAnalysis().getFailCount() == 1

        //第二周 16-22
        assert week.get(5).getXiaomiDataAnalysis().getFailCount() == 1

        //第三周 9-15
        assert week.get(4).getIosDataAnalysis().getFailCount() == 1

        //第四周 2-8
        assert week.get(3).getHuaweiDataAnalysis().getFailCount() == 1

        List<PushServiceDataAnalysisVO> weekAppKeyTest = pushNoticeMsgStatisticService.findByDateTypeAndAppKey(DateUtil.toDate("2018-07-25"),
            PushDataAnalysisDateRangeEnum.WEEK, "test")
        //第一周 appKey TEST
        assert weekAppKeyTest.get(6).getIosDataAnalysis().getSuccessCount() == 1
        assert weekAppKeyTest.get(6).getHuaweiDataAnalysis().getFailCount() == 1
        assert weekAppKeyTest.get(6).getXiaomiDataAnalysis().getFailCount() == 1

        //第一周 appKey TEST2
        List<PushServiceDataAnalysisVO> weekAppKeyTest2 = pushNoticeMsgStatisticService.findByDateTypeAndAppKey(DateUtil.toDate("2018-07-25"),
            PushDataAnalysisDateRangeEnum.WEEK, "test2")

        assert weekAppKeyTest2.get(6).getIosDataAnalysis().getSuccessCount() == 0
        assert weekAppKeyTest2.get(6).getHuaweiDataAnalysis().getFailCount() == 0
        assert weekAppKeyTest2.get(6).getXiaomiDataAnalysis().getFailCount() == 0
    }


    @Test
    @Sql([
        "/com/proper/enterprise/platform/notice/server/push/statistic/push-msg-findByDateTypeAndAppKeyMonth.sql"
    ])
    public void findByDateTypeAndAppKeyMonthTest() {

        List<PushNoticeMsgStatisticEntity> pushNoticeMsgStatistics = pushNoticeMsgStatisticService.getPushStatistic(DateUtil.toDate("2018-03-06"), DateUtil.toDate("2018-07-26"))
        pushNoticeMsgStatisticService.saveAll(pushNoticeMsgStatistics)

        List<PushServiceDataAnalysisVO> month = pushNoticeMsgStatisticService.findByDateTypeAndAppKey(DateUtil.toDate("2018-07-25"),
            PushDataAnalysisDateRangeEnum.MONTH, null)

        assert month.size() == 7
        //第一月
        assert month.get(6).getIosDataAnalysis().getSuccessCount() == 1
        assert month.get(6).getHuaweiDataAnalysis().getFailCount() == 1
        assert month.get(6).getXiaomiDataAnalysis().getFailCount() == 1

        //第二月
        assert month.get(5).getXiaomiDataAnalysis().getFailCount() == 1

        //第三月
        assert month.get(4).getIosDataAnalysis().getFailCount() == 1

        //第四月
        assert month.get(3).getHuaweiDataAnalysis().getFailCount() == 1

        List<PushServiceDataAnalysisVO> monthAppKeyTest = pushNoticeMsgStatisticService.findByDateTypeAndAppKey(DateUtil.toDate("2018-07-25"),
            PushDataAnalysisDateRangeEnum.MONTH, "test")
        //第一月
        assert monthAppKeyTest.get(6).getIosDataAnalysis().getSuccessCount() == 1
        assert monthAppKeyTest.get(6).getHuaweiDataAnalysis().getFailCount() == 1
        assert monthAppKeyTest.get(6).getXiaomiDataAnalysis().getFailCount() == 1

        //第一月
        List<PushServiceDataAnalysisVO> monthAppKeyTest2 = pushNoticeMsgStatisticService.findByDateTypeAndAppKey(DateUtil.toDate("2018-07-25"),
            PushDataAnalysisDateRangeEnum.MONTH, "test2")

        assert monthAppKeyTest2.get(6).getIosDataAnalysis().getSuccessCount() == 0
        assert monthAppKeyTest2.get(6).getHuaweiDataAnalysis().getFailCount() == 0
        assert monthAppKeyTest2.get(6).getXiaomiDataAnalysis().getFailCount() == 0
    }

    @Test
    @Sql([
        "/com/proper/enterprise/platform/notice/server/push/statistic/push-msg-findByDateTypeAndAppKeyDay.sql"
    ])
    public void saveStatisticSomedayTest() {
        assert pushNoticeMsgJpaRepository.count() == 6
        assert pushNoticeMsgStatisticRepository.count() == 0
        pushNoticeMsgStatisticService.saveStatisticSomeday("2018-07-25")
        assert pushNoticeMsgStatisticRepository.count() == 3
    }

    @Test
    @Sql([
        "/com/proper/enterprise/platform/notice/server/push/statistic/push-app.sql"
    ])
    public void getPieDataItem() {
        List<PushNoticeMsgStatisticEntity> pushNoticeMsgStatistics = pushNoticeMsgStatisticService.getPushStatistic(DateUtil.toDate("2018-07-06"), DateUtil.toDate("2018-07-26"))
        pushNoticeMsgStatisticService.saveAll(pushNoticeMsgStatistics)

        List<PushMsgPieDataVO> items = pushNoticeMsgStatisticService.findPieItems("2018-01-01", '2018-12-12')
        assert 3 == items.size()
        PushNoticeMsgPieVO pushNoticeMsgPieVO = pushNoticeMsgStatisticService.findPieDataByDateAndAppKey("2018-01-01", '2018-12-12', 'test,test2')
        pushNoticeMsgPieVO.getPieData()
        for (PushMsgPieDataVO pushMsgPieDataVO : pushNoticeMsgPieVO.getPieData()) {
            if ("test" == pushMsgPieDataVO.getAppKey()) {
                assert pushMsgPieDataVO.getTotalNum() == 5
                assert pushMsgPieDataVO.getFailNum() == 4
                assert pushMsgPieDataVO.getSuccessNum() == 1
            }
            if ("test2" == pushMsgPieDataVO.getAppKey()) {
                assert pushMsgPieDataVO.getTotalNum() == 1
                assert pushMsgPieDataVO.getFailNum() == 1
                assert pushMsgPieDataVO.getSuccessNum() == 0
            }
        }
        List<PushMsgPieDataVO> pushMsgPieDataVOs = pushNoticeMsgPieVO.getPieData()
        assert 2 == pushMsgPieDataVOs.size()
        PushMsgPieDataVO pushMsgPieDataVO1 = pushMsgPieDataVOs.get(0)
        PushMsgPieDataVO pushMsgPieDataVO2 = pushMsgPieDataVOs.get(1)
        assert pushMsgPieDataVO1.getTotalNum() >= pushMsgPieDataVO2.getTotalNum()
    }

    @Test
    @Sql([
        "/com/proper/enterprise/platform/notice/server/push/statistic/push-app.sql"
    ])
    public void getAppTest() {
        PageRequest pageRequest = new PageRequest(0, 10);
        DataTrunk<App> dataTrunk = pushNoticeMsgStatisticService.findApp(null, null, null, null, pageRequest);
        assert dataTrunk.data.size() == 3
    }

}
