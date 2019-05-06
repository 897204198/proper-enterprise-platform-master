package org.flowable.ui.common.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 覆盖flowable内部实现
 * 将flowable对spring-security的引用排除
 */
@EnableConfigurationProperties({
    FlowableCommonAppProperties.class,
    FlowableRestAppProperties.class
})
@Configuration
public class FlowableRemoteIdmAutoConfiguration {
}
