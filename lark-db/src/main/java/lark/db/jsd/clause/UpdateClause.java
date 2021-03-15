package lark.db.jsd.clause;

import lark.db.jsd.UpdateValues;

/**
 * Created by guohua.cui on 15/5/21.
 */
public interface UpdateClause {
    SetClause set(UpdateValues values);
}
