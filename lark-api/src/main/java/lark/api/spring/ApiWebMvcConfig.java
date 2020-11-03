package lark.api.spring;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import lark.api.web.ApiFilter;
import lark.api.web.ApiInterceptor;
import lark.api.web.ApiServlet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy Yuan on 2020/8/18.
 */
@Configuration
public class ApiWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public FilterRegistrationBean<ApiFilter> filterRegistrationBean(){
        FilterRegistrationBean<ApiFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        List<String> uriList = new ArrayList<>(1);
        uriList.add("/**");
        filterRegistrationBean.setFilter(new ApiFilter());
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.setUrlPatterns(uriList);
        filterRegistrationBean.setName("ApiFilter");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        return new ServletRegistrationBean(new ApiServlet(), "/servlet");
    }
}
