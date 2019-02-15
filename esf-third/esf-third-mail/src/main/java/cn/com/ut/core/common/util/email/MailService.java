package cn.com.ut.core.common.util.email;

import java.util.HashMap;
import java.util.Map;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.com.ut.core.common.util.PropertiesUtil;

import freemarker.template.Template;
/**
* 邮件服务
* @author wuxiaohua
* @since 2013-12-22下午2:17:45
*/
@Service
public class MailService {

	/**
	 * 邮件发送者
	 */
	@Autowired(required = false)
	private JavaMailSender sender;

	/**
	 * freeMarkerConfigurer
	 */
	@Autowired(required = false)
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Autowired(required = false)
	private TaskExecutor taskExecutor;

	private static final Logger logger = LoggerFactory.getLogger(MailService.class);

	/**
	 * send simple mail
	 * @param userId
	 */
	public void sendSimpleMail(int userId) {

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom("wuxiaohua17@163.com");
		msg.setTo("wuxiaohua17@126.com");
		msg.setReplyTo("wuxiaohua17@126.com");
		msg.setCc("wuxiaohua17@126.com");
		msg.setSubject("注册成功");
		msg.setText("恭喜，您在UIF门户注册成功!您的用户ID为：" + userId);
		sender.send(msg);
	}

	/**
	 * 发送Html格式邮件
	 * @param mailSetting
	 * @throws MessagingException
	 */
	public void sendHtmlMail(MailSetting mailSetting) throws MessagingException {

		MimeMessage msg = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg, false, "utf-8");
		helper.setFrom(mailSetting.getFrom());
		helper.setTo(mailSetting.getTo());
		if (mailSetting.getCc() != null)
			helper.setCc(mailSetting.getCc());
		helper.setReplyTo(mailSetting.getReplyTo());
		helper.setSubject(mailSetting.getSubject());
		StringBuilder htmlPrefix = new StringBuilder();
		htmlPrefix
				.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
		htmlPrefix
				.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		htmlPrefix.append("<title>UIF</title></head><body>");
		String htmlPostfix = "</body></html>";
		StringBuilder htmlText = new StringBuilder();
		htmlText.append(htmlPrefix);
		htmlText.append(mailSetting.getText());
		htmlText.append(htmlPostfix);
		helper.setText(htmlText.toString(), true);
		sender.send(msg);
	}

	/**
	 * send inline mail
	 * @throws MessagingException
	 */
	public void sendInlineMail() throws MessagingException {

		MimeMessage msg = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg, true, "utf-8");
		helper.setFrom("masterspring@163.com");
		helper.setTo("masterspring3@gmail.com");
		helper.setSubject("注册成功");
		String htmlText = "<html><head>"
				+ "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">"
				+ "</head><body>" + "欢迎访问UIF门户！</hr>" + "<div><img src=\"cid:img01\"></img></div>"
				+ "</body></html>";
		helper.setText(htmlText, true);
		ClassPathResource img = new ClassPathResource("bbt.gif");
		helper.addInline("img01", img);
		sender.send(msg);
	}

	/**
	 * send attachment mail
	 * @throws Exception
	 */
	public void sendAttachmentMail() throws Exception {

		MimeMessage msg = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg, true, "utf-8");
		helper.setFrom("masterspring@163.com");
		helper.setTo("masterspring3@gmail.com");
		helper.setSubject("注册成功");
		helper.setText("欢迎访问UIF门户！");
		ClassPathResource file1 = new ClassPathResource("bbt.zip");
		helper.addAttachment("file01.zip", file1.getFile());

		ClassPathResource file2 = new ClassPathResource("file.doc");
		helper.addAttachment("file02.doc", file2.getFile());
		sender.send(msg);
	}

	/**
	 * send alternative mail
	 * @throws Exception
	 */
	public void sendAlternativeMail() throws Exception {

		MimeMessagePreparator mmp = new MimeMessagePreparator() {
			public void prepare(MimeMessage msg) throws Exception {

				MimeMessageHelper helper = new MimeMessageHelper(msg, true, "utf-8");
				helper.setFrom("masterspring@163.com");
				helper.setTo("masterspring3@gmail.com");
				helper.setSubject("注册成功");

				MimeMultipart mmPart = new MimeMultipart("alternative");
				msg.setContent(mmPart);

				BodyPart plainTextPart = new MimeBodyPart();
				plainTextPart.setText("欢迎访问UIF门户！");
				mmPart.addBodyPart(plainTextPart);

				BodyPart htmlPart = new MimeBodyPart();
				String htmlText = "<html><head>"
						+ "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">"
						+ "</head><body><font size='20' size='30'>" + "欢迎访问UIF门户</font>"
						+ "</body></html>";
				htmlPart.setContent(htmlText, "text/html;charset=utf-8");
				mmPart.addBodyPart(htmlPart);
			}
		};
		sender.send(mmp);
	}

	/**
	 * send template mail
	 * @param userId
	 * @throws MessagingException
	 */
	public void sendTemplateMail(String userId) throws MessagingException {

		MimeMessage msg = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg, false, "utf-8");
		helper.setFrom("masterspring@163.com");
		helper.setTo("masterspring3@gmail.com");
		helper.setSubject("注册成功:基于模板");
		String htmlText = getMailText(userId);
		helper.setText(htmlText, true);
		sender.send(msg);
	}

	/**
	 * get mail text
	 * @param userId
	 * @return 获取邮件文本
	 */
	private String getMailText(String userId) {

		String htmlText = null;
		try {
			Template tpl = freeMarkerConfigurer.getConfiguration().getTemplate("registerUser.ftl");
			Map<String, String> map = new HashMap<String, String>();
			map.put("userId", userId);
			htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(tpl, map);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return htmlText;
	}

	/**
	 * 异步发送邮件
	 * @param mailSetting
	 */
	public void sendAsyncMail(final MailSetting mailSetting) {

		taskExecutor.execute(new Runnable() {
			public void run() {

				try {
					sendHtmlMail(mailSetting);
					logger.debug("邮件发送成功！");
				} catch (Exception e) {
					e.printStackTrace();
					logger.debug("邮件发送失败！异常信息：" + e.getMessage());
				}
			}
		});
	}
	
	/**
	 * send email
	 * @param toEmail
	 * @param subject
	 * @param text
	 */
	public void sendEmail(String toEmail,String subject,String text){
		String from = PropertiesUtil.getProperty("mail.from");
		MailSetting mailSetting = new MailSetting();
		mailSetting.setFrom(from);
		mailSetting.setTo(toEmail);
		mailSetting.setReplyTo(from);
		mailSetting.setSubject(subject);
		mailSetting.setText(text.toString());
		sendAsyncMail(mailSetting);
	}
}
