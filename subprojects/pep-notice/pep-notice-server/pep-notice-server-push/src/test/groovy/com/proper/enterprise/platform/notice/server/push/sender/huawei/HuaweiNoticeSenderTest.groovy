package com.proper.enterprise.platform.notice.server.push.sender.huawei

import com.proper.enterprise.platform.notice.server.api.configurator.NoticeConfigurator
import com.proper.enterprise.platform.notice.server.api.handler.NoticeSendHandler
import com.proper.enterprise.platform.notice.server.api.model.BusinessNoticeResult
import com.proper.enterprise.platform.notice.server.app.dao.entity.NoticeEntity
import com.proper.enterprise.platform.notice.server.app.dao.repository.NoticeRepository
import com.proper.enterprise.platform.notice.server.app.scheduler.NoticeStatusSyncScheduler
import com.proper.enterprise.platform.notice.server.push.constant.HuaweiConstant
import com.proper.enterprise.platform.notice.server.push.mock.MockRetryNotice
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum
import com.proper.enterprise.platform.notice.server.push.enums.huawei.HuaweiErrCodeEnum
import com.proper.enterprise.platform.notice.server.push.mock.MockPushNotice
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class HuaweiNoticeSenderTest extends AbstractTest {

    @Autowired
    private NoticeConfigurator pushNoticeConfigurator

    @Autowired
    private NoticeSendHandler pushNoticeSender

    @Autowired
    private NoticeRepository noticeRepository

    @Autowired
    private NoticeStatusSyncScheduler noticeStatusSyncScheduler

    @Test
    void testHuaweiPush() {
        def config = [:]
        config.put('appSecret', HuaweiConstant.CLIENT_SECRET)
        config.put('pushPackage', HuaweiConstant.PACKAGE_NAME)
        config.put('appId', HuaweiConstant.CLIENT_ID)

        Map request = new HashMap()
        request.put("pushChannel", PushChannelEnum.HUAWEI.toString())
        pushNoticeConfigurator.post('MobileOADev', config, request)

        def notice = new MockPushNotice()
        notice.setTargetTo(HuaweiConstant.TARGET_TO)
        notice.setAppKey('MobileOADev')
        notice.setTitle(System.getProperty('os.name'))
        notice.setContent("${System.getProperty('os.name')} ${System.getProperty('os.arch')} push this notification to test Huawei push app at ${new Date().format('yyyy-MM-dd HH:mm:ss')} in test case")

        notice.setTargetExtMsg('pushChannel', 'HUAWEI')

        def customs = [:]
        customs['_proper_badge'] = 2
        notice.setNoticeExtMsg('customs', customs)

        assert NoticeStatus.SUCCESS == pushNoticeSender.send(notice).getNoticeStatus()
        assert NoticeStatus.SUCCESS == pushNoticeSender.getStatus(notice).getNoticeStatus()

        notice.setNoticeExtMsg('push_type', 'chat')

        customs['_proper_pushtype'] = 'cmd'
        notice.setNoticeExtMsg('customs', customs)

        assert NoticeStatus.SUCCESS == pushNoticeSender.send(notice).getNoticeStatus()

        notice.setNoticeExtMsg('url',
            'https://icmp2.propersoft.cn/icmp/web/#/webapp/workflow/workflowMainPop?param=JTdCJTIydGFza09yUHJvY0RlZktleSUyMiUzQSUyMmU3OThhODdhLWU3MGUtMTFlOC1hYTA3LTAyNDJhYzExMDAwNCUyMiUyQyUyMnByb2NJbnN0SWQlMjIlM0ElMjJlNzk0NjM4Yi1lNzBlLTExZTgtYWEwNy0wMjQyYWMxMTAwMDQlMjIlMkMlMjJuYW1lJTIyJTNBJTIyJUU5JTgzJUE4JUU5JTk3JUE4JUU4JUI0JTlGJUU4JUI0JUEzJUU0JUJBJUJBJUU1JUFFJUExJUU2JTg5JUI5JTIyJTJDJTIyc3RhdGVDb2RlJTIyJTNBbnVsbCUyQyUyMmJ1c2luZXNzT2JqJTIyJTNBJTdCJTIyZm9ybVRpdGxlJTIyJTNBJTIyJUU1JUJDJUEwJUU1JTg5JTkxJUU2JTlFJTk3JUU3JTlBJTg0JUU1JTg3JUJBJUU1JUI3JUFFJUU3JTk0JUIzJUU4JUFGJUI3JUU2JUI1JTgxJUU3JUE4JThCJTIyJTdEJTJDJTIybGF1bmNoJTIyJTNBZmFsc2UlN0Q=&from=app')
        notice.setContent("url跳转测试")
        notice.setTitle("url跳转测试")
        assert NoticeStatus.SUCCESS == pushNoticeSender.send(notice).getNoticeStatus()

        notice.setAppKey("testFail")
        try {
            pushNoticeSender.send(notice)
        } catch (Exception e) {
            assert NoticeStatus.FAIL == pushNoticeSender.getStatus(notice).getNoticeStatus()
            assert "Please check Huawei push config" == pushNoticeSender.getStatus(notice).getMessage()
            e.getMessage().contains("Please check Huawei push config")
        }
    }

    @Test
    void testBeforeSend() {
        def notice = new MockPushNotice()
        notice.setTargetTo(HuaweiConstant.TARGET_TO)
        notice.setTitle(System.getProperty('os.name'))
        notice.setAppKey('MobileOADev')
        notice.setTargetExtMsg('pushChannel', 'HUAWEI')
        try {
            pushNoticeSender.beforeSend(notice)
        } catch (Exception e) {
            e.getMessage().contains("Can't get Huawei push config")
        }
    }

    @Test
    @Ignore
    void testAfterSend() {
        def notice = new MockPushNotice()
        notice.setTargetTo(HuaweiConstant.TARGET_TO)
        notice.setAppKey('MobileOADev')
        notice.setTitle(System.getProperty('os.name'))
        notice.setContent("${System.getProperty('os.name')} ${System.getProperty('os.arch')} push this notification to test Huawei push app at ${new Date().format('yyyy-MM-dd HH:mm:ss')} in test case")

        notice.setTargetExtMsg('pushChannel', 'HUAWEI')
        pushNoticeSender.afterSend(notice)
    }


    @Test
    void invalidTargetTest() {
        def config = [:]
        config.put('appSecret', HuaweiConstant.CLIENT_SECRET)
        config.put('pushPackage', HuaweiConstant.PACKAGE_NAME)
        config.put('appId', HuaweiConstant.CLIENT_ID)

        Map request = new HashMap()
        request.put("pushChannel", PushChannelEnum.HUAWEI.toString())
        pushNoticeConfigurator.post('MobileOADev1', config, request)

        def notice = new MockPushNotice()
        notice.setTargetTo("0867110029070702300001436000CN32")
        notice.setAppKey('MobileOADev1')
        notice.setTitle(System.getProperty('os.name'))
        notice.setContent("${System.getProperty('os.name')} ${System.getProperty('os.arch')} push this notification to test Huawei push app at ${new Date().format('yyyy-MM-dd HH:mm:ss')} in test case")

        notice.setTargetExtMsg('pushChannel', 'HUAWEI')
        notice.setNoticeExtMsg('push_type', '')

        def customs = [:]
        customs['_proper_badge'] = 2
        notice.setNoticeExtMsg('customs', customs)

        BusinessNoticeResult businessNoticeResult = pushNoticeSender.send(notice)
        assert HuaweiErrCodeEnum.INVALID_TARGET.getNoticeCode() == businessNoticeResult.getCode()
        assert HuaweiErrCodeEnum.INVALID_TARGET.getCode() == businessNoticeResult.getMessage()
    }

    @Test
    @NoTx
    void serviceUnavailableTest() {
        MockRetryNotice mockRetryNotice = new MockRetryNotice()
        ResponseEntity<byte[]> res = new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE)
        def notice = new MockPushNotice()

        NoticeEntity noticeSync = new NoticeEntity()
        noticeSync.setAppKey('MobileOADev1')
        noticeSync.setTargetTo("0867110029070702300001436000CN32")
        noticeSync.setTitle(System.getProperty('os.name'))
        noticeSync.setContent("${System.getProperty('os.name')} ${System.getProperty('os.arch')} push this notification to test Huawei push app at ${new Date().format('yyyy-MM-dd HH:mm:ss')} in test case")
        noticeSync.setNoticeType(NoticeType.PUSH)
        noticeSync.setTargetExtMsg("pushChannel", "HUAWEI")
        noticeSync.setBatchId("batchId")
        noticeSync.setRetryCount(0)
        noticeSync.setStatus(NoticeStatus.PENDING)
        def id = noticeRepository.save(noticeSync).getId()
        BusinessNoticeResult businessNoticeResult = mockRetryNotice.isSuccess(res, notice)
        if (businessNoticeResult.getNoticeStatus() == NoticeStatus.RETRY) {
            NoticeEntity noticeSync2 = noticeRepository.findOne(id)
            noticeSync2.setStatus(NoticeStatus.RETRY)
            noticeSync2.setErrorCode(businessNoticeResult.getCode())
            noticeSync2.setErrorMsg(businessNoticeResult.getMessage())
            noticeRepository.updateForSelective(noticeSync2)
        }
        assert noticeRepository.findOne(id).getStatus() == NoticeStatus.RETRY
        assert noticeRepository.findOne(id).getRetryCount() == 0

        noticeStatusSyncScheduler.syncRetry()
        assert noticeRepository.findOne(id).getRetryCount() == 1
    }

}
