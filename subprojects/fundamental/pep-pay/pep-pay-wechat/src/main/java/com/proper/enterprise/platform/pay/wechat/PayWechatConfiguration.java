package com.proper.enterprise.platform.pay.wechat;

import com.proper.enterprise.platform.pay.wechat.model.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class PayWechatConfiguration {

    @Bean
    public Unmarshaller unmarshallWechatNoticeRes() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(WechatNoticeRes.class);
        return marshaller;
    }

    @Bean
    public Unmarshaller unmarshallWechatOrderRes() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(WechatOrderRes.class);
        return marshaller;
    }

    @Bean
    public Unmarshaller unmarshallWechatRefundRes() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(WechatRefundRes.class);
        return marshaller;
    }

    @Bean
    public Unmarshaller unmarshallWechatPayQueryRes() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(WechatPayQueryRes.class);
        return marshaller;
    }

    @Bean
    public Unmarshaller unmarshallWechatRefundQueryRes() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(WechatRefundQueryRes.class);
        return marshaller;
    }

    @Bean
    public Unmarshaller unmarshallWechatBillRes() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(WechatBillRes.class);
        return marshaller;
    }

}
