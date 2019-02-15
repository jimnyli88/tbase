package cn.com.ut.core.common.util.email;
/**
* 发送邮件配置
* @author wuxiaohua
* @since 2013-12-22下午2:17:45
*/
public class MailSetting {

	/**
	 * 邮件发送方
	 */
	private String from = "wuxiaohua17@163.com";
	/**
	 * 邮件接收方
	 */
	private String to;
	/**
	 * 
	 */
	private String cc;
	/**
	 * 回复邮件
	 */
	private String replyTo;
	/**
	 * 邮件标题
	 */
	private String subject;
	/**
	 * 邮件内容
	 */
	private String text;

	/**
	 * 
	 * @return cc
	 */
	public String getCc() {

		return cc;
	}

	/**
	 * 
	 * @param cc
	 */
	public void setCc(String cc) {

		this.cc = cc;
	}

	/**
	 * 
	 * @return replyTo
	 */
	public String getReplyTo() {

		return replyTo;
	}

	/**
	 * 
	 * @param replyTo
	 */
	public void setReplyTo(String replyTo) {

		this.replyTo = replyTo;
	}

	/**
	 * 
	 * @return from
	 */
	public String getFrom() {

		return from;
	}

	/**
	 * 
	 * @param from
	 */
	public void setFrom(String from) {

		this.from = from;
	}

	/**
	 * 
	 * @return to
	 */
	public String getTo() {

		return to;
	}

	/**
	 * 
	 * @param to
	 */
	public void setTo(String to) {

		this.to = to;
	}

	/**
	 * 
	 * @return subject
	 */
	public String getSubject() {

		return subject;
	}

	/**
	 * 
	 * @param subject
	 */
	public void setSubject(String subject) {

		this.subject = subject;
	}

	/**
	 * 
	 * @return text
	 */
	public String getText() {

		return text;
	}

	/**
	 * 
	 * @param text
	 */
	public void setText(String text) {

		this.text = text;
	}

}
