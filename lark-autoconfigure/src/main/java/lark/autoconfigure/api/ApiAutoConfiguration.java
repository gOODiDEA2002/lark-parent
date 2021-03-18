package lark.autoconfigure.api;

import lark.api.boot.ApiApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * @author andy
 */
@Configuration
@ConditionalOnClass({ApiApplication.class})
public class ApiAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ApiResponseHandlerConfig responseHandlerConfig(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        ApiResponseHandlerConfig responseHandlerConfig = new ApiResponseHandlerConfig(requestMappingHandlerAdapter);
        return responseHandlerConfig;
    }

    @Bean
    public ApiMvcConfig mvcConfig() {
        ApiMvcConfig mvcConfig = new ApiMvcConfig();
        return mvcConfig;
    }

    @Bean
    public ApiDocConfig swaggerConfig() {
        ApiDocConfig swaggerConfig = new ApiDocConfig();
        return swaggerConfig;
    }

}
