package lark.autoconfigure.registry;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author andy
 */
@ConfigurationProperties(prefix = "lark.registry")
public class RegistryServiceProperties {
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}