package com.proper.enterprise.platform.workflow.api.notice

import com.proper.enterprise.platform.core.mongo.dao.MongoDAO
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
import org.bson.Document
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

class PupaAutoArchiveTest extends WorkflowAbstractTest {

    private static final String AUTO_ARCHIVETEST_KEY = "gd"

    @Autowired
    private MongoDAO mongoDAO

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql", "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
    public void autoArchive() {
        initPupaConfig()
        identityService.setAuthenticatedUserId('user1')
        Map data = new HashMap()
        data.put("test", "test")
        PEPProcInstVO pepProcInstVO = start(AUTO_ARCHIVETEST_KEY, data)
        assert isEnded(pepProcInstVO.getProcInstId())
        List<Document> archiveData = mongoDAO.query("PEP_PUPA_GD", "{test:'test'}")
        assert archiveData.size() == 1
    }

    private void initPupaConfig() {
        mongoDAO.insertOne("PEP_DEVTOOLS_CUSTOMQUERY", "{\n" +
            "  \"id\" : \"\",\n" +
            "  \"functionName\" : \"测试归档\",\n" +
            "  \"tableName\" : \"PEP_PUPA_GD\",\n" +
            "  \"code\" : \"gd\",\n" +
            "  \"note\" : \"\",\n" +
            "  \"enable\" : true,\n" +
            "  \"_ClientVersion\" : \"js0.6.4-proper\",\n" +
            "  \"_InstallationId\" : \"035c3dc2-df17-d8cc-1867-283cff376e79\",\n" +
            "  \"CT\" : \"2019-05-14 11:19:12\",\n" +
            "  \"LT\" : \"2019-05-14 11:55:51\",\n" +
            "  \"CU\" : \"pep-sysadmin\",\n" +
            "  \"LU\" : \"pep-sysadmin\",\n" +
            "  \"formConfig\" : \"{\\\"formLayout\\\":\\\"horizontal\\\",\\\"formJson\\\":[{\\\"label\\\":\\\"输入框\\\",\\\"key\\\":\\\"Input\\\",\\\"component\\\":{\\\"name\\\":\\\"Input\\\"},\\\"name\\\":\\\"test\\\",\\\"active\\\":false}]}\",\n" +
            "  \"gridConfig\" : \"{\\\"columns\\\":[{\\\"_id\\\":\\\"tq2vollcld8\\\",\\\"title\\\":\\\"输入框\\\",\\\"dataIndex\\\":\\\"qqq\\\"}],\\\"topButtons\\\":[{\\\"_id\\\":\\\"1557806151054.9058\\\",\\\"text\\\":\\\"发起\\\",\\\"name\\\":\\\"start\\\",\\\"position\\\":\\\"top\\\",\\\"type\\\":\\\"primary\\\",\\\"icon\\\":\\\"plus\\\",\\\"enable\\\":true,\\\"default\\\":true},{\\\"_id\\\":\\\"1557803983844.955\\\",\\\"text\\\":\\\"新建\\\",\\\"name\\\":\\\"create\\\",\\\"position\\\":\\\"top\\\",\\\"type\\\":\\\"primary\\\",\\\"icon\\\":\\\"plus\\\",\\\"enable\\\":true,\\\"default\\\":true},{\\\"_id\\\":\\\"1557803983844.6018\\\",\\\"text\\\":\\\"删除\\\",\\\"name\\\":\\\"batchDelete\\\",\\\"position\\\":\\\"top\\\",\\\"type\\\":\\\"default\\\",\\\"icon\\\":\\\"delete\\\",\\\"display\\\":\\\"items=>(items.length > 0)\\\",\\\"enable\\\":true,\\\"default\\\":true}],\\\"rowButtons\\\":[{\\\"_id\\\":\\\"1557803983844.8403\\\",\\\"text\\\":\\\"编辑\\\",\\\"name\\\":\\\"edit\\\",\\\"position\\\":\\\"row\\\",\\\"icon\\\":\\\"edit\\\",\\\"type\\\":\\\"default\\\",\\\"enable\\\":true,\\\"default\\\":true},{\\\"_id\\\":\\\"1557803983844.7983\\\",\\\"text\\\":\\\"删除\\\",\\\"name\\\":\\\"delete\\\",\\\"position\\\":\\\"row\\\",\\\"icon\\\":\\\"delete\\\",\\\"type\\\":\\\"default\\\",\\\"enable\\\":true,\\\"default\\\":true}]}\",\n" +
            "  \"modalConfig\" : \"{\\\"title\\\":\\\"\\\",\\\"width\\\":1000,\\\"footer\\\":[\\\"submit\\\",\\\"cancel\\\"],\\\"saveAfterClosable\\\":true,\\\"maskClosable\\\":false}\",\n" +
            "  \"relaWf\" : true,\n" +
            "  \"wfKey\" : \"gd\"\n" +
            "}")

    }
}
