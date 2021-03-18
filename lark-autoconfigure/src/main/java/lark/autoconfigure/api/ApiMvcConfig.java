package lark.autoconfigure.api;

import lark.api.web.ApiFilter;
import lark.api.web.ApiInterceptor;
import lark.api.web.ApiServlet;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy Yuan on 2020/8/18.
 */
@Configuration
public class ApiMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public FilterRegistrationBean<ApiFilter> filterRegistrationBean() {
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
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new ApiServlet(), "/servlet");
    }

    /**
     * 设置允许跨域访问
     *
     * @param registry 配置注册信息
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                //允许访问的接口地址
                .addMapping("/**")
                //允许发起跨域访问的域名
                .allowedOriginPatterns("*")
                //允许跨域访问的方法
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                //是否带上cookie信息
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }
}
