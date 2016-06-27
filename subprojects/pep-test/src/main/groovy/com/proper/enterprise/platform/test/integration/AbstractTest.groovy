package com.proper.enterprise.platform.test.integration

import org.junit.runner.RunWith
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration("/spring/applicationContext.xml")
@Transactional
@ActiveProfiles("test")
public abstract class AbstractTest {

}
