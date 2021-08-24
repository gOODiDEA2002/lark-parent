package lark.db.jsd.clause;

import lark.db.jsd.LockMode;
import lark.db.jsd.result.BuildResult;
import lark.db.jsd.result.SelectResult;

/**
 * Created by guohua.cui on 15/5/27.
 */
public interface SelectEndClause {
    SelectResult result();
    SelectResult result(LockMode lockMode);
    BuildResult print();
}
