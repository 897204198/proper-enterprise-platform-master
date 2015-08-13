package com.proper.enterprise.platform.test.integration;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"/spring/applicationContext.xml", 
                       "/spring/applicationContext-profile.xml", 
                       "/spring/dal/applicationContext-datasource.xml", 
                       "/spring/dal/applicationContext-jpa.xml"})
@Transactional
@ActiveProfiles("test")
public abstract class AbstractIntegTest {

}
