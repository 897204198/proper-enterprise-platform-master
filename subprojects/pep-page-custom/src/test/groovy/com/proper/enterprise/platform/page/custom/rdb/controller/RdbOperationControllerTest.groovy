package com.proper.enterprise.platform.page.custom.rdb.controller

import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MvcResult

import javax.sql.DataSource

class RdbOperationControllerTest extends AbstractTest {

    @Autowired
    DataSource dataSource

    @Test
    void testGetDataById() throws Exception {
        MvcResult result = get("/rdb/table_test/1", HttpStatus.OK)
        String resultContent = result.getResponse().getContentAsString()
        Map<String, Object> map = (Map<String, Object>) JSONUtil.parse(resultContent, Object.class)
        assert "aa".equals(map.get("name").toString())
    }

    @Test
    void testGetDataForList() throws Exception {
        MvcResult result = get("/rdb/table_test", HttpStatus.OK)
        String resultContent = result.getResponse().getContentAsString()
        List<Map<String, Object>> list = (List<Map<String, Object>>) JSONUtil.parse(resultContent, Object.class)
        assert list.size() == 2
    }

    @Test
    void testGetDataForPage() throws Exception {
        MvcResult result = get("/rdb/page/table_test?pageNo=1&pageSize=5", HttpStatus.OK)
        String resultContent = result.getResponse().getContentAsString()
        Map<String, Object> map = (Map<String, Object>) JSONUtil.parse(resultContent, Object.class)
        assert Integer.parseInt(map.get("count").toString()) == 2
    }

    @Test
    void testAddData() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>()
        param.put("name", "cc")
        mockUser("1", "13800000000")
        post("/rdb/table_test", JSONUtil.toJSON(param), HttpStatus.CREATED)
        MvcResult result = get("/rdb/table_test", HttpStatus.OK)
        String resultContent = result.getResponse().getContentAsString()
        List<Map<String, Object>> list = (List<Map<String, Object>>) JSONUtil.parse(resultContent, Object.class)
        assert list.size() == 3
    }

    @Test
    void testUpdateData() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>()
        param.put("name", "cc")
        mockUser("1", "13800000000")
        put("/rdb/table_test/1", JSONUtil.toJSON(param), HttpStatus.OK)
        MvcResult result = get("/rdb/table_test/1", HttpStatus.OK)
        String resultContent = result.getResponse().getContentAsString()
        Map<String, Object> map = (Map<String, Object>) JSONUtil.parse(resultContent, Object.class)
        assert "cc".equals(map.get("name").toString())
    }

    @Test
    void testDeleteData() throws Exception {
        delete("/rdb/table_test?ids=1", HttpStatus.OK)
        MvcResult result = get("/rdb/table_test", HttpStatus.OK)
        String resultContent = result.getResponse().getContentAsString()
        List<Map<String, Object>> list = (List<Map<String, Object>>) JSONUtil.parse(resultContent, Object.class)
        assert list.size() == 1
    }

    @Test
    void testGetTables() throws Exception {
        MvcResult result = get("/rdb/baseInfo/tables", HttpStatus.OK)
        String resultContent = result.getResponse().getContentAsString()
        List<Map<String, Object>> list = (List<Map<String, Object>>) JSONUtil.parse(resultContent, Object.class)
        boolean flg = false
        for (Map<String, Object> map : list) {
            if ("table_test".equals(map.get("TABLE_NAME"))) {
                flg = true
            }
        }
        assert flg
    }

    @Test
    void testGetTableInfo() throws Exception {
        MvcResult result = get("/rdb/baseInfo/tables/table_test", HttpStatus.OK)
        String resultContent = result.getResponse().getContentAsString()
        List<Map<String, Object>> list = (List<Map<String, Object>>) JSONUtil.parse(resultContent, Object.class)
        assert list.size() == 6
    }

    @Before
    void saveTestData() throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource)
        jdbcTemplate.execute("CREATE TABLE table_test "
            + "(id varchar(255) primary key not null, "
            + "name varchar(255), "
            + "create_user_id varchar(255), "
            + "create_time varchar(255), "
            + "last_modify_user_id varchar(255), "
            + "last_modify_time varchar(255))")
        jdbcTemplate.update("insert into table_test(id,name) values('1','aa')")
        jdbcTemplate.update("insert into table_test(id,name) values('2','bb')")
    }

    @After
    void clearTestData() throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource)
        jdbcTemplate.execute("DROP TABLE table_test")
    }
}
