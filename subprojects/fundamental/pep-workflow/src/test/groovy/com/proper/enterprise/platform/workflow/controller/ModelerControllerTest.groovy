package com.proper.enterprise.platform.workflow.controller

import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.utils.JSONUtil
import com.proper.enterprise.platform.workflow.flowable.rest.api.ModelerController
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class ModelerControllerTest extends AbstractJPATest {

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql", "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
    public void modelerCandidateTest() {
        //候选用户组查询
        List<ModelerController.ModelerIdmModel> candidateGroup1s = JSONUtil.parse(get("/workflow/service/app/rest/ext/modeler/candidate/GROUP?name=group", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert candidateGroup1s.size() == 2
        List<ModelerController.ModelerIdmModel> candidateGroup2s = JSONUtil.parse(get("/workflow/service/app/rest/ext/modeler/candidate/GROUP?name=group1", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert candidateGroup2s.size() == 1
        assert candidateGroup2s.get(0).name == "testgroup1"
        assert candidateGroup2s.get(0).id == "group1:GROUP"

        //候选角色查询
        List<ModelerController.ModelerIdmModel> candidateRole1s = JSONUtil.parse(get("/workflow/service/app/rest/ext/modeler/candidate/ROLE?name=role", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert candidateRole1s.size() == 2
        List<ModelerController.ModelerIdmModel> candidateRole2s = JSONUtil.parse(get("/workflow/service/app/rest/ext/modeler/candidate/ROLE?name=role1", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert candidateRole2s.size() == 1
        assert candidateRole2s.get(0).name == "testrole1"
        assert candidateRole2s.get(0).id == "role1:ROLE"

        //候选用户查询
        List<ModelerController.ModelerIdmModel> candidateUser1s = JSONUtil.parse(get("/workflow/service/app/rest/ext/modeler/candidate/USER?name=user", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert candidateUser1s.size() == 3
        List<ModelerController.ModelerIdmModel> candidateUser2s = JSONUtil.parse(get("/workflow/service/app/rest/ext/modeler/candidate/USER?name=user1", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert candidateUser2s.size() == 1
        assert candidateUser2s.get(0).name == "c"
        assert candidateUser2s.get(0).id == "user1"
    }

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql", "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
    public void modelerAssignee(){

        //候选用户查询
        List<ModelerController.ModelerIdmModel> assigneeUser1s = JSONUtil.parse(get("/workflow/service/app/rest/ext/modeler/assignee?name=user", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert assigneeUser1s.size() == 3
        List<ModelerController.ModelerIdmModel> assigneeUser2s = JSONUtil.parse(get("/workflow/service/app/rest/ext/modeler/assignee?name=user1", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert assigneeUser2s.size() == 1
        assert assigneeUser2s.get(0).name == "c"
        assert assigneeUser2s.get(0).id == "user1"
    }
}
