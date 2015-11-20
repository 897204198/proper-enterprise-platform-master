package com.proper.enterprise.platform.integration.auth.jwt

import com.proper.enterprise.platform.auth.jwt.APISecret
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class APISecretIntegTest extends AbstractIntegTest {

    @Autowired
    APISecret secret

    @Test
    public void testCache() {
        def key = 'k1'
        def s1 = secret.getAPISecret(key)
        assert s1 == secret.getAPISecret(key)

        secret.clearAPISecret(key)
        assert s1 != secret.getAPISecret(key)

        s1 = secret.getAPISecret(key)
        assert s1 == secret.getAPISecret(key)
        // Waiting for cache expired
//        sleep(120 * 1000)
//        assert s1 != secret.getAPISecret(key)
    }

}
