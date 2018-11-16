package com.hw.blog.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;

/**
 * 邮件发送类
 */
@Component
public class SendMailHandler {

    private JavaMailSenderImpl mailSender = null;
    private String from;

    private void init() throws IOException {
        //读取配置文件
        URL url = EmailUtil.findAsResource("mail.properties");
        Properties props = new Properties();
        InputStream ins;
        ins = url.openStream();
        props.load(ins);

        from = props.getProperty("MAIL_USER");

        this.mailSender = new JavaMailSenderImpl();
        //邮箱smtp服务器
        mailSender.setHost(props.getProperty("MAIL_SMTP_HOST"));
        mailSender.setPort(Integer.parseInt(props.getProperty("MAIL_SMTP_PORT")));
        mailSender.setUsername(props.getProperty("MAIL_USER"));
        //邮箱密码
        mailSender.setPassword(props.getProperty("MAIL_PWD"));
    }

    //普通文本Email
    public void sendPureMail(String to, String subject, String text) throws IOException {
        this.init();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        this.mailSender.send(message);
    }

    //带多个附件的Email
    public void sendMailWithAttachment(String to, String subject, String text, List<String> fileUrl) throws MessagingException, IOException {
        this.init();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "GBK");

        helper.setFrom(this.from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        //添加两个附件（附件位置位于java-->resources目录)，可根据需要添加或修改
        /*ClassPathResource image = new ClassPathResource("/coupon.jpg");
        ClassPathResource PDF = new ClassPathResource("/Rop Reference.pdf");
        helper.addAttachment("Coupon.png", image);
        helper.addAttachment("Rop Reference.pdf", PDF);*/

        for (String file : fileUrl) {
            FileDataSource path = new FileDataSource(file);
            Integer index = file.lastIndexOf("\\");
            helper.addAttachment(file.substring(index + 1, file.length()), path);
        }

        this.mailSender.send(message);
    }

    //带附件的HTML格式的Email
    public void sendMailHtmlWithAttachment(String to, String subject, String text, List<String> fileUrl) throws MessagingException, IOException {
        this.init();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "GBK"); //解决乱码问题

        helper.setFrom(this.from);
        helper.setTo(to);
        helper.setSubject(subject);
        //设置META解决乱码问题
        helper.setText(text, true);

        //文件作为附件发送
        for (String file : fileUrl) {
            FileDataSource path = new FileDataSource(file);
            Integer index = file.lastIndexOf("\\");
            helper.addAttachment(file.substring(index + 1, file.length()), path);
        }

        this.mailSender.send(message);
    }

}
