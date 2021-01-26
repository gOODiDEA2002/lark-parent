package lark.net;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class RegistryConfig {
    private String name;
    private String ip;
    private int port;
}
