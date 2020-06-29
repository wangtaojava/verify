package cn.com.hf.contller.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.util.Properties;

public class MailUtil {
	private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

	private static String myEmail = "easypayocr@163.com";
	private static String mypwd = "easypayjx18";
//	private static String myqyEmail = "wangtao@easypay.com.cn";

	public static void send(String toAddress, String path,String subject,String content) {
		try {
			logger.info("===邮件开始发送===");
			/*
			 * 第一步：创建Session，包含邮件服务器网络连接信息
			 */
			Properties props = new Properties();
			// 指定邮件的传输协议，smtp;同时通过验证
			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.smtp.auth", "true");
			Session session = Session.getDefaultInstance(props);
			// 开启调试模式
//			session.setDebug(true);
			/*
			 * 第二步：编辑邮件内容
			 */
			Message message = new MimeMessage(session);
			// 设置邮件消息头
			message.setFrom(new InternetAddress(myEmail));
			message.setRecipients(RecipientType.TO, InternetAddress.parse(toAddress));
			InternetAddress[] addressesCc = new InternetAddress[1];
//			addressesCc[0] = new InternetAddress(myqyEmail);
			addressesCc[0] = new InternetAddress(myEmail);
			message.setRecipients(RecipientType.CC, addressesCc);// 抄送
			message.setSubject(subject);
			// 设置邮件消息内容、包含附件
			Multipart msgPart = new MimeMultipart();
			message.setContent(msgPart);

			MimeBodyPart body = new MimeBodyPart(); // 正文
			MimeBodyPart attach = new MimeBodyPart(); // 附件

			msgPart.addBodyPart(body);
			msgPart.addBodyPart(attach);

			// 设置正文内容
			body.setContent("您好，您之前获取企业信息的结果已返回，企业信息excel文件已在附件中，请查收，无需回复！", "text/html;charset=utf-8");
			// 设置附件内容
			attach.setDataHandler(new DataHandler(new FileDataSource(path)));
			attach.setFileName(MimeUtility.encodeText("企业信息.xls"));

			message.saveChanges();
			/*
			 * 第三步：发送邮件
			 */
			Transport trans = session.getTransport();
			trans.connect("smtp.163.com", myEmail, mypwd);// 邮箱服务器，发送者邮箱，发送者授权码
			trans.sendMessage(message, message.getAllRecipients());
			logger.info("===邮件发送成功===");
		} catch (Exception e) {
			logger.info("===邮件发送失败===");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
//		sendMail();
		send("1392148563@qq.com", "C:\\Users\\pll\\Desktop\\company.xls","test","正文内容");
//		sendMail("wangtao@easypay.com.cn", "你好，这是一封测试邮件，无需回复。", "测试邮件");
	}

}
