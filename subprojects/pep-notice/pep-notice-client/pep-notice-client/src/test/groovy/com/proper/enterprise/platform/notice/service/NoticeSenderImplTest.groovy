package com.proper.enterprise.platform.notice.service

import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.notice.entity.NoticeSetDocument
import com.proper.enterprise.platform.notice.repository.NoticeMsgRepository
import com.proper.enterprise.platform.notice.repository.NoticeSetRepository
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType
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

    @Before
    void init() {
        NoticeSetDocument noticeSetDocument1 = new NoticeSetDocument()
        noticeSetDocument1.setUserId("test1")
        noticeSetDocument1.setCatalog("BPM")
        noticeSetDocument1.setPush(true)
        noticeSetDocument1.setSms(true)
        noticeSetDocument1.setEmail(true)
        noticeSetRepository.save(noticeSetDocument1)

        NoticeSetDocument noticeSetDocument2 = new NoticeSetDocument()
        noticeSetDocument2.setUserId("test2")
        noticeSetDocument2.setCatalog("BPM")
        noticeSetDocument2.setPush(true)
        noticeSetDocument2.setSms(true)
        noticeSetDocument2.setEmail(true)
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
