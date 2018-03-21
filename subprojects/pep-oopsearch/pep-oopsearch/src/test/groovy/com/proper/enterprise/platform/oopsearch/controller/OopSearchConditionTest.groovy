package com.proper.enterprise.platform.oopsearch.controller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.oopsearch.api.serivce.QueryResultService
import com.proper.enterprise.platform.oopsearch.configs.MultiTableConfigTest
import com.proper.enterprise.platform.oopsearch.entity.Table2EntityTest
import com.proper.enterprise.platform.oopsearch.entity.TableEntityTest
import com.proper.enterprise.platform.oopsearch.repository.Table2RepositoryTest
import com.proper.enterprise.platform.oopsearch.repository.TableRepositoryTest
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

import java.text.SimpleDateFormat

class OopSearchConditionTest extends AbstractTest {

    @Autowired
    MultiTableConfigTest multiTableConfigTest

    @Autowired
    QueryResultService queryResultService

    @Autowired
    TableRepositoryTest tableRepositoryTest

    @Autowired
    Table2RepositoryTest table2RepositoryTest

    @Test
    @Sql(["/sql/oopsearch/001-datadics.sql"])
    void testSearchServiceImpl() {
        //初始化mysql到mongo
        get("/search/init", HttpStatus.OK)

        String moduleName = "table2ConfigTest"

        //mongo反向查询
        get("/search/inverse?data=用&moduleName=" + moduleName, HttpStatus.OK)

        String endDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String req = "[{\"key\":\"create_time\",\"value\":\"2017-12-25到" + endDate +
            "\",\"operate\":\"like\",\"table\":\"test_table2\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\",\"table\":\"test_table2\"}]"
        ObjectMapper objectMapper = new ObjectMapper()
        JsonNode jn = objectMapper.readValue(req, JsonNode.class)
        DataTrunk result = queryResultService.assemble(jn, moduleName, "1", "10")
        assert result.data.size() == 1

        //测试无查询条件情况，查询所有
        req = "[{}]"
        jn = objectMapper.readValue(req, JsonNode.class)
        result = queryResultService.assemble(jn, moduleName, "1", "10")
        assert result.data.size() == 3

        req = "[{\"key\":\"create_time\",\"value\":\"2018-01-01或" + endDate +
            "\",\"operate\":\"like\",\"table\":\"test_table2\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\",\"table\":\"test_table2\"}]"
        jn = objectMapper.readValue(req, JsonNode.class)
        result = queryResultService.assemble(jn, moduleName, "1", "10")
        assert result.data.size() == 1

        req = "[{\"key\":\"dept_member_count\",\"value\":\"1\",\"operate\":\">\",\"table\":\"test_table2\"}]"
        jn = objectMapper.readValue(req, JsonNode.class)
        result = queryResultService.assemble(jn, moduleName, "1", "10")
        assert result.data.size() > 1

        req = "[{\"key\":\"create_time\",\"value\":\"2018\",\"operate\":\"like\",\"table\":\"test_table2\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\",\"table\":\"test_table2\"}]"
        jn = objectMapper.readValue(req, JsonNode.class)
        result = queryResultService.assemble(jn, moduleName, "1", "10")
        assert result.data.size()  ==  1

        req = "[{\"key\":\"create_time\",\"value\":\"本年\",\"operate\":\"like\",\"table\":\"test_table2\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\",\"table\":\"test_table2\"}]"
        jn = objectMapper.readValue(req, JsonNode.class)
        result = queryResultService.assemble(jn, moduleName, "1", "10")
        assert result.data.size()  ==  1

        req = "[{\"key\":\"create_time\",\"value\":\"本季\",\"operate\":\"like\",\"table\":\"test_table2\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\",\"table\":\"test_table2\"}]"
        jn = objectMapper.readValue(req, JsonNode.class)
        result = queryResultService.assemble(jn, moduleName, "1", "10")
        assert result.data.size()  ==  1

        req = "[{\"key\":\"create_time\",\"value\":\"本月\",\"operate\":\"like\",\"table\":\"test_table2\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\",\"table\":\"test_table2\"}]"
        jn = objectMapper.readValue(req, JsonNode.class)
        result = queryResultService.assemble(jn, moduleName, "1", "10")
        assert result.data.size()  ==  1

        req = "[{\"key\":\"create_time\",\"value\":\"本周\",\"operate\":\"like\",\"table\":\"test_table2\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\",\"table\":\"test_table2\"}]"
        jn = objectMapper.readValue(req, JsonNode.class)
        result = queryResultService.assemble(jn, moduleName, "1", "10")
        assert result.data.size()  ==  1

        req = "[{\"key\":\"create_time\",\"value\":\"本天\",\"operate\":\"like\",\"table\":\"test_table2\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\",\"table\":\"test_table2\"}]"
        jn = objectMapper.readValue(req, JsonNode.class)
        result = queryResultService.assemble(jn, moduleName, "1", "10")
        assert result.data.size()  ==  1

        req = "[{\"key\":\"create_time\",\"value\":\"2017-12-25到" + endDate + "\",\"operate\":\"like\",\"table\":\"test_table2\"}," +
            "{\"key\":\"dept_member_count\",\"value\":\"20\",\"operate\":\"=\",\"table\":\"test_table2\"}]"
        jn = objectMapper.readValue(req, JsonNode.class)
        result = queryResultService.assemble(jn, moduleName, "1", "10")
        assert result.data.size()  ==  1

    }

    @Before
    void initData() {
        TableEntityTest tableEntityTest = new TableEntityTest()
        tableEntityTest.setUserId("001")
        tableEntityTest.setUserName("张一")
        tableEntityTest.setAge(30)
        tableEntityTest.setDeptId("001")
        tableEntityTest.setCreateTime("2018-01-01")

        TableEntityTest tableEntityTest2 = new TableEntityTest()
        tableEntityTest2.setUserId("002")
        tableEntityTest2.setUserName("张二")
        tableEntityTest2.setAge(32)
        tableEntityTest2.setDeptId("002")
        tableEntityTest2.setCreateTime("2018-01-02")

        TableEntityTest tableEntityTest3 = new TableEntityTest()
        tableEntityTest3.setUserId("003")
        tableEntityTest3.setUserName("张三")
        tableEntityTest3.setAge(33)
        tableEntityTest3.setDeptId("003")
        tableEntityTest3.setCreateTime("2018-02-03")

        tableRepositoryTest.save(tableEntityTest)
        tableRepositoryTest.save(tableEntityTest2)
        tableRepositoryTest.save(tableEntityTest3)

        Table2EntityTest table2EntityTest = new Table2EntityTest()
        table2EntityTest.setDeptId("001")
        table2EntityTest.setDeptName("研发部")
        table2EntityTest.setDeptDesc("产品研发")
        table2EntityTest.setDeptMemberCount(10)

        Table2EntityTest table2EntityTest2 = new Table2EntityTest()
        table2EntityTest2.setDeptId("002")
        table2EntityTest2.setDeptName("实施部")
        table2EntityTest2.setDeptDesc("产品实施")
        table2EntityTest2.setDeptMemberCount(20)

        Table2EntityTest table2EntityTest3 = new Table2EntityTest()
        table2EntityTest3.setDeptId("003")
        table2EntityTest3.setDeptName("销售部")
        table2EntityTest3.setDeptDesc("产品销售")
        table2EntityTest3.setDeptMemberCount(30)

        table2RepositoryTest.save(table2EntityTest)
        table2RepositoryTest.save(table2EntityTest2)
        table2RepositoryTest.save(table2EntityTest3)
    }
}
