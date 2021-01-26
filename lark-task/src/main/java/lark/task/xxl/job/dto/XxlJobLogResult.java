package lark.task.xxl.job.dto;

import lark.core.util.Times;
import lark.task.data.LogRequest;
import lark.task.data.LogResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 {
 "code":200,         // 200 表示正常、其他失败
 "msg": null         // 错误提示消息
 "content":{
 "fromLineNum":0,        // 本次请求，日志开始行数
 "toLineNum":100,        // 本次请求，日志结束行号
 "logContent":"xxx",     // 本次请求日志内容
 "isEnd":true            // 日志是否全部加载完
 }
 }
 * @author andy
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class XxlJobLogResult {
    private int code;
    private String msg;
    private Content content;

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode
    public class Content {
        private int fromLineNum;
        private int toLineNum;
        private String logContent;
        private boolean isEnd;

        public Content( LogResult result ) {
            this.fromLineNum = 1;
            this.toLineNum = result.getTotalRows();
            this.logContent = result.getContent();
            this.isEnd = true;
        }
    }

    public XxlJobLogResult(LogResult result ) {
        this.code = result.isSuccess() ? 200 : 500;
        this.msg = result.getErrorInfo();
        this.content = new Content( result );
    }
}
