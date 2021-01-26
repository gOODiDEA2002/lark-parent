package lark.autoconfigure.task;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author andy
 */
@ConfigurationProperties(prefix = "lark.task")
public class TaskServiceProperties {

    private String address;
    private String token;
    private String executorUrl;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExecutorUrl() {
        return executorUrl;
    }

    public void setExecutorUrl(String executorUrl) {
        this.executorUrl = executorUrl;
    }
}
