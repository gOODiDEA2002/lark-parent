package lark.db.jsd.lambad;

import lark.db.jsd.BasicFilter;
import lark.db.jsd.Groupers;
import lark.db.jsd.Sorters;
import lark.db.jsd.UpdateValues;

import java.util.Collection;

public interface Compare<T, M> {

    CompareFilter<T, M> eq(FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> eq(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> ne(FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> ne(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> lt(FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> lt(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> lte(FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> lte(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> gt(FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> gt(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> gte(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> gte(FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> in(FieldFunction<T, ?> column, Collection value);

    CompareFilter<T, M> in(Boolean ignoreNullValue, FieldFunction<T, ?> column, Collection value);

    CompareFilter<T, M> notIn(FieldFunction<T, ?> column, Collection value);

    CompareFilter<T, M> notIn(Boolean ignoreNullValue, FieldFunction<T, ?> column, Collection value);

    CompareFilter<T, M> like(FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> like(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> likeLeft(FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> likeLeft(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> likeRight(FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> likeRight(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> notLike(FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> notLike(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T, M> between(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object begin, Object end);

    CompareFilter<T, M> between(FieldFunction<T, ?> column, Object begin, Object end);

    CompareFilter<T, M> notBetween(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object begin, Object end);

    CompareFilter<T, M> notBetween(FieldFunction<T, ?> column, Object begin, Object end);

    CompareFilter<T, M> orderByDesc(FieldFunction<T, ?> column);

    CompareFilter<T, M> orderByAsc(FieldFunction<T, ?> column);

    CompareFilter<T, M> groupBy(FieldFunction<T, ?> column);


    BasicFilter select();

    Groupers group();

    Sorters order();

    UpdateValues set();

}
