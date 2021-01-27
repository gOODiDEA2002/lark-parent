package lark.autoconfigure.api;

import lark.api.boot.ApiApplication;
import lark.autoconfigure.task.TaskServiceProperties;
import lark.core.util.Networks;
import lark.task.ExecutorService;
import lark.task.ScheduleService;
import lark.task.TaskConfig;
import lark.task.TaskService;
import lark.task.xxl.job.XxlJobExecutorService;
import lark.task.xxl.job.XxlJobScheduleService;
import lark.task.xxl.job.config.XxlJobScheduleConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * @author andy
 */
@Configuration
@ConditionalOnClass({ApiApplication.class})
public class ApiAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ResponseHandlerConfig responseHandlerConfig( RequestMappingHandlerAdapter requestMappingHandlerAdapter ) {
        ResponseHandlerConfig responseHandlerConfig = new ResponseHandlerConfig( requestMappingHandlerAdapter );
        return responseHandlerConfig;
    }

    @Bean
    public MvcConfig mvcConfig() {
        MvcConfig mvcConfig = new MvcConfig();
        return mvcConfig;
    }

    @Bean
    public SwaggerConfig swaggerConfig() {
        SwaggerConfig swaggerConfig = new SwaggerConfig();
        return swaggerConfig;
    }
}
