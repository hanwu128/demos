package com.example.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot04TaskApplicationTests {

    @Autowired
    JavaMailSender javaMailSender;

    @Test
    public void contextLoads() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        //邮件设置
        mailMessage.setSubject("通知：今晚放假！");
        mailMessage.setText("今晚下班开会!");
        mailMessage.setTo("hw_128@163.com");
        mailMessage.setFrom("hanwu128@163.com");

        javaMailSender.send(mailMessage);
    }

    @Test
    public void test02() throws MessagingException {
        //1、创建一个复杂的消息邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        //邮件设置
        helper.setSubject("通知：今晚放假！");
        helper.setText("<b style = 'color:red' >今晚下班放假! </b>", true);
        helper.setTo("hw_128@163.com");
        helper.setFrom("hanwu128@163.com");

        //上传文件
        helper.addAttachment("111", new File("C:\\Users\\lenovo\\Desktop\\新员工试用期工作总结V2.1.doc"));
        helper.addAttachment("222", new File("C:\\Users\\lenovo\\Desktop\\download.jpg"));

        javaMailSender.send(mimeMessage);
    }

}
