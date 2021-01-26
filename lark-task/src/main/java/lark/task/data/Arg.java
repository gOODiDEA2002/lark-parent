package lark.task.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 任务参数
 * @author cuigh
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Arg {
    /**
     * 参数名称
     */
    private String name;

    /**
     * 参数值
     */
    private String value;
}
