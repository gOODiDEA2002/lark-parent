package lark.db.jsd.clause;

import lark.db.jsd.result.ExecuteResult;

/**
 * Created by guohua.cui on 15/5/21.
 */
public interface ExecuteClause {
    ExecuteResult result();
    int submit();
}
