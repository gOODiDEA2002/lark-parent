package lark.task.xxl.job.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class XxlJobScheduleConfig {
    private String address;
    private String accessToken;
}
