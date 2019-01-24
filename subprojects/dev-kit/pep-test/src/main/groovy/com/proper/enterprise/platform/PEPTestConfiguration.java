package com.proper.enterprise.platform;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.PEPVersion;
import com.proper.enterprise.platform.core.listener.SuccessfulStartListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = "com.proper.enterprise.platform", lazyInit = true)
@Configuration
public class PEPTestConfiguration {

    @Bean
    public void setSuccessMsg() {
        SuccessfulStartListener successfulStartListener =
            PEPApplicationContext.getBean(SuccessfulStartListener.class);
        successfulStartListener.addSuccessMsg("Propersoft Enterprise Platform Test (v " + PEPVersion.getVersion() + ") Started Successfully!");
    }
}
