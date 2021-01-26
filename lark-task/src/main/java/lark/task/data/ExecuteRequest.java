package lark.task.data;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 执行任务参数
 * @author cuigh
 */
@Data
public class ExecuteRequest {
    /**
     * 任务名称
     */
    private String name;

    /**
     * 参数列表
     */
    private List<Arg> args;

    /**
     * 执行时间, 如不指定则立即执行
     */
    private LocalDateTime time;
}

