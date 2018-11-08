package com.proper.enterprise.platform.pay.ali;

import com.proper.enterprise.platform.core.utils.cipher.RSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayAliConfiguration {

    private PayAliProperties payAliProperties;

    @Autowired
    public PayAliConfiguration(PayAliProperties payAliProperties) {
        this.payAliProperties = payAliProperties;
    }

    @Bean
    public RSA aliRSAPay() {
        return new RSA(payAliProperties.getSignAlgoForPay());
    }

    @Bean
    public RSA aliRSARefund() {
        return new RSA(payAliProperties.getSignAlgoForRefund());
    }

}
