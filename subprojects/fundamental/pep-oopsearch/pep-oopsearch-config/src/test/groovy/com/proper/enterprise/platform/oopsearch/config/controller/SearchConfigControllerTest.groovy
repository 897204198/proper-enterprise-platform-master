package com.proper.enterprise.platform.oopsearch.config.controller

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.oopsearch.config.conf.AbstractSearchConfigs
import com.proper.enterprise.platform.oopsearch.config.service.SearchConfigService
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql


@Sql("/com/proper/enterprise/platform/oopsearch/config/configData.sql")
class SearchConfigControllerTest extends AbstractJPATest{

    private  static  final  String URL="/oopsearch/config"

    @Autowired
    private SearchConfigService searchConfigService

    @Test
    void getSearchConfigTest() {
        mockUser('test1', 't1', 'pwd')
        def res = JSONUtil.parse(get(URL + '?pageNo=1&pageSize=10', HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert res.count == 4
        assert res.data.size == 4

        def res2 = JSONUtil.parse(get(URL + '?pageNo=1&pageSize=2', HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert res2.count == 4
        assert res2.data.size == 2

        def res3 = JSONUtil.parse(get(URL + '?pageNo=1&pageSize=2&name=demouser&tablename=demo_user&searchColumn=create_time&columnAlias=demouser_create_time&columnDesc=人员创建时间&url=/demouser&configEnable=',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert res3.count == 1
        assert res3.data.size == 1
    }

    @Test
    void updateSearchConfigTest() {
        mockUser('test1', 't1', 'pwd')

        def configReq = [:]
        configReq['id'] = '001'
        configReq['moduleName'] = 'testuser'
        configReq['tableName'] = 'test_user'
        configReq['searchColumn'] = 'user_id'
        configReq['columnAlias'] = 'demouser_user_id'
        configReq['columnDesc'] = '人员创建时间'
        configReq['url'] = '/demouser'
        configReq['dataBaseType'] = 'RDB'
        configReq['enable'] = true
        put("$URL/001", JSONUtil.toJSON(configReq), HttpStatus.OK)
        put("$URL/000", JSONUtil.toJSON(configReq), HttpStatus.INTERNAL_SERVER_ERROR)
        AbstractSearchConfigs abstractSearchConfigs = searchConfigService.getSearchConfig('testuser')
        assert abstractSearchConfigs.getTableNameList()[0] == 'test_user'
    }

    @Test
    void addSearchEntity(){
        def searchConfigEntity = [:]
        searchConfigEntity['moduleName'] = 'demouser'
        searchConfigEntity['tableName'] = 'demo_user'
        searchConfigEntity['searchColumn'] = 'create_time_test'
        searchConfigEntity['columnAlias'] = 'demouser_create_time_test'
        searchConfigEntity['columnDesc'] = 'demoDesc'
        searchConfigEntity['url'] = '/url'
        searchConfigEntity['dataBaseType'] = 'RDB'
        post(URL, JSONUtil.toJSON(searchConfigEntity), HttpStatus.CREATED)
        searchConfigEntity['url'] = ' '
        post(URL, JSONUtil.toJSON(searchConfigEntity), HttpStatus.INTERNAL_SERVER_ERROR)
        searchConfigEntity['url'] = '/url'
        searchConfigEntity['columnDesc'] = ' '
        post(URL, JSONUtil.toJSON(searchConfigEntity), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @Test
    void deleteSearchEntity() {
        def ids='001,002,003'
        delete(URL+'?ids'+ids,HttpStatus.BAD_REQUEST).response.contentAsString
        delete(URL + '?ids=' + ids, HttpStatus.NO_CONTENT).response.contentAsString
        delete(URL+'?ids='+' ',HttpStatus.NOT_FOUND).response.contentAsString
    }
}
