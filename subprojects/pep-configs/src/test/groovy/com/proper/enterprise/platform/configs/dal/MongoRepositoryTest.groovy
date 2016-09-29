package com.proper.enterprise.platform.configs.dal

import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.configs.dal.document.Customer
import com.proper.enterprise.platform.configs.dal.repository.CustomerRepository
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class MongoRepositoryTest extends AbstractTest {

    @Autowired
    CustomerRepository customerRepository

    @Test
    public void test() {
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
