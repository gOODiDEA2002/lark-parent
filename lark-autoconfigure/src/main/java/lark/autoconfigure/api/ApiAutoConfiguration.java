package lark.autoconfigure.api;

import lark.api.boot.ApiApplication;
import lark.api.web.ApiFilter;
import lark.api.web.ApiServlet;
import lark.api.web.RestExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

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
    public ApiDocConfig docConfig() {
        ApiDocConfig docConfig = new ApiDocConfig();
        return docConfig;
    }

    @Bean
    public FilterRegistrationBean<ApiFilter> filterRegistrationBean() {
        FilterRegistrationBean<ApiFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        List<String> uriList = new ArrayList<>(1);
        uriList.add("/api/**");
        filterRegistrationBean.setFilter(new ApiFilter());
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.setUrlPatterns(uriList);
        filterRegistrationBean.setName("ApiFilter");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new ApiServlet(), "/lark/*");
    }

    @Bean
    public RestExceptionHandler restControllerAdvice() {
        return new RestExceptionHandler();
    }
}
