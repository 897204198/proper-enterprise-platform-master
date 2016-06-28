package com.proper.enterprise.platform.test.integration

import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration("/spring/applicationContext.xml")
@Transactional
@ActiveProfiles("test")
public abstract class AbstractTest {

    @Autowired
    private WebApplicationContext wac

    protected MockMvc mockMvc

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
    }

}
