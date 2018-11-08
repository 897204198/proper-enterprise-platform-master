package com.proper.enterprise.platform.core.mongo.dal

import com.proper.enterprise.platform.core.mongo.dal.document.Customer
import com.proper.enterprise.platform.core.mongo.dal.repository.CustomerRepository
import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.test.AbstractSpringTest
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class MongoRepositoryTest extends AbstractSpringTest {

    @Autowired
    CustomerRepository customerRepository

    @Test
    public void testSaveAndQuery() {
        mockUser("user1", "isMock")
        Authentication.setCurrentUserId("user1")
        def cus = new Customer('fn', 'ln')
        customerRepository.save(cus)
        def newCus = customerRepository.findByLastName('ln').first()

        assert cus.firstName == newCus.firstName
        assert cus.lastName == newCus.lastName
        assert cus.createUserId == 'user1'

        mockUser("user2", "isMock")
        Authentication.setCurrentUserId("user2")
        customerRepository.save(cus)
        assert cus.lastModifyUserId == 'user2'
    }

    @After
    public void tearDown() {
        customerRepository.deleteAll()
    }

}
