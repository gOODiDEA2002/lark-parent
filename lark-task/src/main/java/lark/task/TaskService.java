package lark.task;

import lark.core.codec.JsonCodec;
import lark.core.sync.ThreadPoolBuilder;
import lark.core.util.Exceptions;
import lark.core.util.Strings;
import lark.task.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;

/**
 * @author cuigh
 */
@Component
public class TaskService implements lark.task.ExecutorService  {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);
    private ExecutorService pool = new ThreadPoolBuilder("Task-", 0, Integer.MAX_VALUE, 0).build();
    private ConcurrentMap<String, String> runningTasks = new ConcurrentHashMap<>();
    private ScheduleService scheduleService;
    private Map<String, lark.task.Executor> executors = new HashMap<String, lark.task.Executor>();
    //Executor log
    private Map<String, Integer> logIndexer = new HashMap<>();
    private Map<Integer, ExecuteLogger> lastLogIndexer = new HashMap<>();
    //
    TaskConfig config;

    public TaskService(TaskConfig config, ScheduleService scheduleService) {
        this.config = config;
        this.scheduleService = scheduleService;
    }

    public void setExecutor( String executorName, lark.task.Executor executor ) {
        executors.put( executorName, executor );
    }


    @Override
    public Result registry() {
        RegistryTaskRequest request = new RegistryTaskRequest();
        request.setName( config.getName() );
        request.setAddress( config.getUrl() );
        return scheduleService.registryTask( request );
    }

    @Override
    public Result deregistry() {
        RemoveTaskRequest request = new RemoveTaskRequest();
        request.setName( config.getName() );
        request.setAddress( config.getUrl() );
        return scheduleService.removeTask( request );
    }

    /**
     * 执行任务
     *
     * @param param 任务参数
     * @return
     */
    @Override
    public Result execute(ExecuteParam param) {
        LOGGER.info("接收任务:{}", JsonCodec.encode(param));

        if (param.getType() == ExecuteParam.ExecuteType.AUTO && runningTasks.containsKey(param.getName())) {
            pool.execute(() -> {
                String error = String.format("任务 %s 正在执行, 跳过此次调度(如果多次发生类型情况, 请检查调度时间是否合理)", param.getName());
                LOGGER.warn(error);
                Date start = new Date();
                this.notify( param, param.getName(), param.getId(), newResult(error), start, new Date());
            });
            return newResult(null);
        }

        String name = Strings.isEmpty(param.getAlias()) ? param.getName() : param.getAlias();
        lark.task.Executor executor = executors.get(name);
        if (executor == null) {
            LOGGER.error("找不到任务:{}", name);
            return newResult("找不到任务: " + name);
        }

        try {
            if (param.getType() == ExecuteParam.ExecuteType.AUTO) {
                runningTasks.put(param.getName(), param.getAlias());
            }
            pool.execute(() -> this.execute(executor, param));
        } catch (Exception e) {
            LOGGER.error("提交任务到线程池失败", e);
            return newResult("提交任务到线程池失败: " + e.getMessage());
        }

        return newResult(null);
    }

    private void execute(Executor executor, ExecuteParam param) {
        Date start = new Date();
        try {
            LOGGER.info("开始执行任务:{}，执行方式:{}", param.getName(), param.getType());
            //同一执行器只保留最新的日志
            this.setLogger( param );
            TaskContext ctx = new TaskContext(param);
            executor.execute(ctx);
            this.notify( param, param.getName(), param.getId(), newResult(null), start, new Date());
            LOGGER.info("任务执行成功, 耗时:{}", Duration.ofMillis(System.currentTimeMillis() - start.getTime()));
        } catch (Exception e) {
            String error = e.getMessage();
            if (Strings.isEmpty(error)) {
                error = e.toString();
            }
            this.notify( param, param.getName(), param.getId(), newResult(error), start, new Date());
            LOGGER.error("任务执行失败, 耗时:{}, 错误信息:{}", Duration.ofMillis(System.currentTimeMillis() - start.getTime()),
                    Exceptions.getStackTrace(e));
        } finally {
            if (param.getType() == ExecuteParam.ExecuteType.AUTO) {
                runningTasks.remove(param.getName());
            }
        }
    }

    /**
     * 通知 skynet 任务执行结果
     *
     * @param name   任务名称
     * @param id     任务ID
     * @param result 执行结果
     * @param start  任务执行开始时间
     * @param end    任务执行结束时间
     */
    private void notify( ExecuteParam param, String name, String id, Result result, Date start, Date end ) {
        try {
            NotifyRequest request = new NotifyRequest();
            request.setId(id);
            request.setName(name);
            request.setResult(result);
            request.setStartTime(start);
            request.setEndTime(end);
            request.setLogId( param.getLogId() );
            request.setLogDateTime(param.getLogDateTime());
            Result nr = scheduleService.notify(request);
            if (!nr.isSuccess()) {
                LOGGER.error("通知任务状态错误, ID:{}, Name:{}, Error:{}", id, name, nr.getErrorInfo());
            }
        } catch (Exception e) {
            LOGGER.error("通知任务状态异常, ID:{}, Name:{}, Error:", id, name, e);
        }
    }

    private Result newResult(String error) {
        Result result = new Result();
        result.setSuccess(Strings.isEmpty(error));
        result.setErrorInfo(error);
        return result;
    }

    @Override
    public LogResult log(LogRequest request) {
        int logId = request.getId();
        //
        LogResult result = new LogResult();
        if ( !lastLogIndexer.containsKey( logId ) ) {
            result.setSuccess( false );
            return result;
        }
        //
        ExecuteLogger logger = lastLogIndexer.get( logId );
        result.setSuccess( true );
        result.setContent( logger.getLog() );
        result.setTotalRows( logger.getRows() );
        return result;
    }

    private void setLogger(ExecuteParam param ) {
        logIndexer.put( param.getName(), param.getLogId() );
        //
        ExecuteLogger logger = new ExecuteLogger( param.getLogId(), param.getLogDateTime() );
        param.setLogger( logger );
        lastLogIndexer.put( param.getLogId(), logger );
    }
}
