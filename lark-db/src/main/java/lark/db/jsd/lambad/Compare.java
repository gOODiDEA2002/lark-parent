package lark.db.jsd.lambad;

import lark.db.jsd.BasicFilter;
import lark.db.jsd.Groupers;
import lark.db.jsd.Sorters;
import lark.db.jsd.UpdateValues;

import java.util.Collection;

public interface Compare<T, M> {

    CompareFilter<T, M> eq(FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> eq(Boolean careNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> ne(FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> ne(Boolean careNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> lt(FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> lt(Boolean careNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> lte(FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> lte(Boolean careNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> gt(FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> gt(Boolean careNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> gte(Boolean careNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> gte(FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> in(FieldFunction<T, ?> column, Object... value);

    CompareFilter<T, M> in(Boolean careNullValue, FieldFunction<T, ?> column, Object... value);

    CompareFilter<T, M> notIn(FieldFunction<T, ?> column, Object... value);

    CompareFilter<T, M> notIn(Boolean careNullValue, FieldFunction<T, ?> column, Object... value);

    CompareFilter<T, M> like(FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> like(Boolean careNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> likeLeft(FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> likeLeft(Boolean careNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> likeRight(FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> likeRight(Boolean careNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> notLike(FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> notLike(Boolean careNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> between(Boolean careNullValue, FieldFunction<T, ?> column, Object begin, Object end);

    CompareFilter<T, M> between(FieldFunction<T, ?> column, Object begin, Object end);

    CompareFilter<T, M> notBetween(Boolean careNullValue, FieldFunction<T, ?> column, Object begin, Object end);

    CompareFilter<T, M> notBetween(FieldFunction<T, ?> column, Object begin, Object end);

    CompareFilter<T, M> orderByDesc(FieldFunction<T, ?> column);

    CompareFilter<T, M> orderByAsc(FieldFunction<T, ?> column);

    CompareFilter<T, M> groupBy(FieldFunction<T, ?> column);

    CompareFilter<T, M> or(Boolean careNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> or(FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> or(SelectFilter<T, M> selectFilter);

    CompareFilter<T, M> apply(String sql);


    /**
     * 支持 {}, 参数会在后面转换
     *
     * @description: TODO
     * @return:
     * @author: yandong
     * @date: 2021/3/24 6:22 下午
     */
    CompareFilter<T, M> apply(String sql, Object... objects);


    BasicFilter build();

    Groupers group();

    Sorters order();

    UpdateValues set();

}
