package lark.db.jsd.clause;

import lark.db.jsd.Table;

/**
 * Created by guohua.cui on 15/5/21.
 */
public interface SelectClause {
    FromClause from(Table table);
    FromClause from(String table);
}
