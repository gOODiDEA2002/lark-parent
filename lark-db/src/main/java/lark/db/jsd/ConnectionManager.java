package lark.db.jsd;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Created by guohua.cui on 15/6/3.
 */
public interface ConnectionManager {
    Connection getConnection();
    void closeConnection(Connection conn);
    DataSource getSource();
}
