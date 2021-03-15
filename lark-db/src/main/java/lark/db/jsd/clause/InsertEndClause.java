package lark.db.jsd.clause;

import lark.db.jsd.result.BuildResult;
import lark.db.jsd.result.InsertResult;

/**
 * Created by guohua.cui on 15/5/8.
 */
public interface InsertEndClause {
    BuildResult print();
    InsertResult result();
    InsertResult result(boolean returnKeys);
}
