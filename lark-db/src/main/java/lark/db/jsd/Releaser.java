package lark.db.jsd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by guohua.cui on 16/6/13.
 */
public final class Releaser {
    private static final Logger LOGGER = LoggerFactory.getLogger(Releaser.class);

    public static void release(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                LOGGER.error("close rs failed", e);
            }
        }
    }

    public static void release(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception e) {
                LOGGER.error("close statement failed", e);
            }
        }
    }

    public static void release(ConnectionManager manager, Connection conn) {
        try {
            manager.closeConnection(conn);
        } catch (Exception e) {
            LOGGER.error("close conn failed", e);
        }
    }
}
