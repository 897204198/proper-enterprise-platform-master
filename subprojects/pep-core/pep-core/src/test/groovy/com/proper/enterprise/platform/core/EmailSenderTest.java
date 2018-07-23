package com.proper.enterprise.platform.core;

import com.proper.enterprise.platform.test.AbstractTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Ignore
public class EmailSenderTest extends AbstractTest {

    @Autowired
    private JavaMailSender mailSender;

    @Test
    public void sendSimpleEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("fangming@propersoft.cn");
        message.setTo("fangming312581573@outlook.com");
        message.setSubject("Spring Email Test");
        message.setText("hello world!!");
        mailSender.send(message);
    }
}
