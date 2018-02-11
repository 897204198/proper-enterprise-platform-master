package com.proper.enterprise.platform.search.demo.controller

import com.proper.enterprise.platform.search.api.document.SearchColumn
import com.proper.enterprise.platform.search.api.serivce.MongoDataSyncService
import com.proper.enterprise.platform.search.common.document.SearchDocument
import com.proper.enterprise.platform.search.demo.DemoDeptConfigs
import com.proper.enterprise.platform.search.demo.entity.DemoDeptEntity
import com.proper.enterprise.platform.search.demo.repository.DemoDeptRepository
import com.proper.enterprise.platform.search.demo.repository.DemoUserRepository
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql


class DemoDeptControllerTest extends AbstractTest{

    @Autowired
    private MongoDataSyncService mongoDataSyncService

    @Autowired
    private DemoUserRepository demoUserRepository

    @Autowired
    private DemoDeptConfigs demoDeptConfigs

    @Autowired
    private DemoDeptRepository demoDeptRepository

    @Test
    void testSyncMongoFromDB(){
        initDeptDB()
        get("/search/init",  HttpStatus.OK)
        int count = mongoDataSyncService.findAll().size()
        assert count > 0
    }

    @Test
    void testDeptSearchInfo(){
        initDeptDB()
        get("/search/init",  HttpStatus.OK)
        String input = "研发"
        get("/search/init",  HttpStatus.OK)
        String resultContent = get("/search/dept?data=" + input,  HttpStatus.OK).getResponse().getContentAsString()
        List<SearchColumn> resultList = (List<SearchColumn>)JSONUtil.parse(resultContent,Object.class)
        for (SearchDocument document:resultList){
            assert document.getCon().contains(input)
        }
        assert resultList.size() <= demoDeptConfigs.getLimit()
    }

    @Test
    void testDeptSearchInfoByDate(){
        initDeptDB()
        get("/search/init",  HttpStatus.OK)
        String input = "2018"
        get("/search/init",  HttpStatus.OK)
        String resultContent = get("/search/dept?data=" + input,  HttpStatus.OK).getResponse().getContentAsString()
        List<SearchColumn> resultList = (List<SearchColumn>)JSONUtil.parse(resultContent,Object.class)
        assert resultList.size() == 6

        String input2 = "2018-01"
        get("/search/init",  HttpStatus.OK)
        String resultContent2 = get("/search/dept?data=" + input2,  HttpStatus.OK).getResponse().getContentAsString()
        List<SearchColumn> resultList2 = (List<SearchColumn>)JSONUtil.parse(resultContent2,Object.class)
        assert resultList2.size() == 6

        String input3 = "2018-01-01"
        get("/search/init",  HttpStatus.OK)
        String resultContent3 = get("/search/dept?data=" + input3,  HttpStatus.OK).getResponse().getContentAsString()
        List<SearchColumn> resultList3 = (List<SearchColumn>)JSONUtil.parse(resultContent3,Object.class)
        assert resultList3.size() == 4
    }

    @Test
    void testDeptQuery(){
        initDeptDB()
        get("/search/init",  HttpStatus.OK)
        String req = "[{\"key\":\"create_time\",\"value\":\"2017-12-25到2018-01-27\",\"operate\":\"like\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\"}]"
        String url = "/search/dept/query?req=" + encode(req) + "&tableName=demo_dept"
        String resultContent = get(url,  HttpStatus.OK).getResponse().getContentAsString()
        List resultList = JSONUtil.parse(resultContent,List.class)
        assert resultList.size() == 1
    }

    @Test
    void testDeptQuery2(){
        initDeptDB()
        get("/search/init",  HttpStatus.OK)
        String req = "[{\"key\":\"dept_member_count\",\"value\":20,\"operate\":\"=\"}]"
//        String req = "[{\"key\":\"create_time\",\"value\":\"2017-12-25到2018-01-27\",\"operate\":\"like\"}," +
//            "{\"key\":\"dept_member_count\",\"value\":20,\"operate\":\"=\"}]"
        String url = "/search/dept/query?req=" + encode(req) + "&tableName=demo_dept"
        String resultContent = get(url,  HttpStatus.OK).getResponse().getContentAsString()
        List resultList = JSONUtil.parse(resultContent,List.class)
        assert resultList.size() == 1
    }

    @Test
    void testDeptQueryException(){
        initDeptDB()
        get("/search/init",  HttpStatus.OK)
        String url = "/search/dept/query?req=&tableName=demo_dept"
        String resultContent = get(url,  HttpStatus.OK).getResponse().getContentAsString()
        List resultList = JSONUtil.parse(resultContent,List.class)
        assert resultList.size() == 0
    }

