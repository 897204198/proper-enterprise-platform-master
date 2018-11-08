package com.proper.enterprise.platform.test

import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.transaction.annotation.Transactional

@Transactional(transactionManager = "jpaTransactionManager")
@SqlConfig(encoding = "UTF-8")
abstract class AbstractJPATest extends AbstractSpringTest {
}
