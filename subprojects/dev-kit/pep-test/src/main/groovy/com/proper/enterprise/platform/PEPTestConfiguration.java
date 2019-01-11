package com.proper.enterprise.platform;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = "com.proper.enterprise.platform", lazyInit = true)
@Configuration
public class PEPTestConfiguration {
}
