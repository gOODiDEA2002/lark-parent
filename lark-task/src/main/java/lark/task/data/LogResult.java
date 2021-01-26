package lark.task.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 日志
 * @author cuigh
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class LogResult extends Result {
    private String content;
    private int totalRows;
}

