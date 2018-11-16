package com.hw.blog.util;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.hw.blog.conf.Config;
import com.hw.blog.conf.UserAuthentication;
import com.hw.blog.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 发送邮件的实现类
 */
@Component
public class SendMailHandler2 {

    @Autowired
    private Config config;

    private static SendMailHandler2 sendMailHandler;

    @PostConstruct
    public void init() {
        sendMailHandler = this;
    }

    private static Properties properties = System.getProperties();
    private Session sendMailSession = null;
    private UserAuthentication authenticator = null;
    private Transport trans = null;

    private void connect() throws Exception {
        //读取配置文件
       /* URL url = EmailUtil.findAsResource("mail.properties");
        Properties props = new Properties();
        InputStream ins;
        ins = url.openStream();
        props.load(ins);
        //给properties赋值
        properties.put("mail.smtp.host", props.getProperty("MAIL_SMTP_HOST"));
        properties.put("mail.smtp.port", props.getProperty("MAIL_SMTP_PORT"));
        properties.put("mail.smtp.auth", props.getProperty("MAIL_SMTP_AUTH"));*/
        properties.put("mail.smtp.host", sendMailHandler.config.getMAIL_SMTP_HOST());
        properties.put("mail.smtp.port", sendMailHandler.config.getMAIL_SMTP_PORT());
        properties.put("mail.smtp.auth", sendMailHandler.config.getMAIL_SMTP_AUTH());
        if ("true".equals(sendMailHandler.config.getMAIL_SMTP_AUTH())) {     //是否需要进行安全验证
            authenticator = new UserAuthentication(sendMailHandler.config.getMAIL_USER(), sendMailHandler.config.getMAIL_PWD());
        }
        //根据邮件会话属性和密码验证器构造一个发送邮件的session会话
        sendMailSession = Session.getInstance(properties, authenticator);
        //根据session会话,获得发送连接
        trans = sendMailSession.getTransport("smtp");
        trans.connect(sendMailHandler.config.getMAIL_SMTP_HOST(), sendMailHandler.config.getMAIL_USER(), sendMailHandler.config.getMAIL_PWD());
    }

    /**
     * 发送邮件类
     *
     * @param mail
     * @return
     */
    public boolean sendMail(Email mail) {
        boolean flag = true;
        try {
            this.connect();  //连接邮件服务器操作
            Message mailMessage = null;
            if (!mail.isContainAttach()) {
                mailMessage = getNormalMail(mail);
            } else {
                mailMessage = getArchivesMail(mail);
            }
            trans.send(mailMessage);
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        } finally {
            try {
                trans.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 生成简单邮件消息
     *
     * @param mail
     * @return
     * @throws Exception
     */
    private Message getNormalMail(Email mail) throws Exception {
        //根据session创建一个邮件消息
        Message mailMessage = new MimeMessage(sendMailSession);
        //设置邮件消息的发送者
        mailMessage.setFrom(new InternetAddress(mail.getFrom()));
        //设置邮件的接收者地址
        mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(mail.getToMails()));
        //设置邮件的主题
        mailMessage.setSubject(mail.getSubject());
        //设置邮件发送的时间
        mailMessage.setSentDate(new Date());
        //设置邮件的内容
        mailMessage.setText(mail.getContent());
        return mailMessage;
    }

    /**
     * 生成带附件的邮件消息
     *
     * @param mail
     * @return
     * @throws Exception
     */
    private Message getArchivesMail(Email mail) throws Exception {
        Message mailMessage = new MimeMessage(sendMailSession);
        //设置邮件消息的发送者
        mailMessage.setFrom(new InternetAddress(mail.getFrom()));
        //设置邮件的接收者地址
        mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(mail.getToMails()));
        //设置邮件的主题
        mailMessage.setSubject(mail.getSubject());
        //设置邮件信息生成时间
        mailMessage.setSentDate(new Date());
        Multipart contentPart = new MimeMultipart();
        //设置邮件内容
        mailMessage.setContent(contentPart);
        //1 邮件文本内容
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(mail.getContent(), "text/html; charset=GBK");
        contentPart.addBodyPart(textPart);//将文本部分，添加到邮件内容
        //2 附件
        if (mail.getArchives() != null) {
            for (int i = 0; i < mail.getArchives().length; i++) {
                MimeBodyPart archivePart = new MimeBodyPart();
                //选择出每一个附件文件名
                String filename = mail.getArchives()[i];
                //得到数据源
                FileDataSource fds = new FileDataSource(filename);
                //得到附件本身并至入BodyPart
                archivePart.setDataHandler(new DataHandler(fds));
                //得到文件名同样至入BodyPart
                archivePart.setFileName(fds.getName());
                // 将附件添加到附件集
                contentPart.addBodyPart(archivePart);
            }
        }
        return mailMessage;
    }

    public static void main(String[] args) {
        Email mailEntity = new Email();
        mailEntity.setSubject("设置邮件主题");     //主题
        mailEntity.setToMails("hw_128@163.com"); //接收人邮件地址
        mailEntity.setFrom("hanwu128@163.com");    //发送人邮件地址
        mailEntity.setContent("设置邮件内容");     //内容
        //带附件的
//		mailEntity.setContainAttach(true);       //是否含有附件(true:有 false:无)
//		String[] aa = {"F:\\aaa.txt"};			 //附件的路径(F:\\aaa.txt)
//		mailEntity.setArchives(aa);              //将数组传入到实体的属性中
        //不带附件
        mailEntity.setContainAttach(false);
        SendMailHandler2 sendMailHandler = new SendMailHandler2(); //发送邮件的方法类
        sendMailHandler.sendMail(mailEntity);     //发送
    }
}