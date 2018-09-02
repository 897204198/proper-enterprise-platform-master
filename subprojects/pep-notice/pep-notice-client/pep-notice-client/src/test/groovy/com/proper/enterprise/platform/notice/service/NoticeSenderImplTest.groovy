package com.proper.enterprise.platform.notice.service


import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.notice.client.NoticeSender
import com.proper.enterprise.platform.template.service.TemplateService
import com.proper.enterprise.platform.template.vo.TemplateVO
import com.proper.enterprise.platform.test.AbstractTest
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
    TemplateService templateService

    @Test
    void sendNoticeSingle() {
        Map<String, Object> custom = new HashMap<>()
        custom.put("url", "1")
        Map<String, Object> templateParams = new HashMap<>()
        templateParams.put("pageurl", "2")
        TemplateVO templateVO = templateService.getTemplates("EndCode", templateParams)
        noticeSender.sendNotice("test1", templateVO, custom)
        noticeSender.sendNotice("test2", "test1", templateVO, custom)
    }

    @Test
    void sendNoticeBatch() {
        Map<String, Object> custom = new HashMap<>()
        custom.put("url", "1")
        Map<String, Object> templateParams = new HashMap<>()
        templateParams.put("pageurl", "2")
        TemplateVO templateVO = templateService.getTemplates("EndCode", templateParams)
        Set<String> userIds = new HashSet<>()
        userIds.add("test1")
        userIds.add(null)
        noticeSender.sendNotice(userIds, templateVO, custom)
        noticeSender.sendNotice("test2", userIds, templateVO, custom)
    }

}
