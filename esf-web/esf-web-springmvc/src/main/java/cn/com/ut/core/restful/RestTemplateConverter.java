package cn.com.ut.core.restful;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

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

import cn.com.ut.core.common.util.serializer.FstCodec;
import cn.com.ut.core.common.util.serializer.JavaSerializer;

/**
 * 
 * @author wuxiaohua
 * @since 2016-8-24
 * @see AbstractMessageConverterMethodArgumentResolver
 * @see AbstractMessageConverterMethodProcessor
 *
 */
@Component
public class RestTemplateConverter extends AbstractHttpMessageConverter<RestData> {

	private static final Logger logger = LoggerFactory.getLogger(RestTemplateConverter.class);

	public final static Charset UTF8 = Charset.forName("UTF-8");

	private Charset charset = UTF8;

	public RestTemplateConverter() {

		// 当请求头为空时，Content-Type默认为application/octet-stream
		super(MediaType.APPLICATION_OCTET_STREAM);
	}

	@Override
	protected boolean supports(Class<?> clazz) {

		return (ResponseWrap.class.isAssignableFrom(clazz));
	}

	public Charset getCharset() {

		return this.charset;
	}

	public void setCharset(Charset charset) {

		this.charset = charset;
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {

		return readSupports(clazz) && canRead(mediaType);
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {

		return writeSupports(clazz) && canWrite(mediaType);
	}

	protected boolean writeSupports(Class<?> clazz) {

		return (ResponseData.class.isAssignableFrom(clazz)
				|| RequestData.class.isAssignableFrom(clazz));
	}

	protected boolean readSupports(Class<?> clazz) {

		return (ResponseData.class.isAssignableFrom(clazz)
				|| RequestData.class.isAssignableFrom(clazz));
	}

	protected RestData readInternalWithJdkCodec(Class<? extends RestData> clazz,
			HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

		InputStream inputStream = inputMessage.getBody();
		if (inputStream == null) {
			return null;
		} else {
			byte[] data = IOUtils.toByteArray(inputStream);
			if (RequestData.class.isAssignableFrom(clazz)
					|| ResponseData.class.isAssignableFrom(clazz)
					|| RestData.class.isAssignableFrom(clazz)) {
				RestData restData = JavaSerializer.deserialize(data);
				return restData;
			} else {
				return null;
			}
		}

	}

	protected void writeInternalWithJdkCodec(RestData restData, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		outputMessage.getHeaders()
				.setContentType(new MediaType(MediaType.APPLICATION_OCTET_STREAM, UTF8));
		byte[] data = JavaSerializer.serialize(restData);
		outputMessage.getHeaders().setContentLength(data.length);
		OutputStream out = outputMessage.getBody();
		out.write(data);
	}

	@Override
	protected RestData readInternal(Class<? extends RestData> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {

		InputStream inputStream = inputMessage.getBody();
		if (inputStream == null) {
			return null;
		} else {
			if (RequestData.class.isAssignableFrom(clazz)
					|| ResponseData.class.isAssignableFrom(clazz)
					|| RestData.class.isAssignableFrom(clazz)) {
				RestData restData = FstCodec.decode(inputStream);
				return restData;
			} else {
				return null;
			}
		}

	}

	@Override
	protected void writeInternal(RestData restData, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		outputMessage.getHeaders()
				.setContentType(new MediaType(MediaType.APPLICATION_OCTET_STREAM, UTF8));
		OutputStream output = outputMessage.getBody();
		FstCodec.encode(output, restData);
		output.close();
	}

}
