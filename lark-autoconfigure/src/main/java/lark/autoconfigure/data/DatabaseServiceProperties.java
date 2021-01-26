package lark.autoconfigure.data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author andy
 */
@ConfigurationProperties(prefix = "lark.db")
public class DatabaseServiceProperties {

    private List<String> source;
    private List<String> shard;

    public List<String> getSource() {
        return source;
    }

    public void setSource(List<String> source) {
        this.source = source;
    }

    public List<String> getShard() {
        return shard;
    }

    public void setShard(List<String> shard) {
        this.shard = shard;
    }
}
