package lark.db;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author andy
 */
@Data
@AllArgsConstructor
public class DatabaseConfig {
    private static final String JDBC_TEMPLATE = "jdbc:mysql://%s/%s";
    private String name;
    private String address;
    private String username;
    private String password;
    private int minPoolSize;
    private int maxPoolSize;

    public String getUrl() {
        return String.format( JDBC_TEMPLATE, address, name );
    }
}
