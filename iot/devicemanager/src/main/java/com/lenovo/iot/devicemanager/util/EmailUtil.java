/**
 *
 */
package com.lenovo.iot.devicemanager.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * 邮件工具
 * @desc com.lenovo.iot.devicemanager.util.EmailUtil
 * @author chench9@lenovo.com
 * @date 2017年11月10日
 */
@Configuration
@PropertySource("classpath:/email.properties")
public class EmailUtil {
	@Autowired
    private Environment env;
	private static final EmailUtil instance = new EmailUtil();
	
	private static String from_addr = "";
	private static String from_auth = "";
	private static Properties prop = System.getProperties();
	private static Authenticator auth = null;
	
	static {
		// 初始化服务器设置
		/*prop.put("mail.smtp.host", "smtp.163.com"); // 发送邮件服务器
		prop.put("mail.smtp.port", "465"); // 发送邮件端口
		prop.put("mail.smtp.socketFactory.port", "465");
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		prop.put("mail.smtp.auth", "true"); // 启用认证
		prop.put("mail.transport.protocol", "smtp");
		prop.put("mail.smtp.starttls.enable", false); // SSL模式
		from_addr = "lenovo_moc@163.com"; // 发送邮箱
		from_auth = "qwer1234"; // 密码
		auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from_addr, from_auth);
			}
		};*/
	}
	
	public static EmailUtil getInstance() {
		return instance;
	}
	
	@Bean
	public EmailUtil getEmail() {
		prop.put("mail.smtp.host", env.getProperty("mail.smtp.host")); // 发送邮件服务器
		prop.put("mail.smtp.port", env.getProperty("mail.smtp.port")); // 发送邮件端口
		prop.put("mail.smtp.socketFactory.port", env.getProperty("mail.smtp.socketFactory.port"));
		prop.put("mail.smtp.socketFactory.class", env.getProperty("mail.smtp.socketFactory.class"));
		prop.put("mail.smtp.auth", env.getProperty("mail.smtp.auth")); // 启用认证
		prop.put("mail.transport.protocol", env.getProperty("mail.transport.protocol"));
		prop.put("mail.smtp.starttls.enable", env.getProperty("mail.smtp.starttls.enable")); // SSL模式
		from_addr = env.getProperty("mail.from.addr"); // 发送邮箱
		from_auth = env.getProperty("mail.from.pwd"); // 密码
		auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from_addr, from_auth);
			}
		};
		return instance;
	}

	/**
	 * 发送一封简单文本邮件
	 * @param fromNickName 发件人昵称
	 * @param toList 接收邮件列表
	 * @param ccList 抄送邮件列表,可以为空
	 * @param subject 主题
	 * @param text 文本
	 */
	public boolean sendTextEmail(String fromNickName, List<String> toList, List<String> ccList, String subject, String text) {
		return send(fromNickName, toList, ccList, subject, text, null, null);
	}

	/**
	 * 发送一封HTML邮件
	 * @param fromNickName 发件人昵称
	 * @param toList 接收邮件列表
	 * @param ccList 抄送邮件列表
	 * @param subject 主题
	 * @param html html内容
	 */
	public boolean sendHtmlEmail(String fromNickName, List<String> toList, List<String> ccList, String subject, String html) {
		return send(fromNickName, toList, ccList, subject, html, "text/html;charset=UTF-8", null);
	}

	/**
	 * 发送内容为简单文本且带附件的邮件
	 * @param fromNickName 发件人昵称
	 * @param toList 接收邮件列表
	 * @param ccList 抄送邮件列表
	 * @param subject 主题
	 * @param content 文本内容
	 * @param attach 附件
	 */
	public boolean sendAttachTextEmail(String fromNickName, List<String> toList, List<String> ccList, String subject, String content, File attach) {
		return send(fromNickName, toList, ccList, subject, content, null, attach);
	}

	/**
	 * 发送内容为HTML文本且带附件的邮件
	 * @param fromNickName 发件人昵称
	 * @param toList 接收邮件列表
	 * @param ccList 抄送邮件列表
	 * @param subject 主题
	 * @param content HTML内容
	 * @param attach 附件
	 */
	public boolean sendAttachHtmlEmail(String fromNickName, List<String> toList, List<String> ccList, String subject, String content, File attach) {
		return send(fromNickName, toList, ccList, subject, content, "text/html;charset=UTF-8", attach);
	}

	/**
	 * 发送邮件
	 * @param fromNickName 发件人昵称
	 * @param toList 接收者邮件列表
	 * @param ccList 抄送者邮件列表
	 * @param subject 主题
	 * @param content 内容
	 * @param type 邮件类型
	 * @param attach 附件
	 */
	private boolean send(String fromNickName, List<String> toList, List<String> ccList, String subject, String content, String type, File attach) {
		if(toList == null || toList.isEmpty()) {
			return false;
		}

		Session session = Session.getDefaultInstance(prop, auth);
		try {
			MimeMessage message = new MimeMessage(session); //Create a default MimeMessage object.
			//设置自定义发件人昵称
			String nick="";
			try {
				nick= MimeUtility.encodeText(fromNickName);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			message.setFrom(new InternetAddress(nick+"<"+from_addr+">")); //Set From: header field of the header.
			List<InternetAddress> tmpTOList = new ArrayList<InternetAddress>();
			for(String receiver : toList) {
				tmpTOList.add(new InternetAddress(receiver));
			}
			InternetAddress[] toArr = new InternetAddress[tmpTOList.size()];
			message.addRecipients(Message.RecipientType.TO, tmpTOList.toArray(toArr));
			if(ccList != null && !ccList.isEmpty()) {
				List<InternetAddress> tmpCCList = new ArrayList<InternetAddress>();
				for(String cc : ccList) {
					tmpCCList.add(new InternetAddress(cc));
				}
				InternetAddress[] ccArr = new InternetAddress[tmpCCList.size()];
				tmpCCList.toArray(ccArr);
				message.addRecipients(Message.RecipientType.CC, ccArr);
			}

			message.setSubject(subject.trim()); //Set Subject: header field
			if(attach == null) {
				if(type != null) {
					message.setContent(content, type);
				}else {
					message.setText(content); // Set the actual message
				}
			} else { // 发送附件
				BodyPart bodyPart = new MimeBodyPart();
				if(type != null) {
					bodyPart.setContent(content, type);
				}else {
					bodyPart.setText(content);
				}
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(bodyPart);

				bodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(attach);
				bodyPart.setDataHandler(new DataHandler(source));
				bodyPart.setFileName(attach.getName());
				multipart.addBodyPart(bodyPart);

				message.setContent(multipart);
			}

			message.setSentDate(new Date());
			Transport.send(message); // Send message
			return true;
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
		return false;
	}

}
