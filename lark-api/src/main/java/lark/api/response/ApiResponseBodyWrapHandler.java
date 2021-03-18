package lark.api.response;

import lark.core.enums.BaseEnum;
import lark.core.lang.DataException;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

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
        delegate.handleReturnValue(wrapApiResult(o), methodParameter, modelAndViewContainer, nativeWebRequest);
    }

    private ApiResponse wrapApiResult(Object o) {
        if (o == null) {
            return new ApiResponse.ErrorResponse();
        }
        //
        ApiResponse result = new ApiResponse(o);

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


        return result;
    }


}
