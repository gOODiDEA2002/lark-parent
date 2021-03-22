package lark.db.jsd.util;

import cn.hutool.core.util.ObjectUtil;
import lark.db.jsd.*;

import java.util.Collection;

public class QueryFilter<T> {

    private BasicFilter basicFilter;
    private UpdateValues uv;

    private Class<?> entity;

    public QueryFilter() {
        basicFilter = Filter.create();
        uv = new UpdateValues();
    }

    public QueryFilter(Class c) {
        basicFilter = Filter.create();
        uv = new UpdateValues();
        this.entity = c;
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
    public QueryFilter<T> eq(FieldFunction<T, ?> column, Object value) {
        return eq(false, column, value);
    }

    public QueryFilter<T> eq(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.EQ, value, ignoreNullValue);
    }


    public QueryFilter<T> ne(FieldFunction<T, ?> column, Object value) {
        return ne(false, column, value);
    }

    public QueryFilter<T> ne(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.NE, value, ignoreNullValue);
    }


    public QueryFilter<T> lt(FieldFunction<T, ?> column, Object value) {
        return lt(false, column, value);
    }

    public QueryFilter<T> lt(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.LT, value, ignoreNullValue);
    }


    public QueryFilter<T> lte(FieldFunction<T, ?> column, Object value) {
        return lte(false, column, value);
    }

    public QueryFilter<T> lte(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.LTE, value, ignoreNullValue);
    }


    public QueryFilter<T> gt(FieldFunction<T, ?> column, Object value) {
        return gt(false, column, value);
    }

    public QueryFilter<T> gt(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.GT, value, ignoreNullValue);
    }


    public QueryFilter<T> gte(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.GTE, value, false);
    }

    public QueryFilter<T> gte(FieldFunction<T, ?> column, Object value) {
        return gte(false, column, value);
    }


    public QueryFilter<T> in(FieldFunction<T, ?> column, Collection value) {
        return in(false, column, value);
    }

    public QueryFilter<T> in(Boolean ignoreNullValue, FieldFunction<T, ?> column, Collection value) {
        return addFilter(column, FilterType.IN, value.toArray(), ignoreNullValue);
    }

    public QueryFilter<T> notIn(FieldFunction<T, ?> column, Collection value) {
        return notIn(false, column, value);
    }

    public QueryFilter<T> notIn(Boolean ignoreNullValue, FieldFunction<T, ?> column, Collection value) {
        return addFilter(column, FilterType.NIN, value.toArray(), ignoreNullValue);
    }


    public QueryFilter<T> like(FieldFunction<T, ?> column, Object value) {
        return like(false, column, value);
    }

    public QueryFilter<T> like(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.LK, value, ignoreNullValue);
    }

    public QueryFilter<T> likeLeft(FieldFunction<T, ?> column, Object value) {
        return likeLeft(false, column, value);
    }

    public QueryFilter<T> likeLeft(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.LKLEFT, value, ignoreNullValue);
    }

    public QueryFilter<T> likeRight(FieldFunction<T, ?> column, Object value) {
        return likeRight(false, column, value);
    }

    public QueryFilter<T> likeRight(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.LKRIGHT, value, ignoreNullValue);
    }


    public QueryFilter<T> notLike(FieldFunction<T, ?> column, Object value) {
        return notLike(false, column, value);
    }

    public QueryFilter<T> notLike(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value) {
        return addFilter(column, FilterType.NOTLK, value, ignoreNullValue);
    }


    public QueryFilter<T> between(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object begin, Object end) {
        return addFilter(column, FilterType.BETWEEN, begin, end, ignoreNullValue);
    }

    public QueryFilter<T> between(FieldFunction<T, ?> column, Object begin, Object end) {
        return between(false, column, begin, end);
    }

    public QueryFilter<T> notBetween(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object begin, Object end) {
        return addFilter(column, FilterType.BETWEEN, begin, end, ignoreNullValue);
    }


    public QueryFilter<T> notBetween(FieldFunction<T, ?> column, Object begin, Object end) {
        return notBetween(false, column, begin, end);
    }


    // ------------------update---------------------
    public QueryFilter<T> set(FieldFunction<T, ?> column, Object value) {
        return set(false, column, UpdateType.EQ, value);
    }

    public QueryFilter<T> set(Boolean ignoreNullValue, FieldFunction<T, ?> column, UpdateType updateType, Object value) {
        return setFilter(column, updateType, value, ignoreNullValue);
    }

    private QueryFilter<T> setFilter(FieldFunction<T, ?> column, UpdateType updateType, Object value, Boolean ignoreNullValue) {
        if (ignoreNullValue || ObjectUtil.isNotEmpty(value)) {
            uv.add(FieldUtil.getColumnName(column), updateType, value);
        }
        return this;
    }


    private QueryFilter<T> addFilter(FieldFunction<T, ?> column, FilterType filterType, Object value, Boolean ignoreNullValue) {
        if (ignoreNullValue || ObjectUtil.isNotEmpty(value)) {
            basicFilter.add(FieldUtil.getColumnName(column), filterType, value);
        }
        return this;
    }

    private QueryFilter<T> addFilter(FieldFunction<T, ?> column, FilterType filterType, Object begin, Object end, Boolean ignoreNullValue) {
        if (ignoreNullValue || ObjectUtil.isNotEmpty(begin)) {
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
}