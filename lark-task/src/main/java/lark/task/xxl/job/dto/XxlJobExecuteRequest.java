package lark.task.xxl.job.dto;

import lark.core.util.Strings;
import lark.task.data.Arg;
import lark.task.data.ExecuteParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 //            "jobId":1,                                  // 任务ID
 //            "executorHandler":"demoJobHandler",         // 任务标识
 //            "executorParams":"demoJobHandler",          // 任务参数
 //            "executorBlockStrategy":"COVER_EARLY",      // 任务阻塞策略，可选值参考 com.xxl.job.core.enums.ExecutorBlockStrategyEnum
 //            "executorTimeout":0,                        // 任务超时时间，单位秒，大于零时生效
 //            "logId":1,                                  // 本次调度日志ID
 //            "logDateTime":1586629003729,                // 本次调度日志时间
 //            "glueType":"BEAN",                          // 任务模式，可选值参考 com.xxl.job.core.glue.GlueTypeEnum
 //            "glueSource":"xxx",                         // GLUE脚本代码
 //            "glueUpdatetime":1586629003727,             // GLUE脚本更新时间，用于判定脚本是否变更以及是否需要刷新
 //            "broadcastIndex":0,                         // 分片参数：当前分片
 //            "broadcastTotal":0                          // 分片参数：总分片
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class XxlJobExecuteRequest {
    int jobId;
    String executorHandler;
    String executorParams;
    String executorBlockStrategy;
    int executorTimeout;
    int logId;
    long logDateTime;
    String glueType;
    String glueSource;
    long glueUpdatetime;
    int broadcastIndex;
    int broadcastTotal;

    public ExecuteParam toExecuteParam() {
        ExecuteParam executeParam = new ExecuteParam();
        executeParam.setName( executorHandler );
        executeParam.setLogId( logId );
        executeParam.setLogDateTime( logDateTime );
        //
        if ( !Strings.isEmpty( executorParams ) ) {
            ArrayList<Arg> args = new ArrayList<>();
            //参数形式：name:value|name:value
            String[] params = executorParams.split("\\|");
            for ( String param : params ) {
                if ( !Strings.isEmpty( param ) ) {
                    String[] nameAndValue = param.split( "," );
                    if ( nameAndValue.length == 2 ) {
                        Arg arg = new Arg();
                        arg.setName( nameAndValue[0] );
                        arg.setValue( nameAndValue[1] );
                        args.add( arg );
                    }
                }
            }
            executeParam.setArgs( args );
        }
        //
        return executeParam;
    }
}
