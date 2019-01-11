package com.proper.enterprise.platform

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackageClasses = PEPTestConfiguration.class)
class PEPIntegrationTestApplication {

    static void main(String[] args) {
        SpringApplication.run(PEPIntegrationTestApplication.class, args)
    }

}
