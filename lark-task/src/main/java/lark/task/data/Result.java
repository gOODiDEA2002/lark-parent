package lark.task.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 调用结果
 * @author cuigh
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Result {
    /**
     * 是否成功
     */
    protected boolean success;

    /**
     * 错误信息
     */
    protected String errorInfo;
}

