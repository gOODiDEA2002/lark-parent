package lark.db;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DatabaseConfig {
    private static final String JDBC_TEMPLATE = "jdbc:mysql://%s/%s";
    private String name;
    private String address;
    private String username;
    private String password;

    public String getUrl() {
        return String.format( JDBC_TEMPLATE, address, name );
    }
}
