package lark.db.jsd;

import lark.db.jsd.lambad.SelectFilter;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guohua.cui on 15/5/26.
 */
public final class BasicFilter extends Filter {
    //private static final ExprFilterItem EMPTY_FILTER_ITEM = new ExprFilterItem("1=1");
    private static final BasicFilter EMPTY_FILTER = new BasicFilter().add("1=1");
    private List<FilterItem> items;

    BasicFilter() {
    }

    public List<FilterItem> getItems() {
        return items;
    }

    public static BasicFilter emptyFilter() {
        return EMPTY_FILTER;
    }

    boolean hasItems() {
        return items != null && !items.isEmpty();
    }

    /**
     * 添加自定义表达式查询条件(慎用, 使用此方法将不能保证跨数据库兼容)
     *
     * @param expr 表达式, 如: SUM(COUNT)>100
     * @return
     */
    public BasicFilter add(String expr) {
        this.add(new ExprFilterItem(expr));
        return this;
    }

    /**
     * 添加自定义sql 直接拼接
     *
     * @description: TODO
     * @return:
     * @author: yandong
     * @date: 2021/3/24 2:46 下午
     */
    public BasicFilter addSql(String sql) {
        this.add(new SqlFilterItem((sql)));
        return this;
    }

    public <T, M> BasicFilter addSelectFilter(BasicFilter basicFilter) {
        this.add(new SelectFilterItem(basicFilter));
        return this;
    }


    /**
     * 添加简单查询条件
     *
     * @param column 列
     * @param value  值
     * @return
     */
    public BasicFilter add(String column, Object value) {
        this.add(new OneColumnFilterItem(column, value));
        return this;
    }

    /**
     * 添加指定类型的查询条件
     *
     * @param column     列
     * @param filterType 类型
     * @param value      值
     * @return
     */
    public BasicFilter add(String column, FilterType filterType, Object value) {
        this.add(new OneColumnFilterItem(null, column, filterType, value));
        return this;
    }

    public BasicFilter add(String column, FilterType filterType, Object value, Object value2) {
        this.add(new OneColumnFilterItem(null, column, filterType, value));
        return this;
    }

    public BasicFilter add(Table table, String column, FilterType filterType, Object value) {
        this.add(new OneColumnFilterItem(table, column, filterType, value));
        return this;
    }

    public BasicFilter add(Table table1, String column1, Table table2, String column2) {
        this.add(new TwoColumnFilterItem(table1, column1, FilterType.EQ, table2, column2));
        return this;
    }

    public BasicFilter add(Table table1, String column1, FilterType filterType, Table table2, String column2) {
        this.add(new TwoColumnFilterItem(table1, column1, filterType, table2, column2));
        return this;
    }

    private void add(FilterItem item) {
        if (this.items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
    }
//        IfAdd(when bool, col string, value interface{}) BasicFilters
//        IfAddT(when bool, col string, ft filterType, value interface{}) BasicFilters
//        IfAddF(when bool, t Table, col string, ft filterType, value interface{}) BasicFilters
//        IfAddJ(when bool, t1 Table, col1 string, ft filterType, t2 Table, col2 string) BasicFilters
//        IfAddE(when bool, expr string) BasicFilters

    /**
     * 条件项类型
     */
    enum FilterItemType {
        EXPR, ONE_COLUMN, TWO_COLUMN, SQL, SELECTFILTER;
    }

    interface FilterItem {
        FilterItemType getItemType();


    }

    @Getter
    static class OneColumnFilterItem implements FilterItem {
        private Table table;
        private FilterType type;
        private String column;
        private Object value;
        private Object value2;

        OneColumnFilterItem(String col, Object val) {
            this.column = col;
            this.type = FilterType.EQ;
            this.value = val;
        }

        OneColumnFilterItem(Table t, String col, FilterType type, Object val) {
            this.table = t;
            this.column = col;
            this.type = type;
            this.value = val;
        }

        OneColumnFilterItem(Table t, String col, FilterType type, Object val, Object val2) {
            this.table = t;
            this.column = col;
            this.type = type;
            this.value = val;
            this.value2 = val2;
        }

        @Override
        public FilterItemType getItemType() {
            return FilterItemType.ONE_COLUMN;
        }


    }

    @Getter
    static class TwoColumnFilterItem implements FilterItem {
        private Table table1;
        private String column1;
        private FilterType type;
        private Table table2;
        private String column2;

        TwoColumnFilterItem(Table t1, String col1, FilterType ft, Table t2, String col2) {
            this.table1 = t1;
            this.column1 = col1;
            this.type = ft;
            this.table2 = t2;
            this.column2 = col2;
        }

        @Override
        public FilterItemType getItemType() {
            return FilterItemType.TWO_COLUMN;
        }


    }

    @Getter
    static class ExprFilterItem implements FilterItem {
        private String expr;

        ExprFilterItem(String expr) {
            this.expr = expr;
        }

        @Override
        public FilterItemType getItemType() {
            return FilterItemType.EXPR;
        }
    }

    @Getter
    static class SqlFilterItem implements FilterItem {
        private String sql;

        SqlFilterItem(String sql) {
            this.sql = sql;
        }

        @Override
        public FilterItemType getItemType() {
            return FilterItemType.SQL;
        }
    }

    @Getter
    static class SelectFilterItem implements FilterItem {
        private BasicFilter selectFilter;

        SelectFilterItem(BasicFilter selectFilter) {
            this.selectFilter = selectFilter;
        }

        @Override
        public FilterItemType getItemType() {
            return FilterItemType.SELECTFILTER;
        }
    }


}
