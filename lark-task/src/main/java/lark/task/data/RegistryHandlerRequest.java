package lark.task.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 注册执行器请求
 * @author cuigh
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class RegistryHandlerRequest {
    /**
     * 执行器服务名称
     */
    private String name;

    /**
     * 触发器列表
     */
    private List<Trigger> triggers;

    /**
     * 参数列表
     */
    private List<Arg> args;
}