    @Test
    @Sql(["/sql/search/001-datadics.sql"])
    void testDeptQuery3(){
        initDeptDB()
        get("/search/init",  HttpStatus.OK)
        String req = "[{\"key\":\"create_time\",\"value\":\"2017-12-25到2018-01-27\",\"operate\":\"like\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\"}]"
        String url = "/search/dept/query?req=" + encode(req) + "&tableName=demo_dept"
        String resultContent = get(url,  HttpStatus.OK).getResponse().getContentAsString()
        List resultList = JSONUtil.parse(resultContent,List.class)
        assert resultList.size() == 1

        req = "[{\"key\":\"create_time\",\"value\":\"2017-12-25或2018-01-01\",\"operate\":\"like\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\"}]"
        url = "/search/dept/query?req=" + encode(req) + "&tableName=demo_dept"
        resultContent = get(url,  HttpStatus.OK).getResponse().getContentAsString()
        resultList = JSONUtil.parse(resultContent,List.class)
        assert resultList.size() == 1

        req = "[{\"key\":\"dept_member_count\",\"value\":\"1\",\"operate\":\">\"}]"
        url = "/search/dept/query?req=" + encode(req) + "&tableName=demo_dept"
        resultContent = get(url,  HttpStatus.OK).getResponse().getContentAsString()
        resultList = JSONUtil.parse(resultContent,List.class)
        assert resultList.size() > 1

        req = "[{\"key\":\"create_time\",\"value\":\"2017\",\"operate\":\"like\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\"}]"
        url = "/search/dept/query?req=" + encode(req) + "&tableName=demo_dept"
        resultContent = get(url,  HttpStatus.OK).getResponse().getContentAsString()
        resultList = JSONUtil.parse(resultContent,List.class)

        req = "[{\"key\":\"create_time\",\"value\":\"本年\",\"operate\":\"like\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\"}]"
        url = "/search/dept/query?req=" + encode(req) + "&tableName=demo_dept"
        resultContent = get(url,  HttpStatus.OK).getResponse().getContentAsString()
        resultList = JSONUtil.parse(resultContent,List.class)

        req = "[{\"key\":\"create_time\",\"value\":\"本季\",\"operate\":\"like\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\"}]"
        url = "/search/dept/query?req=" + encode(req) + "&tableName=demo_dept"
        resultContent = get(url,  HttpStatus.OK).getResponse().getContentAsString()
        resultList = JSONUtil.parse(resultContent,List.class)

        req = "[{\"key\":\"create_time\",\"value\":\"本月\",\"operate\":\"like\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\"}]"
        url = "/search/dept/query?req=" + encode(req) + "&tableName=demo_dept"
        resultContent = get(url,  HttpStatus.OK).getResponse().getContentAsString()
        resultList = JSONUtil.parse(resultContent,List.class)

        req = "[{\"key\":\"create_time\",\"value\":\"本周\",\"operate\":\"like\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\"}]"
        url = "/search/dept/query?req=" + encode(req) + "&tableName=demo_dept"
        resultContent = get(url,  HttpStatus.OK).getResponse().getContentAsString()
        resultList = JSONUtil.parse(resultContent,List.class)

        req = "[{\"key\":\"create_time\",\"value\":\"本天\",\"operate\":\"like\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\"}]"
        url = "/search/dept/query?req=" + encode(req) + "&tableName=demo_dept"
        resultContent = get(url,  HttpStatus.OK).getResponse().getContentAsString()
        resultList = JSONUtil.parse(resultContent,List.class)
    }

    private String encode(String url) {
        url = url.replaceAll("\"","%22")
        url = url.replaceAll("\\[","%5B")
        url = url.replaceAll("]","%5D")
        url = url.replaceAll("\\{","%7B")
        url = url.replaceAll("}","%7D")
        return url
    }

    void initDeptDB(){
        DemoDeptEntity searchEntity = new DemoDeptEntity()
        searchEntity.setDeptId("001")
        searchEntity.setDeptName("研发部")
        searchEntity.setDeptDesc("产品研发")
        searchEntity.setCreateTime("2018-01-01")
        searchEntity.setDeptMemberCount(10)

        demoDeptRepository.save(searchEntity)

        DemoDeptEntity searchEntity2 = new DemoDeptEntity()
        searchEntity2.setDeptId("002")
        searchEntity2.setDeptName("实施部")
        searchEntity2.setDeptDesc("产品实施")
        searchEntity2.setCreateTime("2018-01-02")
        searchEntity2.setDeptMemberCount(20)

        demoDeptRepository.save(searchEntity2)

        DemoDeptEntity searchEntity3 = new DemoDeptEntity()
        searchEntity3.setDeptId("001")
        searchEntity3.setDeptName("销售部")
        searchEntity3.setDeptDesc("产品销售")
        searchEntity3.setCreateTime("2018-01-03")
        searchEntity3.setDeptMemberCount(30)

        demoDeptRepository.save(searchEntity3)
    }
}
