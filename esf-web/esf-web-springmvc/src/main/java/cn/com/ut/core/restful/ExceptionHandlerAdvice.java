package cn.com.ut.core.restful;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import cn.com.ut.auth.exception.AuthException;
import cn.com.ut.core.common.constant.ConstantUtil;
import cn.com.ut.core.common.exception.ServiceException;
import cn.com.ut.core.common.exception.ValidateException;
import cn.com.ut.core.common.util.CollectionUtil;
import cn.com.ut.core.common.util.CommonUtil;
import cn.com.ut.core.common.util.ExceptionUtil;

/**
 * 全局异常处理，只对业务Controller生效(basePackages用于排除系统Controller)
 * <p>
 * -1 表示程序员可预见主动抛出的异常，-2表示预料之外的异常
 *
 * @author wuxiaohua
 */
@RestControllerAdvice(annotations = RestController.class, basePackages = {"cn.com.ut"})
public class ExceptionHandlerAdvice {

    private ResponseInfo deal(int code, String msg, Exception ex) {

        ExceptionUtil.logError(ex);
        ResponseInfo responseInfo = ResponseInfo.build();
        responseInfo.setCode(code);
        responseInfo.setMsg(msg);
        return responseInfo;
    }

    @ExceptionHandler({AuthException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseInfo deal(AuthException ex) {

        return deal(ConstantUtil.BIZ_ERR_CODE, CommonUtil.isEmpty(ex.getMessage()) ? "auth failure" : ex.getMessage(), ex);
    }

    @ExceptionHandler({ValidateException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseInfo deal(ValidateException ex) {

        return deal(ConstantUtil.BIZ_ERR_CODE, ex.getMessage(), ex);
    }

    @ExceptionHandler({ServiceException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseInfo deal(ServiceException ex) {

        return deal(ConstantUtil.BIZ_ERR_CODE, ex.getMessage(), ex);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseInfo deal(DataIntegrityViolationException ex) {

        return deal(ConstantUtil.BIZ_ERR_CODE, "存在重复记录或字段内容过长", ex);
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseInfo deal(RuntimeException ex) {

        return deal(ConstantUtil.UNKNOWN_ERR_CODE, "发生系统错误：" + ex.getMessage(), ex);
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseInfo deal(Exception ex) {

        return deal(ConstantUtil.UNKNOWN_ERR_CODE, "发生系统错误： " + ex.getMessage(), ex);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseInfo dealValidException(Exception ex) {


        List<FieldError> fes = null;
        if (ex instanceof MethodArgumentNotValidException) {
            fes = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors();
        } else {
            fes = ((BindException) ex).getFieldErrors();
        }

        return deal(ConstantUtil.BIZ_ERR_CODE, buildErrors(fes), ex);
    }

    private String buildErrors(List<FieldError> fes) {

        StringBuilder builder = new StringBuilder();
        if (CollectionUtil.isEmptyCollection(fes)) {
            return "";
        } else {
            fes.forEach((fe) -> builder.append(fe.getField() + " " + fe.getDefaultMessage() + ";"));
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }
}
