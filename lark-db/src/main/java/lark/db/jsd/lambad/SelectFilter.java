package lark.db.jsd.lambad;

import lark.db.jsd.BasicFilter;
import lark.db.jsd.Groupers;
import lark.db.jsd.Sorters;

import java.util.Collection;

public interface SelectFilter<T> {


    CompareFilter<T> eq(FieldFunction<T, ?> column, Object value);

    CompareFilter<T> eq(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T> ne(FieldFunction<T, ?> column, Object value);

    CompareFilter<T> ne(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T> lt(FieldFunction<T, ?> column, Object value);

    CompareFilter<T> lt(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T> lte(FieldFunction<T, ?> column, Object value);

    CompareFilter<T> lte(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T> gt(FieldFunction<T, ?> column, Object value);

    CompareFilter<T> gt(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T> gte(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T> gte(FieldFunction<T, ?> column, Object value);

    CompareFilter<T> in(FieldFunction<T, ?> column, Collection value);

    CompareFilter<T> in(Boolean ignoreNullValue, FieldFunction<T, ?> column, Collection value);

    CompareFilter<T> notIn(FieldFunction<T, ?> column, Collection value);

    CompareFilter<T> notIn(Boolean ignoreNullValue, FieldFunction<T, ?> column, Collection value);

    CompareFilter<T> like(FieldFunction<T, ?> column, Object value);

    CompareFilter<T> like(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T> likeLeft(FieldFunction<T, ?> column, Object value);

    CompareFilter<T> likeLeft(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T> likeRight(FieldFunction<T, ?> column, Object value);

    CompareFilter<T> likeRight(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T> notLike(FieldFunction<T, ?> column, Object value);

    CompareFilter<T> notLike(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object value);

    CompareFilter<T> between(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object begin, Object end);

    CompareFilter<T> between(FieldFunction<T, ?> column, Object begin, Object end);

    CompareFilter<T> notBetween(Boolean ignoreNullValue, FieldFunction<T, ?> column, Object begin, Object end);

    CompareFilter<T> notBetween(FieldFunction<T, ?> column, Object begin, Object end);

    CompareFilter<T> orderByDesc(FieldFunction<T, ?> column);

    CompareFilter<T> orderByAsc(FieldFunction<T, ?> column);

    CompareFilter<T> groupBy(FieldFunction<T, ?> column);

    BasicFilter select();

    Groupers group();

    Sorters order();

}
