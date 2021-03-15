package lark.db.jsd.clause;

import lark.db.jsd.Filter;

/**
 * Created by guohua.cui on 15/5/21.
 */
public interface DeleteClause {
    DeleteEndClause where(Filter filter);
}
