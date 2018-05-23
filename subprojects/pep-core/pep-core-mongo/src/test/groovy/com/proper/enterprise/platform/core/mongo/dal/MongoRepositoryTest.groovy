package com.proper.enterprise.platform.core.mongo.dal

import com.proper.enterprise.platform.core.mongo.dal.document.Customer
import com.proper.enterprise.platform.core.mongo.dal.repository.CustomerRepository
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class MongoRepositoryTest extends AbstractTest {

    @Autowired
    CustomerRepository customerRepository

    @Test
    public void testSaveAndQuery() {
        mockUser("user1", "isMock")
        def cus = new Customer('fn', 'ln')
        customerRepository.save(cus)
        def newCus = customerRepository.findByLastName('ln').first()

        assert cus.firstName == newCus.firstName
        assert cus.lastName == newCus.lastName
        assert cus.createUserId == 'user1'

        mockUser("user2", "isMock")
        customerRepository.save(cus)
        assert cus.lastModifyUserId == 'user2'
    }

    @After
    public void tearDown() {
        customerRepository.deleteAll()
    }

}
