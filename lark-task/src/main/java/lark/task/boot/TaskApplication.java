package lark.task.boot;

import lark.core.boot.Application;
import lark.core.lang.BusinessException;
import lark.task.Executor;
import lark.task.ScheduleService;
import lark.task.Task;
import lark.task.TaskService;
import lark.task.data.Result;
import lark.task.util.Retries;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;

/**
 * @author cuigh
 */
public class TaskApplication extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskApplication.class);
    public static final int MAX_RETRIES = 3;
    //
    private ScheduleService scheduleService;
    private TaskService executorService;
    //
    private TaskScheduler scheduler;
    private ScheduledFuture future;
    private final Duration interval = Duration.ofMinutes( 3 );

    public TaskApplication( Class<?>... primarySources) {
        this(null, primarySources);
    }

    public TaskApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
        super(resourceLoader, primarySources);
    }

    @Override
    protected void load() {
        super.load();
        //
        scheduleService = ctx.getBean( ScheduleService.class );
        executorService = ctx.getBean( TaskService.class );
        //
        Log logger = getApplicationLog();
        Map<String, Executor> beans = ctx.getBeansOfType(Executor.class);
        logger.info(String.format("Found %d executors", beans.size()));
        //
        beans.forEach((n, executor) -> {
            Class<?> clazz = executor.getClass();
            Task task = clazz.getAnnotation(Task.class);
            String name = (task == null || StringUtils.isEmpty(task.name())) ? clazz.getSimpleName() : task.name();
            logger.info(String.format("Register executor: %s -> %s", name, executor.getClass().getName()));
            executorService.setExecutor(name, executor);
        });
    }

    @Override
    protected void start() {
        scheduler = ctx.getBean( "registryTaskScheduler", ThreadPoolTaskScheduler.class );
        Retries.tryTimes(MAX_RETRIES, () -> {
            Result result = executorService.registry();
            if ( !result.isSuccess() ) {
                throw new BusinessException();
            }
        });
        // 注册Task会导致XXL清空执行器，先屏蔽
//        this.future = scheduler.scheduleWithFixedDelay(() -> {
//            executorService.registry();
//        }, interval);
    }

    @Override
    protected void stop() {
        if ( this.future != null ) {
            this.future.cancel(false );
        }
        //
        executorService.deregistry();
    }

}
