package lark.task.data;


import lark.task.data.Result;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 日志
 * @author cuigh
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class LogRequest {
    /**
     * 本次调度日志ID
     */
    private int id;

    /**
     * 本次调度日志时间
     */
    private LocalDateTime logDateTime;

    /**
     * 日志开始行号，滚动加载日志
     */
    private int lineNum;
}
