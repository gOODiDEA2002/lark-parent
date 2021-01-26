package lark.autoconfigure.util.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties(prefix = "lark.util.cache")
public class CacheServiceProperties {
    private String address;
    private String password;
    private String host;
    private int port;

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