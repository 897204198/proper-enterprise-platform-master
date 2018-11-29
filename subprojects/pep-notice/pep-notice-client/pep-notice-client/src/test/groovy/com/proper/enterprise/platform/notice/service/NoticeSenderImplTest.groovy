package com.proper.enterprise.platform.notice.service

import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.notice.document.NoticeDocument
import com.proper.enterprise.platform.notice.document.NoticeSetDocument
import com.proper.enterprise.platform.notice.entity.PushDeviceEntity
import com.proper.enterprise.platform.notice.enums.AnalysisResult
import com.proper.enterprise.platform.notice.enums.PushDeviceType
import com.proper.enterprise.platform.notice.enums.PushMode
import com.proper.enterprise.platform.notice.repository.NoticeMsgRepository
import com.proper.enterprise.platform.notice.repository.NoticeSetRepository
import com.proper.enterprise.platform.notice.repository.PushDeviceRepository
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType
import com.proper.enterprise.platform.notice.service.impl.NoticeSendServiceImpl
import com.proper.enterprise.platform.template.entity.TemplateEntity
import com.proper.enterprise.platform.template.repository.TemplateRepository
import com.proper.enterprise.platform.template.vo.TemplateDetailVO
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

@Sql
class NoticeSenderImplTest extends AbstractTest {

    @Autowired
    UserService userService

    @Autowired
    NoticeSender noticeSender

    @Autowired
    NoticeMsgRepository noticeMsgRepository

    @Autowired
    NoticeSetRepository noticeSetRepository

    @Autowired
    TemplateRepository templateRepository

    @Autowired
    NoticeSendServiceImpl noticeSendService;

    @Autowired
    PushDeviceRepository deviceRepository

    @Test
    void sendNoticeSingle() {
        Map<String, Object> custom = new HashMap<>()
        custom.put("url", "1")
        Map<String, Object> templateParams = new HashMap<>()
        templateParams.put("pageurl", "2")
        noticeSender.sendNotice("test1", "EndBpmCode", templateParams, custom)
        waitExecutorDone()
        noticeSender.sendNotice("test2", "test1", "EndBpmCode", templateParams, custom)
        waitExecutorDone()
    }

    @Test
    void sendNoticeBatch() {
        Map<String, Object> custom = new HashMap<>()
        custom.put("url", "1")
        Map<String, Object> templateParams = new HashMap<>()
        templateParams.put("pageurl", "2")
        Set<String> userIds = new HashSet<>()
        userIds.add("test1")
        userIds.add(null)
        noticeSender.sendNotice(userIds, "EndBpmCode", templateParams, custom)
        waitExecutorDone()
        noticeSender.sendNotice("test2", userIds, "EndBpmCode", templateParams, custom)
        waitExecutorDone()
    }

    @Test
    void sendNoticeWithContent() {
        Map<String, Object> custom = new HashMap<>()
        custom.put("url", "1")
        noticeSender.sendNotice("test2", "test title1", "test content1", custom, "BPM", NoticeType.PUSH)
        waitExecutorDone()
        noticeSender.sendNotice("test2", "test title2", "test content2", custom, "OB", NoticeType.PUSH)
        waitExecutorDone()
    }

    @Test
    void wrongUsers() {
        Map<String, Object> custom = new HashMap<>()
        custom.put("url", "1")
        Map<String, Object> templateParams = new HashMap<>()
        templateParams.put("pageurl", "2")
        Set<String> userIds = new HashSet<>()
        userIds.add("wrongUser")
        noticeSender.sendNotice("test2", userIds, "EndBpmCode", templateParams, custom)
        waitExecutorDone()
    }

    @Test
    void nullUsers() {
        Map<String, Object> custom = new HashMap<>()
        custom.put("url", "1")
        Map<String, Object> templateParams = new HashMap<>()
        templateParams.put("pageurl", "2")
        noticeSender.sendNotice("test2", null, "EndBpmCode", templateParams, custom)
        waitExecutorDone()
    }

    @Test
    void emptyUsers() {
        Map<String, Object> custom = new HashMap<>()
        custom.put("url", "1")
        Map<String, Object> templateParams = new HashMap<>()
        templateParams.put("pageurl", "2")
        Set<String> userIds = new HashSet<>()
        noticeSender.sendNotice("test2", "EndBpmCode", templateParams, custom)
        waitExecutorDone()
    }

