package com.proper.enterprise.platform.search.common.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.search.api.serivce.QueryResultService
import com.proper.enterprise.platform.search.common.configs.Table2ConfigTest
import com.proper.enterprise.platform.search.common.configs.MultiTableConfigTest
import com.proper.enterprise.platform.search.common.entity.Table2EntityTest
import com.proper.enterprise.platform.search.common.entity.TableEntityTest
import com.proper.enterprise.platform.search.common.repository.Table2RepositoryTest
import com.proper.enterprise.platform.search.common.repository.TableRepositoryTest
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class QueryResultServiceImplTest extends AbstractTest{

    @Autowired
    MultiTableConfigTest multiTableConfigTest

    @Autowired
    Table2ConfigTest configTest

    @Autowired
    QueryResultService queryResultService

    @Autowired
    TableRepositoryTest tableRepositoryTest

    @Autowired
    Table2RepositoryTest table2RepositoryTest

    @Test
    @Sql(["/sql/search/001-datadics.sql"])
    void testSearchServiceImpl(){
        initData()
        get("/search/init",  HttpStatus.OK)

        String tableName = "test_table2"
        String req = "[{\"key\":\"create_time\",\"value\":\"2017-12-25到2018-02-27\",\"operate\":\"like\"}," +
                        "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\"}]"
        req = URLDecoder.decode(req, PEPConstants.DEFAULT_CHARSET.toString())
        ObjectMapper objectMapper = new ObjectMapper()
        JsonNode jn = objectMapper.readValue(req, JsonNode.class)
        List result = queryResultService.assemble(configTest, jn, tableName)
        assert result.size() == 1

        req = "[{\"key\":\"create_time\",\"value\":\"2018-01-01或2018-02-27\",\"operate\":\"like\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\"}]"
        jn = objectMapper.readValue(req, JsonNode.class)
        result = queryResultService.assemble(configTest, jn, tableName)
        assert result.size() == 1

        req = "[{\"key\":\"dept_member_count\",\"value\":\"1\",\"operate\":\">\"}]"
        jn = objectMapper.readValue(req, JsonNode.class)
        result = queryResultService.assemble(configTest, jn, tableName)
        assert result.size() > 1

        req = "[{\"key\":\"create_time\",\"value\":\"2018\",\"operate\":\"like\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\"}]"
        jn = objectMapper.readValue(req, JsonNode.class)
        result = queryResultService.assemble(configTest, jn, tableName)

        req = "[{\"key\":\"create_time\",\"value\":\"本年\",\"operate\":\"like\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\"}]"
        jn = objectMapper.readValue(req, JsonNode.class)
        result = queryResultService.assemble(configTest, jn, tableName)

        req = "[{\"key\":\"create_time\",\"value\":\"本季\",\"operate\":\"like\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\"}]"
        jn = objectMapper.readValue(req, JsonNode.class)
        result = queryResultService.assemble(configTest, jn, tableName)

        req = "[{\"key\":\"create_time\",\"value\":\"本月\",\"operate\":\"like\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\"}]"
        jn = objectMapper.readValue(req, JsonNode.class)
        result = queryResultService.assemble(configTest, jn, tableName)

        req = "[{\"key\":\"create_time\",\"value\":\"本周\",\"operate\":\"like\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\"}]"
        jn = objectMapper.readValue(req, JsonNode.class)
        result = queryResultService.assemble(configTest, jn, tableName)

        req = "[{\"key\":\"create_time\",\"value\":\"本天\",\"operate\":\"like\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\"}]"
        jn = objectMapper.readValue(req, JsonNode.class)
        result = queryResultService.assemble(configTest, jn, tableName)

        req = "[{\"key\":\"create_time\",\"value\":\"2017-12-25到2018-01-27\",\"operate\":\"like\"}," +
            "{\"key\":\"dept_member_count\",\"value\":\"20\",\"operate\":\"=\"}]"
        jn = objectMapper.readValue(req, JsonNode.class)
        result = queryResultService.assemble(configTest, jn, tableName)
        assert result.size() == 1
    }

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
        table2EntityTest.setCreateTime("2018-01-01")
        table2EntityTest.setDeptMemberCount(10)

        Table2EntityTest table2EntityTest2 = new Table2EntityTest()
        table2EntityTest2.setDeptId("002")
        table2EntityTest2.setDeptName("实施部")
        table2EntityTest2.setDeptDesc("产品实施")
        table2EntityTest2.setCreateTime("2018-01-02")
        table2EntityTest2.setDeptMemberCount(20)

        Table2EntityTest table2EntityTest3 = new Table2EntityTest()
        table2EntityTest3.setDeptId("003")
        table2EntityTest3.setDeptName("销售部")
        table2EntityTest3.setDeptDesc("产品销售")
        table2EntityTest3.setCreateTime("2018-01-03")
        table2EntityTest3.setDeptMemberCount(30)

        table2RepositoryTest.save(table2EntityTest)
        table2RepositoryTest.save(table2EntityTest2)
        table2RepositoryTest.save(table2EntityTest3)
    }
}
