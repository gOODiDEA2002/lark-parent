package lark.autoconfigure.util.oss;

import io.netty.handler.codec.http.HttpScheme;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "lark.util.oss")
public class OssServiceProperties {

    private static final String SCHEME = "https://";

    private String address;
    private String username;
    private String password;
    private String type;

    public OssServiceProperties() {
    }

    public OssServiceProperties(String address, String username, String password ) {
        this.address = address;
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public String getEndpoint() {
        return SCHEME + address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
