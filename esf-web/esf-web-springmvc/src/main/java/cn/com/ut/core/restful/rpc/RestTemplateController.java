package cn.com.ut.core.restful.rpc;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cn.com.ut.core.common.annotation.ServiceComponent;
import cn.com.ut.core.common.util.ExceptionUtil;
import cn.com.ut.core.common.util.reflect.ReflectException;
import cn.com.ut.core.common.util.reflect.ReflectUtil;
import cn.com.ut.core.restful.RequestData;
import cn.com.ut.core.restful.ResponseData;

@ServiceComponent(session = false)
@RestController
@RequestMapping(value = "/rest")
public class RestTemplateController implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@PostMapping(value = "/{serviceName}/{methodName}")
	public ResponseData doPost(@PathVariable String serviceName, @PathVariable String methodName,
			@RequestBody RequestData requestData) {

		Object result = null;
		try {
			result = ReflectUtil.invokeServiceMethod(serviceName, methodName,
					requestData.getParams(), applicationContext);
		} catch (ReflectException e) {
			throw e;
		}

		ResponseData responseData = ResponseData.build(result);
		return responseData;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

		this.applicationContext = applicationContext;
	}

	@ExceptionHandler({ Exception.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public String exceptionHandler(Exception ex) {

		ExceptionUtil.logError(ex);
		return ex.getMessage();
	}

}
