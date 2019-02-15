package cn.com.ut.weixin.protocol;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import cn.com.ut.weixin.common.RandomStringGenerator;
import cn.com.ut.weixin.common.Signature;
import cn.com.ut.weixin.common.WeiXinField;
import cn.com.ut.weixin.config.AppConfigure;
import cn.com.ut.weixin.config.JsapiConfigure;
import cn.com.ut.weixin.config.NativeConfigure;

public abstract class ModuleBase {

	/**
	 * 公众账号ID
	 */
	private String appid;
	/**
	 * 商户号
	 */
	@WeiXinField("mch_id")
	private String mchId;
	/**
	 * 随机字符串
	 */
	@WeiXinField("nonce_str")
	private String nonceStr;
	/**
	 * 签名
	 */
	private String sign;
	/**
	 * 签名类型
	 */
	@WeiXinField("sign_type")
	private String signType;

	public ModuleBase() {
		setNonceStr(RandomStringGenerator.getRandomStringByLength(32));
	}

	/**
	 * 初始化公众号支付
	 */
	public void initJsapiPay() {

		setAppid(JsapiConfigure.getAppID());
		setMchId(JsapiConfigure.getMchID());
		setSignType(JsapiConfigure.getSign_type());
	}

	/**
	 * 初始化APP支付
	 */
	public void initAppPay() {

		setAppid(AppConfigure.getAppID());
		setMchId(AppConfigure.getMchID());
		setSignType(AppConfigure.getSign_type());
	}

	/**
	 * 初始化扫码支付
	 */
	public void initNativePay() {

		setAppid(NativeConfigure.getAppID());
		setMchId(NativeConfigure.getMchID());
		setSignType(NativeConfigure.getSign_type());
	}

	public void beforeSend(String key) {

		String sign = Signature.getSign(toMap(), key);
		setSign(sign);// 把签名数据设置到Sign这个属性中
	}

	public Map<String, Object> toMap() {

		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = this.getClass().getDeclaredFields();
		Field[] fatherFields = this.getClass().getSuperclass().getDeclaredFields();
		Object obj = null;
		for (Field field : fields) {
			try {
				WeiXinField ano = field.getAnnotation(WeiXinField.class);
				String fieldName = null;
				if (ano != null) {
					fieldName = ano.value();
				}
				if (fieldName == null || "".equals(fieldName.trim())) {
					fieldName = field.getName();
				}
				PropertyDescriptor pd = new PropertyDescriptor(field.getName(), this.getClass());
				Method getMethod = pd.getReadMethod();// 获得get方法
				obj = getMethod.invoke(this);// 执行get方法返回一个Object
				if (obj != null) {
					map.put(fieldName, obj);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IntrospectionException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		for (Field field : fatherFields) {
			try {
				WeiXinField ano = field.getAnnotation(WeiXinField.class);
				String fieldName = null;
				if (ano != null) {
					fieldName = ano.value();
				}
				if (fieldName == null || "".equals(fieldName.trim())) {
					fieldName = field.getName();
				}
				PropertyDescriptor pd = new PropertyDescriptor(field.getName(), this.getClass());
				Method getMethod = pd.getReadMethod();// 获得get方法
				obj = getMethod.invoke(this);// 执行get方法返回一个Object
				if (obj != null) {
					map.put(fieldName, obj);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IntrospectionException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	public String getAppid() {

		return appid;
	}

	public void setAppid(String appid) {

		this.appid = appid;
	}

	public String getMchId() {

		return mchId;
	}

	public void setMchId(String mchId) {

		this.mchId = mchId;
	}

	public String getNonceStr() {

		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {

		this.nonceStr = nonceStr;
	}

	public String getSign() {

		return sign;
	}

	public void setSign(String sign) {

		this.sign = sign;
	}

	public String getSignType() {

		return signType;
	}

	public void setSignType(String signType) {

		this.signType = signType;
	}

}
