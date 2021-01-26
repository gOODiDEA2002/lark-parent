package lark.task.xxl.job.dto;

import lark.core.util.Strings;
import lark.task.data.Result;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class XxlJobResult {
    private int code;
    private String msg;

    public XxlJobResult( Result result ) {
        this.code = result.isSuccess() ? 200 : 500;
        this.msg = result.getErrorInfo();
    }

    public Result toResult() {
        Result result = new Result();
        result.setErrorInfo( "null" );
        result.setSuccess(code == 200);
        return result;
    }
}
