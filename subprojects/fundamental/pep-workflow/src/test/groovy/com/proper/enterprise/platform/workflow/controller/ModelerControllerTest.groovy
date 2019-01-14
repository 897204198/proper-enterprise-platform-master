package com.proper.enterprise.platform.workflow.controller

import com.proper.enterprise.platform.core.i18n.I18NUtil
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
        List<ModelerController.ModelerIdmModel> candidateGroup1s = JSONUtil.parse(get("/workflow/ext/modeler/candidate/GROUP?name=group", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert candidateGroup1s.size() == 2
        List<ModelerController.ModelerIdmModel> candidateGroup2s = JSONUtil.parse(get("/workflow/ext/modeler/candidate/GROUP?name=group1", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert candidateGroup2s.size() == 1
        assert candidateGroup2s.get(0).name == "testgroup1"
        assert candidateGroup2s.get(0).id == "group1_GROUP"
        assert candidateGroup2s.get(0).typeName == "候选用户组"

        //候选角色查询
        List<ModelerController.ModelerIdmModel> candidateRole1s = JSONUtil.parse(get("/workflow/ext/modeler/candidate/ROLE?name=role", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert candidateRole1s.size() == 2
        List<ModelerController.ModelerIdmModel> candidateRole2s = JSONUtil.parse(get("/workflow/ext/modeler/candidate/ROLE?name=role1", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert candidateRole2s.size() == 1
        assert candidateRole2s.get(0).name == "testrole1"
        assert candidateRole2s.get(0).id == "role1_ROLE"
        assert candidateRole2s.get(0).typeName == "候选角色"

        //候选用户查询
        List<ModelerController.ModelerIdmModel> candidateUser1s = JSONUtil.parse(get("/workflow/ext/modeler/candidate/USER?name=b", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert candidateUser1s.size() == 1
        List<ModelerController.ModelerIdmModel> candidateUser2s = JSONUtil.parse(get("/workflow/ext/modeler/candidate/USER?name=c", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert candidateUser2s.size() == 1
        assert candidateUser2s.get(0).name == "c"
        assert candidateUser2s.get(0).typeName == "候选人"
        assert candidateUser2s.get(0).id == "user1"
    }

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql", "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
    public void modelerAssignee(){

        //候选用户查询
        List<ModelerController.ModelerIdmModel> assigneeUser1s = JSONUtil.parse(get("/workflow/ext/modeler/assignee?name=b", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert assigneeUser1s.size() == 1
        List<ModelerController.ModelerIdmModel> assigneeUser2s = JSONUtil.parse(get("/workflow/ext/modeler/assignee?name=c", HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert assigneeUser2s.size() == 1
        assert assigneeUser2s.get(0).typeName == "候选人"
        assert assigneeUser2s.get(0).name == "c"
        assert assigneeUser2s.get(0).id == "user1"
    }

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql", "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
    void parsingCondition() {
        def data = [:]
        data['sequenceCondition'] = "\${organizationName=='研发部'&&(vacationTime<2||vacationType=='事假')}"
        def list = [["id":"organizationName","name":"部门"],
                    ["id":"vacationTime","name":"请假时长"],
                    ["id":"vacationType","name":"请假类型"]]
        data['params'] = list
        data['parsing'] = true
        def result = resOfPost("/workflow/ext/modeler/condition", JSONUtil.toJSON(data), HttpStatus.OK)
        assert "\${部门} == '研发部' && (\${请假时长} < 2 || \${请假类型} == '事假')" == result.sequenceCondition

        data['sequenceCondition'] = "\${部门}=='研发部'&&(\${请假时长}<2||\${请假类型}=='事假')"
        list = [["id":"organizationName","name":"部门"],
                ["id":"vacationTime","name":"请假时长"],
                ["id":"vacationType","name":"请假类型"]]
        data['params'] = list
        data['parsing'] = false
        result = resOfPost("/workflow/ext/modeler/condition", JSONUtil.toJSON(data), HttpStatus.OK)
        assert "\${organizationName=='研发部'&&(vacationTime<2||vacationType=='事假')}" == result.sequenceCondition

        def dataNatural = [:]
        dataNatural['sequenceCondition'] = "(\${请假时长}+\${出差时长})<2 && \${申请人} =='大哥' ||(\${请假时长}/\${出差时长}>=5)"
        def listParam = [["id":"vacationTime","name":"请假时长"],
                         ["id":"tripTime","name":"出差时长"],
                         ["id":"initiator","name":"申请人"]]
        dataNatural['params'] = listParam
        result = resOfPost("/workflow/ext/modeler/condition", JSONUtil.toJSON(dataNatural), HttpStatus.OK)
        assert "\${(vacationTime+tripTime)<2 && initiator =='大哥' ||(vacationTime/tripTime>=5)}" == result.sequenceCondition

        def dataFlowable = [:]
        dataFlowable['sequenceCondition'] = result.sequenceCondition
        dataFlowable['params'] = listParam
        dataFlowable['parsing'] = true
        result = resOfPost("/workflow/ext/modeler/condition", JSONUtil.toJSON(dataFlowable), HttpStatus.OK)
        assert "(\${请假时长} + \${出差时长}) < 2 && \${申请人} == '大哥' || (\${请假时长} / \${出差时长} >= 5)" == result.sequenceCondition

        dataNatural = [:]
        dataNatural['sequenceCondition'] = "({请假时长}+\${出差时长})<2 && \${申请人} =='大哥' ||(\${请假时长}/\${出差时长}>=5)"
        listParam = [["id":"vacationTime","name":"请假时长"],
                     ["id":"tripTime","name":"出差时长"],
                     ["id":"initiator","name":"申请人"]]
        dataNatural['params'] = listParam
        result = resOfPost("/workflow/ext/modeler/condition", JSONUtil.toJSON(dataNatural), HttpStatus.INTERNAL_SERVER_ERROR)
        assert I18NUtil.getMessage("workflow.condition.parse.error") == result

        dataFlowable['sequenceCondition'] = ""
        dataFlowable['params'] = listParam
        dataFlowable['parsing'] = true
        result = resOfPost("/workflow/ext/modeler/condition", JSONUtil.toJSON(dataFlowable), HttpStatus.INTERNAL_SERVER_ERROR)
        assert I18NUtil.getMessage("workflow.condition.parse.error") == result
    }
}
