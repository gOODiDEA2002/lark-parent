package lark.db.jsd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guohua.cui on 15/5/28.
 */
public final class Columns {
    boolean distinct;
    List<Column> list = new ArrayList<>();

    public Columns() {
        // default ctor
    }


    public Columns(String... cols) {
        this.add(cols);
    }

    public Columns(Table t, String... cols) {
        this.add(t, cols);
    }

    public Columns distinct(boolean enabled) {
        this.distinct = enabled;
        return this;
    }

    /**
     * add table columns
     *
     * @param t
     * @param cols
     * @return
     */
    public Columns add(Table t, String... cols) {
        for (String col : cols) {
            list.add(new SimpleColumn(t, col, null));
        }
        return this;
    }

    /**
     * add table columns
     *
     * @param t
     * @param col
     * @return
     */
    public Columns add(Table t, String col) {
        list.add(new SimpleColumn(t, col, null));
        return this;
    }

    /**
     * add table column with alias
     *
     * @param t
     * @param col
     * @param alias
     * @return
     */
    public Columns addWithAlias(Table t, String col, String alias) {
        list.add(new SimpleColumn(t, col, alias));
        return this;
    }

    /**
     * add expression column, like 'COUNT(*)'
     *
     * @param expr
     * @param alias
     * @return
     */
    public Columns add(String expr, String alias) {
        list.add(new ExprColumn(expr, alias));
        return this;
    }

    public Columns add(String... cols) {
        for (String col : cols) {
            list.add(new SimpleColumn(col));
        }
        return this;
    }

    public Columns addSql(String... cols) {
        for (String col : cols) {
            list.add(new SqlColumn(col));
        }
        return this;
    }

    /**
     * 列
     */
    interface Column {
        String getAlias();
    }

    /**
     * 自定义sql 列
     */
    static class SqlColumn implements Column {
        Table table;
        String column;
        String alias;


        SqlColumn(String column) {
            this.column = column;
        }


        @Override
        public String getAlias() {
            return alias;
        }

    }


    /**
     * 常规数据列
     */
    static class SimpleColumn implements Column {
        Table table;
        String column;
        String alias;

        SimpleColumn(Table table, String column, String alias) {
            this.table = table;
            this.column = column;
            this.alias = alias;
        }

        SimpleColumn(String column) {
            this.column = column;
        }


        @Override
        public String getAlias() {
            return alias;
        }
    }

    /**
     * 聚合列
     */
    static class PolyColumn implements Column {
        Table table;
        String column;
        String function;
        String alias;

        @Override
        public String getAlias() {
            return alias;
        }
    }

    /**
     * 表达式列
     */
    static class ExprColumn implements Column {
        String expr;
        String alias;

        ExprColumn(String expr, String alias) {
            this.expr = expr;
            this.alias = alias;
        }

        @Override
        public String getAlias() {
            return alias;
        }
    }
}
