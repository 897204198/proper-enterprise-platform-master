package com.proper.enterprise.platform.slack;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 统一命名{模块名}+Configuration.java
 * 在test环境有效 若要使用迁移到 java目录下即可
 * 对于bean的配置声明等在Configuration中完成
 */
@Configuration
@PropertySource("classpath:application-slack.properties")
public class SlackConfiguration {
}
