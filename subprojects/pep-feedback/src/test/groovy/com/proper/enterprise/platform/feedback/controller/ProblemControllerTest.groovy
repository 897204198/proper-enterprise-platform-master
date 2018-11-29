package com.proper.enterprise.platform.feedback.controller

import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.feedback.entity.ProblemEntity
import com.proper.enterprise.platform.feedback.entity.RecordEntity
import com.proper.enterprise.platform.feedback.repository.ProblemRepository
import com.proper.enterprise.platform.feedback.repository.RecordRepository
import com.proper.enterprise.platform.feedback.service.ProblemService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MvcResult

@Sql
class ProblemControllerTest extends AbstractTest{

    @Autowired
    ProblemService problemService
    @Autowired
    ProblemRepository problemRepository
    @Autowired
    RecordRepository recordRepository;

    @Before
    void saveData() {

        problemService.addProblem("马什么梅？", "马东梅", "3a4b2474-6f90-4380-891e-72f8c31de686")

    }

    @Test
    void testCurd() {

        MvcResult result = get("/admin/problem?categoryId=3a4b2474-6f90-4380-891e-72f8c31de686", HttpStatus.OK)
        String resultContent = result.getResponse().getContentAsString()
        List<Map<String, Object>> list = (List<ProblemEntity>) JSONUtil.parse(resultContent, Object.class)

        assert 1 == list.size()

        MvcResult result1 = get("/problem/popular?pageNo=1&pageSize=7", HttpStatus.OK)
        String resultContent1 = result1.getResponse().getContentAsString()
        List<Map<String, Object>> list1 = (List<ProblemEntity>) JSONUtil.parse(resultContent1, Object.class)
        assert 1 == list1.size()
    }

    @Test
    void testAdd() {
        post("/admin/problem/add", '{"name": "test", "answer": "测试", "id": "312dasdsdsa"}', HttpStatus.CREATED);

    }

    @Test
    void testProblemInfo () {
        MvcResult result = get("/admin/problem?categoryId=3a4b2474-6f90-4380-891e-72f8c31de686", HttpStatus.OK)
        String resultContent = result.getResponse().getContentAsString()
        List<Map<String, Object>> list = (List<ProblemEntity>) JSONUtil.parse(resultContent, Object.class)
        String id = list.get(0).get("id")
        get("/problem/info?deviceId=123321&problemId="+id, HttpStatus.OK)

        //验证浏览记录加1
        ProblemEntity problem = problemRepository.findOne(id);
        assert problem.views == 1

         get("/problem/assess?code=1&deviceId=123321&problemId="+id, HttpStatus.OK)

        RecordEntity record = recordRepository.findByProblemIdAndDeviceId(id, "123321");
        assert record.getAssess() == "1"
    }

    @Test
    void testAdminProblemInfo() {
        List<ProblemEntity> problems = problemRepository.findAll()
        String id = problems.get(0).getId()
        MvcResult result = get("/admin/problem/" + id, HttpStatus.OK )
        String resultContent = result.getResponse().getContentAsString()
        ProblemEntity problem = (ProblemEntity) JSONUtil.parse(resultContent, Object.class)
        assert problem != null

    }


    @After
    void clearData() {
        problemRepository.deleteAll()
        recordRepository.deleteAll()
    }
}
