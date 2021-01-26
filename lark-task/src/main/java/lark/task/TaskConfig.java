package lark.task;

import lark.core.util.Strings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class TaskConfig {
    private String name;
    private String host;
    private int port;
    private String url;

    public String getUrl() {
        if (Strings.isEmpty( url ) ) {
            return "http://" + this.host + ":" + port;
        }
        return url;
    }
}
