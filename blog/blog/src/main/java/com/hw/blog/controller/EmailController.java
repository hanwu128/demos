package com.hw.blog.controller;

import com.hw.blog.util.SendMailHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 邮件操作
 */
@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private static JavaMailSender javaMailSender;

    @GetMapping("/send/{code}")
    public Object sendEmail(HttpServletRequest request, HttpServletResponse response, @PathVariable String code) {
        try {
            SendMailHandler email = new SendMailHandler();
            String to = "hw_128@163.com";
            String subject = "测试";
            String text = "<a href='http://localhost:8080/user/activate/" + code + "'>点我激活</a>";
            List<String> imgUrl = new ArrayList<String>();
            imgUrl.add("C:\\Users\\lenovo\\Desktop\\新建文本文档.txt");
            imgUrl.add("C:\\Users\\lenovo\\Desktop\\下载.jpg");
            imgUrl.add("C:\\Users\\lenovo\\Desktop\\通用后台管理模板系统.pdf");

            email.sendMailHtmlWithAttachment(to, subject, text, imgUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
