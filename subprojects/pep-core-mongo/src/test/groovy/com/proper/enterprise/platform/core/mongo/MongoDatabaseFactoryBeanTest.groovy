package com.proper.enterprise.platform.core.mongo

import com.mongodb.client.MongoDatabase
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

class MongoDatabaseFactoryBeanTest extends AbstractTest {

    @Autowired
    MongoDatabase mongoDatabase

    @Value('${mongodb.database}')
    private String databaseName

    @Test
    public void doCheckWithoutTrulyConnect() {
        assert mongoDatabase.getName() == databaseName
    }

    @Test
    public void checkObjectType() {
        assert new MongoDatabaseFactoryBean().getObjectType() == MongoDatabase.class
    }

}
