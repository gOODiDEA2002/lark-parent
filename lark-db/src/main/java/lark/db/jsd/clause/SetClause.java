package lark.db.jsd.clause;

import lark.db.jsd.Filter;

/**
 * Created by guohua.cui on 15/5/22.
 */
public interface SetClause extends UpdateEndClause {
    UpdateEndClause where(Filter filter);
}
