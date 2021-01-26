package lark.task.data;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 通知任务执行结果参数
 * @author cuigh
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class NotifyRequest {
    /**
     * 任务唯一标识
     */
    private String id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 执行结果
     */
    private Result result;

    /**
     * 执行开始时间
     */
    private Date startTime;

    /**
     * 执行结束时间
     */
    private Date endTime;

    /**
     * 日志ID（XXL-JOB）
     */
    private int logId;

    /**
     * 日志时间（XXL-JOB）
     */
    private long logDateTime;
}
