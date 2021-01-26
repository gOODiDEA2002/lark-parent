package lark.task.xxl.job.dto;

import lark.task.data.RegistryTaskRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class XxlJobRegistryTaskRequest {
    private String registryGroup = "EXECUTOR";
    private String registryKey;
    private String registryValue;

    public XxlJobRegistryTaskRequest(RegistryTaskRequest request ) {
        this.registryKey = request.getName();
        this.registryValue = request.getAddress();
    }
}
