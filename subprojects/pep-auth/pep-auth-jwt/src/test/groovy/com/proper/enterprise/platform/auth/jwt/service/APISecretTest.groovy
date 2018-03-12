package com.proper.enterprise.platform.auth.jwt.service

import com.proper.enterprise.platform.auth.service.APISecret
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class APISecretTest extends AbstractTest {

    @Autowired
    APISecret secret

    @Test
    void testCache() {
        def key = 'k1'
        def s1 = secret.getAPISecret(key)
        assert s1 == secret.getAPISecret(key)

        secret.clearAPISecret(key)
        assert s1 != secret.getAPISecret(key)

        s1 = secret.getAPISecret(key)
        assert s1 == secret.getAPISecret(key)
        // Waiting for cache expired,
        // expire time depends on the configuration of underlying cache implements
//        sleep(120 * 1000)
//        assert s1 != secret.getAPISecret(key)
    }

}
