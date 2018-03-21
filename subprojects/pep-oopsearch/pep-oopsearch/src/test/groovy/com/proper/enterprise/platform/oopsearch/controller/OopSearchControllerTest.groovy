package com.proper.enterprise.platform.oopsearch.controller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.proper.enterprise.platform.auth.common.jpa.entity.RoleEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity
import com.proper.enterprise.platform.auth.common.jpa.repository.RoleRepository
import com.proper.enterprise.platform.auth.common.jpa.repository.UserRepository
import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.oopsearch.api.serivce.QueryResultService
import com.proper.enterprise.platform.oopsearch.util.SqlInstallUtil
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import java.text.SimpleDateFormat

class OopSearchControllerTest extends AbstractTest{

    @Autowired
    RoleRepository repository

    @Autowired
    UserRepository userRepository

    @Autowired
    QueryResultService queryResultService;

    @Test
    void mutiTableQuery(){

        String endDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String req = "[{\"key\":\"create_time\",\"value\":\"2017-12-25到" + endDate +
            "\",\"operate\":\"like\",\"table\":\"test_table2\"}," +
            "{\"key\":\"dept_name\",\"value\":\"研发\",\"operate\":\"like\",\"table\":\"test_table2\"}]"
        req = URLDecoder.decode(req, PEPConstants.DEFAULT_CHARSET.toString())
        ObjectMapper objectMapper = new ObjectMapper()
        JsonNode jn = objectMapper.readValue(req, JsonNode.class)
        DataTrunk result = queryResultService.assemble(jn, "authusers", "1", "10")
        assert result.data.size() > 1

    }

    @Test
    void addTableElements(){
        List<String> list = new ArrayList<>();
        list.add("pep_auth_users");
        list.add("pep_auth_roles");
        String sql = SqlInstallUtil.addTableElements("", list);
        assert sql.length() > 0;
    }

    @Test
    void testPage(){
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode query = objectMapper.readValue("[{}]", JsonNode.class);

        //第一页
        String moduleName = "authusers"

        DataTrunk result = queryResultService.assemble(query, moduleName, "1", "2")
        assert result.data.size() == 2

        //翻页
        result = queryResultService.assemble(query, moduleName, "2", "2")
        assert result.data.size() == 1

        //page异常数据的处理
        result = queryResultService.assemble(query, moduleName, "", "2")
        assert result.data.size() == 2

        //page异常数据的处理
        result = queryResultService.assemble(query, moduleName, "0", "2")
        assert result.data.size() == 2
    }

    @Before
    void initData() {

        RoleEntity roleEntity = new RoleEntity()
        roleEntity.setName("操作员")
        repository.save(roleEntity)

        UserEntity userEntity1 = new UserEntity('user1', '123456')
        userEntity1.add(roleEntity)
        userEntity1.setName("用户1")
        userEntity1.setPhone("1860000000")
        userEntity1.setEmail("user1@propersoft.com")
        userRepository.save(userEntity1)

        UserEntity userEntity2 = new UserEntity('user2', '123456')
        userEntity2.add(roleEntity)
        userEntity2.setName("用户2")
        userEntity2.setPhone("1860000000")
        userEntity2.setEmail("user2@propersoft.com")
        userRepository.save(userEntity2)

        UserEntity userEntity3 = new UserEntity('user3', '123456')
        userEntity3.add(roleEntity)
        userEntity3.setName("用户3")
        userEntity3.setPhone("1860000000")
        userEntity3.setEmail("user3@propersoft.com")
        userRepository.save(userEntity3)

    }

}
