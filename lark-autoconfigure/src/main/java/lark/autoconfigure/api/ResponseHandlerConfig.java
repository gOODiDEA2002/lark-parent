package lark.autoconfigure.api;

import lark.api.response.ApiResponseBodyWrapHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * API响应结果的配置
 *
 * @author Andy Yuan
 * @date 2020/7/30
 */
@Configuration
public class ResponseHandlerConfig implements InitializingBean {

    private final RequestMappingHandlerAdapter adapter;

    public ResponseHandlerConfig( RequestMappingHandlerAdapter adapter ) {
        this.adapter = adapter;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = adapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList(returnValueHandlers);
        decorateHandlers(handlers);
        adapter.setReturnValueHandlers(handlers);
    }


    private void decorateHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        for (HandlerMethodReturnValueHandler handler : handlers) {
            if (handler instanceof RequestResponseBodyMethodProcessor) {
                ApiResponseBodyWrapHandler decorator = new ApiResponseBodyWrapHandler(handler);
                handlers.set(handlers.indexOf(handler), decorator);
                break;
            }
        }
    }
}