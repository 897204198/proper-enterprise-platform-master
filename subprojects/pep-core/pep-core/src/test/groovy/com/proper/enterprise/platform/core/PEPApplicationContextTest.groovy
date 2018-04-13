package com.proper.enterprise.platform.core

import com.proper.enterprise.platform.core.service.TestService
import com.proper.enterprise.platform.core.utils.SpELParser
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext

class PEPApplicationContextTest extends AbstractTest {

    @Autowired
    private SpELParser parser

    @Value('${database.maxPoolSize}')
    private maximumPoolSize

    @Test
    void cover() {
        ApplicationContext context = PEPApplicationContext.getApplicationContext()
        def beanName = 'testService'
        assert context.getBean(beanName) == PEPApplicationContext.getBean(beanName)
        assert context.getBean(TestService.class) == PEPApplicationContext.getBean(TestService.class)
        assert context.getBean(beanName, TestService.class) == PEPApplicationContext.getBean(beanName, TestService.class)
    }

    @Test
    void invokeStaticMethod() {
        assert parser.parse('#{T(com.proper.enterprise.platform.core.PEPApplicationContext).getBean("dataSource").maxActive}') == maximumPoolSize
    }

}
