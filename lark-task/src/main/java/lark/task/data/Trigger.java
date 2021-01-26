package lark.task.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 触发器
 * @author cuigh
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Trigger {
    /**
     * cron 表达式
     */
    private String cron;

    /**
     * 调度起始时间
     */
    private LocalDateTime start;

    /**
     * 调度结束时间
     */
    private LocalDateTime end;
}