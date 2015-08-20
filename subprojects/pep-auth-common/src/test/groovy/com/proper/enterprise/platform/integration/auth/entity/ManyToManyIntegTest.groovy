package com.proper.enterprise.platform.integration.auth.entity
import com.proper.enterprise.platform.auth.entity.RoleEntity
import com.proper.enterprise.platform.auth.entity.UserEntity
import com.proper.enterprise.platform.auth.repository.RoleRepository
import com.proper.enterprise.platform.auth.repository.UserRepository
import com.proper.enterprise.platform.integration.auth.InsertDataWorker
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class ManyToManyIntegTest extends AbstractIntegTest {

    @Autowired
    InsertDataWorker worker

    @Autowired
    UserRepository userRepo

    @Autowired
    RoleRepository roleRepo

    @Test
    public void checkManyToManyRelationship() {
        worker.insertData()

        UserEntity user = userRepo.findByLoginName(worker.username)
        println "user roles: ${user.roles}"
        user.roles.each {
            println it.users
//            assert it.users.contains(user)
        }

        RoleEntity role = roleRepo.findByCode('roleA')
        println "role users: ${role.users}"
    }

}
