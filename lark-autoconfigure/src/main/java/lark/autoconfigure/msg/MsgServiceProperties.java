package lark.autoconfigure.msg;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author andy
 */
@ConfigurationProperties(prefix = "lark.msg")
public class MsgServiceProperties {
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}