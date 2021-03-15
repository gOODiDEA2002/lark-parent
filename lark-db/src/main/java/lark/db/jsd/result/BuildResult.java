package lark.db.jsd.result;

import lombok.Getter;

import java.util.List;

/**
 * Created by guohua.cui on 15/5/19.
 */
public class BuildResult {
    @Getter
    private String sql;
    @Getter
    private List<Object> args;

    public BuildResult(String sql, List<Object> args) {
        this.sql = sql;
        this.args = args;
    }

    @Override
    public String toString() {
        return String.format("sql: %s, args: %s", sql, args == null ? "null" : args.toString());
    }
}
