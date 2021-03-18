package lark.api.web;


import lark.api.response.ApiResponse;
import lark.core.enums.BaseEnum;
import lark.core.lang.BusinessException;
import lark.core.lang.DataException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


/**
 * 全局异常处理
 *
 * @description: TODO
 * @author: yandong
 * @date: 2021/3/18 9:53 上午
 */
@RestControllerAdvice
public class RestExceptionHandler {


    /**
     * 参数校验错误的异常处理
     *
     * @param e 参数校验错误异常
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuffer errorMsg = new StringBuffer();
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        allErrors.forEach(objectError ->
                errorMsg.append(objectError.getDefaultMessage()).append(",")
        );
        errorMsg.replace(errorMsg.length() - 1, errorMsg.length(), "");
        return ApiResponse.RESULT(BaseEnum.DefaultEnum.CODE1001, errorMsg.toString());
    }


    /**
     * 全局异常
     *
     * @description: TODO
     * @return:
     * @author: yandong
     * @date: 2021/3/18 10:02 上午
     */
    @ExceptionHandler(value = {Exception.class})
    public ApiResponse handleException(Exception ex) {
        return ApiResponse.ERROR(ex.getLocalizedMessage());
    }

    /**
     * DataException
     *
     * @description: TODO
     * @return:
     * @author: yandong
     * @date: 2021/3/18 10:02 上午
     */
    @ExceptionHandler(value = {DataException.class})
    public ApiResponse dataException(DataException dataException) {
        return ApiResponse.EXCEPTION(dataException, dataException.getData());
    }

    /**
     * BusinessException
     *
     * @description: TODO
     * @return:
     * @author: yandong
     * @date: 2021/3/18 10:03 上午
     */
    @ExceptionHandler(value = {BusinessException.class})
    public ApiResponse businessException(BusinessException businessException) {
        return ApiResponse.EXCEPTION(businessException);
    }


}
