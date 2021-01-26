package lark.task.xxl.job.dto;

import lark.core.util.Times;
import lark.task.data.LogRequest;
import lark.task.data.NotifyRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 {
 "logDateTim":0,     // 本次调度日志时间
 "logId":0,          // 本次调度日志ID
 "fromLineNum":0     // 日志开始行号，滚动加载日志
 }
 * @author andy
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class XxlJobLogRequest {
    int logId;
    long logDateTim;
    int fromLineNum;

    public LogRequest toLogRequest() {
        LogRequest request = new LogRequest();
        request.setId( this.logId );
        request.setLogDateTime(Times.toLocalDateTime( this.logDateTim ) );
        request.setLineNum( this.fromLineNum );
        return request;
    }
}
