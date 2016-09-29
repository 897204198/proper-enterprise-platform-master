package com.proper.enterprise.platform.configs.dal.repository;

import com.proper.enterprise.platform.configs.dal.document.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CustomerRepository extends MongoRepository<Customer, String> {

    List<Customer> findByLastName(String lastName);

}
