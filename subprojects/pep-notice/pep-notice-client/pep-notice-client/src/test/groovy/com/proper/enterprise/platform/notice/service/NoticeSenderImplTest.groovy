package com.proper.enterprise.platform.notice.service

import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.notice.entity.NoticeSetDocument
import com.proper.enterprise.platform.notice.repository.NoticeMsgRepository
import com.proper.enterprise.platform.notice.repository.NoticeSetRepository
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType
import com.proper.enterprise.platform.test.AbstractTest
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

    @Test
    void sendNoticeSingle() {
        Map<String, Object> custom = new HashMap<>()
        custom.put("url", "1")
        Map<String, Object> templateParams = new HashMap<>()
        templateParams.put("pageurl", "2")
        noticeSender.sendNotice("test1", "EndCode", templateParams, custom)
        waitExecutorDone()
        noticeSender.sendNotice("test2", "test1", "EndCode", templateParams, custom)
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
        noticeSender.sendNotice(userIds, "EndCode", templateParams, custom)
        waitExecutorDone()
        noticeSender.sendNotice("test2", userIds, "EndCode", templateParams, custom)
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
        noticeSender.sendNotice("test2", userIds, "EndCode", templateParams, custom)
        waitExecutorDone()
    }

    @Test
    void nullUsers() {
        Map<String, Object> custom = new HashMap<>()
        custom.put("url", "1")
        Map<String, Object> templateParams = new HashMap<>()
        templateParams.put("pageurl", "2")
        noticeSender.sendNotice("test2", null, "EndCode", templateParams, custom)
        waitExecutorDone()
    }

    @Test
    void emptyUsers() {
        Map<String, Object> custom = new HashMap<>()
        custom.put("url", "1")
        Map<String, Object> templateParams = new HashMap<>()
        templateParams.put("pageurl", "2")
        Set<String> userIds = new HashSet<>()
        noticeSender.sendNotice("test2", "EndCode", templateParams, custom)
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
    }

}
