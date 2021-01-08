package lark.autoconfigure.util.index;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for index service.
 *
 * @author andy
 */
@ConfigurationProperties(prefix = "lark.util.index")
public class IndexServiceProperties {

    private String address;
    private String host;
    private int port;

    public IndexServiceProperties() {

    }

    public IndexServiceProperties( String address ) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        parseAddress( address );
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getTimeout() {
        return 0;
    }

    private void parseAddress(String address) {
        String[] parts = address.split(":");
        this.host = parts[0];
        this.port = Integer.parseInt(parts[1]);
    }
}
