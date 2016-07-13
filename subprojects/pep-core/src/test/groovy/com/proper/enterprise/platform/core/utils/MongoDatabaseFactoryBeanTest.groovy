package com.proper.enterprise.platform.core.utils

import com.mongodb.client.MongoDatabase
import com.proper.enterprise.platform.test.integration.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired


class MongoDatabaseFactoryBeanTest extends AbstractTest {

    @Autowired
    MongoDatabase mongoDatabase

    @Test
    public void doCheckWithoutTrulyConnect() {
        assert mongoDatabase.getName() == ConfCenter.get('mongodb.database')
    }

}
