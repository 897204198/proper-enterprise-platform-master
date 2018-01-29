package com.proper.enterprise.platform.workflow.controller

import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class EditorRolesResourceTest extends AbstractTest {

    @Sql("/com/proper/enterprise/platform/workflow/roles.sql")
    @Test
    void getUserRolesTest() {
        String filter = 'stro'
        def result = get("/workflow/service/editor-roles?filter=" + filter, HttpStatus.OK).getResponse().getContentAsString()
        Map map = (Map) JSONUtil.parse(result, java.lang.Object.class)
        assert map.get("size") == 4
        List list = (List) map.get("data")
        assert list.size() == 4
        assert list.get(0).get("name") == 'testrole1'

        filter = ''
        result = get("/workflow/service/editor-roles?filter=" + filter, HttpStatus.OK).getResponse().getContentAsString()
        map = (Map) JSONUtil.parse(result, java.lang.Object.class)
        assert map.get("size") == 4
        list = (List) map.get("data")
        assert list.size() == 4
        assert list.get(0).get("name") == 'testrole1'

        filter = 'le1'
        result = get("/workflow/service/editor-roles?filter=" + filter, HttpStatus.OK).getResponse().getContentAsString()
        map = (Map) JSONUtil.parse(result, java.lang.Object.class)
        assert map.get("size") == 2
        list = (List) map.get("data")
        assert list.size() == 2
        assert list.get(0).get("name") == 'testrole1'
    }

}
