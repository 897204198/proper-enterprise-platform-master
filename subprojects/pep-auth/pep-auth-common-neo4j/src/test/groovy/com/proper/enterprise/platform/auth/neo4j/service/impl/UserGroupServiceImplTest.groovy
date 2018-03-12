package com.proper.enterprise.platform.auth.neo4j.service.impl

import com.proper.enterprise.platform.api.auth.service.UserGroupService
import com.proper.enterprise.platform.auth.neo4j.entity.UserGroupNodeEntity
import com.proper.enterprise.platform.sys.i18n.I18NService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class UserGroupServiceImplTest extends AbstractTest {

    @Autowired
    UserGroupService userGroupService

    @Autowired
    I18NService i18NService

    @Test
    void testCreateUserGroup() {
        UserGroupNodeEntity userGroupNodeEntity = new UserGroupNodeEntity()
        userGroupNodeEntity.setSeq(1)
        userGroupNodeEntity = userGroupService.createUserGroup(userGroupNodeEntity)
        assert userGroupNodeEntity.getSeq() == 1
        userGroupNodeEntity.setName('group')
        userGroupService.save(userGroupNodeEntity)
        try {
            userGroupService.createUserGroup(userGroupNodeEntity)
        } catch (Exception e) {
            assert e.getMessage() == i18NService.getMessage("pep.auth.common.usergroup.name.duplicate")
        }
    }
}
