package com.proper.enterprise.platform.slack;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application-slack.properties")
public class SlackConfiguration {
}
