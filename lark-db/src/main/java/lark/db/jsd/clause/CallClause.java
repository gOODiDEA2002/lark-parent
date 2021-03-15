package lark.db.jsd.clause;

import lark.db.jsd.CallParams;

/**
 * Created by guohua.cui on 15/5/21.
 */
public interface CallClause extends CallEndClause {
    CallEndClause with(CallParams params);
}
