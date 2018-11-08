package com.proper.enterprise.platform.core.mongo.dal.repository;

import com.proper.enterprise.platform.core.mongo.dal.document.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CustomerRepository extends MongoRepository<Customer, String> {

    /**
     * 根据 lastName 查询
     * @param lastName lastName
     * @return 集合
     */
    List<Customer> findByLastName(String lastName);

}
