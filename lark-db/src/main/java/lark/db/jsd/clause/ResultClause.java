package lark.db.jsd.clause;

import lark.db.jsd.result.BuildResult;
import lark.db.jsd.result.CallResult;

/**
 * Created by guohua.cui on 15/5/11.
 */
public interface ResultClause {
    CallResult result();
    BuildResult print();
}
