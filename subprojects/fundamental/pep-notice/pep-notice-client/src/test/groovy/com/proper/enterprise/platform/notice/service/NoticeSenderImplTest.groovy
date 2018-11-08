package com.proper.enterprise.platform.notice.service

import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.notice.client.NoticeSender
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

@Sql
class NoticeSenderImplTest extends AbstractJPATest {

    @Autowired
    UserService userService

    @Autowired
    NoticeSender noticeSender

    @Test
    void sendNoticeSingle1() {
        Map<String, Object> custom = new HashMap<>()
        custom.put("url", "1")
        Map<String, Object> templateParams = new HashMap<>()
        templateParams.put("pageurl", "2")
        noticeSender.sendNotice("EndCode", custom, "test1", templateParams)
        noticeSender.sendNotice("EndCode", custom, "test2", "test1", templateParams)
    }

    @Test
    void sendNoticeBatch1() {
        Map<String, Object> custom = new HashMap<>()
        custom.put("url", "1")
        Map<String, Object> templateParams = new HashMap<>()
        templateParams.put("pageurl", "2")
        Set<String> userIds = new HashSet<>()
        userIds.add("test1")
        userIds.add(null)
        noticeSender.sendNotice("EndCode", custom, userIds, templateParams)
        noticeSender.sendNotice("EndCode", custom, "test2", userIds, templateParams)
    }

    @Test
    void sendNoticeSingle2() {
        Map<String, Object> custom = new HashMap<>()
        custom.put("url", "1")
        Map<String, Object> templateParams = new HashMap<>()
        templateParams.put("pageurl", "2")
        noticeSender.sendNotice("EndNotice", "BPM", custom, "test1", "title", "content")
        noticeSender.sendNotice("EndNotice", "BPM", custom, "test2", "test1", "title", "content")
    }

    @Test
    void sendNoticeBatch2() {
        Map<String, Object> custom = new HashMap<>()
        custom.put("url", "1")
        Map<String, Object> templateParams = new HashMap<>()
        templateParams.put("pageurl", "2")
        Set<String> userIds = new HashSet<>()
        userIds.add("test1")
        userIds.add(null)
        noticeSender.sendNotice("EndNotice", "BPM", custom, userIds, "title", "content")
        noticeSender.sendNotice("EndNotice", "BPM", custom, "test2", userIds, "title", "content")
    }

}
