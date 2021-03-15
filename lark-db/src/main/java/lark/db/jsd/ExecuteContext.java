package lark.db.jsd;

import lark.db.jsd.clause.ExecuteClause;
import lark.db.jsd.converter.ConverterManager;
import lark.db.jsd.result.ExecuteResult;

/**
 * SQL语句执行上下文
 * Created by guohua.cui on 15/5/11.
 */
public class ExecuteContext implements ExecuteClause {
    private ConnectionManager manager;
    private String sql;
    private Object[] args;

    ExecuteContext(ConnectionManager manager, String sql, Object... args) {
        this.manager = manager;
        this.sql = sql;
        this.args = args;
        for (int i = 0; i < this.args.length; i++) {
            this.args[i] = ConverterManager.toDbValue(args[i]);
        }
    }

    @Override
    public ExecuteResult result() {
        Debug.log(sql, args);
        return new ExecuteResult(manager, sql, args);
    }

    @Override
    public int submit() {
        Debug.log(sql, args);
        try (ExecuteResult result = new ExecuteResult(manager, sql, args)) {
            return result.getAffectedRows();
        }
    }
}
