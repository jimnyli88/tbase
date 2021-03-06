package cn.com.ut.core.restful;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.com.ut.core.common.util.ExceptionUtil;

/**
 * 
 * @author wuxiaohua
 * @since 2018/7/2
 * @see AbstractMessageConverterMethodArgumentResolver
 * @see AbstractMessageConverterMethodProcessor
 *
 */
@Component
public class RestInfoConverter extends AbstractHttpMessageConverter<RestInfo> {

	private static final Logger logger = LoggerFactory.getLogger(RestInfoConverter.class);

	public final static Charset UTF8 = Charset.forName("UTF-8");

	private Charset charset = UTF8;

	private SerializerFeature[] features = new SerializerFeature[0];

	private SerializeConfig serializeConfig;

	public RestInfoConverter() {

		// 当请求头为空时，Content-Type默认为application/octet-stream
		super(MediaType.APPLICATION_JSON_UTF8, new MediaType("application", "*+json", UTF8),
				MediaType.APPLICATION_FORM_URLENCODED, new MediaType("text", "*", UTF8),
				MediaType.APPLICATION_OCTET_STREAM);

		serializeConfig = new SerializeConfig();
		serializeConfig
				.setPropertyNamingStrategy(com.alibaba.fastjson.PropertyNamingStrategy.SnakeCase);
	}

	@Override
	protected boolean supports(Class<?> clazz) {

		return (RestInfo.class.isAssignableFrom(clazz)
				|| ResponseInfo.class.isAssignableFrom(clazz));
	}

	public Charset getCharset() {

		return this.charset;
	}

	public void setCharset(Charset charset) {

		this.charset = charset;
	}

	public SerializerFeature[] getFeatures() {

		return features;
	}

	public void setFeatures(SerializerFeature... features) {

		this.features = features;
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {

		return writeSupports(clazz) && canWrite(mediaType);
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {

		return readSupports(clazz) && canRead(mediaType);
	}

	protected boolean writeSupports(Class<?> clazz) {

		return ResponseInfo.class.isAssignableFrom(clazz);
	}

	protected boolean readSupports(Class<?> clazz) {

		return RestInfo.class.isAssignableFrom(clazz);
	}

	@Override
	protected RestInfo readInternal(Class<? extends RestInfo> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {

		if (inputMessage.getBody() == null) {
			return null;
		}

		String encodeBody = IOUtils.toString(inputMessage.getBody(), Charset.defaultCharset());
		logger.debug("encode info===={}", encodeBody);

		String mediaSubType = inputMessage.getHeaders().getContentType().getSubtype();
		// 表单格式须先进行解码，并去掉末尾的等于号
		if ("x-www-form-urlencoded".equalsIgnoreCase(mediaSubType)) {
			encodeBody = URLDecoder.decode(encodeBody, "UTF-8");
			encodeBody = encodeBody.substring(0, encodeBody.length() - 1);
			logger.debug("decode info===={}", encodeBody);
		}

		// 请求内容可以是JSON格式或JSON数组格式
		RestInfo intputObject = null;
		try {
			intputObject = JSON.parseObject(encodeBody, clazz);
		} catch (JSONException e) {
			ExceptionUtil.logError(e);
		} finally {
		}

		return intputObject;
	}

	@Override
	protected void writeInternal(RestInfo streamData, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		// 统一设置响应编码和长度，也可以在Controller方法上使用@RequestMapping(produces="application/json;charset=UTF-8")
		outputMessage.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
		Object result = ((ResponseInfo) streamData).getData();
		byte[] bs = null;
		// if (isPrimitive(result)) {
		// outputMessage.getHeaders().setContentType(new MediaType("text", "html",
		// UTF8));
		// bs = TypeConvert.getStringValue(result).getBytes(UTF8);
		// } else if (result != null && result.getClass().isArray()
		// && result.getClass().getComponentType().equals(byte.class)) {
		// bs = (byte[]) result;
		// outputMessage.getHeaders()
		// .setContentType(new MediaType(MediaType.APPLICATION_OCTET_STREAM, UTF8));
		// } else {
		// bs = JSON.toJSONBytes(result,
		// SerializerFeature.WriteNullStringAsEmpty);
		if (((ResponseInfo) streamData).isSnakeCase()) {
			bs = JSON.toJSONBytes(streamData, serializeConfig,
					SerializerFeature.DisableCircularReferenceDetect,
					SerializerFeature.WriteMapNullValue);
		} else {
			bs = JSON.toJSONBytes(streamData, SerializerFeature.DisableCircularReferenceDetect,
					SerializerFeature.WriteMapNullValue);
		}
		// }
		outputMessage.getHeaders().setContentLength(bs.length);
		// 设置Headers必须在getBody方法调用之前，之后存放Headers的Map将变为只读
		OutputStream out = outputMessage.getBody();
		logger.debug("print info===={}", new String(bs));
		out.write(bs);
	}

	protected boolean isPrimitive(Object valueObject) {

		return (valueObject instanceof Number) || (valueObject instanceof Character)
				|| (valueObject instanceof Date) || (valueObject instanceof String);

	}
}
