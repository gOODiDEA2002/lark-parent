package lark.db.jsd.clause;

import lark.db.jsd.Filter;
import lark.db.jsd.Sorters;

/**
 * Created by guohua.cui on 15/6/2.
 */
public interface GroupByClause extends SelectEndClause {
    SelectEndClause limit(int skip, int take);
    SelectEndClause page(int pageIndex, int pageSize);
    OrderByClause orderBy(Sorters sorters);
    HavingClause having(Filter f);
}
