package com.proper.enterprise.platform.workflow.api.notice

import com.proper.enterprise.platform.api.auth.service.AccessTokenService
import com.proper.enterprise.platform.test.AbstractIntegrationTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import com.proper.enterprise.platform.websocket.client.stomp.StompClient
import com.proper.enterprise.platform.websocket.client.stomp.StompFrameStringHandler
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
import org.flowable.engine.HistoryService
import org.flowable.engine.IdentityService
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.lang.Nullable
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.test.context.jdbc.Sql

import java.util.concurrent.CountDownLatch

@Ignore
class PupaAutoArchiveTest extends AbstractIntegrationTest {

    @Autowired
    IdentityService identityService

    @Autowired
    HistoryService historyService

    @Autowired
    private AccessTokenService tokenService

    def url, token

    private static final String PUPA_AUTO_ARCHIVE_KEY = 'pupaAutoArchive'


    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql", "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
    void pupaAutoArchive() {
        token = post('/auth/login', """{"username":"testuser3","pwd":"123456"}""", HttpStatus.OK).getInputStream().getText()
        url = "ws://localhost:$port/stomp?access_token=$token"
        def stompUser = 'u1'
        def client = StompClient.connect(stompUser, url)
        CountDownLatch latch = new CountDownLatch(1)
        client.subscribe("/topic/autoArchive", new StompFrameStringHandler() {
            @Override
            void handleFrame(StompHeaders headers, @Nullable Object payload) {
                Map result = JSONUtil.parse(payload, Map.class)
                if (result.get("procInstId")) {
                    latch.countDown()
                }
            }
        })
        identityService.setAuthenticatedUserId('user3')
        Map form1 = new HashMap()
        form1.put("a", "a")
        PEPProcInstVO pepProcInstVO = start(PUPA_AUTO_ARCHIVE_KEY, form1)
        assert isEnded(pepProcInstVO.getProcInstId())
        Thread.sleep(100000)
    }

    boolean isEnded(String procInstId) {
        return null != historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId).finished().singleResult()
    }

    protected PEPProcInstVO start(String key, Map<String, Object> form) {
        PEPProcInstVO pepProcInstVO = JSONUtil.parse(post('/workflow/process/' + key + "?access_token=" + token, JSONUtil.toJSON(form), HttpStatus.CREATED).getInputStream().getText(), PEPProcInstVO.class)
        return pepProcInstVO
    }
}
