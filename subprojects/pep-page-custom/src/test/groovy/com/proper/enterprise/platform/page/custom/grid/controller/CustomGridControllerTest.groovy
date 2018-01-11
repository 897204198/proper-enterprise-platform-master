package com.proper.enterprise.platform.page.custom.grid.controller

import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.page.custom.grid.document.CustomGridDocument
import com.proper.enterprise.platform.page.custom.grid.repository.CustomGridRepository
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MvcResult

class CustomGridControllerTest extends AbstractTest {

    @Autowired
    private CustomGridRepository customGridRepository

    @Test
    void testGetCustomGridForPage() throws Exception {
        MvcResult result = get("/custom/grid/page/configs?title=测试数据&pageNo=1&pageSize=5", HttpStatus.OK)
        String resultContent = result.getResponse().getContentAsString()
        Map<String, Object> map = (Map<String, Object>) JSONUtil.parse(resultContent, Object.class)
        assert Integer.parseInt(map.get("count").toString()) == 2
    }

    @Test
    void testGetCustomGridByCode() throws Exception {
        MvcResult result = get("/custom/grid/code/configs?code=test", HttpStatus.OK)
        String resultContent = result.getResponse().getContentAsString()
        Map<String, Object> map = (Map<String, Object>) JSONUtil.parse(resultContent, Object.class)
        assert "test".equals(map.get("code").toString())
    }

    @Test
    void testGetCustomGridById() throws Exception {
        MvcResult result = get("/custom/grid/configs/1", HttpStatus.OK)
        String resultContent = result.getResponse().getContentAsString()
        Map<String, Object> map = (Map<String, Object>) JSONUtil.parse(resultContent, Object.class)
        assert "1".equals(map.get("id").toString())
    }

    @Test
    void testSaveCustomGrid() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>()
        param.put("title", "测试数据3")
        param.put("code", "test3")
        param.put("tableName", "table_test3")
        post("/custom/grid/configs", JSONUtil.toJSON(param), HttpStatus.CREATED)
        CustomGridDocument customGridDocument = customGridRepository.getByCode("test3")
        assert "test3".equals(customGridDocument.getCode())
    }

    @Test
    void testUpdateCustomGrid() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>()
        param.put("title", "测试数据3")
        param.put("code", "test3")
        param.put("tableName", "table_test3")
        put("/custom/grid/configs/1", JSONUtil.toJSON(param), HttpStatus.OK)
        CustomGridDocument customGridDocument = customGridRepository.getById("1")
        assert "test3".equals(customGridDocument.getCode())
    }

    @Test
    void testDeleteCustomGrid() throws Exception {
        delete("/custom/grid/configs?ids=1", HttpStatus.OK)
        CustomGridDocument customGridDocument = customGridRepository.getById("1")
        assert customGridDocument == null
    }

    @Before
    void saveTestData() throws Exception {
        customGridRepository.deleteAll()
        CustomGridDocument customGridDocument = new CustomGridDocument()
        customGridDocument.setId("1")
        customGridDocument.setTitle("测试数据")
        customGridDocument.setCode("test")
        customGridDocument.setTableName("table_test")
        customGridRepository.save(customGridDocument)
        customGridDocument = new CustomGridDocument()
        customGridDocument.setId("2")
        customGridDocument.setTitle("测试数据2")
        customGridDocument.setCode("test2")
        customGridDocument.setTableName("table_test2")
        customGridRepository.save(customGridDocument)
    }

    @After
    void clearTestData() throws Exception {
        customGridRepository.deleteAll()
    }
}
