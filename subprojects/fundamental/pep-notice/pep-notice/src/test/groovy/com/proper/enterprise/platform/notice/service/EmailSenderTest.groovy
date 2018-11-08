package com.proper.enterprise.platform.notice.service

import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper

import javax.mail.internet.MimeMessage

@Ignore
class EmailSenderTest extends AbstractJPATest {

    @Autowired
    private JavaMailSender mailSender;

    @Test
    void sendSimpleEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("Wf@propersoft.cn");
        message.setTo("276595311@qq.com");
        message.setSubject("Spring Email Test");
        message.setText("hello world!!");
        mailSender.send(message);
    }

    @Test
    void sendHtmlEmail() {
        MimeMessage mailMessage = mailSender.createMimeMessage()
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage,true,"GBK")
        helper.setFrom("Wf@propersoft.cn")
        helper.setTo("wanghaopeng@propersoft.cn")
        helper.setSubject("title")
        String url = "【wanghp】于【2018-08-08 13:00】至【2018-08-08 17:30】请假【4】小时，<a href='http://docs.easemob.com/im/start'>请悉知</a>"
        helper.setText(url,true)
        mailSender.send(mailMessage)
    }

}
