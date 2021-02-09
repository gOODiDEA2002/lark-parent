package lark.autoconfigure.util.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties(prefix = "lark.util.cache")
public class CacheServiceProperties {
    private static final int DEFAULT_POOL_MIN_SIZE = 1;
    private static final int DEFAULT_POOL_MAX_SIZE = 2;
    private String address;
    private String password;
    private String host;
    private int port;
    private int minPoolSize;
    private int maxPoolSize;

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getMinPoolSize() {
        if ( minPoolSize <= 0 ) {
            minPoolSize = DEFAULT_POOL_MIN_SIZE;
        }
        return minPoolSize;
    }

    public void setMinPoolSize(int minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public int getMaxPoolSize() {
        if ( maxPoolSize <= 0 ) {
            maxPoolSize = DEFAULT_POOL_MAX_SIZE;
        }
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void setAddress(String address) {
        this.address = address;
        parseAddress( address );
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private void parseAddress(String address) {
        String[] parts = address.split(":");
        this.host = parts[0];
        this.port = Integer.parseInt(parts[1]);
    }
}