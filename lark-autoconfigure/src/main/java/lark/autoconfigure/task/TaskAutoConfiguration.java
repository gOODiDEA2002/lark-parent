package lark.autoconfigure.task;

import lark.core.util.Networks;
import lark.task.ExecutorService;
import lark.task.ScheduleService;
import lark.task.TaskConfig;
import lark.task.TaskService;
import lark.task.boot.TaskApplication;
import lark.task.xxl.job.XxlJobExecutorService;
import lark.task.xxl.job.XxlJobScheduleService;
import lark.task.xxl.job.config.XxlJobScheduleConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author andy
 */
@Configuration
@ConditionalOnClass({TaskApplication.class})
@EnableConfigurationProperties(TaskServiceProperties.class)
public class TaskAutoConfiguration {

    @Autowired
    Environment environment;

    @Bean
    @ConditionalOnMissingBean
    public ScheduleService scheduleService(TaskServiceProperties props) {
        XxlJobScheduleConfig xxlJobScheduleConfig = new XxlJobScheduleConfig();
        xxlJobScheduleConfig.setAddress(props.getAddress());
        xxlJobScheduleConfig.setAccessToken(props.getToken());
        //
        ScheduleService scheduleService = new XxlJobScheduleService( xxlJobScheduleConfig );
        return scheduleService;
    }

    @Bean
    public TaskService taskService( TaskServiceProperties props, ScheduleService scheduleService ) {
        TaskConfig config = new TaskConfig();
        config.setHost( Networks.getLocalIP4() );
        config.setName( environment.getProperty( "spring.application.name") );
        config.setPort( Integer.parseInt( environment.getProperty( "server.port") ) );
        config.setUrl( props.getExecutorUrl() );
        //
        return new TaskService( config, scheduleService );
    }

    @Bean
    public XxlJobExecutorService xxlJobExecutorService( ExecutorService taskService ) {
        return new XxlJobExecutorService( taskService );
    }

    @Bean( name = "registryTaskScheduler")
    public ThreadPoolTaskScheduler registryTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("taskScheduler-");
        return scheduler;
    }
}
