package lark.api.response;

import lark.core.enums.BaseEnum;
import lark.core.lang.DataException;
import lark.core.util.Strings;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Andy Yuan
 * @date 2020/7/30
 */
public class ApiResponseBodyWrapHandler implements HandlerMethodReturnValueHandler {

    private final HandlerMethodReturnValueHandler delegate;

    public ApiResponseBodyWrapHandler(HandlerMethodReturnValueHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        return delegate.supportsReturnType(methodParameter);
    }

    @Override
    public void handleReturnValue(Object o, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        Long processTime = getProcessTime( nativeWebRequest );
        if ( processTime == Long.MIN_VALUE ) {
            delegate.handleReturnValue(o, methodParameter, modelAndViewContainer, nativeWebRequest);
        } else {
            delegate.handleReturnValue(wrapApiResult(o, processTime ), methodParameter, modelAndViewContainer, nativeWebRequest);
        }

    }

    private Long getProcessTime( NativeWebRequest nativeWebRequest ) {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        Object beginTime = request.getAttribute( "beginTime");
        if ( beginTime == null ) {
            return Long.MIN_VALUE;
        }
        //
        Long endTime = System.currentTimeMillis();
        return endTime - ( Long ) beginTime;
    }

    private ApiResponse wrapApiResult(Object o, Long processTime ) {
        ApiResponse result = null;
        if (o == null) {
            result = new ApiResponse.ErrorResponse();
        } else {
            result = new ApiResponse(o);
        }
        //
        if (o instanceof ApiFaultException) {
            ApiFaultException e = ((ApiFaultException) o);
            result.setCode(e.getCode());
            result.setMsg(e.getMessage());
        }
        if (o instanceof DataException) {
            DataException e = ((DataException) o);
            result.setCode(e.getCode());
            result.setMsg(e.getMessage());
            result.setData(e.getData());
        }
        if (o instanceof Exception) {
            Exception e = ((Exception) o);
            result.setCode(BaseEnum.DefaultEnum.ERROR.getCode());
            result.setMsg(BaseEnum.DefaultEnum.ERROR.getMsg());
        }
        //
        result.setProcessTime( processTime );
        return result;
    }


}
