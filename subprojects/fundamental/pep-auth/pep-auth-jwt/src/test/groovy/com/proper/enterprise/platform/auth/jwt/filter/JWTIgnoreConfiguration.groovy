package com.proper.enterprise.platform.auth.jwt.filter

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JWTIgnoreConfiguration {

    @Bean
    List<String> ignorePatternsListJWT() {
        return Arrays.asList('PUT:/jwt/filter/ignore/method', '*/workflow/**')
    }

}
