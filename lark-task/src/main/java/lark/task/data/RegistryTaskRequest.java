package lark.task.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 注册任务请求
 * @author cuigh
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class RegistryTaskRequest {
    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 访问地址
     */
    private String address;
}
