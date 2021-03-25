package lark.db.jsd.lambad;

import cn.hutool.core.util.StrUtil;
import lark.db.jsd.*;

import java.util.Collection;


public class CompareFilter<T, M> implements SelectFilter<T, M>, UpdateFilter<T, M>, DeleteFilter<T, M> {

    private BasicFilter basicFilter;
    private UpdateValues uv;
    private Sorters sorters;
    private Groupers groupers;
    private Columns columns;

    private Class<?> entity;

    public CompareFilter() {
        basicFilter = Filter.create();
        uv = new UpdateValues();
        sorters = new Sorters();
        groupers = new Groupers();
        columns = new Columns();
    }

    public void table(Class<T> entity) {
        this.entity = entity;
    }

    public Class<?> getEntity() {
        return entity;
    }

    /**
     * eq
     *
     * @description: TODO
     * @return:
     * @author: yandong
     * @date: 2021/3/18 5:36 下午
     */
    public CompareFilter<T, M> eq(FieldFunction<T, ?> column, Object value) {
        return eq(true, column, value);
    }

    public CompareFilter<T, M> eq(Boolean careNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.EQ, value, careNullValue);
    }


    public CompareFilter<T, M> ne(FieldFunction<T, ?> column, Object value) {
        return ne(true, column, value);
    }

    public CompareFilter<T, M> ne(Boolean careNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.NE, value, careNullValue);
    }


    public CompareFilter<T, M> lt(FieldFunction<T, ?> column, Object value) {
        return lt(true, column, value);
    }

    public CompareFilter<T, M> lt(Boolean careNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.LT, value, careNullValue);
    }


    public CompareFilter<T, M> lte(FieldFunction<T, ?> column, Object value) {
        return lte(true, column, value);
    }

    public CompareFilter<T, M> lte(Boolean careNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.LTE, value, careNullValue);
    }


    public CompareFilter<T, M> gt(FieldFunction<T, ?> column, Object value) {
        return gt(true, column, value);
    }

    public CompareFilter<T, M> gt(Boolean careNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.GT, value, careNullValue);
    }


    public CompareFilter<T, M> gte(Boolean careNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.GTE, value, true);
    }

    public CompareFilter<T, M> gte(FieldFunction<T, ?> column, Object value) {
        return gte(true, column, value);
    }


    public CompareFilter<T, M> in(FieldFunction<T, ?> column, Object... value) {
        return in(true, column, value);
    }

    public CompareFilter<T, M> in(Boolean careNullValue, FieldFunction<T, ?> column, Object... value) {
        return addFilter(column, FilterType.IN, value, careNullValue);
    }

    public CompareFilter<T, M> notIn(FieldFunction<T, ?> column, Object... value) {
        return notIn(true, column, value);
    }

    public CompareFilter<T, M> notIn(Boolean careNullValue, FieldFunction<T, ?> column, Object... value) {
        return addFilter(column, FilterType.NIN, value, careNullValue);
    }


    public CompareFilter<T, M> like(FieldFunction<T, ?> column, Object value) {
        return like(true, column, value);
    }

    public CompareFilter<T, M> like(Boolean careNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.LK, value, careNullValue);
    }

    public CompareFilter<T, M> likeLeft(FieldFunction<T, ?> column, Object value) {
        return likeLeft(true, column, value);
    }

    public CompareFilter<T, M> likeLeft(Boolean careNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.LKLEFT, value, careNullValue);
    }

    public CompareFilter<T, M> likeRight(FieldFunction<T, ?> column, Object value) {
        return likeRight(true, column, value);
    }

    public CompareFilter<T, M> likeRight(Boolean careNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.LKRIGHT, value, careNullValue);
    }


    public CompareFilter<T, M> notLike(FieldFunction<T, ?> column, Object value) {
        return notLike(true, column, value);
    }

    public CompareFilter<T, M> notLike(Boolean careNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.NOTLK, value, careNullValue);
    }


    public CompareFilter<T, M> between(Boolean careNullValue, FieldFunction<T, ?> column, Object begin, Object end) {
        return addFilter(column, FilterType.BETWEEN, begin, end, careNullValue);
    }

    public CompareFilter<T, M> between(FieldFunction<T, ?> column, Object begin, Object end) {
        return between(true, column, begin, end);
    }

    public CompareFilter<T, M> notBetween(Boolean careNullValue, FieldFunction<T, ?> column, Object begin, Object end) {
        return addFilter(column, FilterType.BETWEEN, begin, end, careNullValue);
    }


    public CompareFilter<T, M> notBetween(FieldFunction<T, ?> column, Object begin, Object end) {
        return notBetween(true, column, begin, end);
    }


    public CompareFilter<T, M> orderByDesc(FieldFunction<T, ?> column) {
        String columnName = FieldUtil.getColumnName(column);
        if (StrUtil.isNotEmpty(columnName)) {
            sorters.add(SortType.DESC, FieldUtil.getColumnName(column));
        }
        return this;
    }

    public CompareFilter<T, M> orderByAsc(FieldFunction<T, ?> column) {
        String columnName = FieldUtil.getColumnName(column);
        if (StrUtil.isNotEmpty(columnName)) {
            sorters.add(SortType.ASC, FieldUtil.getColumnName(column));
        }

        return this;
    }

    public CompareFilter<T, M> groupBy(FieldFunction<T, ?> column) {
        String columnName = FieldUtil.getColumnName(column);
        if (StrUtil.isNotEmpty(columnName)) {
            groupers.add(columnName);
        }
        return this;
    }

    @Override
    public CompareFilter<T, M> or(Boolean careNullValue, FieldFunction<T, ?> column, Object value) {
        addFilter(column, FilterType.OR, value, careNullValue);
        return this;
    }


    @Override
    public CompareFilter<T, M> or() {
        addFilter(null, FilterType.OR, null, true);
        return this;
    }

    public CompareFilter<T, M> or(FieldFunction<T, ?> column, Object value) {
        return or(true, column, value);
    }


    public CompareFilter<T, M> or(SelectFilter<T, M> selectFilter) {
        BasicFilter build = selectFilter.build();
        basicFilter.addSelectFilter(build);
        return this;
    }

    @Override
    public CompareFilter<T, M> apply(String sql) {
        basicFilter.addSql(sql);
        return this;
    }

    @Override
    public CompareFilter<T, M> apply(String sql, Object... objects) {
        sql = StrUtil.format(sql, objects);
        apply(sql);
        return this;
    }

    @Override
    public CompareFilter<T, M> select(FieldFunction<T, ?>... column) {
        return addColumn(column);
    }

    @Override
    public CompareFilter<T, M> select(String... column) {
        return addColumn(column);
    }

    @Override
    public CompareFilter<T, M> last(String sql) {
        basicFilter.addLastSql(sql);
        return this;
    }

    ;

    @Override
    public CompareFilter<T, M> last(String sql, Object... objects) {
        sql = StrUtil.format(sql, objects);
        basicFilter.addLastSql(sql);
        return this;
    }

    ;


    // ------------------update---------------------
    public CompareFilter<T, M> set(FieldFunction<T, ?> column, Object value) {
        return set(true, column, UpdateType.EQ, value);
    }

    public CompareFilter<T, M> set(Boolean careNullValue, FieldFunction<T, ?> column, UpdateType updateType, Object value) {
        return setFilter(column, updateType, value, careNullValue);
    }

    private CompareFilter<T, M> setFilter(FieldFunction<T, ?> column, UpdateType updateType, Object value, Boolean careNullValue) {
        if (careNullValue) {
            uv.add(FieldUtil.getColumnName(column), updateType, value);
        }
        return this;
    }

    private CompareFilter<T, M> addColumn(FieldFunction<T, ?>... column) {
        for (FieldFunction<T, ?> tFieldFunction : column) {
            String columnName = FieldUtil.getColumnName(tFieldFunction);
            columns.add(columnName);
        }
        return this;
    }

    private CompareFilter<T, M> addColumn(String... column) {
        for (String s : column) {
            columns.addSql(s);
        }
        return this;
    }


    public Columns col() {
        return columns;
    }


    private CompareFilter<T, M> addFilter(FieldFunction<T, ?> column, FilterType filterType, Object value, Boolean careNullValue) {
        if (careNullValue) {
            basicFilter.add(FieldUtil.getColumnName(column), filterType, value);
        }
        return this;
    }

    private CompareFilter<T, M> addFilter(FieldFunction<T, ?> column, FilterType filterType, Object begin, Object end, Boolean careNullValue) {
        if (careNullValue) {
            basicFilter.add(FieldUtil.getColumnName(column), filterType, begin, end);
        }
        return this;
    }

    public BasicFilter build() {
        return basicFilter;
    }

    public UpdateValues set() {
        return uv;
    }

    public Sorters order() {
        return sorters;
    }

    public Groupers group() {
        return groupers;
    }
}