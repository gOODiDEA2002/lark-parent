package lark.task.xxl.job.dto;

import lark.task.data.RegistryTaskRequest;
import lark.task.data.RemoveTaskRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author andy
 *  {
 *         "registryGroup":"EXECUTOR",                     // 固定值
 *         "registryKey":"xxl-job-executor-example",       // 执行器AppName
 *         "registryValue":"http://127.0.0.1:9999/"        // 执行器地址，内置服务跟地址
 *     }
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class XxlJobRemoveTaskRequest {
    private String registryGroup = "EXECUTOR";
    private String registryKey;
    private String registryValue;

    public XxlJobRemoveTaskRequest(RemoveTaskRequest request ) {
        this.registryKey = request.getName();
        this.registryValue = request.getAddress();
    }
}