    @Test
    void analysis() {

        Set<String> toUserIds = new HashSet<>()
        toUserIds.add("test1")
        toUserIds.add("test2")
        Map<String, NoticeSetDocument> noticeSetMap = new HashMap<>();
        NoticeSetDocument noticeSetDocument = new NoticeSetDocument()
        List<String> noticeChannel = new ArrayList<>()
        noticeChannel.add("push")
        noticeSetDocument.setNoticeChannel(noticeChannel)
        noticeSetMap.put("test1", noticeSetDocument)
        noticeSetMap.put("test2", noticeSetDocument)
        Map<String, Object> custom = new HashMap<>()
        noticeSendService.sendNoticeChannel(null, toUserIds, noticeSetMap, "title", "content", custom, NoticeType.PUSH)
        List<NoticeDocument> result = noticeMsgRepository.findAll()
        assert result.get(0).getAnalysisResult() == AnalysisResult.UNNECESSARY
        assert result.get(0).getTargets() == null
        assert result.get(0).getUsers().size() == 2
        assert result.get(0).getNotes().size() == 3
        assert result.get(0).getNotes().contains("test1 is missing device info, please re login to the app.")
        assert result.get(0).getNotes().contains("test2 is missing device info, please re login to the app.")
        assert result.get(0).getNotes().contains("Can't find effective notice receivers.")

        PushDeviceEntity pushDeviceEntity = new PushDeviceEntity()
        pushDeviceEntity.appKey = "MobileOADev"
        pushDeviceEntity.deviceId = "4665a261c995eeab5bac45545aac0b92"
        pushDeviceEntity.userId = "test1"
        pushDeviceEntity.deviceType = PushDeviceType.android
        pushDeviceEntity.deviceOtherInfo = "{\"model\":\"MI 5\",\"manufacturer\":\"Xiaomi\",\"brand\":\"Xiaomi\",\"sdk_int\":24}"
        pushDeviceEntity.pushMode = PushMode.XIAOMI
        pushDeviceEntity.pushToken = "w69uYXiVgywg4VE/GJmGnfnomKwfGYs743z09wGK8rjexgJ1hgmmg32O4WpahuFd"
        deviceRepository.save(pushDeviceEntity)
        noticeMsgRepository.deleteAll()

        noticeSendService.sendNoticeChannel(null, toUserIds, noticeSetMap, "title", "content", custom, NoticeType.PUSH)
        result = noticeMsgRepository.findAll()
        assert result.get(0).getAnalysisResult() == AnalysisResult.ERROR
        assert result.get(0).getTargets().size() == 1
        assert result.get(0).getUsers().size() == 2
        assert result.get(0).getNotes().size() == 2
        assert result.get(0).getNotes().contains("test2 is missing device info, please re login to the app.")
        assert result.get(0).getNotes().contains("The notice server url 'Not Configured' configuration error. ")
        noticeMsgRepository.deleteAll()
    }

    @Before
    void init() {
        NoticeSetDocument noticeSetDocument1 = new NoticeSetDocument()
        noticeSetDocument1.setUserId("test1")
        noticeSetDocument1.setCatalog("BPM")
        noticeSetDocument1.noticeChannel = ["sms", "push", "email"]
        noticeSetRepository.save(noticeSetDocument1)

        NoticeSetDocument noticeSetDocument2 = new NoticeSetDocument()
        noticeSetDocument2.setUserId("test2")
        noticeSetDocument2.setCatalog("BPM")
        noticeSetDocument2.noticeChannel = ["sms", "push", "email"]
        noticeSetRepository.save(noticeSetDocument2)

        TemplateEntity templateDocument = new TemplateEntity()
        templateDocument.code = "EndBpmCode"
        templateDocument.name = "EndBpmName"
        templateDocument.catalog = "BPM"
        templateDocument.muti = true
        List<TemplateDetailVO> details = new ArrayList<>()
        TemplateDetailVO detail1 = new TemplateDetailVO()
        detail1.title = "流程完结"
        detail1.template = "您的【{processDefinitionName}】已完成，详情请在【发起历史】中查看。"
        detail1.type = "EMAIL"
        details.add(detail1)
        TemplateDetailVO detail2 = new TemplateDetailVO()
        detail2.title = "您有一条已办消息"
        detail2.template = "您的{processDefinitionName}已完成"
        detail2.type = "PUSH"
        details.add(detail2)
        TemplateDetailVO detail3 = new TemplateDetailVO()
        detail3.title = "短信title"
        detail3.template = "短信content"
        detail3.type = "SMS"
        details.add(detail3)
        templateDocument.details = details
        templateRepository.save(templateDocument)
    }

    @After
    void destroy() {
        noticeSetRepository.deleteAll()
        templateRepository.deleteAll()
    }

}
