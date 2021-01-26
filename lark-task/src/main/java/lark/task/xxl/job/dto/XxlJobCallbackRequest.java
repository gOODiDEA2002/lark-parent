package lark.task.xxl.job.dto;

import lark.core.util.Strings;
import lark.task.data.Arg;
import lark.task.data.ExecuteParam;
import lark.task.data.NotifyRequest;
import lark.task.data.Result;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 {
 "logId":1,              // 本次调度日志ID
 "logDateTim":0,         // 本次调度日志时间
 "executeResult":{
 "code": 200,        // 200 表示任务执行正常，500表示失败
 "msg": null
 }
 }
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class XxlJobCallbackRequest {
    int logId;
    long logDateTime;
    XxlJobResult executeResult;

    public XxlJobCallbackRequest( NotifyRequest request ) {
        this.logId = request.getLogId();
        this.logDateTime = request.getLogDateTime();
        this.executeResult = new XxlJobResult( request.getResult() );
    }
}
