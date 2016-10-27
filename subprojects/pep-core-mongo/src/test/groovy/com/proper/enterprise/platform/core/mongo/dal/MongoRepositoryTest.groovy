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
        def cus = new Customer('fn', 'ln')
        customerRepository.save(cus)
        def newCus = customerRepository.findByLastName('ln').first()

        assert cus.firstName == newCus.firstName
        assert cus.lastName == newCus.lastName
    }

    @After
    public void tearDown() {
        customerRepository.deleteAll()
    }

}
