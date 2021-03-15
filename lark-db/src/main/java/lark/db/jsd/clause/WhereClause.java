package lark.db.jsd.clause;

import lark.db.jsd.Groupers;
import lark.db.jsd.Sorters;

/**
 * Created by guohua.cui on 15/5/27.
 */
public interface WhereClause extends SelectEndClause {
    SelectEndClause limit(int skip, int take);
    SelectEndClause page(int pageIndex, int pageSize);
    GroupByClause groupBy(Groupers groupers);
    OrderByClause orderBy(Sorters sorters);
}
