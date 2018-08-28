package com.proper.enterprise.platform.workflow.frame

import com.proper.enterprise.platform.core.mongo.service.MongoShellService
import com.proper.enterprise.platform.workflow.test.WorkflowAbstractTest
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
import org.bson.Document
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

class AutoArchiveTest extends WorkflowAbstractTest {

    private static final String AUTO_ARCHIVE_KEY = 'autoArchive'

    @Autowired
    public MongoShellService mongoShellService

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql"])
    public void autoArchiveTest() {
        identityService.setAuthenticatedUserId('user1')
        Map form1 = new HashMap()
        form1.put("a", "a")
        PEPProcInstVO pepProcInstVO = start(AUTO_ARCHIVE_KEY, form1)
        Map task1 = getTask("form1")
        form1.put("a", "a1")
        form1.put("a1", "a1")
        complete(task1.taskId, form1)
        Map task12 = getTask("form12")
        form1.put("a", "a12")
        form1.put("a1", "a12")
        form1.put("a12", "a12")
        complete(task12.taskId, form1)
        Map task2 = getTask("form2")
        Map form2 = new HashMap()
        form2.put("b", "b")
        complete(task2.taskId, form2)
        assert isEnded(pepProcInstVO.getProcInstId())
        List<Document> form1s = mongoShellService.query("form1", null)
        assert form1s.size() == 1
        assert form1s.get(0).get("a") == "a12"
        assert form1s.get(0).get("a1") == "a12"
        assert form1s.get(0).get("a12") == "a12"
        List<Document> form2s = mongoShellService.query("form2", null)
        assert form2s.size() == 1
        assert form2s.get(0).get("b") == "b"
    }
}
