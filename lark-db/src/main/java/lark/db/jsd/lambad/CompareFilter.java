package lark.db.jsd.lambad;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lark.db.jsd.*;

import java.util.Collection;

import static lark.db.jsd.Shortcut.s;

public class CompareFilter<T> implements SelectFilter<T>, UpdateFilter<T>, DeleteFilter<T> {

    private BasicFilter basicFilter;
    private UpdateValues uv;
    private Sorters sorters;
    private Groupers groupers;

    private Class<?> entity;

    public CompareFilter() {
        basicFilter = Filter.create();
        uv = new UpdateValues();
        sorters = new Sorters();
        groupers = new Groupers();
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
    public CompareFilter<T> eq(FieldFunction<T, ?> column, Object value) {
        return eq(false, column, value);
    }

    public CompareFilter<T> eq(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.EQ, value, ignoreNullValue);
    }


    public CompareFilter<T> ne(FieldFunction<T, ?> column, Object value) {
        return ne(false, column, value);
    }

    public CompareFilter<T> ne(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.NE, value, ignoreNullValue);
    }


    public CompareFilter<T> lt(FieldFunction<T, ?> column, Object value) {
        return lt(false, column, value);
    }

    public CompareFilter<T> lt(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.LT, value, ignoreNullValue);
    }


    public CompareFilter<T> lte(FieldFunction<T, ?> column, Object value) {
        return lte(false, column, value);
    }

    public CompareFilter<T> lte(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.LTE, value, ignoreNullValue);
    }


    public CompareFilter<T> gt(FieldFunction<T, ?> column, Object value) {
        return gt(false, column, value);
    }

    public CompareFilter<T> gt(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.GT, value, ignoreNullValue);
    }


    public CompareFilter<T> gte(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.GTE, value, false);
    }

    public CompareFilter<T> gte(FieldFunction<T, ?> column, Object value) {
        return gte(false, column, value);
    }


    public CompareFilter<T> in(FieldFunction<T, ?> column, Collection value) {
        return in(false, column, value);
    }

    public CompareFilter<T> in(Boolean ignoreNullValue, FieldFunction<T, ?> column, Collection value) {
        return addFilter(column, FilterType.IN, value.toArray(), ignoreNullValue);
    }

    public CompareFilter<T> notIn(FieldFunction<T, ?> column, Collection value) {
        return notIn(false, column, value);
    }

    public CompareFilter<T> notIn(Boolean ignoreNullValue, FieldFunction<T, ?> column, Collection value) {
        return addFilter(column, FilterType.NIN, value.toArray(), ignoreNullValue);
    }


    public CompareFilter<T> like(FieldFunction<T, ?> column, Object value) {
        return like(false, column, value);
    }

    public CompareFilter<T> like(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.LK, value, ignoreNullValue);
    }

    public CompareFilter<T> likeLeft(FieldFunction<T, ?> column, Object value) {
        return likeLeft(false, column, value);
    }

    public CompareFilter<T> likeLeft(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.LKLEFT, value, ignoreNullValue);
    }

    public CompareFilter<T> likeRight(FieldFunction<T, ?> column, Object value) {
        return likeRight(false, column, value);
    }

    public CompareFilter<T> likeRight(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.LKRIGHT, value, ignoreNullValue);
    }


    public CompareFilter<T> notLike(FieldFunction<T, ?> column, Object value) {
        return notLike(false, column, value);
    }

    public CompareFilter<T> notLike(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.NOTLK, value, ignoreNullValue);
    }


    public CompareFilter<T> between(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object begin, Object end) {
        return addFilter(column, FilterType.BETWEEN, begin, end, ignoreNullValue);
    }

    public CompareFilter<T> between(FieldFunction<T, ?> column, Object begin, Object end) {
        return between(false, column, begin, end);
    }

    public CompareFilter<T> notBetween(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object begin, Object end) {
        return addFilter(column, FilterType.BETWEEN, begin, end, ignoreNullValue);
    }


    public CompareFilter<T> notBetween(FieldFunction<T, ?> column, Object begin, Object end) {
        return notBetween(false, column, begin, end);
    }


    public CompareFilter<T> orderByDesc(FieldFunction<T, ?> column) {
        String columnName = FieldUtil.getColumnName(column);
        if (StrUtil.isNotEmpty(columnName)) {
            sorters.add(SortType.DESC, FieldUtil.getColumnName(column));
        }
        return this;
    }

    public CompareFilter<T> orderByAsc(FieldFunction<T, ?> column) {
        String columnName = FieldUtil.getColumnName(column);
        if (StrUtil.isNotEmpty(columnName)) {
            sorters.add(SortType.ASC, FieldUtil.getColumnName(column));
        }

        return this;
    }

    public CompareFilter<T> groupBy(FieldFunction<T, ?> column) {
        String columnName = FieldUtil.getColumnName(column);
        if (StrUtil.isNotEmpty(columnName)) {
            groupers.add(columnName);
        }
        return this;
    }


    // ------------------update---------------------
    public CompareFilter<T> set(FieldFunction<T, ?> column, Object value) {
        return set(false, column, UpdateType.EQ, value);
    }

    public CompareFilter<T> set(Boolean ignoreNullValue, FieldFunction<T, ?> column, UpdateType updateType, Object value) {
        return setFilter(column, updateType, value, ignoreNullValue);
    }

    private CompareFilter<T> setFilter(FieldFunction<T, ?> column, UpdateType updateType, Object value, Boolean ignoreNullValue) {
        if (ignoreNullValue || ObjectUtil.isNotEmpty(value)) {
            uv.add(FieldUtil.getColumnName(column), updateType, value);
        }
        return this;
    }


    private CompareFilter<T> addFilter(FieldFunction<T, ?> column, FilterType filterType, Object value, Boolean ignoreNullValue) {
        if (ignoreNullValue || ObjectUtil.isNotEmpty(value)) {
            basicFilter.add(FieldUtil.getColumnName(column), filterType, value);
        }
        return this;
    }

    private CompareFilter<T> addFilter(FieldFunction<T, ?> column, FilterType filterType, Object begin, Object end, Boolean ignoreNullValue) {
        if (ignoreNullValue || ObjectUtil.isNotEmpty(begin)) {
            basicFilter.add(FieldUtil.getColumnName(column), filterType, begin, end);
        }
        return this;
    }

    public BasicFilter select() {
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