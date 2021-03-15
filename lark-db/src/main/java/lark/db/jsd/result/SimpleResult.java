package lark.db.jsd.result;

import lark.db.jsd.ConnectionManager;
import lark.db.jsd.JsdException;
import lark.db.jsd.Releaser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * Created by guohua.cui on 15/5/26.
 */
public class SimpleResult {
    private int affectedRows;

    public SimpleResult(int affectedRows) {
        this.affectedRows = affectedRows;
    }

    /***
     * 获取受影响的行数
     * @return
     */
    public int getAffectedRows() {
        return this.affectedRows;
    }

    public static SimpleResult of(ConnectionManager manager, BuildResult result) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = manager.getConnection();
            String sql = result.getSql();
            statement = conn.prepareStatement(sql);
            List<Object> args = result.getArgs();
            if (args != null) {
                for (int i=0; i<args.size(); i++) {
                    statement.setObject(i+1, args.get(i));
                }
            }
            int rows = statement.executeUpdate();
            return new SimpleResult(rows);
        } catch (Exception e) {
            throw new JsdException(e);
        } finally {
            Releaser.release(statement);
            Releaser.release(manager, conn);
        }
    }

}
